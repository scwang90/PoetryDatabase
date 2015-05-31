package com.poetry.database.feature;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.poetry.database.dao.base.BaseDao;
import com.poetry.database.dao.base.BaseDaoDbUtilImpl;
import com.poetry.database.model.Author;

public class SettleAuthor extends Settle{


	private BaseDao<Author> mBaseDaoAuthor = new BaseDaoDbUtilImpl<Author>(){};

	/**
	 * 获取所有的诗词 的诗人
	 * @param list
	 * @return
	 */
	private List<String> findAllAuthors(PrintStream out,List<Author> list) {
		out.println("class @获取所有的诗词 的诗人{");
		List<String> authors = new ArrayList<String>();
		for (Author author : list) {
			authors.add(author.getAuthor().replace(" ", ""));
		}
		out.println("authors = "+authors.size());
		out.println("}\r\n");
		return authors;
	}


	public void settle(PrintStream out) throws Exception {

		List<String> authors = new ArrayList<String>(); 
		//----------------------------------------获取所有的诗词 的诗人
		authors = this.findAllAuthors(out, mBaseDaoAuthor.findAll());
		//----------------------------------------拆分诗人
		String[] splits = {"·","、"};
		authors = this.splitTarget(out, authors, splits);
		//----------------------------------------去掉太长的诗人
		authors = this.deleteLongTarget(out, authors,4);
		//----------------------------------------替换指定文字为空
		String[] replaces = {"，","。"};
		authors = this.replaceSpecifyText(out, authors, replaces);
		//----------------------------------------排除重复
		authors = this.deleteRepetitive(out, authors);
		//----------------------------------------去掉包含指定文字的诗人
		String[] deletes = {"无名","鬼"};
		authors = this.deleteSpecifyText(out, authors, deletes);
		//----------------------------------------去掉一个字的诗人
		authors = this.deleteOneTarget(out, authors);
		//----------------------------------------找出特殊符号的诗人
		authors = this.deleteSpecificSymbol(out, authors);

		//把整理的数据写到demo文件中
		this.printDemo("target/authors-demo.txt",authors);
		//把整理的数据写到demo文件中
		this.printData("target/authors.txt",authors);
	}
}
