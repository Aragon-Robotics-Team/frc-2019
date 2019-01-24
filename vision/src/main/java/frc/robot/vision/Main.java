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
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;

public final class Main {
	private static String configFile = "/boot/frc.json";

	@SuppressWarnings("MemberName")
	public static class CameraConfig {
		public String name;
		public String path;
		public JsonObject config;
	}

	public static int team;
	public static boolean server;
	public static List<CameraConfig> cameras = new ArrayList<>();

	private static VisionThread visionThread;
	private static final Object imgLock = new Object();

	private static NetworkTableInstance inst;
	private static NetworkTable table;
	private static NetworkTableEntry xEntry;

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

		cam.config = config;

		cameras.add(cam);
		return true;
	}

	/**
	 * Read configuration file.
	 */
	@SuppressWarnings("PMD.CyclomaticComplexity")
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
	public static void startCamera(CameraConfig config) {
		System.out.println("Starting camera '" + config.name + "' on " + config.path);
		VideoSource camera =
				CameraServer.getInstance().startAutomaticCapture(config.name, config.path);

		Gson gson = new GsonBuilder().create();

		camera.setConfigJson(gson.toJson(config.config));

		visionThread = new VisionThread(camera, new Grip(), Main::pipelineProcess);

	}

	/**
	 * Main.
	 */
	public static void main(String... args) {

		inst = NetworkTableInstance.getDefault();
		table = inst.getTable("datatable");
		xEntry = table.getEntry("X");


		// NetworkTableEntry xEntry = table.getEntry("X");

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

		// start cameras
		for (CameraConfig camera : cameras) {
			startCamera(camera);
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

	public static void pipelineProcess(Grip pipeline) {
		System.out.println(pipeline.filterContoursOutput());


		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(pipeline.filterContoursOutput());
			out.flush();
			byte[] ByteOutput = bos.toByteArray();

			xEntry.setRaw(ByteOutput);



		} catch (IOException ex) {

		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
			}
		}



	}
}
