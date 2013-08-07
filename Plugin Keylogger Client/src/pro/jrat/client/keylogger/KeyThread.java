package pro.jrat.client.keylogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;

import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATObject;

public class KeyThread implements Runnable {

	private boolean enabled = true;

	private RATObject server;
	private PanelKeylogger panel;

	public KeyThread(RATObject server, PanelKeylogger panel) {
		this.server = server;
		this.panel = panel;
	}

	@Override
	public void run() {
		while (enabled) {
			try {
				Thread.sleep(5000L);

				server.addToSendQueue(new PacketBuilder(Plugin.STATUS_HEADER, server) {
					@Override
					public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {						
						
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
