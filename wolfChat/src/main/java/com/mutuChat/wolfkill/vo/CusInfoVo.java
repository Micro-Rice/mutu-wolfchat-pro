/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WeChatTest
 * Package Name:com.mutu.wechat.util
 * File Name:memInfoVo.java
 * Date:2016年12月28日 下午5:24:29
 * 
 */
package com.mutuChat.wolfkill.vo;

 /**
 * ClassName: memInfoVo <br/>
 * Description: TODO <br/>
 * Date: 2016年12月28日 下午5:24:29 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class CusInfoVo {
	private String name;
    private String uid;
    private int peowin;
    private int peonum;
    private int wolfwin;
    private int wolfnum;
    private int otherwin;
    private int othernum;
    private int mvp;
    private int leveladd;
    private String rolename;
    private int rolewin;
    /**
     * 成就点数
     */
    private int achiveNum;
    /**
     * 成就次数，每个角色有一个成就名称；如果有多个需要拓展
     */
    private int achiveFre;
	public int getAchiveNum() {
		return achiveNum;
	}
	public void setAchiveNum(int achiveNum) {
		this.achiveNum = achiveNum;
	}
	public int getAchiveFre() {
		return achiveFre;
	}
	public void setAchiveFre(int achiveFre) {
		this.achiveFre = achiveFre;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getPeowin() {
        return peowin;
    }
    public void setPeowin(int peowin) {
        this.peowin = peowin;
    }
    public int getPeonum() {
        return peonum;
    }
    public void setPeonum(int peonum) {
        this.peonum = peonum;
    }
    public int getWolfwin() {
        return wolfwin;
    }
    public void setWolfwin(int wolfwin) {
        this.wolfwin = wolfwin;
    }
    public int getWolfnum() {
        return wolfnum;
    }
    public void setWolfnum(int wolfnum) {
        this.wolfnum = wolfnum;
    }
    public int getOtherwin() {
        return otherwin;
    }
    public void setOtherwin(int otherwin) {
        this.otherwin = otherwin;
    }
    public int getOthernum() {
        return othernum;
    }
    public void setOthernum(int othernum) {
        this.othernum = othernum;
    }
    public int getMvp() {
        return mvp;
    }
    public void setMvp(int mvp) {
        this.mvp = mvp;
    }
    public int getLeveladd() {
        return leveladd;
    }
    public void setLeveladd(int leveladd) {
        this.leveladd = leveladd;
    }
    public String getRolename() {
        return rolename;
    }
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
    public int getRolewin() {
        return rolewin;
    }
    public void setRolewin(int rolewin) {
        this.rolewin = rolewin;
    }    
}
