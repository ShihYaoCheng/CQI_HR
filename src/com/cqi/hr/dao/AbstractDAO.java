package com.cqi.hr.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import com.cqi.hr.entity.PagingList;
import com.cqi.hr.util.ObjectUtils;

@SuppressWarnings("unchecked")

public abstract class AbstractDAO<T> {
	@Autowired protected ConversionService conversionService;
	@Autowired protected SessionFactory sessionFactory;
	protected Logger logger = Logger.getLogger(getClass());
	protected abstract Class<T> getEntityClass();
	
	private Criteria createCriteriaByExample(Object example) {
		Class<?> cls = example.getClass();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(cls);
		c.add(Example.create(example));
		return c;
	}
	
	@SuppressWarnings("rawtypes")
	protected PagingList createPagingList(int pageSize, int page, Criteria criteria, Projection projection, ResultTransformer resultTransformer, Order... orders) throws Exception {
		if(page<=0) page = 1;

		PagingList<T> paging = new PagingList<T>();
		
		//CurrentPage
		paging.setCurrentPage(page);
		
		//TotalRecords
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select total record count.");
		}
		criteria.setProjection(Projections.rowCount());
		Number uniqueResult = (Number)criteria.uniqueResult();
		if(uniqueResult!=null) {
			paging.setTotalRecords(uniqueResult.intValue());			
		}
		criteria.setProjection(projection);
		criteria.setResultTransformer(resultTransformer==null?Criteria.ROOT_ENTITY:resultTransformer);
		if(logger.isDebugEnabled()) {
			logger.debug("End select total record count.");
		}
		
		if(orders!=null) {
			for(Order order:orders) {
				if(order==null) continue;
				criteria.addOrder(order);
			}
		}
		
		//PageSize
		if(pageSize==0) pageSize = 10;
		if(pageSize>0) {
			criteria.setFirstResult((page-1)*pageSize);
			criteria.setMaxResults(pageSize);
			criteria.setFetchSize(pageSize);
			paging.setPageSize(pageSize);
		} else {
			//PageSize
			paging.setPageSize(paging.getTotalRecords());
		}
		
