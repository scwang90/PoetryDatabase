package com.poetry.database.dao.base;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.poetry.database.annotations.Sql;
import com.poetry.database.annotations.dbmodel.interpreter.Interpreter;
import com.poetry.database.factory.C3P0Factory;
import com.poetry.database.util.AfReflecter;
import com.poetry.database.util.AfStackTrace;

public class BaseDaoDbUtilImpl<T> implements MultiDao<T>{
	
	protected class Model{
		public String id;//数据表主键
		public String table;//数据库表名
		public Field idfield;//数据表主键字段
		public Field[] fields;//所有数据库字段
		/**
		 * insert into table(${column}) values(${values})
		 * name,sex
		 */
		public String column;
		/**
		 * insert into table(${column}) values(${values})
		 * ?,?
		 */
		public String values;
		/**
		 * update table set ${seters} where id=?
		 * name=?,sex=?
		 */
		public String seters;
		
	}

	protected Class<T> clazz;

	protected Model model;

	protected BeanHandler<T> beanHandler;
	protected BeanListHandler<T> beanListHandler;
	protected BeanHandler<Integer> beanintHandler = new BeanHandler<Integer>(Integer.class);
	
	protected QueryRunner qr = new QueryRunner(C3P0Factory.getDataSource());

	public BaseDaoDbUtilImpl() {
		// TODO Auto-generated constructor stub
		clazz = AfReflecter.getActualTypeArgument(this, BaseDaoDbUtilImpl.class, 0);
		beanHandler = new BeanHandler<T>(clazz,new BasicRowProcessorTemplate<T>(clazz));
		beanListHandler = new BeanListHandler<T>(clazz,new BasicRowProcessorTemplate<T>(clazz));
		model = loadModel(clazz);
	}
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	private BaseDaoDbUtilImpl<T>.Model loadModel(Class<T> clazz) {
		// TODO Auto-generated method stub
		Model model = new Model();
		
		List<Field> fields = new ArrayList<Field>();
		for (Field field : AfReflecter.getField(clazz)) {
			if (Interpreter.isColumn(field)) {
				fields.add(field);
			}
		}
		
		model.id = Interpreter.getIdName(clazz);
		model.table = Interpreter.getTableName(clazz);
		model.idfield = Interpreter.getIdField(clazz);
		model.fields = fields.toArray(new Field[0]);
		model.seters = getSeters(model.fields);
		model.column = getColumn(model.fields);
		model.values = getValues(model.fields);
		return model;
	}

