/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.dao
 * File Name:HibernateEntityManager.java
 * Date:2016年1月27日 下午1:58:20
 * 
 */
package com.test.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.test.core.entity.page.Page;
import com.test.core.entity.page.PageInfo;
import com.test.core.entity.page.QueryConditions;

 /**
 * ClassName: HibernateEntityManager <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午1:58:20 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public abstract interface HibernateEntityManager extends EntityManager {
    public abstract void setSF(SessionFactory paramSessionFactory);

    public abstract void updateWithFile(Object paramObject);

    public abstract Object load(Class paramClass, Serializable paramSerializable);

    public abstract List loadAll(Class paramClass);

    public abstract List loadAll(Class paramClass, int paramInt1, int paramInt2);

    public abstract Page loadAll(Class paramClass, PageInfo paramPageInfo);

    public abstract void update(Object paramObject);

    public abstract void delete(Object paramObject);

    public abstract void delete(Collection paramCollection);

    public abstract void delete(Class paramClass, Serializable paramSerializable);

    public abstract Serializable save(Object paramObject);

    public abstract List save(Collection paramCollection);

    public abstract void update(Collection paramCollection);

    public abstract void merge(Object paramObject);

    public abstract Object refresh(Object paramObject);

    public abstract List find(String paramString);

    public abstract List find(String paramString1, String paramString2, Object[] paramArrayOfObject);

    public abstract List find(String paramString, Object[] paramArrayOfObject);

    public abstract List find(String paramString, int paramInt1, int paramInt2);

    public abstract List find(String paramString, Object[] paramArrayOfObject, int paramInt1, int paramInt2);

    public abstract Page find(String paramString, PageInfo paramPageInfo);

    public abstract Page find(String paramString, Object[] paramArrayOfObject, PageInfo paramPageInfo);

    public abstract Page find(String paramString, QueryConditions paramQueryConditions, PageInfo paramPageInfo,
            boolean paramBoolean);

    public abstract List find(String paramString, QueryConditions paramQueryConditions);

    public abstract List find(String paramString, QueryConditions paramQueryConditions, int paramInt);

    public abstract int executeUpdate(String paramString);

    public abstract int executeUpdate(String paramString, Object[] paramArrayOfObject);

    public abstract int executeSQL(String paramString, Object[] paramArrayOfObject);

    public abstract HibernateTemplate getHibernateTemplate();
}