		//Datas
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select data.");
		}
		paging.setDatas(criteria.list());
		if(logger.isDebugEnabled()) {
			logger.debug("End select data.");
		}
		
		//TotalPages
		paging.setTotalPages(paging.getTotalRecords()/pageSize);
		if(paging.getTotalRecords()-paging.getTotalPages()*pageSize!=0) paging.setTotalPages(paging.getTotalPages()+1);
		
		return paging;
	}
	protected PagingList<T> createPagingList(int pageSize, int page, Criteria criteria, Order... orders) throws Exception {
		return createPagingList(pageSize, page, criteria, null, null, orders);
	}
	
	protected PagingList<T> createPagingList(int pageSize, int page, Criteria criteria, String...orders) throws Exception {
		return createPagingList(pageSize, page, criteria, null, null, convertOrders(orders));
	}
	/**
	 * 將現有的List轉為pageList
	 * 
	 * */
	
	protected PagingList<T> createPagingList(int pageSize, int page, List<T> t) throws Exception {
		if(page<=0) page = 1;

		PagingList<T> paging = new PagingList<T>();
		
		//CurrentPage
		paging.setCurrentPage(page);
		paging.setTotalRecords(t.size());
		//TotalRecords
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select total record count.");
		}		
		//PageSize
		if(pageSize==0) pageSize = 10;
		if(pageSize>0) {
			paging.setPageSize(pageSize);
		} else {
			//PageSize
			paging.setPageSize(paging.getTotalRecords());
		}
		List<T> data = new ArrayList<T>();
		//Datas
		if(t.size() > 0){
			int max = (page-1)*pageSize + pageSize;
			if(t.size() < max){
				max = t.size();
			}
			for(int i =(page-1)*pageSize; i < max ; i++){
				data.add(t.get(i));
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select data.");
		}
		paging.setDatas(data);
		if(logger.isDebugEnabled()) {
			logger.debug("End select data.");
		}
		
		//TotalPages
		paging.setTotalPages(paging.getTotalRecords()/pageSize);
		if(paging.getTotalRecords()-paging.getTotalPages()*pageSize!=0) paging.setTotalPages(paging.getTotalPages()+1);
		
		return paging;
	}
	
	public PagingList<T> getPagingList(int pageSize, int page, T example, String... orders) throws Exception {
		Criteria criteria = createCriteriaByExample(example);
		List<Order> orderList = new ArrayList<Order>();
		if(orders!=null) {
			String[] tmp;
			for(String order:orders) {
				tmp = order.split(" ");
				switch (tmp.length) {
				case 0:
					break;
				case 1:
					orderList.add(Order.asc(tmp[0]));
					break;
				default:
					if("desc".equals(tmp[tmp.length-1])) {
						orderList.add(Order.desc(tmp[0]));
					} else {
						orderList.add(Order.asc(tmp[0]));
					}
					break;
				}
			}
		}

		return createPagingList(pageSize, page, criteria, orderList.toArray(new Order[orderList.size()]));
		
	}
	public List<T> getByExample(T example, String... orders) throws Exception {
		Criteria criteria = createCriteriaByExample(example);
		if(orders!=null) {
			String[] tmp;
			for(String order:orders) {
				tmp = order.split(" ");
				switch (tmp.length) {
				case 0:
					break;
				case 1:
					criteria.addOrder(Order.asc(tmp[0]));
					break;
				default:
					if("desc".equals(tmp[tmp.length-1])) {
						criteria.addOrder(Order.desc(tmp[0]));
					} else {
						criteria.addOrder(Order.asc(tmp[0]));
					}
					break;
				}
			}
		}
		return criteria.list();
	}
	
	
	public List<T> get() throws Exception {
		return sessionFactory.getCurrentSession().createCriteria(getEntityClass()).list();
	}
	public List<T> getWithOrderBy(String...orderBys) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		if(orderBys!=null) {
			String[] temp;
			for(String orderBy:orderBys) {
				if(orderBy==null) continue;
				orderBy = orderBy.trim();
				if(orderBy.indexOf(' ')!=-1) {
					temp = orderBy.split(" ");
					if (temp.length==2) {
						if(temp[1].equalsIgnoreCase("ASC")) {
							orders.add(Order.asc(temp[0]));
						} else if(temp[1].equalsIgnoreCase("DESC")) {
							orders.add(Order.desc(temp[0]));
						} else {
							//Ignore
						}
					} else {
						//Ignore
					}
				} else {
					orders.add(Order.asc(orderBy));
				}
			}
		}
		return getWithOrderBy(orders.toArray(new Order[orders.size()]));
	}
	public List<T> getWithOrderBy(Order...orderBys) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		if(orderBys!=null) {
			for(Order orderBy:orderBys) {
				if(orderBy==null) continue;
				criteria.addOrder(orderBy);
			}
		}
		return criteria.list();
	}
	public T get(Serializable id) throws Exception {
		if(id==null) return null;
		return (T)sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}
	public void saveOrUpdate(T t) throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}
	public void merge(T t) throws Exception {
		sessionFactory.getCurrentSession().merge(t);
	}
	public void persist(T t) throws Exception {
		sessionFactory.getCurrentSession().persist(t);
	}
	public void update(T t) throws Exception {
		sessionFactory.getCurrentSession().update(t);
	}
	public void saveOrUpdateAll(Collection<T> objs) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		int counter = 0;
		for(T obj:objs) {
			session.saveOrUpdate(obj);
			++counter;
			if(counter%100==0) session.flush();
		}
	}
	public void deleteAll(Collection<T> objs) throws Exception {
		if(objs==null || objs.size()==0) return;
		
		Session session = sessionFactory.getCurrentSession();
		int counter = 0;
		for(T obj:objs) {
			session.delete(obj);
			++counter;
			if(counter%100==0) session.flush();
		}
	}
	
	public void delete(T t) throws Exception {
		if(t!=null) {
			sessionFactory.getCurrentSession().delete(t);
		}
	}
	
	/**將查詢結果轉換成MAP
	 * key：idField
	 * */
	public Map<?,T> queryToMap(String idField) throws Exception {
		Map<Object,T> result = new LinkedHashMap<Object, T>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.addOrder(Order.asc(idField));
		List<T> list = criteria.list();
		for(T t : list){
			Object o = ObjectUtils.getValue(t, idField);
			if(o != null){
				result.put(o, t);
			}
		}
		return result;
	}
	
	/**將查詢結果轉換成MAP(使用指定的其中一個複合鍵當KEY)
	 * key：idField
	 * */
	public Map<?,T> queryToMap(String idField,String subIdField) throws Exception {
		Map<Object,T> result = new LinkedHashMap<Object, T>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.addOrder(Order.asc(idField));
		List<T> list = criteria.list();
		for(T t : list){
			Object embeddableId = ObjectUtils.getValue(t, idField);
			if(embeddableId != null && embeddableId.getClass().isAnnotationPresent(Embeddable.class)){
				Object o = ObjectUtils.getValue(embeddableId, subIdField);
				result.put(o, t);
			}
		}
		return result;
	}
	
	public Map<?,T> queryToMap(String idField,String subIdField ,Map<String,Object> filter) throws Exception {
		Map<Object,T> result = new LinkedHashMap<Object, T>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		if(filter != null && filter.size() > 0){
			for(String key : filter.keySet()){
				criteria.add(Restrictions.eq(key, filter.get(key)));
			}
		}
		
		criteria.addOrder(Order.asc(idField));
		List<T> list = criteria.list();
		for(T t : list){
			Object embeddableId = ObjectUtils.getValue(t, idField);
			if(embeddableId != null && embeddableId.getClass().isAnnotationPresent(Embeddable.class)){
				Object o = ObjectUtils.getValue(embeddableId, subIdField);
				result.put(o, t);
			}
		}
		return result;
	}
	
	/**將查詢結果轉換成MAP
	 * key：idField
	 * 查詢條件：filter
	 * */
	public Map<?,T>queryToMap(String idField ,Map<String,Object> filter)throws Exception{
		Map<Object,T> result = new LinkedHashMap<Object, T>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		if(filter != null && filter.size() > 0){
			for(String key : filter.keySet()){
				criteria.add(Restrictions.eq(key, filter.get(key)));
			}
		}
		criteria.addOrder(Order.asc(idField));
		List<T> list = criteria.list();
		for(T t : list){
			Object o = ObjectUtils.getValue(t, idField);
			if(o != null){
				result.put(o, t);
			}
			
		}
		return result;
	}
	public Map<?,T>queryToMap(String idField ,Map<String,Object> filter,Order...orders)throws Exception{
		Map<Object,T> result = new LinkedHashMap<Object, T>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		
		if(filter != null && filter.size() > 0){
			for(String key : filter.keySet()){
				criteria.add(Restrictions.eq(key, filter.get(key)));
			}
		}
		
		if(orders != null && orders.length > 0){
			for(Order o: orders){
				criteria.addOrder(o);
			}
		}else{
			criteria.addOrder(Order.asc(idField));
		}
		
		List<T> list = criteria.list();
		for(T t : list){
			Object o = ObjectUtils.getValue(t, idField);
			if(o != null){
				result.put(o, t);
			}
			
		}
		return result;
	}
	
	/**使用SQL查詢，並將查詢結果封裝成entity
	 * version.13.01.29
	 * */
	public <E, V extends E> List<E> get(String sql,List<Object> params, Class<V> entityClass){
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(entityClass);
		if(params != null){
			for(int i = 0;i < params.size();i++){
				Object o = params.get(i);
				query.setParameter(i, o);
			}
		}
		return query.list();
	}
	
	/**使用SQL Function查詢，並將查詢結果封裝成entity
	 * version.13.01.31
	 * */
