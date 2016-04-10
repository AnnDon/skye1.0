package com.skye.core.base.dao;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts(value = { @Signature(args = {  Invocation.class }, method = "intercept", type = MysqlPaginationInterceptor.class) })
public class MysqlPaginationInterceptor extends AbstractPaginationInterceptor {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pageSelect = new StringBuffer(sql.length()+100);
		pageSelect.append("SELECT * FROM ( ");
		pageSelect.append(sql);
		pageSelect.append(" ) _temptab LIMIT ");
		pageSelect.append(offset);
		pageSelect.append(" , ");
		pageSelect.append(limit);
		return pageSelect.toString();
	}

}
