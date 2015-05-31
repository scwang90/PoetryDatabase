package com.poetry.database.dao;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.poetry.database.feature.SettleAuthor;

public class SettleAuthorTest {

	private SettleAuthor settle;

	@Before
	public void init(){
		settle = new SettleAuthor();
	}
	
	@Test
	public void settleTest() throws FileNotFoundException, Exception{
		PrintStream out = new PrintStream("target/settle.authors.java");
		settle.settle(out);
		out.close();
	}
}
