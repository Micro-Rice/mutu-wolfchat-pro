/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.service.impl
 * File Name:WolfKillServiceImpl.java
 * Date:2016年10月27日 下午2:32:46
 * 
 */
package com.mutuChat.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutuChat.service.IWolfKillServive;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.dao.IWolfKillDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.utils.CommonUtils;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.mutuChat.wolfkill.utils.StringUtils;
import com.mutuChat.wolfkill.vo.ChatPlayerInfoVo;
import com.mutuChat.wolfkill.vo.CusInfoVo;
import com.mutuChat.wolfkill.vo.PdataVo;
import com.mutuChat.wolfkill.vo.PlayerInfoVo;
import com.mutuChat.wolfkill.vo.RoleInfoVo;
import com.pospal.vo.ImageResponseDataDetail;
import com.pospal.vo.MemInfoVo;
import com.pospal.vo.PostPointParameter;


 /**
 * ClassName: WolfKillServiceImpl <br/>
 * Description: <br/>
 * Date: 2016年10月27日 下午2:32:46 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Transactional
@Service("wolfKillService")
public class WolfKillServiceImpl implements IWolfKillServive{  
	private static Logger logger = Logger.getLogger(WolfKillServiceImpl.class);
    @Resource
    private IWolfKillDao wolfKillDao;
    
    @Resource
    private IWolfChatDao wolfChatDao;
    
    public List<ChatPlayerInfoVo> getWolfKillMainData(int showSize,String matchNum) {        
        List<ChatPlayerInfoVo> resultDatas = new ArrayList<ChatPlayerInfoVo>();        
        QueryConditions condition = new QueryConditions(); 
        String orderBy = "levelNum desc,achiveNum desc,levelMaxNum desc";
        condition.setOrderBy(orderBy);
        List<WolfKillMainInfo> mainDatas = null;
        List<WolfKillMainInfoHistory> mainHisDatas = null;
        if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
            condition.setConditionEqual(CommonUtils.MATCH_NUM, Integer.parseInt(matchNum));
            mainHisDatas = wolfKillDao.queryMainHisDataByCondition(condition);
            mainDatas = convertMainDatas(mainHisDatas);
        } else {
            mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
        }
        int length;
        if (mainDatas != null) {
            if (mainDatas.size() > showSize) {
                length = showSize;
            } else {
                length = mainDatas.size();
            }
            for (int i = 0; i < length; i++) {
            	ChatPlayerInfoVo resultData = parsePlayerData(mainDatas.get(i),i+1);
                resultDatas.add(resultData);
            }
        }        
        return resultDatas;
    }
    
	public ChatPlayerInfoVo getWolfKillPerData(String uniqueId,String matchNum) {
		ChatPlayerInfoVo resultData = null; 
		QueryConditions condition = new QueryConditions();
		condition.setConditionEqual(CommonUtils.UNIQUE_ID, uniqueId);
		
		List<WolfKillMainInfo> mainDatas = null;
		List<WolfKillPerInfo> perDatas = null;
		List<WolfKillMainInfoHistory> mainHisDatas = null;
		List<WolfKillPerInfoHistory> perHisDatas = null;
		if (!matchNum.isEmpty()) {
			if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
			    condition.setConditionEqual(CommonUtils.MATCH_NUM, Integer.parseInt(matchNum));
			    mainHisDatas = wolfKillDao.queryMainHisDataByCondition(condition);
			    perHisDatas = wolfKillDao.queryPersonHisDataCondition(condition);
			    mainDatas = convertMainDatas(mainHisDatas);
			    perDatas = convertPerDatas(perHisDatas);		    
			} else {
			    mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
		        perDatas = wolfKillDao.queryPersonDataCondition(condition);
			}
		}		
		if (mainDatas != null && perDatas != null) {
			WolfKillMainInfo mainData = mainDatas.get(0);
			resultData = parseMainInfo(mainData);
			List<RoleInfoVo> roleDatas = new ArrayList<RoleInfoVo>();
			for (int i = 0; i < perDatas.size(); i++) {
				RoleInfoVo roleData = parsePerData(perDatas.get(i));
				roleDatas.add(roleData);			
			}
			resultData.setRoleInfos(roleDatas);
		}
		return resultData;
	}
	
	public ChatPlayerInfoVo getPlayerMainDataByUid(String playerUid,String matchNum) {	    
		ChatPlayerInfoVo resultData = null;
        QueryConditions condition = new QueryConditions();
        String orderBy = "levelNum desc";
        condition.setOrderBy(orderBy);
        List<WolfKillMainInfo> mainDatas = null;
        List<WolfKillMainInfoHistory> mainHisDatas = null;
        if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
            condition.setConditionEqual(CommonUtils.MATCH_NUM, Integer.parseInt(matchNum));
            mainHisDatas = wolfKillDao.queryMainHisDataByCondition(condition);
            mainDatas = convertMainDatas(mainHisDatas);
        } else {
            mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
        }        
        /**
         * 只支持Uid查询；名字查询暂不支持
         */
        if (StringUtils.checkIsNum(playerUid)) {
        	for (int i = 0; i < mainDatas.size(); i++) {
        		WolfKillMainInfo mainData = mainDatas.get(i);
        		String pUid = mainData.getUniqueId();
        		if (pUid != null && pUid.equals(playerUid)) {
        			resultData = parsePlayerData(mainData,i+1);
        			break;
        		}
        	}
        } else {
        	for (int i = 0; i < mainDatas.size(); i++) {
        		WolfKillMainInfo mainData = mainDatas.get(i);
        		String pName = mainData.getPlayerName();
        		if (pName != null && pName.equals(playerUid)) {
        			resultData = parsePlayerData(mainData,i+1);
        			break;
        		}
        	}
        }
        return resultData;       
	}
	
	public String updataUserInfo(String userInfo) {
		 List<PdataVo> pdatas = JsonUtils.jsonToArrayList(userInfo, PdataVo.class);
		 List<WolfKillMainInfo> mainInfos = new ArrayList<WolfKillMainInfo>();
		 List<WolfKillPerInfo> perInfos = new ArrayList<WolfKillPerInfo>();
		 Map<String,Integer> levelMap = new HashMap<String,Integer>();
		 levelMap.put(CommonUtils.QTZL, 1);
		 levelMap.put(CommonUtils.BYZL, 2);
		 levelMap.put(CommonUtils.HJZL, 3);
		 levelMap.put(CommonUtils.BJZL, 4);
		 levelMap.put(CommonUtils.ZSZL, 5);
		for (int i = 0; i < pdatas.size(); i++) {
			CusInfoVo info = parseUserInfo(pdatas.get(i));
			QueryConditions condition = new QueryConditions();
	        condition.setConditionEqual(CommonUtils.UNIQUE_ID, info.getUid());
	        List<WolfKillMainInfo> perMainDatas = wolfKillDao.queryMainDataByCondition(condition);
	        if (perMainDatas != null && !perMainDatas.isEmpty()) {
	        	WolfKillMainInfo mainInfo = perMainDatas.get(0);
	        	int pn = mainInfo.getPeoNum() + info.getPeonum();
	        	int pw = mainInfo.getPeoWon() + info.getPeowin();
	        	int wn = mainInfo.getWolfNum() + info.getWolfnum();
	        	int ww = mainInfo.getWolfWon() + info.getWolfwin();
	        	int on = mainInfo.getOtherNum() + info.getOthernum();
	        	int ow = mainInfo.getOtherWon() + info.getOtherwin();
	        	int mvp = mainInfo.getMvp() + info.getMvp();
	        	int achiveNum = 0;
	        	if (mainInfo.getAchiveNum() != null) {
	        		achiveNum = mainInfo.getAchiveNum() + info.getAchiveNum();
	        	} else {
	        		achiveNum = info.getAchiveNum();
	        	}
	        	int levelnum;
	        	String level;
	        	if (mainInfo.getLevelNum() < CommonUtils.BYLV && info.getLeveladd() < 0) {
	        		levelnum = mainInfo.getLevelNum();
	        	} else {
	        		levelnum = mainInfo.getLevelNum() + info.getLeveladd();
	        	}
	        	int maxLevelNum = mainInfo.getLevelMaxNum();
	        	if (maxLevelNum <= levelnum) {
	        		maxLevelNum = levelnum;
	        		mainInfo.setLevelMaxNum(maxLevelNum);
	        	}
	        	if (maxLevelNum < CommonUtils.BYLV) {
	        		level = CommonUtils.QTZL;
	        	} else if (maxLevelNum >= CommonUtils.BYLV && maxLevelNum < CommonUtils.HJLV) {
	        		level = CommonUtils.BYZL;
	        	} else if (maxLevelNum >= CommonUtils.HJLV && maxLevelNum <= CommonUtils.BJLV) {
	        		level = CommonUtils.HJZL;
	        	} else if (maxLevelNum >= CommonUtils.BJLV && maxLevelNum < CommonUtils.ZSLV) {
	        		level = CommonUtils.BJZL;
	        	} else {
	        		level = CommonUtils.ZSZL;
	        	}
	        	String hisLevel = mainInfo.getLevel();
	        	if (levelMap.get(hisLevel) > levelMap.get(level)) {
	        		mainInfo.setLevel(hisLevel);
	        	} else {
	        		mainInfo.setLevel(level);
	        	}
	        	mainInfo.setPlayerName(info.getName());	        	
	        	mainInfo.setLevelNum(levelnum);
	        	mainInfo.setMvp(mvp);
	        	mainInfo.setOtherNum(on);
	        	mainInfo.setOtherWon(ow);
	        	mainInfo.setPeoNum(pn);
	        	mainInfo.setPeoWon(pw);
	        	mainInfo.setWolfNum(wn);
	        	mainInfo.setWolfWon(ww);
	        	mainInfo.setAchiveNum(achiveNum);
	        	mainInfos.add(mainInfo);
	        	
	        	condition.setConditionEqual(CommonUtils.ROLE_NAME, info.getRolename());
	        	List<WolfKillPerInfo> perDatas = wolfKillDao.queryPersonDataCondition(condition);
	        	if (perDatas != null && !perDatas.isEmpty()) {
	        		WolfKillPerInfo perInfo = perDatas.get(0);
	        		int rn = perInfo.getTotalNum() + 1;
	        		int rw = perInfo.getWonNum() + info.getRolewin();
	        		int achiveFre = 0;
	        		if (perInfo.getAchiveFre() != null) {
	        			achiveFre = perInfo.getAchiveFre() + info.getAchiveFre();
	        		} else {
	        			achiveFre = info.getAchiveFre();
	        		}
	        		perInfo.setTotalNum(rn);
	        		perInfo.setWonNum(rw);
	        		perInfo.setPlayerName(info.getName());
	        		perInfo.setAchiveFre(achiveFre);
	        		perInfos.add(perInfo);
	        	} else {
	        		WolfKillPerInfo peData = new WolfKillPerInfo();
	        		peData.setPlayerName(info.getName());
		        	peData.setUniqueId(info.getUid());
		        	peData.setRoleName(info.getRolename());
		        	peData.setTotalNum(1);
		        	peData.setWonNum(info.getRolewin());
		        	peData.setAchiveFre(info.getAchiveFre());
		        	perInfos.add(peData);
	        	}
	        } else {
	        	WolfKillMainInfo pmData = new WolfKillMainInfo();
	        	WolfKillPerInfo peData = new WolfKillPerInfo();
	        	pmData.setPlayerName(info.getName());
	        	pmData.setUniqueId(info.getUid());
	        	pmData.setMvp(info.getMvp());
	        	pmData.setPeoNum(info.getPeonum());
	        	pmData.setPeoWon(info.getPeowin());
	        	pmData.setOtherNum(info.getOthernum());
	        	pmData.setOtherWon(info.getOtherwin());
	        	pmData.setWolfNum(info.getWolfnum());
	        	pmData.setWolfWon(info.getWolfwin());
	        	pmData.setAchiveNum(info.getAchiveNum());
	        	pmData.setLevel(CommonUtils.QTZL);
	        	if (info.getLeveladd() > 0) {
	        		pmData.setLevelNum(info.getLeveladd());
	        		pmData.setLevelMaxNum(info.getLeveladd());
	        	} else {
	        		pmData.setLevelNum(0);
	        		pmData.setLevelMaxNum(0);
	        	}
	        	peData.setPlayerName(info.getName());
	        	peData.setUniqueId(info.getUid());
	        	peData.setRoleName(info.getRolename());
	        	peData.setTotalNum(1);
	        	peData.setWonNum(info.getRolewin());
	        	peData.setAchiveFre(info.getAchiveFre());
	        	mainInfos.add(pmData);
	        	perInfos.add(peData);
	        }
		}
		wolfKillDao.saveMainDataList(mainInfos);
		wolfKillDao.savePerDataList(perInfos);
		return "success||update memInfo success";
	}
	private List<WolfKillMainInfo> convertMainDatas (List<WolfKillMainInfoHistory> hisDatas) {
	    List<WolfKillMainInfo> mainDatas = new ArrayList<WolfKillMainInfo>();
	    if (hisDatas != null) {
	        for (int i = 0; i < hisDatas.size(); i++) {
	            WolfKillMainInfoHistory hisData = hisDatas.get(i);
	            WolfKillMainInfo mainData = new WolfKillMainInfo();
	            mainData.setId(hisData.getId());
	            mainData.setPlayerName(hisData.getPlayerName());
	            mainData.setUniqueId(hisData.getUniqueId());
	            mainData.setPeoNum(hisData.getPeoNum());
	            mainData.setPeoWon(hisData.getPeoWon());
	            mainData.setWolfNum(hisData.getWolfNum());
	            mainData.setWolfWon(hisData.getWolfWon());
	            mainData.setOtherNum(hisData.getOtherNum());
	            mainData.setOtherWon(hisData.getOtherWon());
	            mainData.setMvp(hisData.getMvp());
	            mainData.setLevelNum(hisData.getLevelNum());
	            mainData.setLevel(hisData.getLevel());
	            mainData.setLevelMaxNum(hisData.getLevelMaxNum());
	            mainData.setAchiveNum(hisData.getAchiveNum());
	            mainDatas.add(mainData);
	        }
	    }
	    return mainDatas;
	}
	private List<WolfKillPerInfo> convertPerDatas (List<WolfKillPerInfoHistory> hisDatas) {
        List<WolfKillPerInfo> perDatas = new ArrayList<WolfKillPerInfo>();
        if (hisDatas != null) {
            for (int i = 0; i < hisDatas.size(); i++) {
                WolfKillPerInfoHistory hisData = hisDatas.get(i);
                WolfKillPerInfo perData = new WolfKillPerInfo();
                perData.setId(hisData.getId());
                perData.setPlayerName(hisData.getPlayerName());
                perData.setUniqueId(hisData.getUniqueId());
                perData.setRoleName(hisData.getRoleName());
                perData.setTotalNum(hisData.getTotalNum());
                perData.setWonNum(hisData.getWonNum());
                perData.setAchiveFre(hisData.getAchiveFre());                
                perDatas.add(perData);
            }
        }
        return perDatas;
    }
	private CusInfoVo parseUserInfo(PdataVo userInfo) {
		CusInfoVo info = new CusInfoVo();
		
		info.setName(userInfo.getName());
		info.setUid(String.valueOf(userInfo.getId()));		
		/**
		 * 角色对应关系
		 */
		String roleName = parseRoleName(String.valueOf(userInfo.getRole()));
		info.setRolename(roleName);
		String mvpSign = String.valueOf(userInfo.getMvp());
		String roleachive = String.valueOf(userInfo.getAchieve());
		/**
		 * 成就积分计算,并判断是否达成成就
		 */
		computeAchive(roleachive,roleName,info);
		/**
		 * 根据成就次数，换算成就点数
		 */
		int achiveNum = info.getAchiveFre();
		//int achiveNum = computeAchiveNum(info.getAchiveFre());
		info.setAchiveNum(achiveNum);
		String winSign = String.valueOf(userInfo.getResult());
		if (CommonUtils.ZERO.equals(mvpSign)) {
			info.setMvp(0);
		} else {
			info.setMvp(1);
		}
		int camp = userInfo.getCamp();		
		int leveladd = userInfo.getScore();
		//computeLevelAdd(roleName,winSign,mvpSign,achive,camp);
		info.setLeveladd(leveladd);
		
		if (camp == 2) {
			info.setOthernum(1);
			if (CommonUtils.ONE.equals(winSign)) {
				info.setOtherwin(1);
			}
		} else if (camp == 0) {
			info.setPeonum(1);
			if (CommonUtils.ONE.equals(winSign)) {
				info.setPeowin(1);
			}
		} else if (camp == 1) {
			info.setWolfnum(1);
			if (CommonUtils.ONE.equals(winSign)) {
				info.setWolfwin(1);
			}
		}
		if (CommonUtils.ONE.equals(winSign)) {
			info.setRolewin(1);
		}
		return info;
	}
	/* 计算积分,改为前端计算.
	 * private int computeLevelAdd(String roleName,String winSign,String mvpSign,int achive,int camp) {
		int r = 0;
		if (camp == 0) {
			if (CommonUtils.VILLAGER.equals(roleName) || CommonUtils.WOLFBOY.equals(roleName)) {
				r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.PEOADD:-CommonUtils.PEOADD);
			} else if (CommonUtils.PROPHET.equals(roleName) || CommonUtils.WITCH.equals(roleName) 
					|| CommonUtils.HUNTER.equals(roleName) || CommonUtils.GUARD.equals(roleName) 
					|| CommonUtils.WALKER.equals(roleName) || CommonUtils.KNIGHT.equals(roleName)) {
				r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.GODADD1:-CommonUtils.GODADD1);
			} else if (CommonUtils.IDIOT.equals(roleName) ||CommonUtils. SILENT.equals(roleName) 
					|| CommonUtils.CUPID.equals(roleName) || CommonUtils.THIEF.equals(roleName)
					|| CommonUtils.BEAR.equals(roleName)){
				r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.GODADD2:-CommonUtils.GODADD2);
			} 
		} else if (camp == 1) {
			if (CommonUtils.WOLF.equals(roleName) || CommonUtils.BEAUTYWOLF.equals(roleName)
				|| CommonUtils.DEVIL.equals(roleName) || CommonUtils.WHITEWOLF.equals(roleName)
				|| CommonUtils.WOLFBOY.equals(roleName) || CommonUtils.CUPID.equals(roleName)) {
				r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.WOLADD:-CommonUtils.WOLADD);
			} else if (CommonUtils.THIEF.equals(roleName)) {
				r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.GODADD2:-CommonUtils.GODADD2);
			}
		} else if (camp == 2) {
			r = r + (CommonUtils.ONE.equals(winSign)?CommonUtils.OTHERADD:-CommonUtils.PEOADD);
		}
		if (CommonUtils.ONE.equals(mvpSign)) {
			r = r + CommonUtils.MVPADD;
		}
		if (camp != 2) {
			r = r + achive;
		}
		return r;
		
	}*/
	private int computeAchive(String cjSign,String roleName,CusInfoVo info) {
		int r = 0;
	    if (cjSign == null || roleName == null) {
	        return r;
	    }
	    if (CommonUtils.WITCH.equals(roleName) && cjSign.length() > 1) {
	        String x = cjSign.substring(0,1);
	        String y = cjSign.substring(1,2);
	        if ("4".equals(y)) {
	            info.setAchiveFre(1);
	            r = r + 20;
	        } else if ("3".equals(y)) {
	            r = r - 20;
	        }
	        if ("2".equals(x)) {
	            r = r + 10;
	        }
	    } else if (CommonUtils.PROPHET.equals(roleName)) {
	            if (StringUtils.checkIsNum(cjSign)) {
	                if (Integer.parseInt(cjSign) >= 2) {
	                    info.setAchiveFre(1);
	                    r = r + 20;
	                }
	            }
	    } else if (CommonUtils.HUNTER.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 20;
	            } else if ("2".equals(cjSign)) {
	                r = r - 20;
	            }
	    } else if (CommonUtils.GUARD.equals(roleName)) {
	            if (StringUtils.checkIsNum(cjSign)) {
	                if (Integer.parseInt(cjSign) >= 1) {
	                    info.setAchiveFre(1);
	                    if (Integer.parseInt(cjSign) == 1) {
	                        r = r + 10;
	                    } else if (Integer.parseInt(cjSign) == 2) {
	                        r = r + 25;
	                    } else if (Integer.parseInt(cjSign) >= 3) {
	                        r = r + 40;
	                    }
	                }
	            }
	    } else if (CommonUtils.WALKER.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 20;
	            } else if ("2".equals(cjSign)) {
	                r = r - 20;
	            }
	    } else if (CommonUtils.KNIGHT.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 10;
	            } else if ("2".equals(cjSign) || "3".equals(cjSign)) {
	                r = r - 10;
	            }
	    } else if (CommonUtils.WHITEWOLF.equals(roleName) || CommonUtils.BEAUTYWOLF.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 30;
	            } else if ("2".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 20;
	            } else if ("3".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 20;
	            }
	        }
	   return r;
	}
    private ChatPlayerInfoVo parsePlayerData(WolfKillMainInfo playerData,int order) {
        int tg = playerData.getPeoNum() + playerData.getWolfNum() + playerData.getOtherNum();
        int pw = playerData.getPeoWon();
        int ww = playerData.getWolfWon();
        int ow = playerData.getOtherWon();
        int levelNum = playerData.getLevelNum();
        int mvp = playerData.getMvp();
        String level = playerData.getLevel();
        String name = playerData.getPlayerName();
        String uniqueId = playerData.getUniqueId();
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual("playerId", uniqueId);
        WolfKillChatUserInfo userInfo = wolfChatDao.findChatUserInfo(condition);
        String urlImg = null;
        if (userInfo != null) {
        	urlImg = userInfo.getOpenImg();
        }
        int achiveNum = 0;
        if (playerData.getAchiveNum() != null) {
        	achiveNum = playerData.getAchiveNum();
        }
       
        BigDecimal totRate = computeRate((pw + ww + ow), tg);
        
        ChatPlayerInfoVo player = new ChatPlayerInfoVo();
        player.setpName(name);
        player.setpAchiveNum(achiveNum);
        player.setpWrate(totRate);
        player.setPlayerId(uniqueId);
        player.setMvp(mvp);
        player.setpLevel(level);
        player.setpLevelNum(levelNum);
        player.setpOrder(order);
        player.setpTag(urlImg);      
        return player;
    }
    
    private ChatPlayerInfoVo parseMainInfo(WolfKillMainInfo playerData) {
    	 int tg = playerData.getPeoNum() + playerData.getWolfNum() + playerData.getOtherNum();
    	 int pg = playerData.getPeoNum();
    	 int wg = playerData.getWolfNum();
    	 int og = playerData.getOtherNum();
         int pw = playerData.getPeoWon();
         int ww = playerData.getWolfWon();
         int ow = playerData.getOtherWon();
         int levelNum = playerData.getLevelNum();
         int maxLevelNum = playerData.getLevelMaxNum();
         int mvp = playerData.getMvp();
         int achiveNum = 0;
         if (playerData.getAchiveNum() != null) {
        	 achiveNum = playerData.getAchiveNum();
         }
         String level = playerData.getLevel();
         String name = playerData.getPlayerName();
         String uniqueId = playerData.getUniqueId();
         QueryConditions condition = new QueryConditions();
         condition.setConditionEqual("playerId", uniqueId);
         WolfKillChatUserInfo userInfo = wolfChatDao.findChatUserInfo(condition);
         String urlImg = null;
         if (userInfo != null) {
         	urlImg = userInfo.getOpenImg();
         }
         BigDecimal totRate = computeRate((pw + ww + ow), tg);
         BigDecimal pRate = computeRate(pw,pg);
         BigDecimal wRate = computeRate(ww,wg);
         BigDecimal oRate = computeRate(ow,og);
         
         ChatPlayerInfoVo player = new ChatPlayerInfoVo();
         player.setpName(name);
         player.setpMatchNum(tg);
         player.setpWrate(totRate);
         player.setpPeoNum(pg);
         player.setpPeoRate(pRate);
         player.setpWolNum(wg);
         player.setpWolRate(wRate);
         player.setpOthNum(og);
         player.setpOthRate(oRate);
         player.setMvp(mvp);
         player.setPlayerId(uniqueId);
         player.setpLevel(level);
         player.setpLevelNum(levelNum);
         player.setpLevelMnum(maxLevelNum);
         player.setpAchiveNum(achiveNum);
         player.setpTag(urlImg);
        
         return player;
         
    }
    
    
    
    private String parseRoleName(String sign) {
    	String result = "ADDROLE";
    	if ("2".equals(sign)) {
    		result = CommonUtils.VILLAGER;
    	} else if (CommonUtils.ONE.equals(sign)) {
    		result = CommonUtils.WOLF;
    	} else if ("3".equals(sign)) {
    		result = CommonUtils.PROPHET;
    	} else if ("4".equals(sign)) {
    		result = CommonUtils.WITCH;
    	} else if ("5".equals(sign)) {
    		result = CommonUtils.HUNTER;
    	} else if ("6".equals(sign)) {
    		result = CommonUtils.IDIOT;
    	} else if ("7".equals(sign)) {
    		result = CommonUtils.GUARD;
    	} else if ("8".equals(sign)) {
    		result = CommonUtils.WHITEWOLF;
    	} else if ("9".equals(sign)) {
    		result = CommonUtils.WALKER;
    	} else if ("10".equals(sign)) {
    		result = CommonUtils.BEAUTYWOLF;
    	} else if ("13".equals(sign)) {
    		result = CommonUtils.DEVIL;
    	} else if ("11".equals(sign)) {
    		result = CommonUtils.SILENT;
    	} else if ("12".equals(sign)) {
    		result = CommonUtils.KNIGHT;
    	} else if ("14".equals(sign)) {
    		result = CommonUtils.THIEF;
    	} else if ("15".equals(sign)) {
    		result = CommonUtils.CUPID;
    	} else if ("16".equals(sign)) {
    		result = CommonUtils.WOLFBOY;
    	} else if ("17".equals(sign)) {
    		result = CommonUtils.BEAR;
    	}  
    	return result;
    }
    private BigDecimal computeRate (int a1, int a2) {
        BigDecimal A1 = new BigDecimal(a1);
        BigDecimal r = new BigDecimal(0);
        if (a2 != 0) {
            BigDecimal A2 = new BigDecimal(a2);
            r = A1.divide(A2, 4, BigDecimal.ROUND_HALF_UP);
        }        
        return r;
    }

	private RoleInfoVo parsePerData(WolfKillPerInfo  roleData) {
		String roleName = roleData.getRoleName();
		int tg = roleData.getTotalNum();
		int tw = roleData.getWonNum();
		int achiveFre = 0;
		if (roleData.getAchiveFre() != null) {
			achiveFre = roleData.getAchiveFre();
		}
		BigDecimal rate = computeRate(tw,tg);
		RoleInfoVo role = new RoleInfoVo();
		role.setrName(roleName);
		role.setrMatchNum(tg);
		role.setrWrate(rate);
		role.setrAchiveFre(achiveFre);
        
        return role;
	}
		
	@Override
	public void updatePospalMemInfo(List<ImageResponseDataDetail> rdds) {
		List<WolfKillPospalInfo> posInfos = new ArrayList<WolfKillPospalInfo>();
		List<String> userNums = new ArrayList<String>();
		QueryConditions condition = new QueryConditions();
		try {
			List<WolfKillPospalInfo> pospalDatas = wolfKillDao.queryPospalPoint(condition);
			for (int i = 0; i < pospalDatas.size(); i++) {
				WolfKillPospalInfo posData = pospalDatas.get(i);
				userNums.add(posData.getNumber());
			}
			for (int i = 0; i < rdds.size(); i++) {
				ImageResponseDataDetail rdd = rdds.get(i);
				List<MemInfoVo> memInfos = rdd.getResult();
				for (int j = 0; j < memInfos.size(); j++) {
					MemInfoVo minfo = memInfos.get(j);
					if (!"1".equals(minfo.getEnable())) {
						continue;
					}
					String number = minfo.getNumber();
					if (!userNums.contains(number)) {
						WolfKillPospalInfo pinfo = new WolfKillPospalInfo();
						pinfo.setCustomerUid(minfo.getCustomerUid());
						pinfo.setCategoryName(minfo.getCategoryName());
						pinfo.setAddress(minfo.getAddress());
						pinfo.setBalance(Float.parseFloat(minfo.getBalance()));
						pinfo.setBirthday(minfo.getBirthday());
						pinfo.setDiscount(Float.parseFloat(minfo.getDiscount()));
						pinfo.setCreatedDate(minfo.getCreatedDate());
						pinfo.setEmail(minfo.getEmail());
						pinfo.setEnable(Integer.parseInt(minfo.getEnable()));
						pinfo.setName(minfo.getName());
						pinfo.setNumber(minfo.getNumber());
						pinfo.setOnAccount(Integer.parseInt(minfo.getOnAccount()));
						pinfo.setPhone(minfo.getPhone());
						pinfo.setPoint(Float.parseFloat(minfo.getPoint()));
						pinfo.setQq(minfo.getQq());
						posInfos.add(pinfo);
					}					
				}
			}
			wolfKillDao.savePospalMemList(posInfos);
		} catch (Exception e) {
			logger.error("updatePospalMemInfo error :" +e);
			throw new RuntimeException(e);
		}		
	}

	@Override
	public List<PostPointParameter> getPospalPointChange() {
		  QueryConditions condition = new QueryConditions();        
	      List<WolfKillMainInfo> mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
	      List<WolfKillPospalInfo> pospalDatas = wolfKillDao.queryPospalPoint(condition);
	      List<PostPointParameter> results = new ArrayList<PostPointParameter>();
	      /**
	       * 积分有变动的更新，没有的不更新
	       */
	      for (int i = 0; i < pospalDatas.size(); i++) {
	    	  WolfKillPospalInfo pospal = pospalDatas.get(i);
	    	  String number = pospal.getNumber();
	    	  Float point = pospal.getPoint();
	    	  long uid = pospal.getCustomerUid();
	    	  for (int j = 0; j < mainDatas.size(); j++) {
	    		  WolfKillMainInfo local = mainDatas.get(j);
	    		  String unique = local.getUniqueId();
	    		  int levelNum = local.getLevelMaxNum();
	    		  if (number != null && number.equals(unique)) {
	    			  int p = 0;
	    			  if (point != null) {
	    				  p = point.intValue();
	    			  }
	    			  if (p != levelNum) {
	    				  PostPointParameter re = new PostPointParameter();
	    				  re.setCustomerUid(uid);
	    				  int inc = levelNum -p;
	    				  re.setBalanceIncrement(new BigDecimal(0));
	    				  re.setPointIncrement(new BigDecimal(inc));
	    				  results.add(re);
	    			  }
	    		  }
	    	  }
	      }
	      return results;		
	}
	
	@Override
	public List<PlayerInfoVo> getPlayerBaseInfo() {
		List<PlayerInfoVo> playerInfos = new ArrayList<PlayerInfoVo>();
		QueryConditions condition = new QueryConditions();
		List<WolfKillPospalInfo> pospalDatas = wolfKillDao.queryPospalPoint(condition);
		List<WolfKillMainInfo> mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
		for (int i = 0; i < pospalDatas.size(); i++) {
			WolfKillPospalInfo pospal = pospalDatas.get(i);
			PlayerInfoVo playerInfo = new PlayerInfoVo();
			String uniqueId = pospal.getNumber();
			int point = 0;
			int pointMax = 0;
			String telephone = pospal.getPhone();
			String name = pospal.getName();
			if (uniqueId != null && StringUtils.checkIsNum(uniqueId)) {
				for (int j = 0; j < mainDatas.size(); j++) {
					WolfKillMainInfo mainData = mainDatas.get(j);
					if (uniqueId.equals(mainData.getUniqueId())) {
						if (mainData.getLevelNum() != null) {
							point = mainData.getLevelNum();
						}
						if (mainData.getLevelMaxNum() != null) {
							pointMax = mainData.getLevelMaxNum();
						}
						break;
					}					
				}
				playerInfo.setNum(Long.parseLong(uniqueId));
			}
			playerInfo.setName(name);
			playerInfo.setPoint(point);
			playerInfo.setPointMax(pointMax);
			if (telephone != null && StringUtils.checkIsNum(telephone)) {
				playerInfo.setTelephone(Long.parseLong(telephone));
			}		
			playerInfos.add(playerInfo);
		}
		return playerInfos;
}
	
	@Override
	public List<String> getMatchNums(String playerUid) {
		List<String> res = new ArrayList<String>();
		QueryConditions condition = new QueryConditions();
		if (StringUtils.checkIsNum(playerUid)) {
			condition.setConditionEqual(CommonUtils.UNIQUE_ID, playerUid);
		} else {
			condition.setConditionEqual("playerName", playerUid);
		}
		
		String orderBy = "matchNum desc";
        condition.setOrderBy(orderBy);
		List<Integer> matchNums = wolfKillDao.queryMatchNum(condition);
		if (matchNums != null) {
			for (int i = 0; i < matchNums.size(); i++) {
				int temp = matchNums.get(i);
				if (!res.contains(String.valueOf(temp))) {
					res.add(String.valueOf(temp));
				}
			}
		}		
		return res;
	}

	@Override
	public int getMaxMatchNum() {
		int max = 0;
		QueryConditions condition = new QueryConditions(); 
		List<Integer> matchNums = wolfKillDao.queryMatchNum(condition);
		if (matchNums != null) {
			for (int i = 0; i < matchNums.size(); i++) {
				int temp = matchNums.get(i);
				if (max < temp) {
					max = temp;
				}
			}
		}
		return max;
	}
}
