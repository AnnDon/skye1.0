package com.skye.core.base.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDao extends SqlSessionDaoSupport {
	Logger logger;
	protected SessionFactory hibernateSession;
	protected JdbcTemplate jdbcTemplate;

	public BaseDao() {
		this.logger = LoggerFactory.getLogger(BaseDao.class);
		this.hibernateSession = null;
	}

	

	@Autowired
	@Qualifier("jdbcTemplate")
	public void setJdbcTemplate(JdbcTemplate jdbcTempalte) {
		this.jdbcTemplate = jdbcTempalte;
	}

	public void setSessionFactory(SessionFactory sessionFacotry) {
		this.hibernateSession = sessionFacotry;
	}

	public Session getHibernateSession() {
		return this.hibernateSession.getCurrentSession();
	}

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	public SqlSession getMybatisSession() {
		return getSqlSession();
	}

	public int delete_Hibernate_ComposedQuery(String composedSQL, Object[] values) {
		return updateHibernateComposedSQL(composedSQL, values);
	}

	public int delete_Hibernate_NamedQuery(String namedQuery, Object[] values) {
		int rtnValue = -1;
		Query queryCMD = getHibernateSession().getNamedQuery(namedQuery);
		String queryString = queryCMD.getQueryString();
		Query sqlQuery = getHibernateSession().createQuery(queryString);
		if (values != null) {
			int index = 0;
			Object[] arr$ = values;
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; ++i$) {
				Object value = arr$[i$];
				sqlQuery.setParameter(index, value);
				++index;
			}
		}
		rtnValue = sqlQuery.executeUpdate();
		return rtnValue;
	}

	public <T> void delete_Hibernate_Object(T entity) {
		getHibernateSession().delete(entity);
	}

	public int delete_JDBC(String sqlString, Object[] values) {
		return this.jdbcTemplate.update(sqlString, values);
	}

	public <T> int delete_myBatis(String sqlId, T entity) {
		return getSqlSession().delete(sqlId, entity);
	}

	public <T> void deleteById_Hibernate_Object(Class<T> paramClass,Serializable entityId) {
		delete_Hibernate_Object(findByID_Hibernate_Get(paramClass, entityId));
	}

	public <T> Object[] find_Hibernate_ComposedHQL(String hql, String[] property, String[] operators, Object[] values,
			int offset, int size, boolean isTotalSize, String orderBy, String groupBy, String otherCause) {
		long l1 = System.currentTimeMillis();
		StringBuffer countSql = new StringBuffer();
		StringBuffer fullSql = new StringBuffer();
		Integer count = new Integer(0);
		StringBuffer where = new StringBuffer();
		Query query = null;
		Query queryCount = null;
		if ((property != null) && (property.length > 0) && (values != null) && (values.length > 0)) {
			for (int i = 0; i <= property.length - 1; ++i) {
				if (where.length() == 0)
					where = new StringBuffer(" where ");
				else
					where.append(" and ");
				if ((operators != null) && (operators[i].equalsIgnoreCase("isnull")))
					where.append(property[i]).append(" is null ");
				else if ((operators != null) && (operators[i].equalsIgnoreCase("isnotnull")))
					where.append(property[i]).append(" is not null ");
				else if ((operators != null) && (operators[i].equalsIgnoreCase("isempty")))
					where.append(property[i]).append(" ='' ");
				else if ((operators != null) && (operators[i].equalsIgnoreCase("isnotempty")))
					where.append(property[i]).append(" <>'' ");
				else if ((operators != null) && (operators[i].equalsIgnoreCase("like")))
					where.append(property[i]).append(" like ").append(values[i]);
				else
					where.append(property[i]).append(" " + operators[i]).append(" ? ");
			}
			fullSql = new StringBuffer(hql + where);
			if (groupBy != null)
				fullSql.append(" group by " + groupBy);
			if (orderBy != null)
				fullSql.append(" order by " + orderBy);
			if (otherCause != null)
				fullSql.append(" " + otherCause);
			query = getHibernateSession().createQuery(fullSql.toString());

			int paramIndex = 0;
			for (int i = 0; i < values.length - 1; ++i) {
				if ((operators != null) && (operators[i].equalsIgnoreCase("isnull")))
					if ((operators != null) && (operators[i].equalsIgnoreCase("isnotnull")))
						if ((operators != null) && (operators[i].equalsIgnoreCase("isempty")))
							if ((operators != null) && (operators[i].equalsIgnoreCase("isnotempty")))
								if ((operators != null) && (operators[i].equalsIgnoreCase("like")))
									query.setParameter(paramIndex, values[i]);
				++paramIndex;
			}
		} else {
			fullSql = new StringBuffer(hql + where);
			if (groupBy != null) {
				fullSql.append(" group by " + groupBy);
			}
			if (orderBy != null) {
				fullSql.append(" order by " + orderBy);
			}
			if (otherCause != null) {
				fullSql.append(" " + otherCause);
			}
			if (isTotalSize) {
				countSql = new StringBuffer(hql + where);
				if (groupBy != null) {
					countSql.append(" group by " + groupBy);
				}
				if (otherCause != null) {
					countSql.append(" " + otherCause);
				}

				String tempSQL = countSql.toString();
				countSql = new StringBuffer("select count(*) from ");
				countSql.append(tempSQL.substring(tempSQL.toLowerCase(Locale.ENGLISH).indexOf("from") + 5));
				queryCount = getHibernateSession().createQuery(countSql.toString());
				int paramIndex = 0;
				if (values != null) {
					for (int i = 0; i <= values.length - 1; ++i) {
						if ((operators != null) && (operators[i].equalsIgnoreCase("isnull"))) {
							break;
						}
						if ((operators != null) && (operators[i].equalsIgnoreCase("isnotnull"))) {
							break;
						}
						if ((operators != null) && (operators[i].equalsIgnoreCase("isempty"))) {
							break;
						}
						if ((operators != null) && (operators[i].equalsIgnoreCase("isnotempty"))) {
							break;
						}
						if ((operators != null) && (operators[i].equalsIgnoreCase("like"))) {
							break;
						}
						queryCount.setParameter(paramIndex, values[i]);
						++paramIndex;
					}
					this.logger.info("countSql = " + queryCount.getQueryString());
				}

			}

		}
		if ((offset >= 0) && (size > 0)) {
			query.setFirstResult(offset);
			query.setMaxResults(size);
		}
		List result = query.list();
		if ((isTotalSize) && (result != null) && (!result.isEmpty()))
			count = new Integer(String.valueOf(query.list().get(0)));
		this.logger.info("fullSql = " + query.getQueryString());
		long l2 = System.currentTimeMillis();
		this.logger.info("Execue query time:" + (l2 - l1) + "ms");
		Object[] o = { result, count };
		return o;
	}
	public Object[] find_Hibernate_NamedQuery(String queryID,Object[] params,
			int offset,int size,boolean isTotalSize){
		Query query = getHibernateSession().getNamedQuery(queryID);
		setQueryParameter(query,params);
		if ((offset >= 0) && (size > 0)){
			query.setFirstResult(offset);
			query.setMaxResults(size);
		}
		Integer count = Integer.valueOf("0");
		List result = query.list();
		if ((isTotalSize) && (result != null) && (!(result.isEmpty()))){
			String queryString = query.getQueryString();
			StringBuffer countQueryString = new StringBuffer();
			countQueryString.append("select count(*) from ").append(queryString.substring(queryString.toLowerCase(Locale.ENGLISH).indexOf("from") + 5));
			Query countQuery = getHibernateSession().createQuery(countQueryString.toString());
			setQueryParameter(countQuery,params);
		}
		Object[] objs = {result,count};
		return objs;
	}
	
	public <T> List<T> find_myBatis(String sqlId,Object paramObject){
		return find_myBatis(sqlId, paramObject);
		//return find_myBatis(sqlId,paramObject,-1,-1);
	}

