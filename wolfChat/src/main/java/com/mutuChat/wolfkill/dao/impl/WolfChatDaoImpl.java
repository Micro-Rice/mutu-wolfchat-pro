package com.mutuChat.wolfkill.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPerInfo;
import com.mutuChat.wolfkill.model.WolfKillPerInfoHistory;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
@Repository("wolfChatDao")
public class WolfChatDaoImpl extends BaseDao implements IWolfChatDao{
    private static final String UNIQUEID = "uniqueId";
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
    public List<String> findRoleNameById(String playerId) {
        StringBuffer buf = new StringBuffer("select DISTINCT roleName from wolfKillPerInfo t where t.uniqueId=:uniqueId");
        String hql = buf.toString();
        Query query = currentSession().createQuery(hql);
        query.setString(UNIQUEID, playerId);
        List<String> l = query.list();
        return l;
    }

    @Override
    public List<String> findAllSeasons() {
        StringBuffer buf = new StringBuffer("select DISTINCT season from wolfKillMainInfo");
        String hql = buf.toString();
        Query query = currentSession().createQuery(hql);
        List<String> l = query.list();
        return l;
    }	
}
