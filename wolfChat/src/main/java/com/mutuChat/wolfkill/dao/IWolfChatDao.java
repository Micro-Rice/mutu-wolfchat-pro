package com.mutuChat.wolfkill.dao;

import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;

public interface IWolfChatDao {
	public WolfKillChatUserInfo findChatUserInfo(QueryConditions condition);
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo);
	public WolfKillPospalInfo findPlayerInfo(QueryConditions condition);
}
