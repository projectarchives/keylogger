package pro.jrat.plugin.stub;

import java.io.DataOutputStream;

public abstract interface Activity {
	
	public abstract void write(DataOutputStream dos) throws Exception;

}
