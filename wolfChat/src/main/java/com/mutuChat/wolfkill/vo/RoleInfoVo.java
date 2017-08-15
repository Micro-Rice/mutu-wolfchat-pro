package com.mutuChat.wolfkill.vo;

import java.math.BigDecimal;

public class RoleInfoVo {
	String rName;
	int rMatchNum;
	BigDecimal rWrate;
	int rAchiveFre;
	int rOrder;
	int rtaskNum;
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	public int getrMatchNum() {
		return rMatchNum;
	}
	public void setrMatchNum(int rMatchNum) {
		this.rMatchNum = rMatchNum;
	}
	public BigDecimal getrWrate() {
		return rWrate;
	}
	public void setrWrate(BigDecimal rWrate) {
		this.rWrate = rWrate;
	}
	public int getrAchiveFre() {
		return rAchiveFre;
	}
	public void setrAchiveFre(int rAchiveFre) {
		this.rAchiveFre = rAchiveFre;
	}
	public int getrOrder() {
		return rOrder;
	}
	public void setrOrder(int rOrder) {
		this.rOrder = rOrder;
	}
	public int getRtaskNum() {
		return rtaskNum;
	}
	public void setRtaskNum(int rtaskNum) {
		this.rtaskNum = rtaskNum;
	}
	
}