	/**
	 * 根据 fields 拼出 insert table(${column}) values(${values})
	 * insert into table(${column}) values(${values})
	 * @param fields
	 * @return ?,?
	 */
	private String getValues(Field[] fields) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < fields.length; i++) {
			buffer.append("?,");
		}
		buffer.setLength(buffer.length()-1);
		return buffer.toString();
	}
	/**
	 * 根据 fields 拼出 insert table(${column}) 
	 * insert into table(name,sex) values(${values})
	 * @param fields
	 * @return name,sex
	 */
	private String getColumn(Field[] fields) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		for (Field field : fields) {
			buffer.append(Interpreter.getColumnName(field));
			buffer.append(",");
		}
		buffer.setLength(buffer.length()-1);
		return buffer.toString();
	}
	/**
	 * 根据T的数据拼出 update set name='hello',sex=false 对应的  set string
	 * @param fields 
	 * @param t
	 * @return name=?,sex=?
	 * @throws Exception 
	 */
	protected String getSeters(Field[] fields) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		for (Field field : fields) {
			buffer.append(Interpreter.getColumnName(field));
			buffer.append("=?,");
		}
		buffer.setLength(buffer.length()-1);
		return buffer.toString();
	}
	/**
	 * 根据T的数据拼出 insert values(?,?,?,?) 对应的  Object[]
	 * @param t
	 * @return 
	 * @throws Exception 
	 */
	private Object[] getValues(T t) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<Object>();
		for (Field field : model.fields) {
			field.setAccessible(true);
			list.add(field.get(t));
		}
		return list.toArray(new Object[0]);
	}

	/**
	 * 根据T的数据拼出 insert values(?,?,?,?) 对应的  Object[]
	 * @param t
	 * @return 
	 * @throws Exception 
	 */
	private List<Object> getValuesAsList(T t) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<Object>();
		for (Field field : model.fields) {
			field.setAccessible(true);
			list.add(field.get(t));
		}
		return list;
	}
	/**
	 * 获取对象 t 的 Id
	 * @param t
	 * @return 
	 * @throws Exception 
	 */
	private Object getId(T t) throws Exception {
		// TODO Auto-generated method stub
		Field field = model.idfield;
		field.setAccessible(true);
		return field.get(t);
	}
	
	@Override
	@Sql("insert into ${table}(${column}) values(${values})")
	public int insert(T t) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${column}", String.valueOf(model.column));
		sql = sql.replace("${values}", String.valueOf(model.values));
		return qr.update(sql,getValues(t));
	}

	@Override
	public int delete(Object id) throws Exception {
		// TODO Auto-generated method stub
		return deleteByPropertyName(model.id, id);
	}

	@Override
	@Sql("update ${table} set ${seters} where ${id}=?")
	public int update(T t) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${seters}", String.valueOf(model.seters));
		sql = sql.replace("${id}", String.valueOf(model.id));
		List<Object> args = getValuesAsList(t);
		args.add(getId(t));
		return qr.update(sql,args.toArray(new Object[0]));
	}

	@Sql("select count(*) from ${table}")
	public int countAll() throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		Long count = qr.query(sql,new ScalarHandler<Long>());
		return Integer.valueOf(String.valueOf(count));
	}

	@Override
	public T findById(Object id) throws Exception {
		// TODO Auto-generated method stub
		for (T t : findByPropertyName(model.id, id)) {
			return t;
		}
		return null;
	}

	@Override
	@Sql("select * from ${table}")
	public List<T> findAll() throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		return qr.query(sql,beanListHandler);
	}
	
	@Override
	@Sql("select * from ${table} limit ${start},${limit}")
	public List<T> findByPage(int limit, int start) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${start}", String.valueOf(start));
		sql = sql.replace("${limit}", String.valueOf(limit));
		return qr.query(sql,beanListHandler);
	}

	@Override
	@Sql("delete from ${table} ${where}")
	public int deleteWhere(String where) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${where}", String.valueOf(where));
		return qr.update(sql);
	}

	@Override
	@Sql("delete from ${table} where ${propertyName} = ?")
	public int deleteByPropertyName(String propertyName,Object value) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${propertyName}", String.valueOf(propertyName));
		return qr.update(sql,value);
	}

	@Override
	@Sql("select count(*) from ${table} ${where}")
	public int countWhere(String where) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${where}", String.valueOf(where));
		Long count = qr.query(sql,new ScalarHandler<Long>());
		return Integer.valueOf(String.valueOf(count));
	}

	@Override
	@Sql("select count(*) from ${table} where ${propertyName} = ?")
	public int countByPropertyName(String propertyName,Object value) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${propertyName}", String.valueOf(propertyName));
		Long count = qr.query(sql,new ScalarHandler<Long>(),value);
		return Integer.valueOf(String.valueOf(count));
	}

	@Override
	@Sql("select * from ${table} ${where}")
	public List<T> findWhere( String where) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${where}", String.valueOf(where));
		return qr.query(sql,beanListHandler);
	}

	@Override
	@Sql("select * from ${table} ${where} limit ${start},${limit}")
	public List<T> findWhereByPage(String where, int limit,int start) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${where}", String.valueOf(where));
		sql = sql.replace("${start}", String.valueOf(start));
		sql = sql.replace("${limit}", String.valueOf(limit));
		return qr.query(sql,beanListHandler);
	}

	@Override
	@Sql("select * from ${table} where ${propertyName} = ?")
	public List<T> findByPropertyName(String propertyName,Object value) throws Exception {
		// TODO Auto-generated method stub
		Sql sqlann = AfStackTrace.getCurrentMethodAnnotation(Sql.class);
		String sql = sqlann.value().replace("${table}", model.table);
		sql = sql.replace("${propertyName}", String.valueOf(propertyName));
		return qr.query(sql,beanListHandler,value);
	}
	
	public static class BasicRowProcessorTemplate<T> extends BasicRowProcessor{

		public BasicRowProcessorTemplate(Class<T> clazz) {
			super(new BeanProcessorTemplate<T>(clazz));
		}
		
	}

	public static class BeanProcessorTemplate<T> extends BeanProcessor {

		protected Class<T> clazz;
		
		public BeanProcessorTemplate() {
			// TODO Auto-generated constructor stub
			clazz = AfReflecter.getActualTypeArgument(this, BeanProcessorTemplate.class, 0);
		}
		
		public BeanProcessorTemplate(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		/**
		 * 重写BeanProcessor的实现,使用策略模式
		 */
		protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
				PropertyDescriptor[] props) throws SQLException {

			int cols = rsmd.getColumnCount();
			int columnToProperty[] = new int[cols + 1];
			Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

			for (int col = 1; col <= cols; col++) {
				String columnName = rsmd.getColumnLabel(col);
				if (null == columnName || 0 == columnName.length()) {
					columnName = rsmd.getColumnName(col);
				}
				for (int i = 0; i < props.length; i++) {
					if (match(columnName, props[i].getName())) {// 与BeanProcessor不同的地方
						columnToProperty[col] = i;
						break;
					}
				}
			}

			return columnToProperty;
		}

		/**
		 * 驼峰转换的匹配器
		 */
		private boolean match(String column, String name) {
			// TODO Auto-generated method stub
			Field field = AfReflecter.getFieldNoException(clazz, name);
			if (field != null && Interpreter.getColumnName(field).equals(column)) {
				return true;	
			}
			name = name.replace("-", "").toLowerCase(Locale.ENGLISH);
			column = column.replace("-", "").toLowerCase(Locale.ENGLISH);
			return column.toString().equals(name);
		}
	}
}
