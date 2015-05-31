package com.poetry.database.feature;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.poetry.database.dao.base.BaseDao;
import com.poetry.database.dao.base.BaseDaoDbUtilImpl;
import com.poetry.database.model.Poetry;

public class SettleTitle extends Settle{

	private BaseDao<Poetry> mBaseDaoPoetry = new BaseDaoDbUtilImpl<Poetry>(){};

	/**
	 * 获取所有的诗词 的标题
	 * @param list
	 * @return
	 */
	private List<String> findAllTitles(PrintStream out,List<Poetry> list) {
		out.println("class @获取所有的诗词 的标题{");
		List<String> titles = new ArrayList<String>();
		for (Poetry poetry : list) {
			titles.add(poetry.getTitle().replace(" ", ""));
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}

	/**
	 * 去除诗人中（以后的字符
	 * @param out
	 * @param targets
	 * @return
	 */
	private List<String> deleteBracketAfter(PrintStream out,List<String> targets) {
		out.println("class @去除诗人中（以后的字符 {");
		for (int i = 0; i < targets.size(); i++) {
			String author = targets.get(i);
			if (author.contains("（")) {
				out.print(author);
				targets.set(i, author.substring(0,author.indexOf("（")));
				out.println("--"+targets.get(i));
			}else if (author.contains("(")) {
				out.print(author);
				targets.set(i, author.substring(0,author.indexOf("(")));
				out.println("->"+targets.get(i));
			}
		}
		out.println("}\r\n");
		return targets;
	}
	
	public void settle(PrintStream out) throws Exception {

		List<String> titles = new ArrayList<String>(); 
		//----------------------------------------获取所有的诗词 的标题
		titles = this.findAllTitles(out, mBaseDaoPoetry.findAll());
		//----------------------------------------去除诗人中（以后的字符
		titles = this.deleteBracketAfter(out, titles);
		//----------------------------------------拆分标题
		String[] brackets = {"\\[|\\]","\\(|\\)","\\{|\\}","（|）","·","、","，"};
		titles = this.splitTarget(out, titles,brackets);
		//----------------------------------------替换指定文字为空
		String[] replaces1 = {"一首","二首","三首","四首","五首","六首","七首","八首"};
		titles = this.replaceSpecifyText(out, titles, replaces1);
		//----------------------------------------替换指定文字为空
		String[] replaces2 = {"之一","之二","之三","之四","之五","之六","之七","之八"};
		titles = this.replaceSpecifyText(out, titles, replaces2);
		//----------------------------------------替换指定文字为空
		String[] replaces3 = {"其一","其二","其三","其四","其五","其六","其七","其八"};
		titles = this.replaceSpecifyText(out, titles, replaces3);
		//----------------------------------------替换指定文字为空
		String[] replaces4 = {"并序","集序","序","集","，","。"};
		titles = this.replaceSpecifyText(out, titles, replaces4);
		//----------------------------------------去掉太长的标题
		titles = this.deleteLongTarget(out, titles,4);
		//----------------------------------------排除重复
		titles = this.deleteRepetitive(out, titles);
		//----------------------------------------去掉包含指定文字的标题
		String[] deletes = {"死","鬼","古诗","《"};
		titles = this.deleteSpecifyText(out, titles, deletes);
		//----------------------------------------去掉一个字的标题
		titles = this.deleteOneTarget(out, titles);
		//----------------------------------------找出特殊符号的标题
		titles = this.deleteSpecificSymbol(out, titles);
		
		//把整理的数据写到demo文件中
		this.printDemo("target/titles-demo.txt",titles);
		//整理压缩数据
		this.printData("target/titles.txt",titles);
	}
}
