/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebTest
 * Package Name:com.test.wolfkill.dao
 * File Name:IMemInfoDao.java
 * Date:2017年4月28日 下午4:44:26
 * 
 */
package com.mutuChat.wolfkill.dao;

import java.util.List;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.MutuAccountInfo;
import com.mutuChat.wolfkill.model.MutuMemInfo;


 /**
 * ClassName: IMemInfoDao <br/>
 * Description: TODO <br/>
 * Date: 2017年4月28日 下午4:44:26 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public interface IMemInfoDao {
    MutuAccountInfo findAccountInfoByName(QueryConditions condition);
    MutuMemInfo findMemInfoByMnum(QueryConditions condition);
    List<MutuMemInfo> getAllMemInfo(QueryConditions condition);
    void updateMemInfos(MutuMemInfo memInfo);
    void delMemInfos(List<MutuMemInfo> memInfos);
    void saveAccountInfo(MutuAccountInfo accInfo);
}
