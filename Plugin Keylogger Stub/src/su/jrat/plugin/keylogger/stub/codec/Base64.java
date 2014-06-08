package su.jrat.plugin.keylogger.stub.codec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {

	public static String encode(String s) {
		try {
			return new BASE64Encoder().encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	public static String decode(String s) {
		try {
			return new String(new BASE64Decoder().decodeBuffer(s));
		} catch (IOException e) {
			return s;
		}
	}

}
