package com.mutuChat.wolfkill.dao.impl;

import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.dao.IWolfKillOutDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
@Repository("wolfKillOutDao")
public class WolfKillOutDaoImpl extends BaseDao implements IWolfKillOutDao{

	@Override
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo) {
		super.saveWithFlush(chatUserInfo);			
	}

}
