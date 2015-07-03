package com.poetry.database.util;

import java.util.regex.Pattern;

/**
 * 字符串常用工具类
 * @author 树朾
 */
public class AfStringUtil {

	public static boolean isEmpty(String string) {
		// TODO Auto-generated method stub
		return string == null || string.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String string) {
		// TODO Auto-generated method stub
		return string != null && string.trim().length() > 0;
	}

	public static boolean equals(String l,String r){
		if (l != r) {
			if (l != null || r != null) {
				return l.equals(r);
			}
			return false;
		}
		return true;
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * @param email
	 * @return 是否合法
	 */
	public static boolean emailFormat(String email) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return Pattern.compile(pattern).matcher(email).find();
	}
	
}
