package com.mutuChat.wolfkill.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseOutDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatOutDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;

@Repository("wolfChatOutDao")
public class WolfChatOutDaoImpl  extends BaseOutDao implements IWolfChatOutDao{

	@Override
	public WolfKillChatUserInfo findChatUserInfo(QueryConditions condition) {
		String hql = "from WolfKillChatUserInfo";
		List<WolfKillChatUserInfo> l = super.find(hql, condition);
		 if (l == null || l.isEmpty()) {
	            return null;
	        }
	        return l.get(0);
	}

	@Override
	public void saveChatUserInfo(WolfKillChatUserInfo chatUserInfo) {
		super.saveWithFlush(chatUserInfo);
		
	}

	@Override
	public List<WolfKillPregameInfo> findPregameInfo(QueryConditions condition) {
		String hql = "from WolfKillPregameInfo";       
        return super.find(hql, condition);
	}

	@Override
	public void deletePregameInfo(List<WolfKillPregameInfo> preInfos) {
		super.deleteAll(preInfos);	
		
	}

	@Override
	public void savePregameInfo(WolfKillPregameInfo preInfo) {
		super.saveWithFlush(preInfo);			
	}

}
