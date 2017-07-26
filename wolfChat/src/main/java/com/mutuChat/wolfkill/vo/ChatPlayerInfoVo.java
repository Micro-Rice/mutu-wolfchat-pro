package com.mutuChat.wolfkill.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChatPlayerInfoVo {
	String pName;
	String pLevel;
	String pTag;
	int pLevelNum;
	int pOrder;
	int pLevelMnum;
	BigDecimal pWrate;
	int pMatchNum;
	int pAchiveNum;
	int pPeoNum;
	BigDecimal pPeoRate;
	int pWolNum;
	BigDecimal pWolRate;
	int pOthNum;
	BigDecimal pOthRate;
	int maxOrder;
	String imgUrl;
	List<RoleInfoVo> roleInfos = new ArrayList<RoleInfoVo>();
	List<HisMatchVo> hisMatchs = new ArrayList<HisMatchVo>();
	
	public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public int getMaxOrder() {
        return maxOrder;
    }
    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }
    public List<RoleInfoVo> getRoleInfos() {
		return roleInfos;
	}
	public void setRoleInfos(List<RoleInfoVo> roleInfos) {
		this.roleInfos = roleInfos;
	}
	public List<HisMatchVo> getHisMatchs() {
		return hisMatchs;
	}
	public void setHisMatchs(List<HisMatchVo> hisMatchs) {
		this.hisMatchs = hisMatchs;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpLevel() {
		return pLevel;
	}
	public void setpLevel(String pLevel) {
		this.pLevel = pLevel;
	}
	public String getpTag() {
		return pTag;
	}
	public void setpTag(String pTag) {
		this.pTag = pTag;
	}
	public int getpLevelNum() {
		return pLevelNum;
	}
	public void setpLevelNum(int pLevelNum) {
		this.pLevelNum = pLevelNum;
	}
	public int getpOrder() {
		return pOrder;
	}
	public void setpOrder(int pOrder) {
		this.pOrder = pOrder;
	}
	public int getpLevelMnum() {
		return pLevelMnum;
	}
	public void setpLevelMnum(int pLevelMnum) {
		this.pLevelMnum = pLevelMnum;
	}
	public BigDecimal getpWrate() {
		return pWrate;
	}
	public void setpWrate(BigDecimal pWrate) {
		this.pWrate = pWrate;
	}
	public int getpMatchNum() {
		return pMatchNum;
	}
	public void setpMatchNum(int pMatchNum) {
		this.pMatchNum = pMatchNum;
	}
	public int getpAchiveNum() {
		return pAchiveNum;
	}
	public void setpAchiveNum(int pAchiveNum) {
		this.pAchiveNum = pAchiveNum;
	}
	public int getpPeoNum() {
		return pPeoNum;
	}
	public void setpPeoNum(int pPeoNum) {
		this.pPeoNum = pPeoNum;
	}
	public BigDecimal getpPeoRate() {
		return pPeoRate;
	}
	public void setpPeoRate(BigDecimal pPeoRate) {
		this.pPeoRate = pPeoRate;
	}
	public int getpWolNum() {
		return pWolNum;
	}
	public void setpWolNum(int pWolNum) {
		this.pWolNum = pWolNum;
	}
	public BigDecimal getpWolRate() {
		return pWolRate;
	}
	public void setpWolRate(BigDecimal pWolRate) {
		this.pWolRate = pWolRate;
	}
	public int getpOthNum() {
		return pOthNum;
	}
	public void setpOthNum(int pOthNum) {
		this.pOthNum = pOthNum;
	}
	public BigDecimal getpOthRate() {
		return pOthRate;
	}
	public void setpOthRate(BigDecimal pOthRate) {
		this.pOthRate = pOthRate;
	}
	
	
}
