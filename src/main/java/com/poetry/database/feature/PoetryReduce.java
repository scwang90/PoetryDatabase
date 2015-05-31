package com.poetry.database.feature;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.poetry.database.util.AfCharset;
/**
 * 藏头诗数据库压缩
 * @author SCWANG
 */
public class PoetryReduce {
	/**
	 * 开始压缩
	 * @param out
	 * @param resource
	 * @throws IOException
	 */
	public void Reduce(PrintStream out,String resource) throws IOException {
		InputStream input = ClassLoader.getSystemResourceAsStream(resource);
		byte[] bytes = new byte[input.available()+2];
		input.read(bytes, 2, bytes.length-2);
		input.close();
		bytes[0] = -1;
		bytes[1] = -2;
		String src = new String(bytes,AfCharset.Unicode);
		out.println(src);
		
		byte[] bytesgbk = src.getBytes(AfCharset.GBK);
		FileOutputStream file = new FileOutputStream(resource);
		file.write(bytesgbk);
		file.close();
		
	}
	
}
