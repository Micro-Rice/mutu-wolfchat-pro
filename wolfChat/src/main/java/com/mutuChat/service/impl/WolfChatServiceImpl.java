package com.mutuChat.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.dao.IWolfKillDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
@Transactional
@Service("wolfChatService")
public class WolfChatServiceImpl implements IWolfChatService{
	private final static String OPENID = "openId";
	private final static String PLAYERID = "playerId";
	private final static String NUMBER = "number";
	private final static String UNIQUEID = "uniqueId";
	private final static String SEASON = "season";
	private final static String ROLENAME = "roleName";
	private static Logger logger = Logger.getLogger(WolfChatServiceImpl.class);
	
	@Resource
    private IWolfChatDao wolfChatDao;
	
	@Resource
    private IWolfKillDao wolfKillDao;
	
	@Override
	public WolfKillChatUserInfo queryUserOpenInfoByOpenid(String openid) {
		
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(OPENID, openid);
        WolfKillChatUserInfo chatUser = wolfChatDao.findChatUserInfo(condition);
		return chatUser;
	}

	@Override
	public void saveUserOpenInfo(ChatUserInfoVo chatUserInfoVo) {
		String openid = chatUserInfoVo.getOpenid();
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(OPENID, openid);
		WolfKillChatUserInfo chatUserInfo = wolfChatDao.findChatUserInfo(condition);
		if (chatUserInfo == null) {
			chatUserInfo = new WolfKillChatUserInfo();
			chatUserInfo.setOpenId(openid);
		}
		/**
		 * 这里需要更新图片信息
		 */
		chatUserInfo.setOpenImg(chatUserInfoVo.getHeadimgurl());
		wolfChatDao.saveChatUserInfo(chatUserInfo);
		
	}

	@Override
	public WolfKillPospalInfo queryPlayerInfoById(String playerId) {		
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(NUMBER, playerId);
		return wolfChatDao.findPlayerInfo(condition);
	}

	@Override
	public void savePlayerInfo(WolfKillChatUserInfo playerInfo) {
		wolfChatDao.saveChatUserInfo(playerInfo);		
	}

    @Override
    public WolfKillChatUserInfo queryUserOpenInfoByPlayerId(String playerId) {
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(PLAYERID, playerId);
        WolfKillChatUserInfo chatUser = wolfChatDao.findChatUserInfo(condition);
        return chatUser;
    }


    @Override
    public int getLevelOrderById(String playerId, String season) {
        int r = Integer.MAX_VALUE;
        QueryConditions condition = new QueryConditions();
        String orderBy = "levelNum desc";
        condition.setOrderBy(orderBy);
        condition.setConditionEqual(SEASON, season);
        List<WolfKillMainInfo> mainInfos = wolfKillDao.queryMainDataByCondition(condition);
        for (int i = 0; i < mainInfos.size(); i++) {
            WolfKillMainInfo mInfo = mainInfos.get(i);
            if (playerId != null && playerId.equals(mInfo.getUniqueId())) {
                r = i + 1;
                break;
            }
        }
        return r;    
    }

    @Override
    public List<WolfKillMainInfo> getPlayerMainById(String playerId,String season) {
        QueryConditions condition = new QueryConditions();
        if (season != null) {
            condition.setConditionEqual(SEASON, season);
        }
        condition.setConditionEqual(UNIQUEID, playerId);
        return wolfKillDao.queryMainDataByCondition(condition);
    }

    @Override
    public List<WolfKillPerInfo> getPerInfoById(String playerId, String season) {
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(UNIQUEID, playerId);
        if (season != null) {
            condition.setConditionEqual(SEASON, season);
        }        
        return wolfKillDao.queryPersonDataCondition(condition);
    }

    @Override
    public List<String> getRoleNameById(String playerId) {
        return wolfChatDao.findRoleNameById(playerId);
    }

    @Override
    public int gerMaxOrderById(String playerId) {
        int r = Integer.MAX_VALUE;
        List<String> seasons = wolfChatDao.findAllSeasons();
        for (int i = 0; i < seasons.size(); i++) {
            String sea = seasons.get(i);
            int rtemp = this.getLevelOrderById(playerId,sea);
            if (r > rtemp) {
                r = rtemp;
            }
        }
        return r;
    }

    @Override
    public List<WolfKillPerInfo> getPerInfoByRole(String roleName, String season) {
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(ROLENAME, roleName);
        if (season != null) {
            condition.setConditionEqual(SEASON, season);
        } 
        return wolfKillDao.queryPersonDataCondition(condition);
    }

    @Override
    public List<WolfKillPerInfo> getPerInfoByRoleAndId(String playerId, String roleName, String season) {
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(ROLENAME, roleName);
        condition.setConditionEqual(PLAYERID, playerId);
        if (season != null) {
            condition.setConditionEqual(SEASON, season);
        } 
        return wolfKillDao.queryPersonDataCondition(condition);
    }
}
