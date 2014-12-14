package com.core.builder;

import java.util.HashMap;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.core.context.DaoContext;
import com.core.entity.Entity;

public class BeanHandlerManager {

	private static final HashMap<Class<?>, BasicRowProcessor> PROCESSOR_CACHE = new HashMap<>();
	
	public static final BasicRowProcessor getProcessor(Class<?> type) {
		BasicRowProcessor processor = PROCESSOR_CACHE.get(type);
		if(processor == null) {
			Entity entity = DaoContext.getEntity(type);
			processor = new BasicRowProcessor(new BeanProcessor(entity.getMapper()));
			PROCESSOR_CACHE.put(type, processor);
		}
		return processor;
	}
	
	public static final <T> BeanHandler<T> getBeanHandler(Class<T> type) {
		return new BeanHandler<>(type, getProcessor(type));
	}
	
	public static final <T> BeanListHandler<T> getBeanListHandler(Class<T> type) {
		return new BeanListHandler<>(type, getProcessor(type));
	}
	
}
