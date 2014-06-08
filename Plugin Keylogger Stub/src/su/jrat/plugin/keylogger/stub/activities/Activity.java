package su.jrat.plugin.keylogger.stub.activities;

import java.io.DataOutputStream;

public abstract interface Activity {
	
	public abstract void write(DataOutputStream dos) throws Exception;

}
