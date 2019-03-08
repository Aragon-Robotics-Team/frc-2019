package frc.robot.util;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SendableMaster {
    private static SendableMaster instance;

    private int depth;
    private ShuffleboardTab currentTab;
    private String baseName;
    private String tabName;

    private SendableMaster() {
        depth = 0;
        currentTab = null;
        baseName = null;
    }

    public static synchronized SendableMaster getInstance() {
        if (instance == null) {
            instance = new SendableMaster();
        }

        return instance;
    }

    // in BetterSendable.createSendable: SendableMaster.add("name", new Command())
    // .add(new Command()); .add(new SendableSRX()); .add("name", newSendableSRX())
    // for each subsystem: SendableMaster.add(subsystem)
    public void add(BetterSendable betterSendable) {
        add(null, betterSendable);
    }

    public void add(String name, BetterSendable betterSendable) {
        if (depth == 0) {
            addRoot(betterSendable);
        } else {
            addBranch(name, betterSendable);
        }
    }

    public void add(Sendable sendable) {
        add(null, sendable);
    }

    public void add(String name, Sendable sendable) {
        if (depth == 0) {
            throw new IllegalStateException("Cannot call add(Sendable) outside of createSendable");
        }

        String sendableName = sendable.getName();

        if (isNull(name)) {
            if (isNull(baseName)) {
                try {
                    addToTab(tabName, sendable);
                } catch (IllegalArgumentException ex) {
                    addToTab(null, sendable);
                }
            } else {
                try {
                    addToTab(baseName, sendable); // If error, append sendableName
                } catch (IllegalArgumentException ex) {
                    if (isNull(sendableName)) {
                        throw ex;
                    } else {
                        addToTab(append(baseName, sendableName), sendable);
                    }
                }
            }
        } else {
            if (isNull(baseName)) {
                try {
                    addToTab(name, sendable); // If error, append sendableName
                } catch (IllegalArgumentException ex) {
                    if (isNull(sendableName)) {
                        throw ex;
                    } else {
                        addToTab(append(name, sendableName), sendable);
                    }
                }
            } else {
                addToTab(append(baseName, name), sendable);
            }
        }
    }

    // Only top level BetterSendable; gets own tab
    private void addRoot(BetterSendable betterSendable) {
        baseName = "";
        tabName = betterSendable.getTabName();
        currentTab = Shuffleboard.getTab(tabName);

        createSendable(betterSendable);
    }

    // Add BetterSendable to existing tab; recurse into createSendable
    private void addBranch(String name, BetterSendable betterSendable) {
        String oldBaseName = baseName;

        if (name == null) {
            name = betterSendable.getTabName();
        }

        baseName = append(baseName, name);
        createSendable(betterSendable);

        baseName = oldBaseName;
    }

    private void createSendable(BetterSendable betterSendable) {
        depth += 1;
        betterSendable.createSendable(this);
        depth -= 1;
    }

    private void addToTab(String name, Sendable sendable) {
        System.out.println("Adding: " + name + " " + sendable);

        if (name == null) {
            currentTab.add(sendable);
        } else {
            currentTab.add(name, sendable);
        }
    }

    private static boolean isNull(String string) {
        return (string == null || string == "");
    }

    private static String append(String a, String b) {
        return a + (!isNull(a) && isNull(b) ? "." : "") + b;
    }

    // public void add(BetterSendable betterSendable, String name, Sendable sendable) {
    // if (!isOpen) {
    // throw new IllegalStateException("SendableMaster has already been initialized");
    // }

    // if (!map.containsKey(betterSendable)) {
    // map.put(betterSendable, new HashMap<String, Sendable>());
    // }

    // map.get(betterSendable).put(name, sendable);
    // }

    // private static void initTab() {
    // isOpen = false;

    // Sendable mainSendable = betterSendable.getMainSendable();
    // if (mainSendable != null) {
    // tab.add(mainName, mainSendable);
    // }

    // Map<String, Sendable> sendables = betterSendable.getSendables();
    // if (sendables != null) {
    // for (Map.Entry<String, Sendable> entry : sendables.entrySet()) {
    // String name = entry.getKey();
    // Sendable sendable = entry.getValue();

    // if (BetterSendable.class.isAssignableFrom(sendable.getClass())) {
    // BetterSendable newBetterSendable = (BetterSendable) sendable;
    // init(tab, name, betterSendable)
    // }
    // }
    // }
    // }
}
