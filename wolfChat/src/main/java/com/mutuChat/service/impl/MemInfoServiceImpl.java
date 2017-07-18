/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebTest
 * Package Name:com.test.service.impl
 * File Name:MemInfoServiceImpl.java
 * Date:2017年4月28日 下午4:35:17
 * 
 */
package com.mutuChat.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutuChat.service.IMemInfoService;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IMemInfoDao;
import com.mutuChat.wolfkill.model.MutuAccountInfo;
import com.mutuChat.wolfkill.model.MutuMemInfo;

 /**
 * ClassName: MemInfoServiceImpl <br/>
 * Description: TODO <br/>
 * Date: 2017年4月28日 下午4:35:17 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Transactional
@Service("memInfoService")
public class MemInfoServiceImpl implements IMemInfoService{
    
    @Resource
    private IMemInfoDao memInfoDao;
    
    @Override
    public String vertifyMemInfo(String name, String pass) {
        String msg = "";
        QueryConditions condition = new QueryConditions(); 
        condition.setConditionEqual("accName", name);
        MutuAccountInfo macc = memInfoDao.findAccountInfoByName(condition);
        if (macc == null || pass == null || !pass.equals(macc.getAccPass())) {
            msg = "error|用户名密码错误!";
        } else {
            msg = "success|" + macc.getUid();
        }       
        return msg;
    }

    @Override
    public List<MutuMemInfo> queryMemInfos() {
        QueryConditions condition = new QueryConditions();
        String orderBy = "memId asc";
        condition.setOrderBy(orderBy);
        return memInfoDao.getAllMemInfo(condition);
    }

    @Override
    public String updateMemInfo(String mNum, String mName, String mPhone) {
        MutuMemInfo memInfo = new MutuMemInfo();
        memInfo.setMemId(Integer.parseInt(mNum));
        memInfo.setMemName(mName);
        memInfo.setMemPhone(mPhone);
        String msg = "";
        try {
            memInfoDao.updateMemInfos(memInfo);
            msg = "success|";
        } catch(Exception e) {
            msg = "保存失败，请重试!";
        }        
        return msg;
    }

    @Override
    public String deleteMemInfos(List<MutuMemInfo> memInfos) {
        String msg = "";
        try {
            memInfoDao.delMemInfos(memInfos);
            msg = "success|";
        } catch(Exception e) {
            msg = "删除失败，请重试!";
        }
        return msg;
    }

    @Override
    public String updateAccInfo(String mUid, String mName, String mpass,String opass) {
        String msg = "";
        QueryConditions condition = new QueryConditions(); 
        condition.setConditionEqual("accName", mName);
        MutuAccountInfo macc = memInfoDao.findAccountInfoByName(condition);
        if (macc == null || opass == null || !opass.equals(macc.getAccPass())) {
            msg = "error|原密码错误!";
        } else {
            macc.setAccPass(mpass);
        }
        try {
            memInfoDao.saveAccountInfo(macc);
            msg = "success|";
        } catch (Exception e) {
            msg = "修改失败，请重试!";
        }
        
        return msg;
    }

    @Override
    public String vertifyMemNum(int mNum) {
        String msg = "";
        QueryConditions condition = new QueryConditions(); 
        condition.setConditionEqual("memId", mNum);
        MutuMemInfo macc = memInfoDao.findMemInfoByMnum(condition);
        if (macc != null) {
            msg = "error|会员ID重复!";
        } else {
            msg = "success";
        }       
        return msg;
    }

}
