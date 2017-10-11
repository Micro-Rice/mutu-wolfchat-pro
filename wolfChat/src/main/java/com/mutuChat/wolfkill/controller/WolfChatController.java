package com.mutuChat.wolfkill.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import com.mutuChat.service.IWolfChatBusiService;
import com.mutuChat.service.IWolfChatService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.mutuChat.wolfkill.vo.PlayerInfoVo;
import com.pospal.utils.tools.JsonConvertor;

@Controller
public class WolfChatController {	
	private static Logger logger = Logger.getLogger(WolfChatController.class);
	
	@Autowired
    private IWolfChatBusiService wolfChatBusiService;
	
	@Autowired
	private IWolfChatService wolfChatService;
	
	private static ComMethod cMethod = new ComMethod();
	
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
        String jsonPreInfo = null;
        HttpSession session=request.getSession();
        String shopName = request.getParameter("shopName");
        logger.debug("shopName is" + shopName);
        String userCode = request.getParameter("code");
        
        WolfKillChatUserInfo chatUser = wolfChatBusiService.getAndSaveChatPlayerInfo(userCode, session, backmsg);
        if (chatUser != null) {
            openid = chatUser.getOpenId();
            strJsonUser = JsonUtils.toJson(chatUser);
        }
        List<String> rooms = initRoomLayout("/roomName.properties",shopName);
        if (openid != null && !openid.isEmpty()) {
            WolfKillPregameInfo preInfo = wolfChatService.getPregameInfoByOpenId(openid);
            jsonPreInfo = JsonUtils.toJson(preInfo);
        }
        ModelAndView mav = new ModelAndView("selectSeat");
        mav.addObject("rMsg",backmsg);
        mav.addObject("userInfo",strJsonUser); 
        mav.addObject("rooms", JsonUtils.toJson(rooms));
        mav.addObject("preInfo",jsonPreInfo);
        return mav;
    }
	
	@ResponseBody
	@RequestMapping(value = "gotoSelect", method = RequestMethod.GET)
    public Map<String,Object> gotoSelect(HttpServletRequest request) {
	    Map<String,Object> result = new HashMap<String,Object>();
	    String openIdCode = request.getParameter("pq");
	    String roomIdCode = request.getParameter("rz");
	    String seatIdCode = request.getParameter("sw");
	    String message = "";
	    if (openIdCode != null  && roomIdCode != null && seatIdCode != null) {                     
            try {
                
                byte[] room = Base64.decodeBase64(roomIdCode);
                byte[] open = Base64.decodeBase64(openIdCode);
                byte[] seat = Base64.decodeBase64(seatIdCode);
                String roomId = new String(room,"utf-8");
                String openId = new String(open,"utf-8");
                String seatId = new String(seat,"utf-8");
                
                if (openId != null) {
                    WolfKillPregameInfo preInfo = wolfChatService.getPregameInfoByOpenId(openId);
                    if (preInfo != null) {
                        preInfo.setRoomId(roomId);
                        preInfo.setSeatId(Integer.parseInt(seatId));
                        /**
                         * 保存
                         */
                        message = wolfChatService.saveRoomAndSeatInfo(preInfo);
                    } else {
                        WolfKillPregameInfo  newPreInfo = new WolfKillPregameInfo();
                        newPreInfo.setOpenId(openId);
                        newPreInfo.setRoomId(roomId);
                        newPreInfo.setSeatId(Integer.parseInt(seatId));
                        /**
                         * 保存
                         */
                        message = wolfChatService.saveRoomAndSeatInfo(newPreInfo);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
                message = "error: "+e; 
            }                   
        }
	    result.put("message",message);
	    return result;	    
	}
	@RequestMapping(value = "showResp", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    public ModelAndView showResp(HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView("selectSuc");
	    String roomIdCode = request.getParameter("rz");
        String seatIdCode = request.getParameter("sw");
        String room = "";
        String seat = "";
        if (roomIdCode != null && seatIdCode != null) {  
            try {
                room = new String(Base64.decodeBase64(roomIdCode),"utf-8");
                seat = new String(Base64.decodeBase64(seatIdCode),"utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
            }
        } else {
            logger.error("roomCode or seatCode is null");
        }
        mav.addObject("room",room);
        mav.addObject("seat",seat);
	    return mav;	    
	}
	
	@RequestMapping(value = "forwardUserBand", method = RequestMethod.GET)
	public ModelAndView forwardUserBand(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("userlogin");
		String userCode = request.getParameter("rz");
		String user = null;
		if (userCode != null) {
			try {
				user = new String(Base64.decodeBase64(userCode),"utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
            }
		} else {
			logger.error("userCode is null");
		}		
		mav.addObject("userInfo",user);
		return mav;
	}
	
	/**
     * 通过房间名获取扫码会员信息
     */
    @RequestMapping(value = "queryChatPlayerId", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String queryChatPlayerId(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-queryChatPlayerId begin" );   	
    	String room = request.getParameter("room");
    	List<PlayerInfoVo> players = wolfChatService.getChatPlayerInfo(room);
    	return JsonConvertor.toJson(players);    	   	
    }
    
    @RequestMapping(value = "updateChatPlayerId", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String updateChatPlayerId(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-updateChatPlayerId begin");
    	String pdata = null;
    	try {
			pdata = cMethod.convertStreamToString(request.getInputStream());
		} catch (IOException e) {
			String message = "error||pdata is null";
			logger.error("updateChatPlayerId is error"+ message); 
			return message;
		}
    	logger.info("the data from local is:" +pdata);
    	String message = "error||pdata is null";
        if (pdata != null) {
        	 message = wolfChatService.updatePreChatPlayer(pdata);
        	 if (message.startsWith("success")) {
        		 logger.info("updateChatPlayerId is success");       		 
        	 } else {
        		 logger.error("updateChatPlayerId is error"+ message); 
        	 }        	 
        } else {
        	logger.error("updateChatPlayerId is error"+ message); 
        }
        return message;   	   	
    }
    
	
	public List<String> initRoomLayout(String file,String shopName){
	    ComMethod comMethod = new ComMethod();
	    Properties props = comMethod.getProperties(file);
        List<String> keys = Arrays.asList(props.stringPropertyNames().toArray(
                new String[props.stringPropertyNames().size()]));
        Collections.sort(keys);
        List<String> rooms = new ArrayList<String>();
        for (int i = 0; i < keys.size(); i++){
            String key = (String) keys.get(i);
            String val = props.getProperty(key);
            if (val != null && val.equals(shopName)) {
                rooms.add(key);
            }           
        }
        return rooms;
    }
}
