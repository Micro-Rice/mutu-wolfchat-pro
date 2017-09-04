package com.mutuChat.wolfkill.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.pospal.utils.tools.JsonConvertor;

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
	@RequestMapping(value = "forwardPerChat", method = RequestMethod.GET)
	public ModelAndView forwardPerChat(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("chatPerInfo");		
		return mav;
	}
	
	@RequestMapping(value = "selectSeat", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    public ModelAndView selectSeat(HttpServletRequest request) {
	    logger.info("##selectSeat begin##");
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
        String jsonPreInfo = null;
        if (openid != null && !openid.isEmpty()) {
            WolfKillPregameInfo preInfo = wolfChatService.getPregameInfoByOpenId(openid);
            jsonPreInfo = JsonUtils.toJson(preInfo);
        }
        List<String> rooms = initRoomLayout("/roomName.properties");
        ModelAndView mav = new ModelAndView("selectSeat");
        mav.addObject("rMsg",backmsg);
        mav.addObject("openid",openid);
        mav.addObject("userInfo",strJsonUser);
        mav.addObject("rooms", JsonUtils.toJson(rooms));
        mav.addObject("preInfo",jsonPreInfo);
        return mav;
    }
	private List<String> initRoomLayout(String file){
	    ComMethod comMethod = new ComMethod();
	    Properties props = comMethod.getProperties(file);
        List<String> keys = Arrays.asList(props.stringPropertyNames().toArray(
                new String[props.stringPropertyNames().size()]));
        Collections.sort(keys);
        List<String> rooms = new ArrayList<String>();
        for (int i = 0; i < keys.size(); i++){
            String key = (String) keys.get(i);
            String val = props.getProperty(key);
            rooms.add(val);
        }
        return rooms;
    }
}
