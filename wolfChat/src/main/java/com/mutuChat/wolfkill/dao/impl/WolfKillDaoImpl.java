/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.wolfkill.dao.impl
 * File Name:WolfkillDao.java
 * Date:2016年10月27日 下午3:27:21
 * 
 */
package com.mutuChat.wolfkill.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfKillDao;
import com.mutuChat.wolfkill.model.MutuMemInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;



 /**
 * ClassName: WolfkillDao <br/>
 * Description:  <br/>
 * Date: 2016年10月27日 下午3:27:21 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Repository("wolfkillDao")
public class WolfKillDaoImpl extends BaseDao implements IWolfKillDao{

    @Override
    public List<WolfKillMainInfo> queryMainDataByCondition(QueryConditions condition) {
        String hql = "from WolfKillMainInfo ";
        return super.find(hql, condition);
    }

    @Override
    public List<WolfKillPerInfo> queryPersonDataCondition(QueryConditions condition) {
        String hql = "from WolfKillPerInfo ";
        return super.find(hql, condition);
    }

	@Override
	public void saveMainData(WolfKillMainInfo mainInfo) {
		super.save(mainInfo);
	}

	@Override
	public void savePerData(WolfKillPerInfo perInfo) {
		super.save(perInfo);
	}

	@Override
	public void saveMainDataList(List<WolfKillMainInfo> mainInfos) {
		super.saveAll(mainInfos);		
	}

	@Override
	public void savePerDataList(List<WolfKillPerInfo> perInfos) {
		super.saveAll(perInfos);
	}

	@Override
	public void savePospalMemList(List<WolfKillPospalInfo> pospalInfos) {
		super.saveAll(pospalInfos);		
	}

	@Override
	public List<WolfKillPospalInfo> queryPospalPoint(QueryConditions condition) {
		String hql = "from WolfKillPospalInfo ";
        return super.find(hql, condition);
	}

	@Override
	public void deletePospalMemList(List<WolfKillPospalInfo> pospalInfos) {
		super.deleteAll(pospalInfos);
	}



    @Override
    public List<MutuMemInfo> queryMemInfoByLocal(QueryConditions condition) {
        String hql = "from MutuMemInfo ";
        return super.find(hql, condition);
    }
}
