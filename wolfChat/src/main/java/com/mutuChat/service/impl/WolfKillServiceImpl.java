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
import com.mutuChat.wolfkill.dao.IWolfKillDao;
import com.mutuChat.wolfkill.model.MutuMemInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.utils.StringUtils;
import com.mutuChat.wolfkill.vo.CusInfoVo;
import com.mutuChat.wolfkill.vo.PlayerInfoVo;
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
    
    private final static String LINE = "-";
    private final static String UNDERLINE = "_";
    private final static String UNIQUE_ID="uniqueId";
    private final static String MATCH_NUM = "matchNum";
    private final static String ROLE_NAME="roleName";
    private final static String ZERO="0";
    private final static String ONE="1";
	private final static String PROPHET = "prophet";
	private final static String WITCH = "witch";
	private final static String HUNTER = "hunter";
	private final static String IDIOT = "idiot";
	private final static String SILENT = "silent";
	private final static String GUARD = "guard";
	private final static String WALKER = "walker";
	private final static String WHITEWOLF = "whitewolf";
	private final static String BEAUTYWOLF = "beautywolf";
	private final static String WOLF = "wolf";
	private final static String CUPID = "cupid";
	private final static String VILLAGER = "villager";
	private final static String KNIGHT = "knight";
	private final static String DEVIL ="devil";
	private final static String QTZL = "青铜战狼";
	private final static String BYZL = "白银战狼";
	private final static String HJZL = "黄金战狼";
	private final static String BJZL = "白金战狼";
	private final static String ZSZL = "钻石战狼";
	private final static String PEOTEAM = "好人阵营";
	private final static String WOLFTEAM = "狼人阵营";
	private final static String OTHERTEAM = "其他阵营";
	private final static int BYLV = 1500;
	private final static int HJLV = 3000;
	private final static int BJLV = 4500;
	private final static int ZSLV = 6000;
	private final static int PEOADD = 20;
	private final static int GODADD1 = 30;
	//private final static int GODADD2 = 70;
	private final static int WOLADD = 40;
	//private final static int OTHADD = 70;
	private final static int MVPADD = 0;
	private static Logger logger = Logger.getLogger(WolfKillServiceImpl.class);
    @Resource
    private IWolfKillDao wolfKillDao;
    
    public List<String> getWolfKillMainData(int showSize,String matchNum) {        
        List<String> resultDatas = new ArrayList<String>();        
        QueryConditions condition = new QueryConditions(); 
        String orderBy = "levelNum desc";
        condition.setOrderBy(orderBy);
        List<WolfKillMainInfo> mainDatas = null;
        List<WolfKillMainInfoHistory> mainHisDatas = null;
        if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
            condition.setConditionEqual(MATCH_NUM, Integer.parseInt(matchNum));
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
                String resultData = parsePlayerData(mainDatas.get(i),i+1);
                resultDatas.add(resultData);
            }
        }        
        return resultDatas;
    }
    
	public List<String> getWolfKillPerData(String uniqueId,String matchNum) {
		List<String> resultDatas = new ArrayList<String>(); 
		QueryConditions condition = new QueryConditions();
		condition.setConditionEqual(UNIQUE_ID, uniqueId);
		
		List<WolfKillMainInfo> mainDatas = null;
		List<WolfKillPerInfo> perDatas = null;
		List<WolfKillMainInfoHistory> mainHisDatas = null;
		List<WolfKillPerInfoHistory> perHisDatas = null;
		if (!matchNum.isEmpty()) {
			if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
			    condition.setConditionEqual(MATCH_NUM, Integer.parseInt(matchNum));
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
			String mainInfo = parseMainInfo(mainData);
			resultDatas.add(mainInfo);
			for (int i = 0; i < perDatas.size(); i++) {
				String resultData = parsePerData(perDatas.get(i));
				resultDatas.add(resultData);			
			}
		}
		sortDatasByTotal(resultDatas);
		return resultDatas;
	}
	
	public String getPlayerMainDataByUid(String playerUid,String matchNum) {	    
		String resultData = null;
        QueryConditions condition = new QueryConditions();
        String orderBy = "levelNum desc";
        condition.setOrderBy(orderBy);
        List<WolfKillMainInfo> mainDatas = null;
        List<WolfKillMainInfoHistory> mainHisDatas = null;
        if (StringUtils.checkIsNum(matchNum) && Integer.parseInt(matchNum) > 0) {
            condition.setConditionEqual(MATCH_NUM, Integer.parseInt(matchNum));
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
        }         
        return resultData;       
	}
	
	public String updataUserInfo(String userInfo) {
		String[] userObj = userInfo.split(UNDERLINE);
		 List<WolfKillMainInfo> mainInfos = new ArrayList<WolfKillMainInfo>();
		 List<WolfKillPerInfo> perInfos = new ArrayList<WolfKillPerInfo>();
		 Map<String,Integer> levelMap = new HashMap<String,Integer>();
		 levelMap.put(QTZL, 1);
		 levelMap.put(BYZL, 2);
		 levelMap.put(HJZL, 3);
		 levelMap.put(BJZL, 4);
		 levelMap.put(ZSZL, 5);
		for (int i = 0; i < userObj.length; i++) {
			CusInfoVo info = parseUserInfo(userObj[i]);
			if (info == null) {
				return "error||updataUserInfo failer, userdata is illegal format";
			}
			QueryConditions condition = new QueryConditions();
	        condition.setConditionEqual(UNIQUE_ID, info.getUid());
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
	        	if (mainInfo.getLevelNum() < BYLV && info.getLeveladd() < 0) {
	        		levelnum = mainInfo.getLevelNum();
	        	} else {
	        		levelnum = mainInfo.getLevelNum() + info.getLeveladd();
	        	}
	        	int maxLevelNum = mainInfo.getLevelMaxNum();
	        	if (maxLevelNum <= levelnum) {
	        		maxLevelNum = levelnum;
	        		mainInfo.setLevelMaxNum(maxLevelNum);
	        	}
	        	if (maxLevelNum < BYLV) {
	        		level = QTZL;
	        	} else if (maxLevelNum >= BYLV && maxLevelNum < HJLV) {
	        		level = BYZL;
	        	} else if (maxLevelNum >= HJLV && maxLevelNum <= BJLV) {
	        		level = HJZL;
	        	} else if (maxLevelNum >= BJLV && maxLevelNum < ZSLV) {
	        		level = BJZL;
	        	} else {
	        		level = ZSZL;
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
	        	
	        	condition.setConditionEqual(ROLE_NAME, info.getRolename());
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
	        	pmData.setLevel(QTZL);
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
	private CusInfoVo parseUserInfo(String userInfo) {
		CusInfoVo info = new CusInfoVo();
		if (userInfo.indexOf(LINE) < 0 || userInfo.split(LINE).length != 6) {
			logger.error("updataUserInfo is error: userData is illegal format");
			return null;
		}
		/**
		 * hardCode,以后改成JSON;
		 * 0-name
		 * 1-uid
		 * 2-rolename
		 * 3-win or lose
		 * 4-mvp
		 * 5-roleachive
		 */
		info.setName(userInfo.split(LINE)[0]);
		info.setUid(userInfo.split(LINE)[1]);		
		/**
		 * 角色对应关系
		 */
		String roleName = parseRoleName(userInfo.split(LINE)[2]);
		info.setRolename(roleName);
		String mvpSign = userInfo.split(LINE)[4];
		String cupidSign = ZERO;
		String roleachive = userInfo.split(LINE)[5];
		/**
		 * 成就积分计算,并判断是否达成成就
		 */
		int achive = computeAchive(roleachive,roleName,info);
		/**
		 * 根据成就次数，换算成就点数
		 */
		int achiveNum = info.getAchiveFre();
		//int achiveNum = computeAchiveNum(info.getAchiveFre());
		info.setAchiveNum(achiveNum);
		String winSign = userInfo.split(LINE)[3];
		if (ZERO.equals(mvpSign)) {
			info.setMvp(0);
		} else {
			info.setMvp(1);
		}
		String team = getTeam(roleName,cupidSign);
		if (ONE.equals(winSign)) {
			if (ONE.equals(mvpSign)) {
				if (VILLAGER.equals(roleName)) {
					info.setLeveladd(PEOADD+MVPADD+achive);
				} else if (PROPHET.equals(roleName) || WITCH.equals(roleName) || HUNTER.equals(roleName)
						|| GUARD.equals(roleName) || WALKER.equals(roleName) || KNIGHT.equals(roleName)) {
					info.setLeveladd(GODADD1+MVPADD+achive);
				}else if (WOLFTEAM.equals(team)) {
					info.setLeveladd(WOLADD+MVPADD+achive);
				} else if (IDIOT.equals(roleName) || SILENT.equals(roleName) || CUPID.equals(roleName)) {
					info.setLeveladd(GODADD1+MVPADD+achive);
				}
			} else {
				if (VILLAGER.equals(roleName)) {
					info.setLeveladd(PEOADD+achive);
				} else if (PROPHET.equals(roleName) || WITCH.equals(roleName) || HUNTER.equals(roleName)
						|| GUARD.equals(roleName) || WALKER.equals(roleName) || KNIGHT.equals(roleName)) {
					info.setLeveladd(GODADD1+achive);
				}else if (WOLFTEAM.equals(team)) {
					info.setLeveladd(WOLADD+achive);
				} else if (IDIOT.equals(roleName) || SILENT.equals(roleName) || CUPID.equals(roleName)) {
					info.setLeveladd(GODADD1+achive);
				}
			}			
		} else {
			if (ONE.equals(mvpSign)) {
				if (VILLAGER.equals(roleName)) {
					info.setLeveladd(-PEOADD+MVPADD+achive);
				} else if (PROPHET.equals(roleName) || WITCH.equals(roleName) || HUNTER.equals(roleName)
						|| GUARD.equals(roleName) || WALKER.equals(roleName) || KNIGHT.equals(roleName)) {
					info.setLeveladd(-GODADD1+MVPADD+achive);
				}else if (WOLFTEAM.equals(team)) {
					info.setLeveladd(-WOLADD+MVPADD+achive);
				} else if (IDIOT.equals(roleName) || SILENT.equals(roleName) || CUPID.equals(roleName)) {
					info.setLeveladd(-GODADD1+MVPADD+achive);
				}
			} else {
				if (VILLAGER.equals(roleName)) {
					info.setLeveladd(-PEOADD+achive);
				} else if (PROPHET.equals(roleName) || WITCH.equals(roleName) || HUNTER.equals(roleName)
						|| GUARD.equals(roleName) || WALKER.equals(roleName) || KNIGHT.equals(roleName)) {
					info.setLeveladd(-GODADD1+achive);
				}else if (WOLFTEAM.equals(team)) {
					info.setLeveladd(-WOLADD+achive);
				} else if (IDIOT.equals(roleName) || SILENT.equals(roleName) || CUPID.equals(roleName)) {
					info.setLeveladd(-GODADD1+achive);
				}
			}		
		}
		
		if (OTHERTEAM.equals(team)) {
			info.setOthernum(1);
			if (ONE.equals(winSign)) {
				info.setOtherwin(1);
			}
		} else if (PEOTEAM.equals(team)) {
			info.setPeonum(1);
			if (ONE.equals(winSign)) {
				info.setPeowin(1);
			}
		} else if (WOLFTEAM.equals(team)) {
			info.setWolfnum(1);
			if (ONE.equals(winSign)) {
				info.setWolfwin(1);
			}
		}
		if (ONE.equals(winSign)) {
			info.setRolewin(1);
		}
		return info;
	}
	private int computeAchive(String cjSign,String roleName,CusInfoVo info) {
		int r = 0;
	    if (cjSign == null || roleName == null) {
	        return r;
	    }
	    if (WITCH.equals(roleName) && cjSign.length() > 1) {
	        //String x = cjSign.substring(0,1);
	        String y = cjSign.substring(1,2);
	        if ("4".equals(y)) {
	            info.setAchiveFre(1);
	            r = r + 5;
	        } else if ("3".equals(y)) {
	            r = r - 10;
	        }
	        /*if ("2".equals(x)) {
	            r = r + 10;
	        }*/
	    } else {
	        if (PROPHET.equals(roleName)) {
	            if (StringUtils.checkIsNum(cjSign)) {
	                if (Integer.parseInt(cjSign) >= 2) {
	                    info.setAchiveFre(1);
	                    r = r + 10;
	                }
	            }
	        } else if (HUNTER.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 5;
	            } else if ("2".equals(cjSign)) {
	                r = r - 5;
	            }
	        } else if (GUARD.equals(roleName)) {
	            if (StringUtils.checkIsNum(cjSign)) {
	                if (Integer.parseInt(cjSign) >= 1) {
	                    info.setAchiveFre(1);
	                    if (Integer.parseInt(cjSign) == 1) {
	                        r = r + 5;
	                    } else if (Integer.parseInt(cjSign) == 2) {
	                        r = r + 10;
	                    } else if (Integer.parseInt(cjSign) >= 3) {
	                        r = r + 15;
	                    }
	                }
	            }
	        } else if (WALKER.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 5;
	            } else if ("2".equals(cjSign)) {
	                r = r - 5;
	            }
	        } else if (KNIGHT.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 5;
	            } else if ("2".equals(cjSign) || "3".equals(cjSign)) {
	                r = r - 5;
	            }
	        } else if (WHITEWOLF.equals(roleName) || BEAUTYWOLF.equals(roleName)) {
	            if ("1".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 15;
	            } else if ("2".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 10;
	            } else if ("3".equals(cjSign)) {
	                info.setAchiveFre(1);
	                r = r + 10;
	            }
	        }
	    }
	    return r;
	}
    private String parsePlayerData(WolfKillMainInfo playerData,int order) {
        int tg = playerData.getPeoNum() + playerData.getWolfNum() + playerData.getOtherNum();
        int pw = playerData.getPeoWon();
        int ww = playerData.getWolfWon();
        int ow = playerData.getOtherWon();
        int levelNum = playerData.getLevelNum();
        int mvp = playerData.getMvp();
        String level = playerData.getLevel();
        String name = playerData.getPlayerName();
        String uniqueId = playerData.getUniqueId();
        int achiveNum = 0;
        if (playerData.getAchiveNum() != null) {
        	achiveNum = playerData.getAchiveNum();
        }
       
        BigDecimal totRate = computeRate((pw + ww + ow), tg);
        
        StringBuffer tempBuffer = new StringBuffer();
        tempBuffer.append(name).append(LINE)
        .append(achiveNum).append(LINE)
        .append(totRate).append(LINE)
        .append(mvp).append(LINE)
        .append(uniqueId).append(LINE)
        .append(level).append(LINE)
        .append(levelNum).append(LINE)
        .append(order);
       
        return tempBuffer.toString();
    }
    
    private String parseMainInfo(WolfKillMainInfo playerData) {
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
         
         BigDecimal totRate = computeRate((pw + ww + ow), tg);
         BigDecimal pRate = computeRate(pw,pg);
         BigDecimal wRate = computeRate(ww,wg);
         BigDecimal oRate = computeRate(ow,og);
         
         StringBuffer tempBuffer = new StringBuffer();
         tempBuffer.append(name).append(LINE)
         .append(tg).append(LINE)
         .append(totRate).append(LINE)                
         .append(pg).append(LINE)
         .append(pRate).append(LINE)
         .append(wg).append(LINE)
         .append(wRate).append(LINE)
         .append(og).append(LINE)
         .append(oRate).append(LINE)
         .append(mvp).append(LINE)
         .append(uniqueId).append(LINE)
         .append(level).append(LINE) 
         .append(levelNum).append(LINE) 
         .append(maxLevelNum).append(LINE) 
         .append(achiveNum);
        
         return tempBuffer.toString();
         
    }
    
    private String getTeam(String roleName, String cupidSign) {
    	String result = "";
    	if (ZERO.equals(cupidSign)) {
    		if (PROPHET.equals(roleName) || WITCH.equals(roleName) || HUNTER.equals(roleName) || 
    				IDIOT.equals(roleName) || SILENT.equals(roleName) || GUARD.equals(roleName) ||
    				WALKER.equals(roleName) ||  VILLAGER.equals(roleName) || KNIGHT.equals(roleName)) {
        		result = PEOTEAM;
        	} else if (WOLF.equals(roleName) || WHITEWOLF.equals(roleName) || BEAUTYWOLF.equals(roleName) ||
        			DEVIL.equals(roleName)) {
        		result = WOLFTEAM;
        	}
    	} else {
    		result = OTHERTEAM;
    	}
    	return result;
    }
    
    private String parseRoleName(String sign) {
    	String result = "ADDROLE";
    	if ("2".equals(sign)) {
    		result = VILLAGER;
    	} else if (ONE.equals(sign)) {
    		result = WOLF;
    	} else if ("3".equals(sign)) {
    		result = PROPHET;
    	} else if ("4".equals(sign)) {
    		result = WITCH;
    	} else if ("5".equals(sign)) {
    		result = HUNTER;
    	} else if ("6".equals(sign)) {
    		result = IDIOT;
    	} else if ("7".equals(sign)) {
    		result = GUARD;
    	} else if ("8".equals(sign)) {
    		result = WHITEWOLF;
    	} else if ("9".equals(sign)) {
    		result = WALKER;
    	} else if ("10".equals(sign)) {
    		result = BEAUTYWOLF;
    	} else if ("13".equals(sign)) {
    		result = DEVIL;
    	} else if ("11".equals(sign)) {
    		result = SILENT;
    	} else if ("12".equals(sign)) {
    		result = KNIGHT;
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

	private String parsePerData(WolfKillPerInfo  roleData) {
		String roleName = roleData.getRoleName();
		int tg = roleData.getTotalNum();
		int tw = roleData.getWonNum();
		int achiveFre = 0;
		if (roleData.getAchiveFre() != null) {
			achiveFre = roleData.getAchiveFre();
		}
		BigDecimal rate = computeRate(tw,tg);
		StringBuffer tempBuffer = new StringBuffer();
        tempBuffer.append(roleName).append(LINE)
        .append(tg).append(LINE)
        .append(rate).append(LINE)
        .append(achiveFre);
        
        return tempBuffer.toString();
	}
	
	private void sortDatasByTotal(List<String>  datas) {
		int size = datas.size();
		for (int i = 0; i < size; i++) {						
			for (int j = i+1; j < size; j++) {				
				String per1 = datas.get(i);
				String t1 = per1.split(LINE)[1];
				BigDecimal T1 = new BigDecimal(t1);
				String per2 = datas.get(j);
				String t2 = per2.split(LINE)[1];
				BigDecimal T2 = new BigDecimal(t2);
				if (T1.compareTo(T2) < 0) {
					datas.set(i, per2);
					datas.set(j, per1);
				}
			}
		}
	}	
	@Override
	public void updatePospalMemInfo(List<ImageResponseDataDetail> rdds) {
		List<WolfKillPospalInfo> posInfos = new ArrayList<WolfKillPospalInfo>();
		QueryConditions condition = new QueryConditions();
		try {
			List<WolfKillPospalInfo> pospalDatas = wolfKillDao.queryPospalPoint(condition);
			wolfKillDao.deletePospalMemList(pospalDatas);
			for (int i = 0; i < rdds.size(); i++) {
				ImageResponseDataDetail rdd = rdds.get(i);
				List<MemInfoVo> memInfos = rdd.getResult();
				for (int j = 0; j < memInfos.size(); j++) {
					MemInfoVo minfo = memInfos.get(j);
					if (!"1".equals(minfo.getEnable())) {
						continue;
					}
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
	    		  int levelNum = local.getLevelNum();
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

	/*@Override
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
	}*/
	/**本地数据库*/
	@Override
    public List<PlayerInfoVo> getPlayerBaseInfo() {
        List<PlayerInfoVo> playerInfos = new ArrayList<PlayerInfoVo>();
        QueryConditions condition = new QueryConditions();
        List<MutuMemInfo> memInfos = wolfKillDao.queryMemInfoByLocal(condition);
        List<WolfKillMainInfo> mainDatas = wolfKillDao.queryMainDataByCondition(condition); 
        for (int i = 0; i < memInfos.size(); i++) {
            MutuMemInfo mem = memInfos.get(i);
            PlayerInfoVo playerInfo = new PlayerInfoVo();
            int uniqueId = mem.getMemId();
            int point = 0;
            int pointMax = 0;
            String telephone = mem.getMemPhone();
            String name = mem.getMemName();
            
            for (int j = 0; j < mainDatas.size(); j++) {
                WolfKillMainInfo mainData = mainDatas.get(j);
                if (String.valueOf(uniqueId).equals(mainData.getUniqueId())) {
                    if (mainData.getLevelNum() != null) {
                        point = mainData.getLevelNum();
                    }
                    if (mainData.getLevelMaxNum() != null) {
                        pointMax = mainData.getLevelMaxNum();
                    }
                    break;
                }                   
            }
            playerInfo.setNum(new Long(uniqueId));

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
		condition.setConditionEqual(UNIQUE_ID, playerUid);
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
