package com.skye.core.base.dao;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPaginationInterceptor implements Interceptor {
	private Logger log;
	public AbstractPaginationInterceptor(){
		this.log=LoggerFactory.getLogger(AbstractPaginationInterceptor.class);
	}
	public Object intercept(Invocation invocation) throws Throwable{
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		ObjectFactory objectFactory = new DefaultObjectFactory();
		ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, objectFactory, objectWrapperFactory);
		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		
		if(rowBounds==null||rowBounds==RowBounds.DEFAULT){
			return invocation.proceed();
		}
		
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		metaStatementHandler.setValue("delegate.boundSql.sql", getLimitString(originalSql,rowBounds.getOffset(),rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.boundSql.offset",Integer.valueOf(0));
		metaStatementHandler.setValue("delegate.boundSql.limit",Integer.valueOf(2147483647));
		
		if(this.log.isDebugEnabled()){
			BoundSql boundSql = statementHandler.getBoundSql();
			this.log.debug("生成分业sql："+boundSql);
		}
		return invocation.proceed();
	}
	public Object plugin(Object target){
		return Plugin.wrap(target, this);
	}
	public void setProperties(Properties prop){}
	public abstract String getLimitString(String originalSql, int offset, int limit);
}
