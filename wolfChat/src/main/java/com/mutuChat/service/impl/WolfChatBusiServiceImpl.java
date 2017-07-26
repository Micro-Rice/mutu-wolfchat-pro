package com.mutuChat.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mutuChat.service.IWolfChatBusiService;
import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.utils.StringUtils;
import com.mutuChat.wolfkill.vo.ChatErrorVo;
import com.mutuChat.wolfkill.vo.ChatPlayerInfoVo;
import com.mutuChat.wolfkill.vo.ChatTokenVo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
import com.mutuChat.wolfkill.vo.HisMatchVo;
import com.mutuChat.wolfkill.vo.RoleInfoVo;
import com.pospal.utils.tools.JsonConvertor;
@Service("wolfChatBusiService")
public class WolfChatBusiServiceImpl implements IWolfChatBusiService{
	
	@Autowired
    private IWolfChatService wolfChatService;
	
	public final static String APPID = "wx41b1efe6d6d3127f";
	public final static String SECRET = "4dff1cb1b0234c09a219afb0186f2965";
	private static Logger logger = Logger.getLogger(WolfChatBusiServiceImpl.class);
	
	@Override
	public WolfKillChatUserInfo getAndSaveChatPlayerInfo(String code, HttpSession session,String backmsg) {
		String openid = null;
		WolfKillChatUserInfo chatUser = null;
		if (session != null && session.getAttribute("openid") != null) {
			openid = session.getAttribute("openid").toString();
		}		
		if (openid != null) {
			logger.info("the session openid is"+openid);			
		} else {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			String param = "appid="+APPID+"&secret="+SECRET+"&code="+code+"&grant_type=authorization_code";
			String msg = ComMethod.sendGet(url, param);
			if (msg.indexOf("errcode") > -1) {
				ChatErrorVo errorMsg = JsonConvertor.fromJson(msg,ChatErrorVo.class);
				backmsg = errorMsg.getErrmsg();
				logger.info("the error Msg is"+backmsg);
			} else {
				ChatTokenVo tokenContent = JsonConvertor.fromJson(msg,ChatTokenVo.class);
				String accessToken = tokenContent.getAccess_token();
				openid = tokenContent.getOpenid();
				session.setAttribute("openid", openid);
				String infoUrl = "https://api.weixin.qq.com/sns/userinfo";
				String infoParam = "access_token="+accessToken+"&openid="+openid+"&lang=zh_CN";
				String infoMsg = ComMethod.sendGet(infoUrl,infoParam);
				if (infoMsg.indexOf("errcode") > -1) {
					ChatErrorVo errorMsg = JsonConvertor.fromJson(infoMsg,ChatErrorVo.class);
					backmsg = errorMsg.getErrmsg();
					logger.info("the wxBack openid is"+openid+ "And the error Msg is" +backmsg);
				} else {
					logger.info("the wxBack openid is"+openid);	
					logger.info("the backMsg  is"+infoMsg);
					ChatUserInfoVo userInfoContent = JsonConvertor.fromJson(infoMsg,ChatUserInfoVo.class);						
					wolfChatService.saveUserOpenInfo(userInfoContent);		
				}
			}			
		}
		if (openid != null) {
			chatUser = wolfChatService.queryUserOpenInfoByOpenid(openid);
		}
		return chatUser;
	}

	@Override
	public String bindUserInfo(String playerId, String phone, String openid) {
		String errorMsg = "";
		WolfKillPospalInfo playerInfo = wolfChatService.queryPlayerInfoById(playerId);
		if (playerInfo == null) {
			errorMsg = "error2";
		} else {
			if (phone != null && phone.equals(playerInfo.getPhone())) {
				if (openid == null) {
					errorMsg = "error3";
				} else {
					WolfKillChatUserInfo chatUser = wolfChatService.queryUserOpenInfoByOpenid(openid);
					if (chatUser != null) {
						chatUser.setPlayerId(playerId);
						chatUser.setPlayerPhone(phone);
						wolfChatService.savePlayerInfo(chatUser);
					} else {
						errorMsg = "error3";
					}
				}
			} else {
				errorMsg = "error1";
			}
		}
		return errorMsg;
	}

