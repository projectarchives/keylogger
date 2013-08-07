package pro.jrat.stub.keylogger;

import java.io.DataOutputStream;

public abstract interface Activity {
	
	public abstract void write(DataOutputStream dos) throws Exception;

}
