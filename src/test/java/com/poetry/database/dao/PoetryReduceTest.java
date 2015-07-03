package com.poetry.database.dao;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import com.poetry.database.feature.PoetryReduce;
/**
 * 藏头诗数据库压缩
 * @author 树朾
 */
/**
 * 藏头诗数据库压缩
 * @author 树朾
 */
public class PoetryReduceTest {
	
	PoetryReduce mReduce = new PoetryReduce();
	/**
	 * 开始压缩
	 * @param out
	 * @param resource
	 * @throws IOException
	 */
	@Test
	public void ReduceTest() throws IOException {
		PrintStream out = new PrintStream("poetry.reduce.out.txt");
		mReduce.Reduce(out , "wujue.txt");
	}
	
}