	@Override
	public ChatPlayerInfoVo getChatPlayerInfo(String playerId,String season) {
	    ChatPlayerInfoVo rInfo = new ChatPlayerInfoVo();	    
	    if (playerId != null) {	        
	        String imgUrl = null;
	        String playerTag = null;
	        WolfKillChatUserInfo chatUser = wolfChatService.queryUserOpenInfoByPlayerId(playerId);
	        if (chatUser != null) {
	            imgUrl = chatUser.getOpenImg();
	            playerTag = chatUser.getPlayerTag();
	        }
	        /**
	         * season不为null,显示相应赛季数据;为null,显示所有统计数据
	         */	        
	        int levelNum = 0;
	        int pOrder = 0;
	        List<WolfKillMainInfo> playerMains = wolfChatService.getPlayerMainById(playerId,season);
	        List<WolfKillPerInfo> perInfos = wolfChatService.getPerInfoById(playerId,season);
	        /**
             * 获得当季数据，获取实时数据
             */            
            WolfKillMainInfo playerTemp = null;
            if ("0".equals(season)) {
                if (!playerMains.isEmpty()) {
                    playerTemp = playerMains.get(0);
                }                
            } else {
                List<WolfKillMainInfo> playerTemps = wolfChatService.getPlayerMainById(playerId, "0");
                if (!playerTemps.isEmpty()) {
                    playerTemp = playerTemps.get(0);
                }
            }            
	        if (season != null) {	            
	            if (!playerMains.isEmpty()) {
	                levelNum = playerMains.get(0).getLevelNum();
	            }	           
	            pOrder = wolfChatService.getLevelOrderById(playerId, season);
	        } else {
	            if (playerTemp != null) {
	                levelNum = playerTemp.getLevelNum();
	            }	            
	            pOrder = wolfChatService.getLevelOrderById(playerId, "0");
	        }
	        /**
	         * 获得段位,历史最高积分,历史最高排名
	         */	        
	        String level = "青铜战狼";
	        String playerName = null;
	        int levelMax = 0;
	        if (playerTemp != null) {
	            level = playerTemp.getLevel();
	            levelMax = playerTemp.getLevelMaxNum();
	            playerName = playerTemp.getPlayerName();
	        }
	        int maxOrder = wolfChatService.gerMaxOrderById(playerId);
	        /**
	         * 计算基本信息
	         */
	        comBasicInfo(rInfo,playerMains);
	        /**
	         * 计算角色信息
	         * 先获得所有角色名
	         */
	        List<String> roleNames = wolfChatService.getRoleNameById(playerId);
	        comRoleInfo(rInfo,perInfos,roleNames);
	        /**
	         * 获得角色的排名
	         */
	        comRoleTaskOrder(rInfo,playerId,season);
	        /**
	         * 获得历史比赛
	         */
	        comHisMatch(rInfo,playerId);
	        rInfo.setMaxOrder(maxOrder);
	        rInfo.setpLevel(level);
	        rInfo.setpLevelMnum(levelMax);
	        rInfo.setpLevelNum(levelNum);
	        rInfo.setpOrder(pOrder);
	        rInfo.setpName(playerName);
	        rInfo.setpTag(playerTag);
	        rInfo.setImgUrl(imgUrl);
	    }
	    
		return rInfo;
	}
	private void comHisMatch(ChatPlayerInfoVo rInfo,String playerId) {
	    
	}
	private void comBasicInfo(ChatPlayerInfoVo rInfo, List<WolfKillMainInfo>playerMains) {
	    int matchNum = 0;
	    int winNum = 0;
	    int mvp = 0;
	    int achiveNum = 0;
	    int pNum = 0;
	    int wNum = 0;
	    int oNum = 0;
	    int pWin = 0;
	    int wWin = 0;
	    int oWin = 0;
	    for (int i = 0; i < playerMains.size(); i++) {
	        WolfKillMainInfo pm = playerMains.get(i);
	        matchNum = matchNum + pm.getPeoNum() + pm.getWolfNum() + pm.getOtherNum();
	        mvp = mvp + pm.getMvp();
	        achiveNum = achiveNum + pm.getAchiveNum();
	        winNum = winNum + pm.getPeoWon() + pm.getWolfWon() + pm.getOtherWon();
	        pNum = pNum + pm.getPeoNum();
	        wNum = wNum + pm.getWolfNum();
	        oNum = oNum + pm.getOtherNum();
	        pWin = pWin + pm.getPeoWon();
	        wWin = wWin + pm.getWolfWon();
	        oWin = oWin + pm.getOtherWon();
	    }
	    BigDecimal wrate = new BigDecimal("0");
	    if (matchNum > 0) {
	        wrate = new BigDecimal(winNum).divide(new BigDecimal(matchNum), 4, BigDecimal.ROUND_HALF_UP);
	    }
	    BigDecimal peoRate = new BigDecimal("0");
        if (pNum > 0) {
            peoRate = new BigDecimal(pWin).divide(new BigDecimal(pNum), 4, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal wolfRate = new BigDecimal("0");
        if (wNum > 0) {
            wolfRate = new BigDecimal(wWin).divide(new BigDecimal(wNum), 4, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal othRate = new BigDecimal("0");
        if (oNum > 0) {
            othRate = new BigDecimal(oWin).divide(new BigDecimal(oNum), 4, BigDecimal.ROUND_HALF_UP);
        }
        rInfo.setpAchiveNum(achiveNum);
        rInfo.setpMatchNum(matchNum);
        rInfo.setpOthNum(oNum);
        rInfo.setpOthRate(othRate);
        rInfo.setpPeoNum(pNum);
        rInfo.setpPeoRate(peoRate);
        rInfo.setpWolNum(wNum);
        rInfo.setpWolRate(wolfRate);
        rInfo.setpWrate(wrate);
	}
	
	private void comRoleInfo(ChatPlayerInfoVo rInfo, List<WolfKillPerInfo>perInfos,
	        List<String> roleNames) {
	    List<RoleInfoVo> rvs = new ArrayList<RoleInfoVo>();
	    if (roleNames != null) {
	        for (int i = 0; i < roleNames.size(); i++) {
	            String rn = roleNames.get(i);	            
	            RoleInfoVo rv = new RoleInfoVo();
	            int rNum = 0;
	            int rWin = 0;
	            int rAchive = 0;
	            boolean flag = false;
	            int rTask = 0;
	            for (int j = 0; j < perInfos.size(); j++) {
	                WolfKillPerInfo pi = perInfos.get(j);
	                if(rn.equals(pi.getRoleName())) {
	                    flag = true;
	                    rNum = rNum + pi.getTotalNum();
	                    rWin = rWin + pi.getWonNum();
	                    rTask = rTask + pi.getRoleTask();
	                    if (pi.getAchiveFre() != null){
	                        rAchive = rAchive + pi.getAchiveFre();
	                    }	                    
	                }
	            }
	            BigDecimal rRate = new BigDecimal("0");
	            if (rNum > 0) {
	                rRate = new BigDecimal(rWin).divide(new BigDecimal(rNum), 4, BigDecimal.ROUND_HALF_UP);
	            }
	            /**
	             * 玩家使用过这个角色，才有信息
	             */
	            if (flag) {
	                rv.setrName(rn);
	                rv.setrAchiveFre(rAchive);
	                rv.setrMatchNum(rNum);
	                rv.setrWrate(rRate);
	                rv.setRtaskNum(rTask);
	                rvs.add(rv);
	            }
	        }
	    }
	    rInfo.setRoleInfos(rvs);
	}
	
	private void comRoleTaskOrder(ChatPlayerInfoVo rInfo,String playerId,String season) {
	    List<RoleInfoVo> rvs = rInfo.getRoleInfos();
	    for (int i = 0; i < rvs.size(); i++) {
	        int order = -1;
	        RoleInfoVo rv = rvs.get(i);
	        String roleName = rv.getrName();
	        List<String> playerTasks = new ArrayList<String>();
	        List<WolfKillPerInfo> perInfos = wolfChatService.getPerInfoByRole(roleName, season);
	        List<String> uniqueIds = new ArrayList<String>();
	        for (int j = 0; j < perInfos.size(); j++) {
	            String uniqueId = perInfos.get(j).getUniqueId();
	            if (!uniqueIds.contains(uniqueId)) {
	                uniqueIds.add(uniqueId);
	            }
	        }
	        for (int j = 0; j < uniqueIds.size(); j++) {
	            int ptask = 0;
	            String uId = uniqueIds.get(j);
	            List<WolfKillPerInfo> pIs = wolfChatService.getPerInfoByRoleAndId(uId, roleName, season);
	            for (int k = 0; k < pIs.size(); k++) {
	                ptask = ptask + pIs.get(k).getRoleTask();
	            }
	            String playerTask = uId + "-" + ptask;
	            playerTasks.add(playerTask);
	        }	        
	        sortTaskList(playerTasks);
	        for (int j = 0; j < playerTasks.size(); j++) {
	            String pt = playerTasks.get(j);
	            String pid = pt.split("-")[0];
	            if (playerId != null && playerId.equals(pid)) {
	                order = j + 1;
	                break;
	            }
	        }
	        rv.setrOrder(order);
	    }
	}
	
	private void sortTaskList(List<String> playerTasks) {
	    if (playerTasks != null) {
	        int size = playerTasks.size();
	        for (int i = 0; i < size; i++) {
	            for (int j = i+1; j < size; j++) {
	                String pt1 = playerTasks.get(i);
	                String pt2 = playerTasks.get(j);
	                int ta1 = Integer.parseInt(pt1.split("-")[1]);
	                int ta2 = Integer.parseInt(pt2.split("-")[1]);
	                if (ta1 < ta2) {
	                    playerTasks.set(i, pt2);
	                    playerTasks.set(j, pt1);
	                }
	            }	            
	        }
	    }
	}
}
