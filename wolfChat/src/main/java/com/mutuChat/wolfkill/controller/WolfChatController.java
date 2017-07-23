package com.mutuChat.wolfkill.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mutuChat.service.IWolfChatBusiService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.vo.ChatPlayerInfoVo;
import com.pospal.utils.tools.JsonConvertor;

@Controller
public class WolfChatController {
	public final static String APPID = "wx41b1efe6d6d3127f";
	public final static String SECRET = "4dff1cb1b0234c09a219afb0186f2965";
	private static Logger logger = Logger.getLogger(WolfChatController.class);
	
	@Autowired
    private IWolfChatBusiService wolfChatBusiService;
	
	
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
			strJsonUser = JsonConvertor.toJson(chatUser);
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
		String playerId = request.getParameter("playerId");
		/**
		 * 判断是否跳转过来的请求，如果是，则直接显示会员信息（只有绑定成功才能跳转）
		 */
		if (playerId != null) {
			ChatPlayerInfoVo  chatPlayer = wolfChatBusiService.getChatPlayerInfo(playerId);
			mav.addObject("playerId",playerId);
			mav.addObject("jsonChatPlayer",JsonConvertor.toJson(chatPlayer));
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
					ChatPlayerInfoVo  chatPlayer = wolfChatBusiService.getChatPlayerInfo(playerId);
					mav.addObject("playerId",playerId);
					mav.addObject("jsonChatPlayer",JsonConvertor.toJson(chatPlayer));
				} else {
					String openid = chatUser.getOpenId();
					String strJsonUser = JsonConvertor.toJson(chatUser);
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
}
