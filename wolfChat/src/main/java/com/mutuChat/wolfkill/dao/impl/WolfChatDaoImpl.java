package com.mutuChat.wolfkill.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mutuChat.wolfkill.controller.WolfChatController;
import com.mutuChat.wolfkill.core.BaseDao;
import com.mutuChat.wolfkill.core.QueryConditions;
import com.mutuChat.wolfkill.dao.IWolfChatDao;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillGoodsInfo;
import com.mutuChat.wolfkill.model.WolfKillMatchHis;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.vo.RoleOrderVo;
@Repository("wolfChatDao")
public class WolfChatDaoImpl extends BaseDao implements IWolfChatDao{
    private static final String UNIQUEID = "uniqueId";
    private static final String ROLENAME = "roleName";
    private static final String SEASON = "season";
    private static Logger logger = Logger.getLogger(WolfChatDaoImpl.class);
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
        StringBuffer buf = new StringBuffer("select DISTINCT roleName from WolfKillPerInfo t where t.uniqueId=:uniqueId");
        String hql = buf.toString();
        Query query = currentSession().createQuery(hql);
        query.setString(UNIQUEID, playerId);
        List<String> l = query.list();
        return l;
    }

    @Override
    public List<String> findAllSeasons() {
        StringBuffer buf = new StringBuffer("select distinct season from WolfKillMainInfo");
        String hql = buf.toString();
        Query query = currentSession().createQuery(hql);
        List<String> l = query.list();
        return l;
    }

	@Override
	public List<WolfKillMatchHis> findHisMatchByPlayerId(String playerId) {
	    StringBuffer buf = new StringBuffer("select a from WolfKillMatchHis a left join a.wolfKillMatchHisDets b "
	            + "where b.playerId=:playerId");
	    String hql = buf.toString();
        Query query = currentSession().createQuery(hql);
        query.setString("playerId", playerId);
        List<WolfKillMatchHis> l = query.list();
        return l;
	}

    @Override
    public WolfKillMatchHis findHisMatchByMatchId(int matchId) {
        Criteria crit = currentSession().createCriteria(WolfKillMatchHis.class);
        crit.add(Restrictions.eq("fid", matchId));
        if (crit.list() == null || crit.list().isEmpty()) {
            return null;
        }
        WolfKillMatchHis hisMatch = (WolfKillMatchHis)crit.list().get(0);
        Field[] fieldlist = hisMatch.getClass().getDeclaredFields();
        Method subMethod;
        try {
            for (int j = 0; j < fieldlist.length; j++) {
                Field fld = fieldlist[j];
                if (fld.getType().equals(java.util.List.class)) {
                    subMethod = WolfKillMatchHis.class.getMethod("get" + fld.getName().replace("wolf", "Wolf"));
                    if (!Hibernate.isInitialized(subMethod.invoke(hisMatch))) {
                        Hibernate.initialize(subMethod.invoke(hisMatch));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Hibernate initialize error:" + WolfKillMatchHis.class.getName(), e);
        }
        return hisMatch;
    }

    @Override
    public List<RoleOrderVo> findAllplayerIDAchive(String roleName, String season) {
        String hql = null;
        Query query = null;
        List<RoleOrderVo> roleOrders = new ArrayList<RoleOrderVo>();
        if (season != null) {
            hql =  "select uniqueId,SUM(roleTask),SUM(totalNum),SUM(wonNum) from WolfKillPerInfo t"
                    + " where t.roleName=:roleName and t.season=:season"
                    + " group by uniqueId"
                    + " order by SUM(roleTask) desc";
            query = currentSession().createQuery(hql);
            query.setString(ROLENAME, roleName);
            query.setString(SEASON, season);
        } else {
            hql =  "select uniqueId,SUM(roleTask), SUM(totalNum),SUM(wonNum) from WolfKillPerInfo t"
                    + " where t.roleName=:roleName"
                    + " group by uniqueId"
                    + " order by SUM(roleTask) desc";
            query = currentSession().createQuery(hql);
            query.setString(ROLENAME, roleName);
        }
        List<Object[]> l = query.list();
        for (Object[] objs : l) {
            RoleOrderVo roleOrder = new RoleOrderVo();
            roleOrder.setsPlayerId(String.valueOf(objs[0]));
            roleOrder.setsAchiveNum(String.valueOf(objs[1]));
            roleOrder.setmNum(Integer.parseInt(String.valueOf(objs[2])));
            roleOrder.setwNum(Integer.parseInt(String.valueOf(objs[3])));
            roleOrders.add(roleOrder);
        }
        return roleOrders;
    }

    @Override
    public List<RoleOrderVo> findAllRoleTWNum() {
        String hql ="select roleName,SUM(totalNum),SUM(wonNum) from WolfKillPerInfo group by roleName";
        List<RoleOrderVo> roleOrders = new ArrayList<RoleOrderVo>();
        Query query = currentSession().createQuery(hql);
        List<Object[]> l = query.list();
        for (Object[] objs : l) {
            RoleOrderVo roleOrder = new RoleOrderVo();
            roleOrder.setrName(String.valueOf(objs[0]));
            roleOrder.setmNum(Integer.parseInt(String.valueOf(objs[1])));
            roleOrder.setwNum(Integer.parseInt(String.valueOf(objs[2])));
            roleOrders.add(roleOrder);
        }
        return roleOrders;
    }

    @Override
    public List<WolfKillGoodsInfo> findAllGoodsInfo(QueryConditions condition) {
        String hql = "from WolfKillGoodsInfo";
        List<WolfKillGoodsInfo> l = super.find(hql, condition);       
        return l;
    }
    @Override
    public void updatePlayerTagById(String playerTag,String playerId) {
        String hql = "update WolfKillChatUserInfo w set w.playerTag=:playerTag"
                + " where w.playerId=:playerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("playerTag", playerTag);
        query.setParameter("playerId", playerId);
        query.executeUpdate();        
    }

    @Override
    public void updateMainPlayerNameById(String playerName, String playerId) {
        String hql = "update WolfKillMainInfo w set w.playerName=:playerName"
                + " where w.uniqueId=:playerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("playerName", playerName);
        query.setParameter("playerId", playerId);
        query.executeUpdate();
    }

    @Override
    public void updateRolePlayerNameById(String playerName, String playerId) {
        String hql = "update WolfKillPerInfo w set w.playerName=:playerName"
                + " where w.uniqueId=:playerId";
        Query query = currentSession().createQuery(hql);
        query.setParameter("playerName", playerName);
        query.setParameter("playerId", playerId);
        query.executeUpdate();
    }	
}
