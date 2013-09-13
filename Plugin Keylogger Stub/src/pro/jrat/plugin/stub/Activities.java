package pro.jrat.plugin.stub;

import java.util.ArrayList;
import java.util.List;

public class Activities {

	public static final boolean unix = System.getProperty("os.name").toLowerCase().contains("mac") || System.getProperty("os.name").toLowerCase().contains("linux");
	public static final List<Activity> activities = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		if (activity instanceof Key && unix && activities.size() > 0 && ((Key)activities.get(activities.size() - 1)).getKey() == (((Key)activity).getKey())) {
			//return;
		}
		
		if (activities.size() > 2500) {
			activities.clear();
		}
		
		activities.add(activity);
	}
}