//	public <E, V extends E> List<E> getWithFunction(String functionName,List<Object> params, Class<V> entityClass){
	public <E> List<E> getWithFunction(String functionName,List<Object> params, Class<E> entityClass){
		return get(new StringBuilder("select * from table(").append(functionName).append(")").toString(), params, entityClass);
	}
	/**使用SQL查詢分頁資料<br>
	 * 修正BUG：筆數錯誤<br>
	 * By Siolk 2013/07/26<br>
	 * SQL關鍵字FROM、ORDER必須大寫
	 * */
	protected <E>PagingList<E> getPagingListBySQL(int pageSize, int page, String sql, List<Object> params,Class<E> clazz) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		PagingList<E> paging = new PagingList<E>();
		
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(clazz);
		for(int i = 0;i < params.size();i++){
			query.setParameter(i, params.get(i));
		}
		
		paging.setCurrentPage(page);
		paging.setPageSize(pageSize);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		query.setFetchSize(pageSize);
		
		List<E> queryResult = query.list();
		paging.setDatas(queryResult);
		
		
		//查詢總筆數與總頁數
		int fromIndex    = sql.indexOf("FROM");
		int orderByIndex = sql.lastIndexOf("ORDER");
		StringBuilder countSql = new StringBuilder("select count(*) from (");
		if(fromIndex!=-1) {
			if(orderByIndex!=-1) {
				countSql.append(sql.substring(0, orderByIndex));
			} else {
				countSql.append(sql.substring(0));
			}
			countSql.append(")");
			Query countQuery = session.createSQLQuery(countSql.toString());
			for(int i = 0;i < params.size();i++){
				countQuery.setParameter(i, params.get(i));
			}
			
			List<?> countResult = countQuery.list();
			if(countResult!=null && countResult.size()>0) {
				
				Number recordCount =(Number)countResult.get(0);
				paging.setTotalRecords(recordCount.intValue()); 
				
				int totalPages=0;
				try{
					totalPages = new BigDecimal(recordCount.toString()).divide(new BigDecimal(""+paging.getPageSize()), RoundingMode.CEILING).intValue();
				}catch(ArithmeticException e){
					logger.error(e);
				}
				paging.setTotalPages(totalPages);		
				if(paging.getTotalPages()<=0) paging.setTotalPages(1);
			}
		}
		return paging;
	}
	
	protected PagingList<T> createPagingList(int pageSize, int page, String sql, List<Object> params,Class<T> clazz) throws Exception {
		if(page<=0) page = 1;
		PagingList<T> paging = new PagingList<T>();
		//CurrentPage
		paging.setCurrentPage(page);
		
		//TotalRecords
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select total record count.");
		}
		
		Session session = sessionFactory.getCurrentSession();
	
		StringBuilder countSql = new StringBuilder("select count(*)  from (").append(sql).append(")");
		Query countQuery = session.createSQLQuery(countSql.toString());
		for(int i = 0;i < params.size();i++){
			countQuery.setParameter(i, params.get(i));
		}
		Number countResult = (Number)countQuery.uniqueResult();
		if(countResult!=null) {
			logger.info("recordCount = "+countResult.intValue());
			paging.setTotalRecords(countResult.intValue()); 
		}
	
		
		
		
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(clazz);
		for(int i = 0;i < params.size();i++){
			query.setParameter(i, params.get(i));
		}
		
		//PageSize
		if(pageSize==0) pageSize = 10;
		if(pageSize>0) {
			
			logger.info("FirstResult = " + (page-1)*pageSize);
			logger.info("MaxResult = "+pageSize);
			query.setFirstResult((page-1)*pageSize);
			query.setMaxResults(pageSize);
			query.setFetchSize(pageSize);
			paging.setPageSize(pageSize);
			
		} else {
			//PageSize
			paging.setPageSize(paging.getTotalRecords());
		}
		
		//Datas
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select data.");
		}
		paging.setDatas(query.list());
		if(logger.isDebugEnabled()) {
			logger.debug("End select data.");
		}
		
		//TotalPages
		paging.setTotalPages(paging.getTotalRecords()/pageSize);
		if(paging.getTotalRecords()-paging.getTotalPages()*pageSize!=0) paging.setTotalPages(paging.getTotalPages()+1);
		
		return paging;
	}
	protected PagingList<Map<String, Object>> createPagingList(int pageSize, int page, String sql, List<Object> params, List<String> orderByColumns) throws Exception {
		if(page<=0) page = 1;
		PagingList<Map<String, Object>> paging = new PagingList<Map<String, Object>>();
		//CurrentPage
		paging.setCurrentPage(page);
		
		//TotalRecords
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select total record count.");
		}
		
		Session session = sessionFactory.getCurrentSession();
	
		StringBuilder countSql = new StringBuilder("select count(*)  from (").append(sql).append(")");
		Query countQuery = session.createSQLQuery(countSql.toString());
		for(int i = 0;i < params.size();i++){
			countQuery.setParameter(i, params.get(i));
		}
		Number countResult = (Number)countQuery.uniqueResult();
		if(countResult!=null) {
			logger.info("recordCount = "+countResult.intValue());
			paging.setTotalRecords(countResult.intValue()); 
		}
		
		if(orderByColumns!=null && orderByColumns.size()>0) {
			StringBuilder sb = new StringBuilder(sql);
			sb.append(" ORDER BY");
			for(int i=0;i<orderByColumns.size();++i) {
				if(i>0) sb.append(",");
				sb.append(" ").append(orderByColumns.get(i));
			}
			sql = sb.toString();
		}
		
		SQLQuery query = session.createSQLQuery(sql);
		for(int i = 0;i < params.size();i++){
			query.setParameter(i, params.get(i));
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		//PageSize
		if(pageSize==0) pageSize = 10;
		if(pageSize>0) {
			logger.info("FirstResult = " + (page-1)*pageSize);
			logger.info("MaxResult = "+pageSize);
			query.setFirstResult((page-1)*pageSize);
			query.setMaxResults(pageSize);
			query.setFetchSize(pageSize);
			paging.setPageSize(pageSize);
			
		} else {
			//PageSize
			paging.setPageSize(paging.getTotalRecords());
		}
		
		//Datas
		if(logger.isDebugEnabled()) {
			logger.debug("Prepare select data.");
		}
		
		paging.setDatas(query.list());
		if(logger.isDebugEnabled()) {
			logger.debug("End select data.");
		}
		
		//TotalPages
		paging.setTotalPages(paging.getTotalRecords()/pageSize);
		if(paging.getTotalRecords()-paging.getTotalPages()*pageSize!=0) paging.setTotalPages(paging.getTotalPages()+1);
		
		return paging;
	}
	
	public int update(String sql,List<Object> param) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		for(int i = 0;i < param.size();i++){
			query.setParameter(i, param.get(i));
		}
		return query.executeUpdate();
	}
	public int delete(String sql,List<Object> param) throws Exception{
		return update(sql, param);
	}
	
	
	
	public int deleteByExample(T t)throws Exception{
		return deleteByExample(t, false);
	}
	public int deleteByExample(T t,boolean isMySQL)throws Exception{
		StringBuilder sql = new StringBuilder();
		List<Object> vals = new ArrayList<Object>();
		Field[] fields = t.getClass().getDeclaredFields();
		sql.append("delete ");
		if(isMySQL){
			sql.append(" from ");
		}
		sql.append(getRealTableName(t.getClass()));
		int i = 0;
		for(Field f : fields){
			if(!f.getName().equals("serialVersionUID")){
				String name = getRealColumnName(t.getClass(), f.getName());
				if(name == null){name = getRealColumnNameByMethod(t.getClass(), f.getName());logger.info("name is null");}
				Object o = getValue(t, f.getName());
				if(name != null && o != null){
					if(i==0){sql.append(" where ");}else{sql.append(" and ");}
					sql.append(" ").append(name).append("=?");
					vals.add(o);
					++i;
				}else{
					logger.info("name=" + name + ",o=" + o);
				}
			}else{
				logger.info("This pojo has field that name is serialVersionUID.");
			}
		}
		
		return delete(sql.toString(), vals);  
	}
	
	
	/**取得實際欄位名稱
	 * 限屬性設定@Column時使用
	 * */
	@SuppressWarnings("hiding")
	public <T> String getRealColumnName(Class<T> clazz,String columnName){
		try {
			Field field = clazz.getDeclaredField(columnName);
			if(field.isAnnotationPresent(Column.class)){
				Column column = field.getAnnotation(Column.class);
				if(column != null && column.name() != null && column.name().trim().length() > 0){
					return column.name();
				}else{
					return columnName;
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	/**取得實際欄位名稱
	* 限方法設定@Column時使用
	 * */
	@SuppressWarnings("hiding")
	public <T> String getRealColumnNameByMethod(Class<T> clazz,String columnName){ 
		logger.info("columnName=" + columnName);
		try {
			logger.info("methodName="+ getMethodName(columnName, true));
			Method method = clazz.getDeclaredMethod(getMethodName(columnName, true));
			if(method.isAnnotationPresent(Column.class)){
				Column column = method.getAnnotation(Column.class);
				if(column != null && column.name() != null && column.name().trim().length() > 0){
					logger.info("column is " + column.name());
					return column.name();
				}else{
					logger.info("column is " + columnName);
					return columnName;
				}
			}else{
				logger.info("is not isAnnotationPresent.");
			}
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	/**取得實際表格名稱*/
	@SuppressWarnings("hiding")
	public <T> String getRealTableName(Class<T> clazz){
		try {
			if(clazz.isAnnotationPresent(Table.class)){
				Table table = clazz.getAnnotation(Table.class);
				if(table != null && table.name() != null && table.name().trim().length() > 0){
					return table.name();
				}else{
					return clazz.getSimpleName();
				}
			}
		} catch (Exception e) {}
		return null;
	}
	
	/**取得方法名稱
	 * @param fieldName 屬性名稱<br/>
	 * @param isGetter  是否為getter方法<br/>
	 * example1:getMethodName("user",false);return value is setUser;<br/>
	 * example2:getMethodName("user",true); return value is getUser;<br/>
	 * */
	public String getMethodName(String fieldName, boolean isGetter) {
		if(fieldName==null) return "";

		char[] str = new char[fieldName.length()+3];
		str[0] = isGetter?'g':'s';
		str[1] = 'e';
		str[2] = 't';
		fieldName.getChars(0, fieldName.length(), str, 3);
		str[3] = Character.toUpperCase(str[3]);
		
		return new String(str);
	}

	/**取得物件的屬性值
	 * @param t         物件實例<br/>
	 * @param fieldName 屬性名稱<br/>
	 * #須有對應的getter方法
	 * */
	@SuppressWarnings("hiding")
	public <T> Object getValue(T t,String fieldName)throws Exception{
		Method m = t.getClass().getDeclaredMethod(getMethodName(fieldName,true));
		return m.invoke(t);
	}
	
	/**查詢筆數
	 * hibernate4 Only
	 * */
	public int countBySQL(String sql ,List<Object> params){
		int result = 0;
		Session session = this.sessionFactory.getCurrentSession();
		sql = "select count(*) CNT from (" + sql + ")";
		SQLQuery countQuery = session.createSQLQuery(sql.toString());
		countQuery.addScalar("CNT", IntegerType.INSTANCE);
		for(int i = 0;i < params.size();i++){
			countQuery.setParameter(i, params.get(i));
		}
		countQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> countResult = countQuery.list();
		if(countResult!=null && countResult.size()>0) {
			Object o = countResult.get(0);
			if(o instanceof Map){
				Map<String,?> m = (Map<String,?>)o;
				Number recordCount =(Number) m.get("CNT");
				result = recordCount.intValue();
			}
		}
		return result;
	}
	
	/**查詢筆數*/
	public int count(final DetachedCriteria detachedCriteria)throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		Object o = criteria.setProjection(Projections.rowCount()).uniqueResult();
		if(o instanceof Long){
			return Integer.parseInt(o.toString());
		}else{
			return 0;
		}
	}
	/**查詢加總*/
	public <N>N sum(final DetachedCriteria detachedCriteria,final String field,final Class<N> clazz) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		Object o = criteria.setProjection(Projections.sum(field)).uniqueResult();
		N result = null;
		if(clazz.isInstance(o)){result = clazz.cast(o);}
        return result;
	} 
	/**查詢最大值*/
	public <N>N max(final DetachedCriteria detachedCriteria,final String field,final Class<N> clazz) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		Object o = criteria.setProjection(Projections.max(field)).uniqueResult();
		N result = null;
		if(clazz.isInstance(o)){result = clazz.cast(o);}
        return result;
	} 
	
	/**查詢最大值*/
	public <N>N avg(final DetachedCriteria detachedCriteria,final String field,final Class<N> clazz) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		Object o = criteria.setProjection(Projections.avg(field)).uniqueResult();
		N result = null;
		if(clazz.isInstance(o)){result = clazz.cast(o);}
        return result;
	} 
	
	/**重設結束時間*/
	public Date resetEndDate(Date src){
		Calendar c = Calendar.getInstance();
		c.setTime(src);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	/**重設結束時間*/
	public Date resetBeginDate(Date src){
		Calendar c = Calendar.getInstance();
		c.setTime(src);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	protected static final Order[] convertOrders(String[] orders) {
		List<Order> orderList = new ArrayList<Order>();
		
		if(orders!=null) {
			String[] tmp;
			for(String order:orders) {
				tmp = order.split(" ");
				switch (tmp.length) {
				case 0:
					break;
				case 1:
					orderList.add(Order.asc(tmp[0]));
					break;
				default:
					if("desc".equals(tmp[tmp.length-1]) || "DESC".equals(tmp[tmp.length-1])) {
						orderList.add(Order.desc(tmp[0]));
					} else {
						orderList.add(Order.asc(tmp[0]));
					}
					break;
				}
			}
		}
		
		if(orderList.size()==0) return null;
		
		return orderList.toArray(new Order[orderList.size()]);
	}
	
	public String getNextId(String field, int colLength) throws Exception{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(getEntityClass());
		criteria.setProjection(Projections.max(field));
		Object result = criteria.uniqueResult();
		String id = "0";
		if(result != null){
			id = result.toString();
		}
		Integer nextId = Integer.valueOf(id).intValue() + 1;
		StringBuilder nextIdString = new StringBuilder();
		for(int i = 0; i < colLength - nextId.toString().length(); i++){
			nextIdString.append("0");
		}
		nextIdString.append(nextId.toString());
		
		return nextIdString.toString();
	}
	
	@SuppressWarnings("hiding")
	public <T> T selectOne(Criteria criteria) {
		List<T> l = criteria.list();
		if(l!=null && l.size()==1) {
			return l.get(0);
		}
		return null;
	}
}
