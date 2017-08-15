package com.mutuChat.wolfkill.dao;

import java.util.List;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillGoodsInfo;
import com.mutuChat.wolfkill.model.WolfKillMatchHis;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.vo.RoleOrderVo;

public interface IWolfChatDao {
	public WolfKillChatUserInfo findChatUserInfo(QueryConditions condition);
	
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo);
	
	public WolfKillPospalInfo findPlayerInfo(QueryConditions condition);
		
	public List<String> findRoleNameById(String playerId);
	
	public List<String> findAllSeasons();	
	
	public List<WolfKillMatchHis> findHisMatchByPlayerId(String playerId);
	
	public WolfKillMatchHis findHisMatchByMatchId(int matchId);
	
	public List<RoleOrderVo> findAllplayerIDAchive(String roleName,String season);
	
	public List<RoleOrderVo> findAllRoleTWNum();
	
	public List<WolfKillGoodsInfo> findAllGoodsInfo(QueryConditions condition);
	
	public void updateMainPlayerNameById(String playerName,String playerId);
	
	public void updateRolePlayerNameById(String playerName,String playerId);
	
	public void updatePlayerTagById(String playerTag,String playerId);
	
}
