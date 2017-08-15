package com.mutuChat.service;

import java.util.List;

import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillGoodsInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMatchHis;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
import com.mutuChat.wolfkill.vo.RoleOrderVo;

public interface IWolfChatService {
	/**
	 * 查询用户微信信息
	 * @param openid
	 * @return
	 */
	public WolfKillChatUserInfo queryUserOpenInfoByOpenid(String openid);
	public WolfKillChatUserInfo queryUserOpenInfoByPlayerId(String playerId);
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
	
	
	public List<WolfKillMainInfo> getPlayerMainById(String playerId,String season);
		
	public List<WolfKillPerInfo> getPerInfoById(String playerId,String season);	
	
	public List<String> getRoleNameById(String playerId);
		
	public int getLevelOrderById(String playerId, String season);
	
	public int gerMaxOrderById(String playerId);		
	
	public List<WolfKillMatchHis> getHisMatchByPlayerId(String playerId);
	
	public WolfKillMatchHis getHisMatchByMatchId(int matchId);
	
	public List<RoleOrderVo> getAllPlayerAchive(String roleName,String season);
	
	public List<RoleOrderVo> getAllRoleTWNum();
	
	   
    public List<WolfKillGoodsInfo> getWolfGoodsInfo();
    
    String saveNameAndTag(String playerName,String playerTag,String playerId);
	
}
