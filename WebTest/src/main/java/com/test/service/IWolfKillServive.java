/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.service
 * File Name:IWolfKillServive.java
 * Date:2016年10月27日 下午2:29:35
 * 
 */
package com.test.service;

import java.util.List;

import com.pospal.vo.ImageResponseDataDetail;
import com.pospal.vo.PostPointParameter;
import com.test.utils.PlayerInfoVo;

 /**
 * ClassName: IWolfKillServive <br/>
 * Description: TODO <br/>
 * Date: 2016年10月27日 下午2:29:35 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public interface IWolfKillServive {
    /*
     * 查询所有人概况信息
     */
    public List<String> getWolfKillMainData(int showSize,String matchNum);
    /*
     * 查询个人详细信息
     */
    public List<String> getWolfKillPerData(String playerName,String matchNum);
    /*
     * 查询个人概况信息
     */
    public String getPlayerMainDataByUid(String playerUid,String matchNum);
    /**
     * 查询玩家基本信息
     */
    public List<PlayerInfoVo> getPlayerBaseInfo();
    /**
     * 处理个人数据
     */
    public String updataUserInfo(String userInfo);
    /**
     * 保存銀豹会员数据到数据库
     */
    public void updatePospalMemInfo(List<ImageResponseDataDetail> rdds);
    /**
     * 或得银豹会员积分变动
     */
    public List<PostPointParameter> getPospalPointChange();
    
    /**
     * 查询个人历史赛季数
     */
    public List<String> getMatchNums(String playeyUid);
    /**
     * 查询最大的历史赛季
     */
    public int getMaxMatchNum();
}
