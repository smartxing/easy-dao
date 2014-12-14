package com.core.util;

import java.lang.reflect.Method;

public class BeanUtils {

	public static Object getProperty(Object obj, String field, boolean isBool) {
		String methodName = "";
		if (isBool) {
			methodName = "is".concat(StringUtils.firstUpper(field));
		} else {
			methodName = "get".concat(StringUtils.firstUpper(field));
		}
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			throw new IllegalArgumentException("方法不存在或拒绝访问: " + methodName, e);
		}

	}
	
	public static void setProperty(Object obj,String property,Object value,Class<?> parameterTypes)
	{
		String methodName = "set".concat(StringUtils.firstUpper(property));
		Method method;
		try {
			method = obj.getClass().getMethod(methodName, parameterTypes);
			method.setAccessible(true);
			method.invoke(obj, value);
		}  catch (Exception e) {
			throw new IllegalArgumentException("方法不存在或拒绝访问: " + methodName, e);
		}

	}

}
