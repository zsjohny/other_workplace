package com.jiuy.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
	public static byte[] stream2Byte(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = is.read(b, 0, b.length)) != -1) {
			baos.write(b, 0, len);
		}
		byte[] buffer = baos.toByteArray();
		return buffer;
	}

	public static byte[] inputStream2Byte(InputStream inStream) throws Exception {
		int count = 0;
		while (count == 0) {
			count = inStream.available();
		}
		byte[] b = new byte[count];
		inStream.read(b);
		return b;
	}

	public static InputStream byte2InputStream(byte[] b) throws Exception {
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

	public static OutputStream byte2OutputStream(byte[] b) throws Exception {
		OutputStream o = new ByteArrayOutputStream();
		o.write(b);
		return o;
	}
}