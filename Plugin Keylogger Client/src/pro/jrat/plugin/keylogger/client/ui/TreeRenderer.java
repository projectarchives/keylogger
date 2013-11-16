package pro.jrat.plugin.keylogger.client.ui;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings({ "serial", "deprecation" })
public class TreeRenderer extends DefaultTreeCellRenderer {
	
	public static final Icon DAY_ICON = new ImageIcon(System.getProperty("jrat.dir") + File.separator + "plugins/Keylogger/day.png");
	public static final Icon MONTH_ICON = new ImageIcon(System.getProperty("jrat.dir") + File.separator + "plugins/Keylogger/month.png");
	public static final Icon YEAR_ICON = new ImageIcon(System.getProperty("jrat.dir") + File.separator + "plugins/Keylogger/year.png");
	public static final Icon DEFAULT_ICON = new ImageIcon(System.getProperty("jrat.dir") + File.separator + "plugins/Keylogger/default.png");

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, this.getFont().getSize()));

		if (value instanceof DayTreeNode) {
			setIcon(DAY_ICON);
		} else if (value instanceof MonthTreeNode) {
			setIcon(MONTH_ICON);
		} else if (value instanceof YearTreeNode) {
			setIcon(YEAR_ICON);
		} else {
			setIcon(DEFAULT_ICON);
		}
		
		Date date = new Date();
		String day = Integer.toString(date.getDate());
		String month = Integer.toString(date.getMonth() + 1);
		String year = Integer.toString(date.getYear() + 1900);

		if (value.toString().equals(day + ".log") || value.toString().equals(month) || value.toString().equals(year)) {
			this.setFont(new Font(this.getFont().getName(), Font.BOLD, this.getFont().getSize()));
		}

		return this;
	}
}

