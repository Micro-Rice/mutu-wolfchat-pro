/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:wolfChat
 * Package Name:com.mutuChat.wolfkill.vo
 * File Name:RoleOrderVo.java
 * Date:2017年7月27日 下午5:02:29
 * 
 */
package com.mutuChat.wolfkill.vo;

 /**
 * ClassName: RoleOrderVo <br/>
 * Description: TODO <br/>
 * Date: 2017年7月27日 下午5:02:29 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class RoleOrderVo {
    String rName;
    int mNum;
    int wNum;
    String sPlayerId;
    String sOpenImg;
    String sAchiveNum;
    public int getwNum() {
        return wNum;
    }
    public void setwNum(int wNum) {
        this.wNum = wNum;
    }
    public String getrName() {
        return rName;
    }
    public void setrName(String rName) {
        this.rName = rName;
    }
    public int getmNum() {
        return mNum;
    }
    public void setmNum(int mNum) {
        this.mNum = mNum;
    }
    public String getsPlayerId() {
        return sPlayerId;
    }
    public void setsPlayerId(String sPlayerId) {
        this.sPlayerId = sPlayerId;
    }
    public String getsOpenImg() {
        return sOpenImg;
    }
    public void setsOpenImg(String sOpenImg) {
        this.sOpenImg = sOpenImg;
    }
    public String getsAchiveNum() {
        return sAchiveNum;
    }
    public void setsAchiveNum(String sAchiveNum) {
        this.sAchiveNum = sAchiveNum;
    }
}
