package com.mutuChat.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillGoodsInfo;
import com.mutuChat.wolfkill.model.WolfKillMatchHis;
import com.mutuChat.wolfkill.vo.ChatPlayerInfoVo;
import com.mutuChat.wolfkill.vo.HisMatchVo;
import com.mutuChat.wolfkill.vo.RoleInfoVo;
import com.mutuChat.wolfkill.vo.RoleOrderVo;

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
	
	ChatPlayerInfoVo getChatPlayerInfo(String playerId,String mSeason);	
	
	WolfKillMatchHis getHisMatchInfo(int matchId);
	
	List<RoleOrderVo> getRoleOrderInfo();		
	
}
