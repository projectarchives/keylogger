package pro.jrat.stub.keylogger;

import java.io.DataOutputStream;

public class Title implements Activity {
	
	private final String title;
	
	public Title(String title) {
		this.title = title;
	}

	@Override
	public void write(DataOutputStream dos) throws Exception {		
		dos.writeUTF(title);
	}

}
