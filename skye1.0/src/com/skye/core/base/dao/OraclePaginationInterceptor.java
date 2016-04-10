package com.skye.core.base.dao;

public class OraclePaginationInterceptor  extends AbstractPaginationInterceptor{

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pageSelect = new StringBuffer(sql.length()+100);
		pageSelect.append("SELECT * FROM ( select row_.*,rownum rownum_ from ( ");
		pageSelect.append(sql);
		pageSelect.append(" ) row_ ) where rownum_>").append(offset).append(" and rownum_<").append(offset+limit);
		return pageSelect.toString();
	}

}
