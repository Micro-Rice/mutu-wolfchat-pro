package com.mutuChat.wolfkill.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
@Repository("wolfChatDao")
public class WolfChatDaoImpl extends BaseDao implements IWolfChatDao{

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
	public WolfKillPospalInfo findPlayerInfo(QueryConditions condition) {
		String hql = "from WolfKillPospalInfo";
		List<WolfKillPospalInfo> l = super.find(hql, condition);
		 if (l == null || l.isEmpty()) {
	            return null;
	        }
	        return l.get(0);
	}
	
	 @Override
	    public List<WolfKillPregameInfo> findPregameInfo(QueryConditions condition) {
	        String hql = "from WolfKillPregameInfo";       
	        return super.find(hql, condition);
	    }
	 @Override
	    public void savePregameInfo(WolfKillPregameInfo preInfo) {
	        super.saveWithFlush(preInfo);
	    }

	@Override
	public void deletePregameInfo(List<WolfKillPregameInfo> preInfos) {
		super.deleteAll(preInfos);		
	}

	@Override
	public void savePregameInfos(List<WolfKillPregameInfo> preInfos) {
		super.saveAll(preInfos);		
	}
}
