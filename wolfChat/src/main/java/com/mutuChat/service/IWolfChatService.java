package com.mutuChat.service;

import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;

public interface IWolfChatService {
	/**
	 * 查询用户微信信息
	 * @param openid
	 * @return
	 */
	public WolfKillChatUserInfo queryUserOpenInfoByOpenid(String openid);
	/**
	 * 保存微信用户信息
	 * @param chatUserInfoVo
	 */
	public void saveUserOpenInfo(ChatUserInfoVo chatUserInfoVo);
	/**
	 * 根据ID查询云端会员信息
	 * @param playerId
	 * @return
	 */
	public WolfKillPospalInfo queryPlayerInfoById(String playerId);
	/**
	 * 保存用户的会员卡号，PHONE信息
	 * @param playerInfo
	 */
	public void savePlayerInfo(WolfKillChatUserInfo playerInfo);
	
	 public WolfKillPregameInfo getPregameInfoByOpenId(String openId);    
		
	 public String saveRoomAndSeatInfo(WolfKillPregameInfo preInfo);
}
