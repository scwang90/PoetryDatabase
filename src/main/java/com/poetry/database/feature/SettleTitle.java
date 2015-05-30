package com.poetry.database.feature;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.poetry.database.dao.base.BaseDao;
import com.poetry.database.dao.base.BaseDaoDbUtilImpl;
import com.poetry.database.model.Poetry;

public class SettleTitle {

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
	 * 去除标题中（以后的字符
	 * @param out
	 * @param titles
	 * @return
	 */
	private List<String> deleteBracketAfter(PrintStream out,List<String> titles) {
		out.println("class @去除标题中（以后的字符 {");
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			if (title.contains("（")) {
				out.print(title);
				titles.set(i, title.substring(0,title.indexOf("（")));
				out.println("--"+titles.get(i));
			}else if (title.contains("(")) {
				out.print(title);
				titles.set(i, title.substring(0,title.indexOf("(")));
				out.println("->"+titles.get(i));
			}
		}
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 去掉太长的标题
	 * @param out
	 * @param titles
	 * @return
	 */
	private List<String> deleteLongTitle(PrintStream out,List<String> titles) {
		out.println("class @去掉太长的标题 {");
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			if (title.length() > 6) {
				titles.remove(i--);
				out.println(title);
			}
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 排除重复
	 * @param out
	 * @param titles
	 * @return
	 */
	private List<String> deleteRepetitive(PrintStream out,List<String> titles) {
		out.println("class @排除重复 {");
		Set<String> settitles = new HashSet<>();
		for (String title : titles) {
			settitles.add(title);
		}
		out.println("titles = "+titles.size()+"->"+settitles.size());
		titles = new ArrayList<String>(settitles);
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 替换指定文字为空
	 * @param out
	 * @param titles
	 * @return
	 */
	private List<String> replaceSpecifyText(PrintStream out,List<String> titles,String[] replaces) {
		out.println("class @替换指定标题内容为空 {");
		out.print("=>");
		for (String replace : replaces) {
			out.print(replace+" ");
		}
		out.println();
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			for (String replace : replaces) {
				if (title.contains(replace)) {
					out.print(title);
					titles.set(i, title.replace(replace, ""));
					out.println("->"+titles.get(i));
					break;
				}
			}
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 
	 * @param out
	 * @param titles
	 * @param deletes 
	 * @return
	 */
	private List<String> deleteSpecifyText(PrintStream out,List<String> titles, String[] deletes) {
		out.println("class @去掉包含指定文字的标题 {");
		out.print("=>");
		for (String delete : deletes) {
			out.print(delete+" ");
		}
		out.println();
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			for (String delete : deletes) {
				if (title.contains(delete)) {
					out.println(title);
					titles.remove(i--);
				}
			}
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 去掉一个字的标题
	 * @param out
	 * @param titles
	 * @param deletes
	 * @return
	 */
	private List<String> deleteOneTitle(PrintStream out,List<String> titles) {
		out.println("class @去掉一个字的标题 {");
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			if (title.length() == 1) {
				titles.remove(i--);
				out.print(title+" ");
			}
		}
		out.println(" ");
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 拆分标题
	 * @param out
	 * @param titles
	 * @param splits 
	 * @return
	 */
	private List<String> splitTitle(PrintStream out,List<String> titles, String[] splits) {
		out.println("class @拆分标题 {");
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			for (String split : splits) {
				if (title.contains(split)) {
					out.println(title);
					String[] divs = title.split(split);
					titles.set(i, divs[0]);
					if (divs.length > 1) {
						titles.add(i, divs[1]);
					}
					break;
				}
			}
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	/**
	 * 删除特殊字符
	 * @param out
	 * @param titles
	 * @param splits
	 * @return
	 */
	private List<String> deleteSpecificSymbol(PrintStream out,List<String> titles) {
		out.println("class @找出特殊符号的标题 {");
		for (int i = 0; i < titles.size(); i++) {
			String title = titles.get(i);
			if (!title.matches("[\\u4E00-\\u9FA5]+")) {
				titles.remove(i--);
				out.println(title);
			}
		}
		out.println("titles = "+titles.size());
		out.println("}\r\n");
		return titles;
	}
	
	public void settle(PrintStream out) throws Exception {

		List<String> titles = new ArrayList<String>(); 
		//----------------------------------------获取所有的诗词 的标题
		titles = this.findAllTitles(out, mBaseDaoPoetry.findAll());
		//----------------------------------------去除标题中（以后的字符
		titles = this.deleteBracketAfter(out, titles);
		//----------------------------------------去掉太长的标题
		titles = this.deleteLongTitle(out, titles);
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
		//----------------------------------------排除重复
		titles = this.deleteRepetitive(out, titles);
		//----------------------------------------去掉包含指定文字的标题
		String[] deletes = {"死","鬼","古诗","《"};
		titles = this.deleteSpecifyText(out, titles, deletes);
		//----------------------------------------去掉一个字的标题
		titles = this.deleteOneTitle(out, titles);
		//----------------------------------------拆分标题
		String[] splits = {"·"};
		titles = this.splitTitle(out, titles, splits);
		//----------------------------------------找出特殊符号的标题
		titles = this.deleteSpecificSymbol(out, titles);
		
		printDemo("titles-demo.txt",titles);
		//把整理的数据写到demo文件中
	}
	/**
	 * 输出结果事例
	 * @param demo
	 * @param titles
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printDemo(String demo, List<String> titles) throws FileNotFoundException{
		// TODO Auto-generated method stub
		PrintStream print = new PrintStream(demo);
		for (String title : titles) {
			print.println(title);
		}
		print.close();
	}
}
