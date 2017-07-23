package com.mutuChat.service.impl;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mutuChat.service.IWolfChatBusiService;
import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
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
	public ChatPlayerInfoVo getChatPlayerInfo(String playerId) {
		// TODO 自动生成的方法存根
		return null;
	}


}
