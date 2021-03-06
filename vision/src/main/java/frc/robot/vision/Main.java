/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;

public final class Main {
	private static String configFile = "/boot/frc.json";

	public static class CameraConfig {
		public String name;
		public String path;
		public JsonObject config;
		public JsonElement streamConfig;
	}

	public static int team;
	public static boolean server;
	public static List<CameraConfig> cameraConfigs = new ArrayList<>();

	public static CvSource AugmentCam;
	public static Clock clock = Clock.systemUTC();

	private Main() {
	}

	/**
	 * Report parse error.
	 */
	public static void parseError(String str) {
		System.err.println("config error in '" + configFile + "': " + str);
	}

	/**
	 * Read single camera configuration.
	 */
	public static boolean readCameraConfig(JsonObject config) {
		CameraConfig cam = new CameraConfig();

		// name
		JsonElement nameElement = config.get("name");
		if (nameElement == null) {
			parseError("could not read camera name");
			return false;
		}
		cam.name = nameElement.getAsString();

		// path
		JsonElement pathElement = config.get("path");
		if (pathElement == null) {
			parseError("camera '" + cam.name + "': could not read path");
			return false;
		}
		cam.path = pathElement.getAsString();

		// stream properties
		cam.streamConfig = config.get("stream");

		cam.config = config;

		cameraConfigs.add(cam);
		return true;
	}

	/**
	 * Read configuration file.
	 */
	public static boolean readConfig() {
		// parse file
		JsonElement top;
		try {
			top = new JsonParser().parse(Files.newBufferedReader(Paths.get(configFile)));
		} catch (IOException ex) {
			System.err.println("could not open '" + configFile + "': " + ex);
			return false;
		}

		// top level must be an object
		if (!top.isJsonObject()) {
			parseError("must be JSON object");
			return false;
		}
		JsonObject obj = top.getAsJsonObject();

		// team number
		JsonElement teamElement = obj.get("team");
		if (teamElement == null) {
			parseError("could not read team number");
			return false;
		}
		team = teamElement.getAsInt();

		// ntmode (optional)
		if (obj.has("ntmode")) {
			String str = obj.get("ntmode").getAsString();
			if ("client".equalsIgnoreCase(str)) {
				server = false;
			} else if ("server".equalsIgnoreCase(str)) {
				server = true;
			} else {
				parseError("could not understand ntmode value '" + str + "'");
			}
		}

		// cameras
		JsonElement camerasElement = obj.get("cameras");
		if (camerasElement == null) {
			parseError("could not read cameras");
			return false;
		}
		JsonArray cameras = camerasElement.getAsJsonArray();
		for (JsonElement camera : cameras) {
			if (!readCameraConfig(camera.getAsJsonObject())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Start running the camera.
	 */
	public static VideoSource startCamera(CameraConfig config) {
		System.out.println("Starting camera '" + config.name + "' on " + config.path);
		CameraServer inst = CameraServer.getInstance();
		UsbCamera camera = new UsbCamera(config.name, config.path);
		// MjpegServer server = inst.startAutomaticCapture(camera);

		// setup a cvSource where you can put furames and it should just work
		AugmentCam = inst.putVideo("Augmented", 320, 240);

		Gson gson = new GsonBuilder().create();

		camera.setConfigJson(gson.toJson(config.config));
		camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);

		if (config.streamConfig != null) {
			// server.setConfigJson(gson.toJson(config.streamConfig));
		}

		return camera;
	}

	/**
	 * Main.
	 */
	public static void main(String... args) {
		if (args.length > 0) {
			configFile = args[0];
		}

		// read configuration
		if (!readConfig()) {
			return;
		}

		// start NetworkTables
		NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
		if (server) {
			System.out.println("Setting up NetworkTables server");
			ntinst.startServer();
		} else {
			System.out.println("Setting up NetworkTables client for team " + team);
			ntinst.startClientTeam(team);
		}
		// send Timestamp to RIO for synchronization
		System.out.println("main");
		System.out.println(clock.instant());
		ByteArrayOutput.setNetworkObject(clock.instant(), "table", "target_offsets");

		// start cameras
		List<VideoSource> cameras = new ArrayList<>();
		for (CameraConfig cameraConfig : cameraConfigs) {
			cameras.add(startCamera(cameraConfig));
		}

		// start image processing on camera 0 if present
		if (cameras.size() >= 1) {
			VisionThread visionThread =
					new VisionThread(cameras.get(0), new GripPostProcessing(), pipeline -> {
						// do something with pipeline results
						// System.out.println("start callback pipeline");
						AugmentCam.putFrame(pipeline.AugmentCamOutput);
						// System.out.println(pipeline.grip.filterContoursOutput());
						// System.out.println(pipeline.grip.filterContoursOutput());
						// ByteArrayOutput.setNetworkObject(pipeline.visionTargets, "table",
						// "visionTargets");
						double[] x_offset_angles = new double[pipeline.visionTargets.size()];
						for (int i = 0; i < pipeline.visionTargets.size(); i++) {
							GripPostProcessing.VisionTarget v = pipeline.visionTargets.get(i);
							x_offset_angles[i] = CoordTransform.transformCoordsToOffsetAngle(
									new double[] {(double) v.bounding.x + 0.5 * v.bounding.width,
											(double) v.bounding.y + 0.5 * v.bounding.height})[0];
						}
						for (int i = 0; i < x_offset_angles.length; i++) {
							System.out.print(x_offset_angles[i]*180/Math.PI + " ");
						}
						System.out.println();
						ByteArrayOutput.setNetworkObject(x_offset_angles, "table",
								"target_offsets");
					});
			/*
			 * something like this for GRIP: VisionThread visionThread = new
			 * VisionThread(cameras.get(0), new GripPipeline(), pipeline -> { ... });
			 */
			System.out.println("start visionthread");
			visionThread.start();
		}

		// loop forever
		for (;;) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				return;
			}
		}
	}

	// public static void pipelineProcess(GripPostProcessing pipeline) {
	// System.out.println(pipeline.grip.filterContoursOutput());
	// // ByteArrayOutput.setNetworkObject(pipeline.visionTargets, "table",
	// // "visionTargets");
	// double[] x_offset_angles = new double[pipeline.visionTargets.size()];
	// for (int i = 0; i < pipeline.visionTargets.size(); i++) {
	// GripPostProcessing.VisionTarget v = pipeline.visionTargets.get(i);
	// x_offset_angles[i] = CoordTransform.transformCoordsToOffsetAngle(
	// new double[] {(double) v.bounding.height, (double) v.bounding.width})[0];
	// }
	// for (int i = 0; i < x_offset_angles.length; i++) {
	// System.out.print(x_offset_angles[i] + "");
	// }
	// ByteArrayOutput.setNetworkObject(x_offset_angles, "table", "target_offsets");
	// AugmentCam.putFrame(pipeline.AugmentCamOutput);

	// }
}
