/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebTest
 * Package Name:com.test.wolfkill.dao.impl
 * File Name:MemInfoDaoImpl.java
 * Date:2017年4月28日 下午4:55:46
 * 
 */
package com.mutuChat.wolfkill.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IMemInfoDao;
import com.mutuChat.wolfkill.model.MutuAccountInfo;
import com.mutuChat.wolfkill.model.MutuMemInfo;


 /**
 * ClassName: MemInfoDaoImpl <br/>
 * Description: TODO <br/>
 * Date: 2017年4月28日 下午4:55:46 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Repository("memInfoDao")
public class MemInfoDaoImpl extends BaseDao implements IMemInfoDao{

    @Override
    public MutuAccountInfo findAccountInfoByName(QueryConditions condition) {
        String hql = "from MutuAccountInfo";
        List<MutuAccountInfo> l = super.find(hql, condition);
        if (l == null || l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    @Override
    public List<MutuMemInfo> getAllMemInfo(QueryConditions condition) {
        String hql = "from MutuMemInfo";
        return super.find(hql, condition);
    }

    @Override
    public void updateMemInfos(MutuMemInfo memInfo) {
        super.saveWithFlush(memInfo);
    }

    @Override
    public void delMemInfos(List<MutuMemInfo> memInfos) {
        super.deleteAll(memInfos);
    }

    @Override
    public void saveAccountInfo(MutuAccountInfo accInfo) {
        super.saveWithFlush(accInfo);
    }

    @Override
    public MutuMemInfo findMemInfoByMnum(QueryConditions condition) {
        String hql = "from MutuMemInfo";
        List<MutuMemInfo> l = super.find(hql, condition);
        if (l == null || l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }



}
