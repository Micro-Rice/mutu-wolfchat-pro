package com.mutuChat.wolfkill.dao;

import java.util.List;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;

public interface IWolfChatOutDao {
	public WolfKillChatUserInfo findChatUserInfo(QueryConditions condition);
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo);
	public List<WolfKillPregameInfo> findPregameInfo(QueryConditions condition);
	public void deletePregameInfo(List<WolfKillPregameInfo> preInfos);
	public void savePregameInfo(WolfKillPregameInfo preInfo);
}
