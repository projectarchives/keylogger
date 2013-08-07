package pro.jrat.client.keylogger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import pro.jrat.api.BaseControlPanel;
import pro.jrat.api.RATMenuItemActionListener;
import pro.jrat.api.RATObject;

public class MenuListener implements RATMenuItemActionListener {

	@Override
	public void onClick(List<RATObject> servers) {
		try {
			if (servers.size() > 0) {
				final RATObject server = servers.get(0);
				BaseControlPanel panel = null;

				if (Plugin.entry.instances.containsKey(server.getIP())) {
					panel = Plugin.entry.instances.get(server.getIP());
				} else {
					panel = Plugin.entry.newPanelInstance(server);
					Plugin.entry.instances.put(server.getIP(), panel);
				}
				
				final BaseControlPanel finalPanel = panel;

				JFrame frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						finalPanel.onClose();
						Plugin.entry.instances.remove(server.getIP());
					}
				});
				frame.setTitle("Keylogger - " + server.getIP());
				frame.setSize(500, 350);
				frame.setLocationRelativeTo(null);
				frame.setIconImage(new ImageIcon(Plugin.ICON_LOCATION).getImage());
				frame.setLocationRelativeTo(null);
				frame.add(panel);
				frame.setVisible(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}