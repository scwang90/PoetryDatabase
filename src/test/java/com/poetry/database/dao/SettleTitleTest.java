package com.poetry.database.dao;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.poetry.database.feature.SettleTitle;

public class SettleTitleTest {

	private SettleTitle settle;

	@Before
	public void init(){
		settle = new SettleTitle();
	}
	
	@Test
	public void settleTest() throws FileNotFoundException, Exception{
		PrintStream out = new PrintStream("settle.titles.java");
		settle.settle(out);
		out.close();
	}
}