//	public <T> List<T> findAll_myBatis(String sqlId,int offset,int limit){
//	
//		return find_myBatis(sqlId,null,offset,limit);
//	}



	public <T> List<T> find_myBatis(String sqlId,Object paramObject,boolean isColNameToUpperCase){
		return find_myBatis(sqlId,paramObject,isColNameToUpperCase);
	}
	
//	public <T> List<T> findAll_myBatis(String sqlId,int offset,int limit,boolean isColNameToUpperCase){
//		return find_myBatis(sqlId,null,offset,limit,isColNameToUpperCase);
//	}



	public <T> List<T> findAll_Hibernate_ComposedHQL(String composedHQL){
		return queryHibernateCommosedHQL(composedHQL,null);
	}

	public <T> List<T> findAll_Hibernate_NamedQuery(String namedQuery){
		Query queryCMD = getHibernateSession().getNamedQuery(namedQuery);
		String queryString = queryCMD.getQueryString();
		return queryHibernateCommosedHQL(queryString,null);
	}
	
	public <T> List<T> findAll_Hibernate_Object(Class<T> paramClass){
		Criteria criteria = getHibernateSession().createCriteria(paramClass);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	public List findAll_JDBC(String sqlString){
		return this.jdbcTemplate.queryForList(sqlString);
	}
	
	public Object findByID_Hibernate_ComposedHQL(String ComposedHQL,Serializable entityId){
		Object vo = null;
		List resultList = queryHibernateCommosedHQL(ComposedHQL,new Object[]{entityId});
		Iterator it = resultList.iterator();
		if(it.hasNext()) vo = it.next();
		if(vo!=null) getHibernateSession().refresh(vo);
		return vo;
	}
	
	public <T> T findByID_Hibernate_Get(Class<T> paramClass,Serializable entityId){
	    Object vo = null;
	    vo = getHibernateSession().get(paramClass, entityId);
	    if(vo!=null) getHibernateSession().refresh(vo);
	    return (T) vo;
	}
	public Object findByID_Hibernate_Load(Class<Object> paramClass,Serializable entityId){
	    Object vo = null;
	    vo = getHibernateSession().load(paramClass, entityId);
	    if(vo!=null) getHibernateSession().refresh(vo);
	    return vo;
	}
	
	public Object findByID_Hibernate_NamedQuery(String namedQuery,Serializable entityId){
		Object vo = null;
		Query queryCMD = getHibernateSession().getNamedQuery(namedQuery);
		String queryString = queryCMD.getQueryString();
		List resultList = queryHibernateCommosedHQL(queryString,new Object[]{entityId});
		Iterator it = resultList.iterator();
		if(it.hasNext()) vo = it.next();
		if(vo!=null) getHibernateSession().refresh(vo);
		return vo;
	}
	
	public <T> T findByID_myBatis(String sqlId,Serializable entityId){
		return findByID_myBatis(sqlId,entityId,true);
	}
	public <T> T findByID_myBatis(String sqlId,Serializable entityId,boolean isColNameToUpperCase){
		Object rtnValue = getSqlSession().selectOne(sqlId, entityId);
		if((rtnValue!=null)&&(Map.class.isAssignableFrom(rtnValue.getClass()))){
			Map tempValue = (Map) rtnValue;
			if(isColNameToUpperCase){
				if(isMapKeyToUpperCase(tempValue))
					rtnValue = convertMapKeyToUpperCase(tempValue);
			}else if(isMapKeyToLowerCase(tempValue)){
				rtnValue = convertMapKeyToLowerCase(tempValue);
			}
		}
		return (T) rtnValue;
	}
	
	public int sqve_Hibernate_ComposedQuery(String composedSQL,Object[] values){
		return updateHibernateComposedSQL(composedSQL,values);
	}
	public <T> void save_Hibernate_Object(T entity){
		getHibernateSession().merge(entity);
	}
	public int save_JDBC(String sqlString,Object[] values){
		return this.jdbcTemplate.update(sqlString, values);
	}
	public int save_myBatis(String sqlId){
		return getSqlSession().insert(sqlId, null);
	}
	public <T> int save_myBatis(String sqlId,T entity){
		return getSqlSession().insert(sqlId, entity);
	}
	
	public int update_Hibernate_ComposedQuery(String composedSQL,Object[] values){
		return updateHibernateComposedSQL(composedSQL,values);
	}
	public int update_Hibernate_NamedQuery(String namedSql,Object[] values){
		Query queryCMD = getHibernateSession().getNamedQuery(namedSql);
		String queryString = queryCMD.getQueryString();
		Query sqlq = getHibernateSession().createQuery(queryString);
		if(values!=null){
			int index=0;
			Object[] arr$ = values;
			int len$ = arr$.length;
			for(int i$=0;i$<len$;++i$){
				Object value = arr$[i$];
				sqlq.setParameter(index, value);
				++index;
			}
		}
		return sqlq.executeUpdate();
	}
	public <T> void update_Hibernate_Object(T entity){
		getHibernateSession().update(entity);
	}
	
	public int update_JDBC(String sqlString,Object[] values){
		return this.jdbcTemplate.update(sqlString, values);
	}
	public <T> int update_myBatis(String sqlId,T entity){
		return getSqlSession().update(sqlId, entity);
	}
	public <T> List<T> findAll_myBatis(String sqlId){
		return findAll_myBatis(sqlId,null,-1,-1);
	}
	public <T> List<T> findAll_myBatis(String sqlId,boolean isColNameToUpperCase){
		return find_myBatis(sqlId,null,isColNameToUpperCase);
	}
	public <T> List<T> findAll_myBatis(String sqlId,Object paramObject,int offset,int size){
		return findAll_myBatis(sqlId,paramObject,offset,size,true);
	}
	public <T> List<T> findAll_myBatis(String sqlId,Object paramObject,int offset,int size,boolean isColNameToUpperCase){
		List resultList = null;
		if((offset>=0)&&(size>0)){
			RowBounds rowBounds = new RowBounds(offset, size);
			resultList = getSqlSession().selectList(sqlId, paramObject,rowBounds);
		}else{
			resultList = getSqlSession().selectList(sqlId, paramObject);
		}
		if(isColNameToUpperCase){
			if(isToUpperCase(resultList))
				resultList = convertListKeyToLowerCase(resultList);
		}else if(isToLowerCase(resultList)){
			resultList = convertListKeyToLowerCase(resultList);
		}
		return resultList;
	}
	private int updateHibernateComposedSQL(String composedSQL,Object[] values){
		int rtnValue = -1;
		Query sqlQuery = getHibernateSession().createQuery(composedSQL);
		int index = 0;
		if(values!=null){
			Object[] arr$ = values;
			int len$ = arr$.length;
			for(int i$=0;i$<len$;++i$){
				Object value = arr$[i$];
				sqlQuery.setParameter(index, value);
				++index;
			}
		}
		rtnValue = sqlQuery.executeUpdate();
		return rtnValue;
	}
	private <T> List<T> queryHibernateCommosedHQL(String composedHQL,Object[] values){
		Query sqlQuery = getHibernateSession().createQuery(composedHQL);
		int index = 0;
		if(values!=null){
			Object[] arr$ = values;
			int len$ = arr$.length;
			for(int i$=0;i$<len$;++i$){
				Object value = arr$[i$];
				sqlQuery.setParameter(index, value);
				++index;
			}
		}
		return sqlQuery.list();
	}
	private void setQueryParameter(Query query,Object[] values){
		if(values!=null){
			int index = 0;
			Object[] arr$ = values;
			int len$ = arr$.length;
			for(int i$=0;i$<len$;++i$){
				Object value = arr$[i$];
				query.setParameter(index, value);
				++index;
			}
		}
	}
	private boolean isToUpperCase(Object resultObj){
		boolean rtnValue = false;
		if(resultObj!=null){
			if(List.class.isAssignableFrom(resultObj.getClass())){
				List rtnList = (List) resultObj;
				if(rtnList.size()>0){
					Object oneRecord = rtnList.get(0);
					if(oneRecord!=null&&(Map.class.isAssignableFrom(oneRecord.getClass()))){
						Map oneMap = (Map) oneRecord;
						rtnValue = isMapKeyToLowerCase(oneMap);
					}
				}
			}else if(Map.class.isAssignableFrom(resultObj.getClass())){
				rtnValue = isMapKeyToUpperCase((Map)resultObj);
			}
		}
		return rtnValue;
	}
	private boolean isToLowerCase(Object resultObj){
		boolean rtnValue = false;
		if(resultObj!=null){
			if(List.class.isAssignableFrom(resultObj.getClass())){
				List rtnList = (List) resultObj;
				if(rtnList.size()>0){
					Object oneRecord = rtnList.get(0);
					if(oneRecord!=null&&(Map.class.isAssignableFrom(oneRecord.getClass()))){
						Map oneMap = (Map) oneRecord;
						rtnValue = isMapKeyToUpperCase(oneMap);
					}
				}
			}else if(Map.class.isAssignableFrom(resultObj.getClass())){
				rtnValue = isMapKeyToLowerCase((Map)resultObj);
			}
		}
		return rtnValue;
	}
	private boolean isMapKeyToUpperCase(Map<String, Object> oneMap){
		boolean rtnValue = false;
		if(oneMap!=null){
			Iterator oneRecordKeys = oneMap.keySet().iterator();
			if(oneRecordKeys.hasNext()){
				String oneKey = (String) oneRecordKeys.next();
				if(!(oneKey.equals(oneKey.toUpperCase())))
					rtnValue = true;
			}
		}
		return rtnValue;
	}
	private boolean isMapKeyToLowerCase(Map<String, Object> oneMap){
		boolean rtnValue = false;
		if(oneMap!=null){
			Iterator oneRecordKeys = oneMap.keySet().iterator();
			if(oneRecordKeys.hasNext()){
				String oneKey = (String) oneRecordKeys.next();
				if(!(oneKey.equals(oneKey.toLowerCase())))
					rtnValue = true;
			}
		}
		return rtnValue;
	}
	private List<Map<String,Object>> convertListKeyToUpperCase(List<?> list){
		List rtnValue = null;
		if((list !=null)&&(list.size()>0)){
			rtnValue = new ArrayList();
			for(int index=0;index<list.size();++index){
				Map oneRecord = (Map) list.get(index);
				if(oneRecord!=null)
					rtnValue.add(convertMapKeyToUpperCase(oneRecord));
				else 
					rtnValue.add(null);
			}
		}
		return rtnValue;
	}
	private Map<String,Object> convertMapKeyToUpperCase(Map<String,Object> oneRecord){
		Map rtnValue = new HashMap();
		Iterator keys = oneRecord.keySet().iterator();
		while(keys.hasNext()){
			String keyStr = (String) keys.next();
			Object value = oneRecord.get(keyStr);
			rtnValue.put(keyStr.toUpperCase(),value);
		}
		return rtnValue;
	}
	private Map<String,Object> convertMapKeyToLowerCase(Map<String,Object> oneRecord){
		Map rtnValue = new HashMap();
		Iterator keys = oneRecord.keySet().iterator();
		while(keys.hasNext()){
			String keyStr = (String) keys.next();
			Object value = oneRecord.get(keyStr);
			rtnValue.put(keyStr.toLowerCase(),value);
		}
		return rtnValue;
	}
	private List<Map<String,Object>> convertListKeyToLowerCase(List<?> list){
		List rtnValue = null;
		if((list !=null)&&(list.size()>0)){
			rtnValue = new ArrayList();
			for(int index=0;index<list.size();++index){
				Map oneRecord = (Map) list.get(index);
				if(oneRecord!=null)
					rtnValue.add(convertMapKeyToLowerCase(oneRecord));
				else 
					rtnValue.add(null);
			}
		}
		return rtnValue;
	}
	
}
