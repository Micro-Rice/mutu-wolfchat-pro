package com.mutuChat.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
@Transactional
@Service("wolfChatService")
public class WolfChatServiceImpl implements IWolfChatService{
	private final static String OPENID = "openId";
	private final static String NUMBER = "number";
	private static Logger logger = Logger.getLogger(WolfChatServiceImpl.class);
	
	@Resource
    private IWolfChatDao wolfChatDao;
	
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
			/**
             * 需要生成不重复的短ID与openId对应
             */
            /*int playerId = ComMethod.randomByTime(1000000000);
            chatUserInfo.setPlayerId(String.valueOf(playerId));*/
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
    public WolfKillPregameInfo getPregameInfoByOpenId(String openId) {
        WolfKillPregameInfo preInfo = null;
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual("openId", openId);
        List<WolfKillPregameInfo> preInfos = wolfChatDao.findPregameInfo(condition);
        if (!preInfos.isEmpty()) {
            preInfo = preInfos.get(0);            
        }
        return preInfo;
    }
	
	@Override
    public String saveRoomAndSeatInfo(WolfKillPregameInfo preInfo) {
        String message = "";
        if (preInfo != null) {
            String room = preInfo.getRoomId();
            Integer seat = preInfo.getSeatId();
            if (room != null && seat != null) {
                QueryConditions condition = new QueryConditions();
                condition.setConditionEqual("roomId", room);
                condition.setConditionEqual("seatId", seat);
                List<WolfKillPregameInfo> preInfos = wolfChatDao.findPregameInfo(condition);
                if (!preInfos.isEmpty()) {
                    WolfKillPregameInfo preInfoEd = preInfos.get(0);
                    if (preInfoEd.getOpenId() != null && preInfoEd.getOpenId().equals(preInfo.getOpenId())) {
                        message = "success";
                    } else {
                        message = "error1";
                    }
                } else {
                    wolfChatDao.savePregameInfo(preInfo);
                    message = "success";
                }
            } else {
                logger.error("room is " + room +"And seat is" + seat);
            }
        } else {
            logger.error("WolfKillPregameInfo is null");
        }
        return message;
    }
}
