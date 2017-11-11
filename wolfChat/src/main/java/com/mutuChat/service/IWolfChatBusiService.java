package com.mutuChat.service;

import javax.servlet.http.HttpSession;

import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;

public interface IWolfChatBusiService {
	/**
	 * 
	 * @param code
	 *                微信code
	 * @param session
	 *                用户Session暂存openid
	 * @param backMsg
	 *                 微信返回错误信息
	 * @return
	 */
	WolfKillChatUserInfo  getAndSaveChatPlayerInfo(String code,HttpSession session,String backMsg);
	
	String bindUserInfo(String playerId, String phone,String openid);
	
	WolfKillChatUserInfo  getOutAndSaveChatPlayerInfo(String code,HttpSession session,String backMsg,String shopName);
}
