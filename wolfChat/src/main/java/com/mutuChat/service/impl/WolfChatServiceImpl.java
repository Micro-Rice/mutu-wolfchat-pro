package com.mutuChat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
import com.mutuChat.wolfkill.vo.PlayerInfoVo;
@Transactional
@Service("wolfChatService")
public class WolfChatServiceImpl implements IWolfChatService{
	private final static String OPENID = "openId";
	private final static String NUMBER = "number";
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
	private static Logger logger = Logger.getLogger(WolfChatServiceImpl.class);
	
	@Resource
    private IWolfChatDao wolfChatDao;
	
	@Resource
    private IWolfKillDao wolfkillDao;
	
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
		String openName = chatUserInfoVo.getNickname();
		QueryConditions condition = new QueryConditions();
        condition.setConditionEqual(OPENID, openid);
		WolfKillChatUserInfo chatUserInfo = wolfChatDao.findChatUserInfo(condition);
		if (chatUserInfo == null) {
			chatUserInfo = new WolfKillChatUserInfo();
			chatUserInfo.setOpenId(openid);
			chatUserInfo.setOpenName(openName);
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
            String openId = preInfo.getOpenId();
            if (openId != null && room != null && seat != null) {
            	QueryConditions conditionF = new QueryConditions();
                conditionF.setConditionEqual("openId", openId);
                List<WolfKillPregameInfo> preInfoFs = wolfChatDao.findPregameInfo(conditionF);
                if (!preInfoFs.isEmpty()) {
                	wolfChatDao.deletePregameInfo(preInfoFs);
                }
                QueryConditions condition = new QueryConditions();
                condition.setConditionEqual("roomId", room);
                condition.setConditionEqual("seatId", seat);
                List<WolfKillPregameInfo> preInfos = wolfChatDao.findPregameInfo(condition);
                if (!preInfos.isEmpty()) {
                    WolfKillPregameInfo preInfoEd = preInfos.get(0);
                    preInfoEd.setOpenId(preInfo.getOpenId());
                	wolfChatDao.savePregameInfo(preInfoEd);
                	message = "success";
                } else {
                    wolfChatDao.savePregameInfo(preInfo);
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

	@Override
	public List<PlayerInfoVo> getChatPlayerInfo(String room) {
		List<PlayerInfoVo> rList = new ArrayList<PlayerInfoVo>();
		if (room != null) {
			QueryConditions condition = new QueryConditions();
			room = roomMap.get(room);
            condition.setConditionEqual("roomId", room);
            List<WolfKillPregameInfo> preInfos = wolfChatDao.findPregameInfo(condition);
            for (int i = 0; i < preInfos.size(); i++) {
            	WolfKillPregameInfo preInfo = preInfos.get(i);
            	String openId = preInfo.getOpenId();
            	if (openId != null) {
            		QueryConditions condition1 = new QueryConditions();
                	condition1.setConditionEqual("openId",openId);
                	WolfKillChatUserInfo chatUser = wolfChatDao.findChatUserInfo(condition1);
                	if (chatUser != null) {
                		String playerId = chatUser.getPlayerId();
                		String openName = chatUser.getOpenName();
                		QueryConditions condition2 = new QueryConditions();
                		condition2.setConditionEqual("uniqueId",playerId);
                		List<WolfKillMainInfo> mainInfos = wolfkillDao.queryMainDataByCondition(condition2);
                		int point = 0;
            			int pointMax = 0;
                		if (!mainInfos.isEmpty()) {
                			WolfKillMainInfo mainInfo = mainInfos.get(0);            			
                			if (mainInfo.getLevelNum() != null) {
                				point = mainInfo.getLevelNum();
                			}
                			if (mainInfo.getLevelMaxNum() != null) {
                				pointMax = mainInfo.getLevelMaxNum();
                			}
                			openName = mainInfo.getPlayerName();
                		}
                		PlayerInfoVo playerInfo = new PlayerInfoVo();
                		playerInfo.setName(openName);
                		if (playerId != null) {
                			playerInfo.setNum(Long.parseLong(playerId));
                		}            		
                		playerInfo.setPoint(point);
                		playerInfo.setPointMax(pointMax);
                		playerInfo.setSeat(preInfo.getSeatId());
                		playerInfo.setRoom(encodeRoomMap.get(preInfo.getRoomId()));
                		playerInfo.setTelephone(Long.parseLong(chatUser.getPlayerPhone()));
                		rList.add(playerInfo);
                	}
            	}            	
            }
		}
		return rList;
	}

	@Override
	public String updatePreChatPlayer(String pdata) {		
		List<WolfKillPregameInfo> preInfos = new ArrayList<WolfKillPregameInfo>();
		if (pdata.startsWith("DEL")) {
			String room = pdata.substring(3);
			QueryConditions condition1 = new QueryConditions();
			room = roomMap.get(room);
	        condition1.setConditionEqual("roomId", room);
	        List<WolfKillPregameInfo> depres = wolfChatDao.findPregameInfo(condition1);
	        wolfChatDao.deletePregameInfo(depres);
		} else {
			List<PlayerInfoVo> pdatas = JsonUtils.jsonToArrayList(pdata, PlayerInfoVo.class);
			String room = null;
			for (int i = 0; i < pdatas.size(); i++) {
				WolfKillPregameInfo preInfo = new WolfKillPregameInfo();
				PlayerInfoVo info = pdatas.get(i);
				preInfo.setSeatId(info.getSeat());
				preInfo.setRoomId(roomMap.get(info.getRoom()));
				/**
				 * 最后一个数据决定ROOM
				 */
				room = info.getRoom();
				Long playerId = info.getNum();
				QueryConditions condition = new QueryConditions();
				condition.setConditionEqual("playerId", String.valueOf(playerId));
				WolfKillChatUserInfo chatUser = wolfChatDao.findChatUserInfo(condition);
				if (chatUser != null) {
					String openId = chatUser.getOpenId();
					preInfo.setOpenId(openId);
				}
				preInfos.add(preInfo);
			}
			QueryConditions condition1 = new QueryConditions();
			room = roomMap.get(room);
	        condition1.setConditionEqual("roomId", room);
	        List<WolfKillPregameInfo> depres = wolfChatDao.findPregameInfo(condition1);
	        wolfChatDao.deletePregameInfo(depres);
	        wolfChatDao.savePregameInfos(preInfos);
		}		               
		return "success";
	}
}
