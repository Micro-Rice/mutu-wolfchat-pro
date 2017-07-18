/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.dao
 * File Name:BaseDao.java
 * Date:2016年10月27日 下午3:37:03
 * 
 */
package com.test.core.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.test.core.entity.page.QueryConditions;

 /**
 * ClassName: BaseDao <br/>
 * Description: TODO <br/>
 * Date: 2016年10月27日 下午3:37:03 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class BaseDao {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    public static final int MAX_RETURN_RECORD = 1000;

    /**
     * 
     * currentSession:获取当前session <br/>
     * 
     * @exception 无
     * <br/>
     * @return 当前session <br/>
     */
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 
     * getById:通过ID获取数据 <br/>
     * 
     * @param clz
     *            clz
     * @param id
     *            id
     * @return 数据对象 <br/>
     */
    public Object getById(Class clz, Serializable id) {
        // TODO Auto-generated method stub
        return currentSession().get(clz, id);
    }

    /**
     * 
     * save:保存数据对象 <br/>
     * 
     * @param obj
     *            需要保存的数据对象 
     * @throws HibernateException
     *            HibernateException <br/>
     */
    public void save(Object obj) {
        currentSession().saveOrUpdate(obj);
    }
    
    /**
     * 
     * saveWithFlush:保存数据对象 <br/>
     * 
     * @param obj
     *            需要保存的数据对象 
     * @throws HibernateException
     *            HibernateException <br/>
     */
    public void saveWithFlush(Object obj) {
        currentSession().clear();
        currentSession().saveOrUpdate(obj);
        currentSession().flush();
    }
    
    /**
     * 
     * save:保存数据对象 <br/>
     * 
     * @param obj
     *            需要保存的数据对象 
     * @throws HibernateException
     *            HibernateException <br/>
     */
    public void merge(Object obj) {
        currentSession().merge(obj);
    }

    /**
     * 
     * delete:删除数据对象 <br/>
     * 
     * @param obj
     *            需要删除的数据对象 
     * @throws HibernateException
     *            HibernateException <br/>
     */
    public void delete(Object obj) {
        // TODO Auto-generated method stub
        currentSession().delete(obj);
    }
    
    public void deleteWithFlush(Object obj) {
        // TODO Auto-generated method stub
        currentSession().clear();
        currentSession().delete(obj);
        currentSession().flush();
    }

    /**
     * 
     * deleteAll:删除列表中的数据对象 <br/>
     * 
     * @param l
     *            需要删除的列表 <br/>
     */
    public void deleteAll(List l) {
        // TODO Auto-generated method stub
        if (l == null || l.size() == 0) {
            return;
        }
        for (int i = 0; i < l.size(); i++) {
        	currentSession().clear();
        	currentSession().delete(l.get(i));
        	currentSession().flush();
        }
    }
    
    /**
     * saveAll:批量保存列表 <br/>
     * 
     * @param l
     *            列表 
     * @throws HibernateException
     *            HibernateException <br/>
     */
    public void saveAll(List l) {
        if (l == null || l.size() == 0) {
            return;
        }
        for (int i = 0; i < l.size(); i++) {
        	currentSession().clear();
        	currentSession().saveOrUpdate(l.get(i));
        	currentSession().flush();
        }
    }

    /**
     * 根据条件使用HQL语句查询数据。<br>
     * <p/>
     * 具有功能：<br>
     * 1）支持查询分页，该方法会利用数据库本身的分页技术实现。说明如下：<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;a)如果数据库(如MySQL,Oracle,SQL Server2005等)支持limit
     * n,m，查询效率最高；<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;b)如果数据库(如informix,Sybase 12.5.3,SQL Server等)支持top
     * n，查询效率次之（查询结果越大，效率越低）； <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;c)如果以上两种均不支持，查询效率最低。<br>
     * 2）支持查询总记录数<br>
     * 3）支持order by，group by,having等 <br>
     * <p/>
     * 不支持功能：<br>
     * 1）不支持select里的嵌套子查询。如不允许这种用法：select a,b,(select c from table1) as d from
     * table2 ...<br>
     * 2）条件与条件之间不支持or，而是用and。<br>
     * <p/>
     * 示例如下：<br>
     * 
     * <pre>
     * 1)查询所有用户信息:
     * <p/>
     * xxxDao.select("from TUser",null,null,null,0,0);
     * <p/>
     * 2)查询用户名含有"admin"开头，注册日期2006-12-01之前前10条用户信息(用户名及注册日期):
     * <p/>
     * xxxDao.select("select userName,createDate from TUser",
     *               new String[]{"userName","createDate"},
     *               new String[]{"like",">="},
     *               new Object[]{"admin%","2006-12-01"},0,10);
     * <p/>
     * <b>注意：对于多对象关联查询，必须指定返回的对象类型。</b><br>
     * 示例如下：
     * <pre>
     * <b>1. 查询结果bean，A的属性a、b来源于已影射的bean M,N</b>
     * class A{
     *      String a,b;
     *      public A(String a,String b){
     *          this.a=a;
     *          thia.b=b;
     *      }
     * }
     * <p/>
     * <b>2.编写查询方法</b>
     * Query q= session.createQuery("select new A(M.a,N.b) from M as M,N as N where M.id=N.id");
     *
     * @param hql           HQL查询语句（不带Where条件）。不允许在select段内使用子查询，如不允许这种用法：
     *                      select a,b,(select c from table1) as d from table2 ...
     *                      1)对于查询全部对象属性，(<b>select *</b>)不可写。如：from TUser；
     *                      2)对于查询部分对象属性，则必须写完整，如：select userName,password from TUser;
     * @param propertyNames 查询条件的属性名列表
     * @param operators     查询条件的操作符列表，如果查询条件中存在不为<b>=</b>的操作符，需要填写该列表，否则为null，
     *                      应与属性名列表一一对应。操作符包括=, >=, <=, <>, !=, like。
     * @param values        查询条件的值列表，该列表应当与属性列表一一对应
     * @param offset        查询结果的起始行，从0开始。如果不需要，则设置为-1。
     * @param size          查询结果的最大行数。如果不需要，则设置为-1
     * @param isTotalSize   是否需要返回本次查询的总记录数，默认false
     * @param orderBy       排序字符串,不含order by字符串，如orderBy="a desc,b asc",最后生成为：order by a desc,b asc
     * @param groupBy       分组字符串,不含group by 字符串，如groupBy="a desc,b asc",最后生成为：group by a desc,b asc
     * @param otherCause    where后面的其它语句，如排序(order by),分组(group by)及聚集(having)。
     *                      如"group by name order by name desc"
     * @return Object[]    有两个值，第一个值表示查询结果列表List list = (List)Object[0]
     *         第二个表示查询返回的总记录数，int count = ((Integer)Object[1]).intValue;
     * @throws com.longtop.intelliweb.sample.exception.Exception
     *          基础不可控异常类
     *          </pre>
     */

    private Object[] find(final String hql, final String[] propertyNames, final String[] operators,
            final Object[] values, final int offset, final int size, final boolean isTotalSize, final String orderBy,
            final String groupBy, final String otherCause) {
        Query query;
        String countSql;
        String fullSql;
        Integer count = Integer.valueOf(0);
        Map map = new HashMap();

        if (hql == null || hql.trim().equals("")) {
            throw new HibernateException("hql is null or empty");
        }

        String where = "";
        if (hql.toLowerCase().indexOf("where") != -1) {
            where = " ";
        }

        if (propertyNames != null && propertyNames.length > 0 && values != null && values.length > 0) {
            if (propertyNames.length != values.length) {
                throw new HibernateException("propertyNames length noe equal values length");
            }

            if (operators != null && propertyNames.length != operators.length) {
                throw new HibernateException("propertyNames length noe equal operators length");
            }

            for (int i = 0; i <= propertyNames.length - 1; i++) {
                if ("".equals(where)) {
                    where = " where ";
                } else {
                    where += "and ";
                }
                if (operators != null && operators[i].equalsIgnoreCase("isnull")) {
                    where += propertyNames[i] + " is null ";
                } else if (operators != null && operators[i].equalsIgnoreCase("isnotnull")) {
                    where += propertyNames[i] + " is not null ";
                } else if (operators != null && operators[i].equalsIgnoreCase("isempty")) {
                    where += propertyNames[i] + " = '' ";
                } else if (operators != null && operators[i].equalsIgnoreCase("isnotempty")) {
                    where += propertyNames[i] + " <> '' ";
                } else {
                    where += propertyNames[i] + (operators == null || operators[i] == null ? "=" : " " + operators[i])
                            + " ? ";
                }
            }

            fullSql = hql + where;
            fullSql += orderBy == null || orderBy.trim().equals("") ? "" : " order by " + orderBy;
            fullSql += groupBy == null || groupBy.trim().equals("") ? "" : " group by " + groupBy;
            fullSql += otherCause == null || otherCause.trim().equals("") ? "" : " " + otherCause;

            query = currentSession().createQuery(fullSql);

            for (int i = 0; i <= values.length - 1; i++) {
                if (operators != null && operators[i].equalsIgnoreCase("isnull")) {
                    continue;
                }
                if (operators != null && operators[i].equalsIgnoreCase("isnotnull")) {
                    continue;
                }
                if (operators != null && operators[i].equalsIgnoreCase("isempty")) {
                    continue;
                }
                if (operators != null && operators[i].equalsIgnoreCase("isnotempty")) {
                    continue;
                }
                // 要求强类型对应，例如ID必须为long.
                query.setParameter(i, values[i]);
            }

        } else {

            fullSql = hql + where;
            fullSql += orderBy == null || orderBy.trim().equals("") ? "" : " order by " + orderBy;
            fullSql += groupBy == null || groupBy.trim().equals("") ? "" : " group by " + groupBy;
            fullSql += otherCause == null || otherCause.trim().equals("") ? "" : " " + otherCause;

            query = currentSession().createQuery(fullSql);

        }

        // 如果需要统计本次查询总记录数
        if (isTotalSize) {
            countSql = hql + where;
            countSql += groupBy == null || groupBy.trim().equals("") ? "" : " group by " + groupBy;
            countSql += otherCause == null || otherCause.trim().equals("") ? "" : " " + otherCause;
            countSql = "select count(*) from " + countSql.substring(countSql.toLowerCase().indexOf("from") + 5);
            Query query2 = currentSession().createQuery(countSql);

            if (values != null) {
                for (int i = 0; i <= values.length - 1; i++) {
                    if (operators != null && operators[i].equalsIgnoreCase("isnull")) {
                        continue;
                    }
                    if (operators != null && operators[i].equalsIgnoreCase("isnotnull")) {
                        continue;
                    }
                    if (operators != null && operators[i].equalsIgnoreCase("isempty")) {
                        continue;
                    }
                    if (operators != null && operators[i].equalsIgnoreCase("isnotempty")) {
                        continue;
                    }
                    query2.setParameter(i, values[i]);
                }
            }
            count = Integer.valueOf(String.valueOf(query2.list().get(0)));
        }
        if (offset > 0) {
            query.setFirstResult(offset);
        }

        if (size > 0) {
            query.setMaxResults(size);
        }
        map.put("list", query.list());
        map.put("count", count);
        // session.close();//防止query.list() 出现僵死的情况

        return new Object[] { map.get("list"), map.get("count") };
    }

    /**
     * 根据条件使用HQL语句查询数据。用queryCondition包装了方法 find(final String hql, final
     * String[] propertyNames, final String[] operators, final Object[] values,
     * final int offset, final int size, final boolean isTotalSize, final String
     * orderBy, final String groupBy, final String otherCause) ====多表查询示例
     * start=====： HQL = select new com.travelsky.pss.dao.VO1(s.flightsNo,e.id)
     * from Flights s, dict e ..... VO1要求:必须有针对这些属性的构造函数（如：public VO1(String
     * name,long id)),可以通过ECLIPSE自动生成。 注意类型要求一致性，例如long与Long ====多表查询示例
     * end=====：
     * 
     * @param hql
     *            HQL查询语句（不带Where条件）。不允许在select段内使用子查询，如不允许这种用法： select
     *            a,b,(select c from table1) as d from table2 ...
     *            1)对于查询全部对象属性，(<b>select *</b>)不可写。如：from TUser；
     *            2)对于查询部分对象属性，则必须写完整，如：select userName,password from TUser;
     * @param condition
     *            查询条件，
     * @return page 返回带分页的查询结果
     */
    public List find(String hql, QueryConditions condition) {

        int index = condition.getPropertyNames().size();
        String[] propertyNames = new String[index];
        String[] operators = new String[index];

        for (int i = 0; i < index; i++) {
            propertyNames[i] = (String) condition.getPropertyNames().get(i);
            operators[i] = (String) condition.getOperators().get(i);
        }

        Object[] objs = find(hql, propertyNames, operators, condition.getValues().toArray(), 0,
                MAX_RETURN_RECORD, false, condition.getOrderBy(), condition.getGroupBy(),
                condition.getOtherHql());
        List result = (ArrayList) objs[0];
        return result;

    }

    /**
     * 根据条件使用HQL语句查询数据。用queryCondition包装了方法 find(final String hql, final
     * String[] propertyNames, final String[] operators, final Object[] values,
     * final int offset, final int size, final boolean isTotalSize, final String
     * orderBy, final String groupBy, final String otherCause) ====多表查询示例
     * start=====： HQL = select new com.travelsky.pss.dao.VO1(s.flightsNo,e.id)
     * from Flights s, dict e ..... VO1要求:必须有针对这些属性的构造函数（如：public VO1(String
     * name,long id)),可以通过ECLIPSE自动生成。 注意类型要求一致性，例如long与Long ====多表查询示例
     * end=====：
     * 
     * @param hql
     *            HQL查询语句（不带Where条件）。不允许在select段内使用子查询，如不允许这种用法： select
     *            a,b,(select c from table1) as d from table2 ...
     *            1)对于查询全部对象属性，(<b>select *</b>)不可写。如：from TUser；
     *            2)对于查询部分对象属性，则必须写完整，如：select userName,password from TUser;
     * @param condition
     *            查询条件，
     * @param maxRowNum
     *            本次查询返回最多的结果条数
     * @return page 返回带分页的查询结果
     */
    public List find(String hql, QueryConditions condition, int maxRowNum) {

        int index = condition.getPropertyNames().size();
        String[] propertyNames = new String[index];
        String[] operators = new String[index];

        for (int i = 0; i < index; i++) {
            propertyNames[i] = (String) condition.getPropertyNames().get(i);
            operators[i] = (String) condition.getOperators().get(i);
        }

        Object[] objs = find(hql, propertyNames, operators, condition.getValues().toArray(), 0, maxRowNum, false,
                condition.getOrderBy(), condition.getGroupBy(), condition.getOtherHql());
        List result = (ArrayList) objs[0];
        return result;

    }
}
