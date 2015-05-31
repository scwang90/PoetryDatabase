package com.poetry.database.feature;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.poetry.database.util.AfCharset;

public class Settle {

	/**
	 * 整理压缩数据
	 * @param string
	 * @param titles
	 * @throws Exception 
	 */
	protected void printData(String data, List<String> titles) throws Exception {
		// TODO Auto-generated method stub
		String titledata = builderDataString(titles);
		byte[] bytes = titledata.getBytes(AfCharset.GBK);
		AfCharset.mixup(bytes);
		FileOutputStream stream = new FileOutputStream(data);
		stream.write(bytes);
		stream.close();
	}

	protected String builderDataString(List<String> list) {
		// TODO Auto-generated method stub
		StringWriter writer = new StringWriter();
		for (String poetry : list) {
			writer.write(poetry);
			writer.write("_");
		}
		return writer.toString();
	}
	
	/**
	 * 输出结果事例
	 * @param demo
	 * @param titles
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	protected void printDemo(String demo, List<String> titles) throws FileNotFoundException{
		// TODO Auto-generated method stub
		PrintStream print = new PrintStream(demo);
		for (String title : titles) {
			print.println(title);
		}
		print.close();
	}
	/**
	 * 拆分对象
	 * @param out
	 * @param targets
	 * @param brackets 
	 * @return
	 */
	protected List<String> splitTarget(PrintStream out,List<String> targets, String[] brackets) {
		out.println("class @拆分对象 {");
		for (int i = 0; i < targets.size(); i++) {
			for (String bracket : brackets) {
				String target = targets.get(i);
				String[] splits = target.split(bracket);
				if (splits.length > 1) {
					out.print(target);
					for (String split : splits) {
						if (split.length() > 0) {
							out.print("->"+split.trim());
							targets.add(i++, split.trim());
						}
					}
					targets.remove(i--);
					out.println();
				}
			}
		}
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 去掉太长的对象
	 * @param out
	 * @param targets
	 * @param length 
	 * @return
	 */
	protected List<String> deleteLongTarget(PrintStream out,List<String> targets, int length) {
		out.println("class @去掉太长的对象 {");
		for (int i = 0; i < targets.size(); i++) {
			String target = targets.get(i);
			if (target.length() > length) {
				targets.remove(i--);
				out.println(target);
			}
		}
		out.println("targets = "+targets.size());
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 排除重复
	 * @param out
	 * @param targets
	 * @return
	 */
	protected List<String> deleteRepetitive(PrintStream out,List<String> targets) {
		out.println("class @排除重复 {");
		Set<String> settargets = new HashSet<>();
		for (String target : targets) {
			settargets.add(target);
		}
		out.println("targets = "+targets.size()+"->"+settargets.size());
		targets = new ArrayList<String>(settargets);
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 替换指定文字为空
	 * @param out
	 * @param targets
	 * @return
	 */
	protected List<String> replaceSpecifyText(PrintStream out,List<String> targets,String[] replaces) {
		out.println("class @替换指定对象内容为空 {");
		out.print("=>");
		for (String replace : replaces) {
			out.print(replace+" ");
		}
		out.println();
		for (int i = 0; i < targets.size(); i++) {
			String target = targets.get(i);
			for (String replace : replaces) {
				if (target.contains(replace)) {
					String replaceed = target.replace(replace, "").trim();
					out.print(target);
					if (replaceed.length() > 0) {
						targets.set(i, replaceed);
						out.println("->"+targets.get(i));
					}else {
						targets.remove(i--);
						out.println("->"+"delete");
					}
					break;
				}
			}
		}
		out.println("targets = "+targets.size());
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 去掉包含指定文字的对象
	 * @param out
	 * @param targets
	 * @param deletes 
	 * @return
	 */
	protected List<String> deleteSpecifyText(PrintStream out,List<String> targets, String[] deletes) {
		out.println("class @去掉包含指定文字的对象 {");
		out.print("=>");
		for (String delete : deletes) {
			out.print(delete+" ");
		}
		out.println();
		for (int i = 0; i < targets.size(); i++) {
			String target = targets.get(i);
			for (String delete : deletes) {
				if (target.contains(delete)) {
					out.println(target);
					targets.remove(i--);
				}
			}
		}
		out.println("targets = "+targets.size());
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 去掉一个字的对象
	 * @param out
	 * @param targets
	 * @param deletes
	 * @return
	 */
	protected List<String> deleteOneTarget(PrintStream out,List<String> targets) {
		out.println("class @去掉一个字的对象 {");
		for (int i = 0; i < targets.size(); i++) {
			String target = targets.get(i);
			if (target.length() == 1) {
				targets.remove(i--);
				out.print(target+" ");
			}
		}
		out.println(" ");
		out.println("targets = "+targets.size());
		out.println("}\r\n");
		return targets;
	}
	/**
	 * 去掉包含特殊字符的对象
	 * @param out
	 * @param targets
	 * @param splits
	 * @return
	 */
	protected List<String> deleteSpecificSymbol(PrintStream out,List<String> targets) {
		out.println("class @去掉包含特殊字符的对象 {");
		for (int i = 0; i < targets.size(); i++) {
			String target = targets.get(i);
			if (!target.matches("[\\u4E00-\\u9FA5]+")) {
				targets.remove(i--);
				out.println(target);
			}
		}
		out.println("targets = "+targets.size());
		out.println("}\r\n");
		return targets;
	}
	

}
