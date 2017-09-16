package com.mutuChat.wolfkill.dao;

import java.util.List;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;

public interface IWolfChatDao {
	public WolfKillChatUserInfo findChatUserInfo(QueryConditions condition);
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo);
	public WolfKillPospalInfo findPlayerInfo(QueryConditions condition);
	
	public List<WolfKillPregameInfo> findPregameInfo(QueryConditions condition);
	
	public void savePregameInfo(WolfKillPregameInfo preInfo);
}
