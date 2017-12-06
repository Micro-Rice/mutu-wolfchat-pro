/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebTest
 * Package Name:com.test.service
 * File Name:IMemInfoService.java
 * Date:2017年4月28日 下午4:33:29
 * 
 */
package com.mutuChat.service;

import java.util.List;

import com.mutuChat.wolfkill.model.MutuMemInfo;


 /**
 * ClassName: IMemInfoService <br/>
 * Description:  <br/>
 * Date: 2017年4月28日 下午4:33:29 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public interface IMemInfoService {
    String vertifyMemInfo(String name, String pass);
    
    String vertifyMemNum(int mNum);
    
    List<MutuMemInfo> queryMemInfos();
    
    String updateMemInfo(String mNum, String mName, String mPhone);
    
    String deleteMemInfos(List<MutuMemInfo> memInfos);
    
    String updateAccInfo(String mUid, String mName , String mpass, String opass);
}
