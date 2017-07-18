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
package com.test.wolfkill.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.core.dao.BaseDao;
import com.test.core.entity.page.QueryConditions;
import com.test.wolfkill.dao.IWolfKillDao;
import com.test.wolfkill.model.WolfKillPerInfoHistory;
import com.test.wolfkill.model.WolfKillMainInfo;
import com.test.wolfkill.model.WolfKillMainInfoHistory;
import com.test.wolfkill.model.WolfKillPerInfo;
import com.test.wolfkill.model.WolfKillPospalInfo;

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
    public List<WolfKillMainInfoHistory> queryMainHisDataByCondition(QueryConditions condition) {
        String hql = "from WolfKillMainInfoHistory ";
        return super.find(hql, condition);
    }

    @Override
    public List<WolfKillPerInfoHistory> queryPersonHisDataCondition(QueryConditions condition) {
        String hql = "from WolfKillPerInfoHistory ";
        return super.find(hql, condition);
    }

	@Override
	public List<Integer> queryMatchNum(QueryConditions condition) {
		String hql = "select matchNum from WolfKillMainInfoHistory ";
		return super.find(hql, condition);
	}
}
