package com.mutuChat.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutuChat.service.IWolfChatOutService;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatOutDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
@Transactional("outManager")
@Service("wolfChatOutService")
public class WolfChatOutServiceImpl implements IWolfChatOutService{
	private final static String OPENID = "openId";
	public static final Map<String, String> roomMap = new HashMap<String, String>();
	public static final Map<String, String> encodeRoomMap = new HashMap<String, String>();
	static {
		roomMap.put("1", "禁忌森林");
		roomMap.put("2", "静默狼啸");
		roomMap.put("3", "穆图之影");
		roomMap.put("4", "至高民意");
	}
	static {
		encodeRoomMap.put("禁忌森林", "1");
		encodeRoomMap.put("静默狼啸", "2");
		encodeRoomMap.put("穆图之影", "3");
		encodeRoomMap.put("至高民意", "4");
	}
	private static Logger logger = Logger.getLogger(WolfChatOutServiceImpl.class);
	
	@Resource
    private IWolfChatOutDao wolfChatOutDao;
	
	
	@Override
	public WolfKillChatUserInfo queryUserOpenInfoByOpenid(String openid) {
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(OPENID, openid);
        WolfKillChatUserInfo chatUser = wolfChatOutDao.findChatUserInfo(condition);
		return chatUser;
	}

	@Override
	public void saveUserOpenInfo(ChatUserInfoVo chatUserInfoVo,String shopName) {
		String openid = chatUserInfoVo.getOpenid();
		String openName = chatUserInfoVo.getNickname();
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(OPENID, openid);
		WolfKillChatUserInfo chatUserInfo = wolfChatOutDao.findChatUserInfo(condition);
		if (chatUserInfo == null) {
			chatUserInfo = new WolfKillChatUserInfo();
			chatUserInfo.setOpenId(openid);
			chatUserInfo.setOpenName(openName);
			chatUserInfo.setShopName(shopName);
			/**
             * 需要生成不重复的短ID与openId对应
             */
            int playerId = ComMethod.randomByTime(10000);
            String strPlayerId = shopName + playerId;
            chatUserInfo.setPlayerId(strPlayerId);
		}
		/**
		 * 这里需要更新图片信息
		 */
		chatUserInfo.setOpenImg(chatUserInfoVo.getHeadimgurl());
		wolfChatOutDao.saveChatUserInfo(chatUserInfo);
		
	}

	@Override
	public WolfKillPregameInfo getPregameInfoByOpenId(String openId) {
		WolfKillPregameInfo preInfo = null;
        QueryConditions condition = new QueryConditions();
        condition.setConditionEqual("openId", openId);
        List<WolfKillPregameInfo> preInfos = wolfChatOutDao.findPregameInfo(condition);
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
            String openId = preInfo.getOpenId();
            if (openId != null && room != null && seat != null) {
            	QueryConditions conditionF = new QueryConditions();
                conditionF.setConditionEqual("openId", openId);
                List<WolfKillPregameInfo> preInfoFs = wolfChatOutDao.findPregameInfo(conditionF);
                if (!preInfoFs.isEmpty()) {
                	wolfChatOutDao.deletePregameInfo(preInfoFs);
                }
                QueryConditions condition = new QueryConditions();
                condition.setConditionEqual("roomId", room);
                condition.setConditionEqual("seatId", seat);
                List<WolfKillPregameInfo> preInfos = wolfChatOutDao.findPregameInfo(condition);
                if (!preInfos.isEmpty()) {
                    WolfKillPregameInfo preInfoEd = preInfos.get(0);
                    preInfoEd.setOpenId(preInfo.getOpenId());
                    wolfChatOutDao.savePregameInfo(preInfoEd);
                	message = "success";
                } else {
                	preInfo.setId(null);
                	wolfChatOutDao.savePregameInfo(preInfo);
                    message = "success";
                }
            } else {
            	 logger.error("openId is " +openId+" room is " + room +" And seat is " + seat);
            }
        } else {
            logger.error("WolfKillPregameInfo is null");
        }
        return message;
	}
}
