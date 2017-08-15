package com.mutuChat.wolfkill.controller;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mutuChat.service.IWolfChatBusiService;
import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillGoodsInfo;
import com.mutuChat.wolfkill.model.WolfKillMainInfo;
import com.mutuChat.wolfkill.model.WolfKillMatchHis;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.mutuChat.wolfkill.utils.StringUtils;
import com.mutuChat.wolfkill.vo.ChatPlayerInfoVo;
import com.mutuChat.wolfkill.vo.RoleOrderVo;

@Controller
public class WolfChatController {
	public final static String APPID = "wx41b1efe6d6d3127f";
	public final static String SECRET = "4dff1cb1b0234c09a219afb0186f2965";
	private static Logger logger = Logger.getLogger(WolfChatController.class);
	
	@Autowired
    private IWolfChatBusiService wolfChatBusiService;
	@Autowired
	private IWolfChatService wolfChatService;	
	
	/**微信code只能用一次，如果用户换头像，在这个code有效期内登陆，可能数据库存的url会失效*/
	@RequestMapping(value = "getUserInfos", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public ModelAndView getUserInfos(HttpServletRequest request) {
		String backmsg = null;
		String openid = null;
		String strJsonUser = null;
		HttpSession session=request.getSession();
		String userCode = request.getParameter("code");
		WolfKillChatUserInfo chatUser = wolfChatBusiService.getAndSaveChatPlayerInfo(userCode, session, backmsg);
		if (chatUser != null) {
			openid = chatUser.getOpenId();
			strJsonUser = JsonUtils.toJson(chatUser);
		}
		ModelAndView mav = new ModelAndView("userlogin");
		mav.addObject("rMsg",backmsg);
		mav.addObject("openid",openid);
		mav.addObject("userInfo",strJsonUser);
        return mav;
    }
	@ResponseBody
	@RequestMapping(value = "userBind", method = RequestMethod.GET)
	public Map<String, Object> bindUserInfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();		
		String playerId = request.getParameter("username");
		String phone = request.getParameter("phone");
		String openid = request.getParameter("openid");
		String errorMsg = wolfChatBusiService.bindUserInfo(playerId, phone, openid);
		if (!errorMsg.isEmpty()) {
			logger.error("the error Msg is" + errorMsg);
		}		
		map.put("loginResult",errorMsg);
		return map;
	}
	@RequestMapping(value = "forwardPerChat", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public ModelAndView forwardPerChat(HttpServletRequest request) {		
		ModelAndView mav = new ModelAndView("perChatInfo");
		String playerId = "3";
		//String playerId = request.getParameter("playerId");
		/**
		 * 赛季数据
		 */
		String mSeason = request.getParameter("mSeason");		
		/**
		 * 判断是否跳转过来的请求，如果是，则直接显示会员信息（只有绑定成功才能跳转）
		 */
		if (playerId != null) {
			ChatPlayerInfoVo  chatPlayer = wolfChatBusiService.getChatPlayerInfo(playerId,mSeason);
			mav.addObject("playerId",playerId);
			mav.addObject("jsonChatPlayer",JsonUtils.toJson(chatPlayer));
		} else {
			/**
			 * 先获取微信头像信息，判断用户是否绑定，如果没有绑定，跳转绑定页面，如果绑定，显示个人信息；
			 */
			String backmsg = null;
			HttpSession session=request.getSession();
			String userCode = request.getParameter("code");
			WolfKillChatUserInfo chatUser = wolfChatBusiService.getAndSaveChatPlayerInfo(userCode, session, backmsg);
			if (chatUser != null) {
				if (chatUser.getPlayerId() != null) {
					playerId = chatUser.getPlayerId();
					ChatPlayerInfoVo  chatPlayer = wolfChatBusiService.getChatPlayerInfo(playerId,mSeason);
					mav.addObject("playerId",playerId);
					mav.addObject("jsonChatPlayer",JsonUtils.toJson(chatPlayer));
				} else {
					String openid = chatUser.getOpenId();
					String strJsonUser = JsonUtils.toJson(chatUser);
					mav.setViewName("userlogin");
					mav.addObject("rMsg",backmsg);
					mav.addObject("openid",openid);
					mav.addObject("userInfo",strJsonUser);
				}
			} else {
				logger.error("the WolfKillChatUserInfo is null");
				logger.error("the backmsg is " + backmsg);
			}
		}				
		return mav;
	}
	@RequestMapping(value = "forwardHisMatch", method = RequestMethod.GET)
	public ModelAndView forwardHisMatch(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView("hisMatch");
	    String userEncode = request.getParameter("playerId");
	    String matchEncode = request.getParameter("matchId");
	    String playerId = null;
	    int matchId = 12345;
	    WolfKillMatchHis hisMatch = wolfChatBusiService.getHisMatchInfo(matchId);
	    if (userEncode != null && matchEncode != null) {
	        try {
	            playerId = new String(Base64.decodeBase64(userEncode),"UTF-8");
	            String strMatch = new String(Base64.decodeBase64(matchEncode),"UTF-8");
	            if (StringUtils.checkIsNum(strMatch)) {
	                matchId = Integer.parseInt(strMatch);
	            }
	            hisMatch = wolfChatBusiService.getHisMatchInfo(matchId);
	        }catch(UnsupportedEncodingException e) {
	            logger.error("DecodeBase64 is" +e);
	        }
	    }
	    mav.addObject("playerId",playerId);
	    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	    mav.addObject("hisMatchInfo" ,gson.toJson(hisMatch));
	    return mav;
	}
	
	@RequestMapping(value = "forwardRoleOrder", method = RequestMethod.GET)
	public ModelAndView forwardRoleOrder(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView("roleOrder");
	    List<RoleOrderVo> roleOrders = wolfChatBusiService.getRoleOrderInfo();
	    mav.addObject("roleOrder" ,JsonUtils.toJson(roleOrders));
	    return mav;
	}
	
	@RequestMapping(value = "forwardGoodsInfo", method = RequestMethod.GET)
	public ModelAndView forwardGoodsInfo(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView("goodsInfo");
	    List<WolfKillGoodsInfo> goodInfos = wolfChatService.getWolfGoodsInfo();
	    mav.addObject("goodInfo" ,JsonUtils.toJson(goodInfos));
	    return mav;
	}
	
	@RequestMapping(value = "forwardIndividual", method = RequestMethod.GET)
	public ModelAndView forwardIndividual(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("settings");
        String userEncode = request.getParameter("playerId");
        String playerId = null;
        String playerTag = null;
        String playerName = null;
        if (userEncode != null) {
            try {
                playerId = new String(Base64.decodeBase64(userEncode),"UTF-8");
            }catch(UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
            }
            WolfKillChatUserInfo chatUser = wolfChatService.queryUserOpenInfoByPlayerId(playerId);
            if (chatUser != null) {
                playerTag = chatUser.getPlayerTag();
            }
            List<WolfKillMainInfo> mainInfos = wolfChatService.getPlayerMainById(playerId, "0");
            if (!mainInfos.isEmpty()) {
                playerName = mainInfos.get(0).getPlayerName();
            }            
        }
        mav.addObject("playerTag" ,playerTag);
        mav.addObject("playerName" ,playerName);
        return mav;
    }
	
	@RequestMapping(value = "modifyIndividual", method = RequestMethod.GET)
	@ResponseBody
	public String modifyIndividual(HttpServletRequest request) {
	    String msg = null;
	    String nameEncode = request.getParameter("playerName");
	    String tagEncode = request.getParameter("playerTag");
	    String userEncode = request.getParameter("playerId");
	    String playerName = null;
	    String playerTag = null;
	    String playerId = null;
	    if (nameEncode != null && tagEncode != null) {
	        try {
	            playerName = new String(Base64.decodeBase64(nameEncode),"UTF-8");
	            playerTag = new String(Base64.decodeBase64(tagEncode),"UTF-8");
	            playerId = new String(Base64.decodeBase64(userEncode),"UTF-8");
	        }catch(UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
            }
	    }
	    msg = wolfChatService.saveNameAndTag(playerName, playerTag, playerId);
	    return msg;
	}
}
