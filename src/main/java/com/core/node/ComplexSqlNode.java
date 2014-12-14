package com.core.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.core.util.StringUtils;

/**
 * 复杂节点比如and,or 
 * @author Administrator
 *
 */
public class ComplexSqlNode implements SqlNode {

	private List<SqlNode> children = new ArrayList<SqlNode>();
	private List<Object> params = new ArrayList<Object>();
	private String separator;
	private boolean hasBrackets;

	public ComplexSqlNode(String separator, boolean hasBrackets,
			SqlNode... nodes) {
		this.separator = separator;
		this.hasBrackets = hasBrackets;
		Collections.addAll(children, nodes);
	}

	public ComplexSqlNode(String separator, SqlNode... nodes) {
		this(separator, true, nodes);
	}

	public Collection<SqlNode> getChildren() {
		return children;
	}

	public void append(SqlNode node) {
		children.add(node);
	}

	@Override
	public String getSql() {
		if(size() == 0)
		{
			return "";
		}
		if(size() == 1)
		{
			return children.get(0).getSql();
		}
		//select * from A.B where a=b and (a=n or a=c )
		ArrayList<String> sqls = new ArrayList<String>();
		for(int i=0;i<children.size();i++)
		{
			SqlNode node = children.get(i);
			if(node == null) continue;
			String sql = node.getSql();
			if(node instanceof ComplexSqlNode)
			{
				//A= B and A=C and (A=B or A=B)
				if(((ComplexSqlNode)node).hasBrackets)
				{
					sql = String.format("( %s )", sql);
				}
			}
			sqls.add(sql);
		}
		return StringUtils.join(sqls.toArray(), separator);
	}

	@Override
	public Object[] getParam() {
		for(SqlNode node : children) {
			if(node == null) continue;
			Collections.addAll(params, node.getParam());
		}
		return params.toArray();
	}
	
	public int size() {
		return children.size();
	}
}
