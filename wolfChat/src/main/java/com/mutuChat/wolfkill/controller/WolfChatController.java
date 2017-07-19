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

import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPospalInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.vo.ChatErrorVo;
import com.mutuChat.wolfkill.vo.ChatTokenVo;
import com.mutuChat.wolfkill.vo.ChatUserInfoVo;
import com.pospal.utils.tools.JsonConvertor;

@Controller
public class WolfChatController {
	public final static String APPID = "wx41b1efe6d6d3127f";
	public final static String SECRET = "4dff1cb1b0234c09a219afb0186f2965";
	private static Logger logger = Logger.getLogger(WolfChatController.class);
	
	@Autowired
    private IWolfChatService wolfChatService;
	/**微信code只能用一次，如果用户换头像，在这个code有效期内登陆，可能数据库存的url会失效*/
	@RequestMapping(value = "getUserInfos", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public ModelAndView getUserInfos(HttpServletRequest request) {
		String backmsg = null;
		String strJsonUser = null;
		String openid = null;
		HttpSession session=request.getSession();
		String userCode = request.getParameter("code");
		if (session.getAttribute("openid") != null) {
			openid = session.getAttribute("openid").toString();
		}	
		if (openid != null) {
			logger.info("the session openid is"+openid);			
		} else {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			String param = "appid="+APPID+"&secret="+SECRET+"&code="+userCode+"&grant_type=authorization_code";
			String msg = ComMethod.sendGet(url, param);
			if (msg.indexOf("errcode") > -1) {
				ChatErrorVo errorMsg = JsonConvertor.fromJson(msg,ChatErrorVo.class);
				backmsg = errorMsg.getErrmsg();
				logger.info("the error Msg is"+backmsg);
			} else {
				ChatTokenVo tokenContent = JsonConvertor.fromJson(msg,ChatTokenVo.class);
				String accessToken = tokenContent.getAccess_token();
				openid = tokenContent.getOpenid();
				session.setAttribute("openid", openid);
				String infoUrl = "https://api.weixin.qq.com/sns/userinfo";
				String infoParam = "access_token="+accessToken+"&openid="+openid+"&lang=zh_CN";
				String infoMsg = ComMethod.sendGet(infoUrl,infoParam);
				if (infoMsg.indexOf("errcode") > -1) {
					ChatErrorVo errorMsg = JsonConvertor.fromJson(infoMsg,ChatErrorVo.class);
					backmsg = errorMsg.getErrmsg();
					logger.info("the wxBack openid is"+openid+ "And the error Msg is" +backmsg);
				} else {
					logger.info("the wxBack openid is"+openid);	
					logger.info("the backMsg  is"+infoMsg);
					ChatUserInfoVo userInfoContent = JsonConvertor.fromJson(infoMsg,ChatUserInfoVo.class);						
					wolfChatService.saveUserOpenInfo(userInfoContent);		
				}
			}			
		}
		if (openid != null) {
			WolfKillChatUserInfo chatUser = wolfChatService.queryUserOpenInfoByOpenid(openid);
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
		String errorMsg = "";
		String playerId = request.getParameter("username");
		String phone = request.getParameter("phone");
		String openid = request.getParameter("openid");
		WolfKillPospalInfo playerInfo = wolfChatService.queryPlayerInfoById(playerId);
		if (playerInfo == null) {
			errorMsg = "error2";
		} else {
			if (phone != null && phone.equals(playerInfo.getPhone())) {
				if (openid == null) {
					errorMsg = "error3";
				} else {
					WolfKillChatUserInfo chatUser = wolfChatService.queryUserOpenInfoByOpenid(openid);
					if (chatUser != null) {
						chatUser.setPlayerId(Integer.parseInt(playerId));
						chatUser.setPlayerPhone(phone);
						wolfChatService.savePlayerInfo(chatUser);
					} else {
						errorMsg = "error3";
					}
				}
			} else {
				errorMsg = "error1";
			}
		}
		map.put("loginResult",errorMsg);
		return map;
	}
}
