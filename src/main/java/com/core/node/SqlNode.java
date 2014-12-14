package com.core.node;

/**
 * 
 * @author lb.x 
 *
 */
public interface SqlNode {
	
	/**
	 * 生成sql查询语句
	 * @return sql语句
	 */
	public String getSql();
	
	/**
	 * 保留对应的sql查询语句的参数
	 * @return sql param
	 */
	public Object[] getParam();

}
