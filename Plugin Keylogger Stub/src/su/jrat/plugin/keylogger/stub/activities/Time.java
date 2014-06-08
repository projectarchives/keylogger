package su.jrat.plugin.keylogger.stub.activities;

import java.io.DataOutputStream;
import java.util.Date;

public class Time implements Activity {

	private String time;

	public Time(long timeMillis) {
		time = getDate(new Date(timeMillis));
	}

	@Override
	public void write(DataOutputStream dos) throws Exception {
		dos.writeUTF(time);
	}
	
	@Override
	public String toString() {
		return time;
	}
	
	@SuppressWarnings("deprecation")
	public String getDate(Date date) {
		return (date.getYear() + 1900) + "/" + (date.getMonth() + 1) + "/" + (date.getDate()) + " " + date.getHours() + ":" + date.getMinutes();
	}

}
