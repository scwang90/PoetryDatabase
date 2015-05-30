package com.poetry.database.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.junit.Before;
import org.junit.Test;

import com.poetry.database.annotations.Sql;
import com.poetry.database.dao.base.BaseDao;
import com.poetry.database.dao.base.BaseDaoDbUtilImpl;
import com.poetry.database.factory.C3P0Factory;
import com.poetry.database.model.Author;
import com.poetry.database.model.Poetry;
import com.poetry.database.util.AfCharset;
import com.poetry.database.util.AfStackTrace;
import com.poetry.database.util.JacksonUtil;

public class DaoTest {

	private QueryRunner mrunner;
	private BaseDao<Author> mBaseDaoAuthor;
	private BaseDao<Poetry> mBaseDaoPoetry;

	@Before
	public void init(){
		mrunner = new QueryRunner(C3P0Factory.getDataSource());
		mBaseDaoAuthor = new BaseDaoDbUtilImpl<Author>(){};
		mBaseDaoPoetry = new BaseDaoDbUtilImpl<Poetry>(){};
	}
	
	@Test
	@Sql("select * from t_author")
	public void runnerAuthor() throws SQLException{
		String sql = AfStackTrace.getCurrentMethodAnnotation(Sql.class).value();
		Object[] query = mrunner.query(sql, new ArrayHandler());
		for (Object object : query) {
			System.out.println(JacksonUtil.toJson(object));
		}
//		List<Author> list = mrunner.query(sql , new BeanListHandler<Author>(Author.class,new BasicRowProcessor(new BeanProcessorTemplate<Author>(){})));
//		List<Author> list = mrunner.query(sql , new BeanListHandler<Author>(Author.class,new BasicRowProcessorTemplate<Author>(Author.class){}));
//		for (Author author : list) {
//			System.out.println(JacksonUtil.toJson(author));
//		}
	}
	@Test
	public void readAllAuthor() throws Throwable{
		InputStream stream = new FileInputStream("authors.txt");
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
//		stream.read(bytes, 2, bytes.length-2);
		stream.close();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((bytes[i] - (100*(i&1)-50))%256);
		}
//		bytes[0] = -2;
//		bytes[1] = -1;
		String authors = new String(bytes,AfCharset.GBK);
		String[] split = authors.split("_");
		for (String string : split) {
			System.out.println(string);
		}
	}

	@Test
	public void findAllAuthor() throws Throwable{
		List<Author> list = mBaseDaoAuthor.findAll();
		PrintStream print = new PrintStream("authors-demo.txt", AfCharset.GBK);
		for (Author author : list) {
			print.println(author.getAuthor());
//			System.out.println(JacksonUtil.toJson(author));
		}
		print.close();
		String authors = builderAuthorString(list);
		System.out.println(authors);
		byte[] bytes = authors.getBytes(AfCharset.GBK);
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((bytes[i] + (100*(i&1)-50))%256);
		}
//		System.out.println(bytes[0]);
//		System.out.println(bytes[1]);
		FileOutputStream stream = new FileOutputStream("authors.txt");
		stream.write(bytes);
//		stream.write(bytes,2,bytes.length-2);
		stream.close();
//		PrintStream printStream = new PrintStream(new File("authors"),AfCharset.Unicode);
//		printStream.println(authors);
//		printStream.close();
	}

	private String builderAuthorString(List<Author> list) {
		// TODO Auto-generated method stub
		StringWriter writer = new StringWriter();
		for (Author author : list) {
			writer.write(author.getAuthor());
			writer.write("_");
		}
		return writer.toString();
	}

	@Test
	public void findAllPoetry() throws Throwable{
		List<Poetry> list = mBaseDaoPoetry.findAll();
		PrintStream print = new PrintStream("titles-demo.txt", AfCharset.GBK);
		for (Poetry poetry : list) {
			print.println(poetry.getTitle());
		}
		print.close();
		String titles = builderPoetryString(list);
//		System.out.println(titles);
		byte[] bytes = titles.getBytes(AfCharset.Unicode);
//		for (int i = 0; i < bytes.length; i++) {
//			bytes[i] = (byte) ((bytes[i] + (100*(i&1)-50))%256);
//		}
		FileOutputStream stream = new FileOutputStream("titles");
		stream.write(bytes);
		stream.close();
	}

	private String builderPoetryString(List<Poetry> list) {
		// TODO Auto-generated method stub
		StringWriter writer = new StringWriter();
		for (Poetry poetry : list) {
			writer.write(poetry.getTitle());
			writer.write("_");
		}
		return writer.toString();
	}
}
