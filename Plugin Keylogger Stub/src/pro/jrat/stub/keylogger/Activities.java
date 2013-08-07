package pro.jrat.stub.keylogger;

import java.util.ArrayList;
import java.util.List;

public class Activities {

	public static final List<Activity> activities = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		activities.add(activity);
	}
}
