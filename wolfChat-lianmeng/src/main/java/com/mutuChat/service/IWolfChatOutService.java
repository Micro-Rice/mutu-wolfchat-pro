package com.mutuChat.service;


import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;

public interface IWolfChatOutService {
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
	public void saveUserOpenInfo(ChatUserInfoVo chatUserInfoVo,String shopName);
	
	 public WolfKillPregameInfo getPregameInfoByOpenId(String openId);    
		
	 public String saveRoomAndSeatInfo(WolfKillPregameInfo preInfo);
}
