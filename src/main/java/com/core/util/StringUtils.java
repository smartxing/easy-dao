package com.core.util;

import java.util.Arrays;

public class StringUtils {

	public static String repeat(Object obj, String separator, int repeat) {
		Object[] array = new Object[repeat];
		Arrays.fill(array, obj);
		return join(array, separator);
	}
	
	public static String repeat(String str, int repeat) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < repeat; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static String join(Object[] array, String separator) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < array.length; i++) {
			if(i > 0) sb.append(separator);
			sb.append(array[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 特殊的字符串连接方法, 在每个对象后追加 append
	 * @param array
	 * @param append
	 * @param separator
	 * @return
	 */
	public static String join(Object[] array, String append, String separator) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < array.length; i++) {
			if(i > 0) sb.append(separator);
			sb.append(array[i]).append(append);
		}
		return sb.toString();
	}
	
	public static String firstUpper(String str) {
		if(isEmpty(str)) return "";
		
		char[] array = str.toCharArray();
		array[0] = Character.toUpperCase(array[0]);
		return String.valueOf(array);
	}
	
	public static String firstLower(String str) {
		if(isEmpty(str)) return "";
		
		char[] array = str.toCharArray();
		array[0] = Character.toLowerCase(array[0]);
		return String.valueOf(array);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 将字符串从驼峰标识法转换为短横线分割法
	 * @param field 要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String convertField(String str) {
		StringBuilder sb = new StringBuilder(str);
		for(int i = sb.length() - 1; i >= 0; i--) {
			char c = sb.charAt(i);
			if(Character.isUpperCase(c)) {
				sb.setCharAt(i, Character.toLowerCase(c));
				sb.insert(i, '_');
			}
		}
		return sb.toString();
	}
}

