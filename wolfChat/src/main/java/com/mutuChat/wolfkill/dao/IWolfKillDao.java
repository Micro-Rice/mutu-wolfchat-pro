/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.wolfkill.dao
 * File Name:WolfkillDao.java
 * Date:2016年10月27日 下午3:19:17
 * 
 */
package com.mutuChat.wolfkill.dao;

import java.util.List;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.MutuMemInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;


 /**
 * ClassName: WolfkillDao <br/>
 * Description: TODO <br/>
 * Date: 2016年10月27日 下午3:19:17 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
public interface IWolfKillDao {
    
    public List<WolfKillMainInfo> queryMainDataByCondition(QueryConditions condition);
    public List<WolfKillPerInfo> queryPersonDataCondition(QueryConditions condition);
    
    public void saveMainData(WolfKillMainInfo mainInfo);
    public void savePerData(WolfKillPerInfo perInfo);
    
    public void saveMainDataList(List<WolfKillMainInfo> mainInfos);
    public void savePerDataList(List<WolfKillPerInfo> perInfos);
    
    public void savePospalMemList(List<WolfKillPospalInfo> pospalInfos);
    
    public List<WolfKillPospalInfo> queryPospalPoint(QueryConditions condition);
    
    public List<MutuMemInfo> queryMemInfoByLocal(QueryConditions condition);
    
    public void deletePospalMemList(List<WolfKillPospalInfo> pospalInfos);
    
}
