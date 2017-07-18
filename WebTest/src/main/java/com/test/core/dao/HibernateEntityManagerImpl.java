/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.test.core.dao;

import com.test.core.entity.AbstractEntity;
import com.test.core.entity.page.Page;
import com.test.core.entity.page.PageInfo;
import com.test.core.entity.page.QueryConditions;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateEntityManagerImpl extends HibernateDaoSupport implements HibernateEntityManager {
    @Resource(name = "sessionFactory")
    public void setSF(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    public void delete(Object entity) {
        getHibernateTemplate().delete(entity);
    }

    public void delete(Collection entities) {
        getHibernateTemplate().deleteAll(entities);
    }

    public void delete(Class entityClass, Serializable id) {
        getHibernateTemplate().delete(load(entityClass, id));
    }

    public int executeUpdate(String hql) {
        return getHibernateTemplate().bulkUpdate(hql);
    }

    public int executeUpdate(String hql, Object[] params) {
        return getHibernateTemplate().bulkUpdate(hql, params);
    }

    @SuppressWarnings("unchecked")
    public int executeSQL(final String sql, final Object[] params) {
        Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.length; ++i) {
                        sqlQuery.setParameter(i, params[i]);
                    }
                }
                return Integer.valueOf(sqlQuery.executeUpdate());
            }
        });
        return result.intValue();
    }

    public List find(String queryString) {
        return getHibernateTemplate().find(queryString);
    }

    public List find(String queryString, Object[] params) {
        return getHibernateTemplate().find(queryString, params);
    }

    public List find(String queryString, String parameterName, Object[] params) {
        return getHibernateTemplate().findByNamedParam(queryString, parameterName, params);
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    public List find(final String queryString, final int start, final int size) {
        List list = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryString);
                query.setFirstResult(start);
                query.setFetchSize(size);
                query.setMaxResults(size);
                return query.list();
            }
        });
        return list;
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    public List find(final String queryString, final Object[] params, final int start, final int size) {
        List list = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryString);
                if (params != null) {
                    for (int i = 0; i < params.length; ++i) {
                        query.setParameter(i, params[i]);
                    }
                }
                query.setFirstResult(start);
                query.setFetchSize(size);
                query.setMaxResults(size);
                return query.list();
            }
        });
        return list;
    }

    public Object load(Class entityClass, Serializable id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    public List loadAll(Class entityClass) {
        return getHibernateTemplate().loadAll(entityClass);
    }
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public List loadAll(final Class entityClass, final int start, final int size) {
        List list = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(entityClass);
                criteria.setFetchSize(size);
                criteria.setFirstResult(start);
                criteria.setMaxResults(size);
                return criteria.list();
            }
        });
        return list;
    }

    public Object refresh(Object entity) {
        getHibernateTemplate().refresh(entity);
        return entity;
    }

    public Serializable save(Object entity) {
        return getHibernateTemplate().save(entity);
    }

    public List save(Collection entities) {
        List result = new ArrayList();
        if (entities != null) {
            Iterator iterator = entities.iterator();
            while (iterator.hasNext()) {
                Object entity = iterator.next();
                result.add(save(entity));
            }
        }
        return result;
    }

    public void update(Object entity) {
        merge(entity);
    }

    private boolean isObjectExisted(Object entity) {
        String entityName = entity.getClass().getName();
        String hql = " from " + entityName + " where id = " + ((AbstractEntity) entity).getId();
        List result = find(hql);

        return (result.size() == 1);
    }

    public void merge(Object entity) {
        if (isObjectExisted(entity))
            getHibernateTemplate().merge(entity);
        else
            throw new HibernateException("ID是" + ((AbstractEntity) entity).getId() + "的记录不存在，修改失败！");
    }

    public void update(Collection entities) {
        if (entities != null) {
            Iterator iterator = entities.iterator();
            while (iterator.hasNext()) {
                Object entity = iterator.next();
                update(entity);
            }
        }
    }

    public void updateWithFile(Object obj) {
        getHibernateTemplate().update(obj);
    }

    public Page find(String queryString, PageInfo pageInfo) {
        Page result = new Page();
        List data = find(queryString, pageInfo.getStartIndex(), pageInfo.getRowsOfPage());
        result.setData(data);
        result.setPageInfo(pageInfo);
        preparePageInfo(queryString, null, pageInfo);
        return result;
    }

    public Page find(String queryString, Object[] params, PageInfo pageInfo) {
        Page result = new Page();
        List data = find(queryString, params, pageInfo.getStartIndex(), pageInfo.getRowsOfPage());
        result.setData(data);
        result.setPageInfo(pageInfo);
        preparePageInfo(queryString, params, pageInfo);
        return result;
    }

    private void preparePageInfo(String queryString, Object[] params, PageInfo pageInfo) {
        String queryStr1 = "";
        String queryStr2 = "";
        String queryStr3 = "";
        int index = queryString.toUpperCase().indexOf("FROM");
        if (index != -1) {
            queryStr1 = queryString.substring(index);
            queryStr2 = "SELECT COUNT(*) " + queryStr1;
            index = queryStr2.toUpperCase().lastIndexOf("ORDER");
            if (index != -1) {
                queryStr3 = queryStr2.substring(0, index);
                computePage(queryStr3, params, pageInfo);
            } else {
                computePage(queryStr2, params, pageInfo);
            }
        }
    }

    private void computePage(String queryString, Object[] params, PageInfo pageInfo) {
        List rowcount = null;
        if (params != null)
            rowcount = getHibernateTemplate().find(queryString, params);
        else {
            rowcount = getHibernateTemplate().find(queryString);
        }
        if ((rowcount != null) && (rowcount.size() > 0)) {
            int count = ((Number) rowcount.get(0)).intValue();
            pageInfo.setTotalRowCount(count);
            if (count % pageInfo.getRowsOfPage() > 0)
                pageInfo.setTotalPageCount(count / pageInfo.getRowsOfPage() + 1);
            else {
                pageInfo.setTotalPageCount(count / pageInfo.getRowsOfPage());
            }

            if (count == 0)
                pageInfo.setTotalPageCount(1);
        }
    }

    public Page loadAll(Class entityClass, PageInfo pageInfo) {
        Page result = new Page();
        List data = loadAll(entityClass, pageInfo.getStartIndex(), pageInfo.getRowsOfPage());
        result.setData(data);
        result.setPageInfo(pageInfo);
        String queryString = "SELECT COUNT(*) FROM " + entityClass.getName();

        computePage(queryString, null, pageInfo);
        return result;
    }

    @SuppressWarnings("unchecked")
    private Object[] find(final String hql, final String[] propertyNames, final String[] operators, final Object[] values, final int offset,
            final int size, final boolean isTotalSize, final String orderBy, final String groupBy, final String otherCause) {
        Map map = (Map) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                        StringBuilder countSqlStrBd = new StringBuilder("");
                        StringBuilder fullSqlStrBd = new StringBuilder("");
                        Integer count = Integer.valueOf(0);
                        Map map = new HashMap();

                        if ((hql == null) || (hql.trim().equals(""))) {
                            throw new SQLException();
                        }

                        StringBuilder whereStrBd = new StringBuilder("");
                        if (hql.toLowerCase().indexOf("where") != -1)
                            whereStrBd.append(" ");
                        Query query;
                        if ((propertyNames != null) && (propertyNames.length > 0)
                                && (values != null) && (values.length > 0)) {
                            if (propertyNames.length != values.length) {
                                throw new HibernateException("propertyNames length noe equal values length");
                            }

                            if ((operators != null)
                                    && (propertyNames.length != operators.length)) {
                                throw new HibernateException("propertyNames length noe equal operators length");
                            }

                            for (int i = 0; i <= propertyNames.length - 1; ++i) {
                                if ("".equals(whereStrBd.toString()))
                                    whereStrBd.append(" where ");
                                else {
                                    whereStrBd.append("and ");
                                }
                                if ((operators != null) && (operators[i].equalsIgnoreCase("isnull")))
                                    whereStrBd.append(propertyNames[i]).append(" is null ");
                                else if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("isnotnull")))
                                    whereStrBd.append(propertyNames[i]).append(" is not null ");
                                else if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("isempty")))
                                    whereStrBd.append(propertyNames[i]).append(" = '' ");
                                else if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("isnotempty")))
                                    whereStrBd.append(propertyNames[i]).append(" <> '' ");
                                else if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("like")))
                                    whereStrBd.append(propertyNames[i]).append(" like ? ")
                                            .append(" escape '\\'");
                                else {
                                    whereStrBd.append(propertyNames[i]).append(" " + operators[i])
                                            .append(" ? ");
                                }

                            }

                            fullSqlStrBd.delete(0, fullSqlStrBd.length());
                            fullSqlStrBd.append(hql).append(whereStrBd);
                            fullSqlStrBd.append(" order by " + orderBy);
                            fullSqlStrBd.append(" group by " + groupBy);
                            fullSqlStrBd.append(" " + otherCause);

                            query = session.createQuery(fullSqlStrBd.toString());

                            for (int i = 0; i <= values.length - 1; ++i) {
                                if ((operators != null) && (operators[i].equalsIgnoreCase("isnull"))) {
                                    continue;
                                }
                                if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("isnotnull"))) {
                                    continue;
                                }
                                if ((operators != null) && (operators[i].equalsIgnoreCase("isempty"))) {
                                    continue;
                                }
                                if ((operators != null)
                                        && (operators[i].equalsIgnoreCase("isnotempty"))) {
                                    continue;
                                }

                                query.setParameter(i, values[i]);
                            }
                        } else {
                            fullSqlStrBd.delete(0, fullSqlStrBd.length());
                            fullSqlStrBd.append(hql).append(whereStrBd);
                            fullSqlStrBd.append(" order by " + orderBy);
                            fullSqlStrBd.append(" group by " + groupBy);
                            fullSqlStrBd.append(" " + otherCause);

                            query = session.createQuery(fullSqlStrBd.toString());
                        }

                        if (isTotalSize) {
                            countSqlStrBd.delete(0, countSqlStrBd.length());
                            countSqlStrBd.append(hql).append(whereStrBd);
                            countSqlStrBd.append(" group by " + groupBy);
                            countSqlStrBd.append(" " + otherCause);

                            String countSql = countSqlStrBd.toString();

                            countSql = "select count(1) from("
                                    + HibernateEntityManagerImpl.this.getSqlFromHql(countSql) + ")";

                            Query query2 = session.createSQLQuery(countSql);

                            if (values != null) {
                                for (int i = 0; i <= values.length - 1; ++i) {
                                    if ((operators != null)
                                            && (operators[i].equalsIgnoreCase("isnull"))) {
                                        continue;
                                    }
                                    if ((operators != null)
                                            && (operators[i].equalsIgnoreCase("isnotnull"))) {
                                        continue;
                                    }
                                    if ((operators != null)
                                            && (operators[i].equalsIgnoreCase("isempty"))) {
                                        continue;
                                    }
                                    if ((operators != null)
                                            && (operators[i].equalsIgnoreCase("isnotempty"))) {
                                        continue;
                                    }
                                    query2.setParameter(i, values[i]);
                                }
                            }
                            count = Integer.valueOf(query2.list().get(0).toString());
                        }
                        if (offset > 0) {
                            query.setFirstResult(offset);
                        }

                        if (size > 0) {
                            query.setMaxResults(size);
                        }
                        map.put("list", query.list());
                        map.put("count", count);
                        return map;
                    }
                });
        return new Object[] { map.get("list"), map.get("count") };
    }

    private String getSqlFromHql(String hql) {
        QueryTranslatorImpl ts = new QueryTranslatorImpl(hql, hql, Collections.EMPTY_MAP,
                (SessionFactoryImplementor) super.getSessionFactory());

        ts.compile(Collections.EMPTY_MAP, false);
        return ts.getSQLString();
    }

    public Page find(String hql, QueryConditions condition, PageInfo pageInfo, boolean isTotalSize) {
        Page result = new Page();

        int index = condition.getPropertyNames().size();
        String[] propertyNames = new String[index];
        String[] operators = new String[index];

        for (int i = 0; i < index; ++i) {
            propertyNames[i] = ((String) condition.getPropertyNames().get(i));
            operators[i] = ((String) condition.getOperators().get(i));
        }

        if (pageInfo.getCurrentPageNum() < 1) {
            pageInfo.setCurrentPageNum(1);
        }

        Object[] objs = find(hql, propertyNames, operators, condition.getValues().toArray(),
                (pageInfo.getCurrentPageNum() - 1) * pageInfo.getRowsOfPage(), pageInfo.getRowsOfPage(), isTotalSize,
                condition.getOrderBy(), condition.getGroupBy(), condition.getOtherHql());

        if (isTotalSize) {
            int count = ((Integer) objs[1]).intValue();
            pageInfo.setTotalRowCount(count);
            if (count % pageInfo.getRowsOfPage() > 0)
                pageInfo.setTotalPageCount(count / pageInfo.getRowsOfPage() + 1);
            else {
                pageInfo.setTotalPageCount(count / pageInfo.getRowsOfPage());
            }

            if (count == 0) {
                pageInfo.setTotalPageCount(1);
            }
        }
        result.setData((List) objs[0]);
        result.setPageInfo(pageInfo);

        return result;
    }

    public List find(String hql, QueryConditions condition) {
        int index = condition.getPropertyNames().size();
        String[] propertyNames = new String[index];
        String[] operators = new String[index];

        for (int i = 0; i < index; ++i) {
            propertyNames[i] = ((String) condition.getPropertyNames().get(i));
            operators[i] = ((String) condition.getOperators().get(i));
        }

        Object[] objs = find(hql, propertyNames, operators, condition.getValues().toArray(), 0, 0, false,
                condition.getOrderBy(), condition.getGroupBy(), condition.getOtherHql());

        return ((ArrayList) objs[0]);
    }

    public List find(String hql, QueryConditions condition, int maxRowNum) {
        int index = condition.getPropertyNames().size();
        String[] propertyNames = new String[index];
        String[] operators = new String[index];

        for (int i = 0; i < index; ++i) {
            propertyNames[i] = ((String) condition.getPropertyNames().get(i));
            operators[i] = ((String) condition.getOperators().get(i));
        }

        Object[] objs = find(hql, propertyNames, operators, condition.getValues().toArray(), 0, maxRowNum, false,
                condition.getOrderBy(), condition.getGroupBy(), condition.getOtherHql());

        return ((ArrayList) objs[0]);
    }
}