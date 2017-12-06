package com.mutuChat.wolfkill.controller;

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
import com.mutuChat.service.IWolfChatOutService;
import com.mutuChat.wolfkill.model.WolfKillChatUserInfo;
import com.mutuChat.wolfkill.model.WolfKillPregameInfo;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.utils.JsonUtils;

@Controller
public class WolfChatOutController {
	private static Logger logger = Logger.getLogger(WolfChatOutController.class);
	@Autowired
    private IWolfChatBusiService wolfChatBusiService;
	
	@Autowired
	private IWolfChatOutService wolfChatOutService;
	
	@RequestMapping(value = "selectLiSeat", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    public ModelAndView selectLiSeat(HttpServletRequest request) {
	    logger.info("##selectSeat begin##");
	    String backmsg = null;
        String openid = null;
        String strJsonUser = null; 
        String jsonPreInfo = null;
        HttpSession session=request.getSession();
        String shopName = request.getParameter("shopName");
        logger.debug("shopName is" + shopName);
        String userCode = request.getParameter("code");
        
        WolfKillChatUserInfo chatUser = wolfChatBusiService.getOutAndSaveChatPlayerInfo(userCode, session, backmsg,shopName);
        if (chatUser != null) {
            openid = chatUser.getOpenId();
            strJsonUser = JsonUtils.toJson(chatUser);
        }
        List<String> rooms = initRoomLayout("/roomName.properties",shopName);
        if (openid != null && !openid.isEmpty()) {
            WolfKillPregameInfo preInfo = wolfChatOutService.getPregameInfoByOpenId(openid);
            jsonPreInfo = JsonUtils.toJson(preInfo);
        }
        ModelAndView mav = new ModelAndView("selectLiSeat");
        mav.addObject("rMsg",backmsg);
        mav.addObject("userInfo",strJsonUser); 
        mav.addObject("rooms", JsonUtils.toJson(rooms));
        mav.addObject("preInfo",jsonPreInfo);
        return mav;
    }
	
	@ResponseBody
	@RequestMapping(value = "gotoLiSelect", method = RequestMethod.GET)
    public Map<String,Object> gotoLiSelect(HttpServletRequest request) {
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
                    WolfKillPregameInfo preInfo = wolfChatOutService.getPregameInfoByOpenId(openId);
                    if (preInfo != null) {
                        preInfo.setRoomId(roomId);
                        preInfo.setSeatId(Integer.parseInt(seatId));
                        /**
                         * 保存
                         */
                        message = wolfChatOutService.saveRoomAndSeatInfo(preInfo);
                    } else {
                        WolfKillPregameInfo  newPreInfo = new WolfKillPregameInfo();
                        newPreInfo.setOpenId(openId);
                        newPreInfo.setRoomId(roomId);
                        newPreInfo.setSeatId(Integer.parseInt(seatId));
                        /**
                         * 保存
                         */
                        message = wolfChatOutService.saveRoomAndSeatInfo(newPreInfo);
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
            if (val != null && val.startsWith(shopName)) {
                rooms.add(key);
            }           
        }
        return rooms;
    }
}
