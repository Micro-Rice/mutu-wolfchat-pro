/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.woflkill.controller
 * File Name:WolfkillController.java
 * Date:2016年10月27日 下午2:00:41
 * 
 */
package com.mutuChat.wolfkill.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pospal.utils.tools.JsonConvertor;
import com.pospal.vo.ImageResponseData;
import com.pospal.vo.ImageResponseDataDetail;
import com.pospal.vo.PostPointParameter;
import com.mutuChat.service.IPospalService;
import com.mutuChat.service.IWolfKillServive;
import com.mutuChat.wolfkill.utils.ComMethod;
import com.mutuChat.wolfkill.vo.PlayerInfoVo;



 /**
 * ClassName: WolfkillController <br/>
 * Description:  <br/>
 * Date: 2016年10月27日 下午2:00:41 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Controller
public class WolfKillController {
    
    private static final String LEFT_BRACKET = "[";
    private static final String LABEL_ITEM = "{label:'";
    private static final String VAL_ITEM = "',val:'";
    private static final String RIGHT_QUOTE = "'";
    private static final String RIGHT_BRACKET = "]";
	private final static int SHOWSIZE = 20;
    private static Logger logger = Logger.getLogger(WolfKillController.class);
    private static ComMethod cMethod = new ComMethod();
    
    @Autowired
    private IWolfKillServive wolfKillService;
    
    @Autowired
    private IPospalService pospalService;
    
    @ResponseBody
    @RequestMapping(value = "getWolfkillData", method = RequestMethod.GET)
    public Map<String, Object> getWolfkillData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String matchNum = request.getParameter("matchNum");
        List<String> wolfDatas = wolfKillService.getWolfKillMainData(SHOWSIZE,matchNum);
        map.put("playerInfo", getJSonFormatStringlist(wolfDatas));
        logger.info(cMethod.getIpAddr(request) + "-login success" );
        return map;
    }
    
    @RequestMapping(value = "getWolfkill", method = RequestMethod.GET)
    public ModelAndView getWolfkillAllData(HttpServletRequest request) {
    	ModelAndView mav = new ModelAndView("playerInfo");
        List<String> wolfDatas = wolfKillService.getWolfKillMainData(Integer.MAX_VALUE,"0");
        mav.addObject("pInfo", getJSonFormatStringlist(wolfDatas));
        logger.info(cMethod.getIpAddr(request) + "-login success:getWolfkill" );
        return mav;
    }
    
    @RequestMapping(value = "forwardPerInfo", method = RequestMethod.GET)
    public ModelAndView forwardPerInfo(HttpServletRequest request) {
        String userEncode = request.getParameter("playerName");
        String seqEncode = request.getParameter("seq");
        String matchNumEncode = request.getParameter("mnum");
        List<String> wolfPerDatas = new ArrayList<String>();
        String sequence = "";
        String user = "";
        String matchNum = "";
        int maxMatchNum = wolfKillService.getMaxMatchNum() +1;
        if (userEncode != null && !userEncode.isEmpty()) {       	        	 
        	try {
				byte[] byteData = Base64.decodeBase64(userEncode);
				byte[] seqData = Base64.decodeBase64(seqEncode);
				byte[] matchData = Base64.decodeBase64(matchNumEncode);
				user = new String(byteData, "utf-8");
				sequence =  new String(seqData,"utf-8");
				matchNum =  new String(matchData,"utf-8");
				wolfPerDatas = wolfKillService.getWolfKillPerData(user,matchNum); 
			} catch (UnsupportedEncodingException e) {
				logger.error("DecodeBase64 is" +e);
			}       	       	
        }        
        ModelAndView mav = new ModelAndView("wolfKillPer");
        mav.addObject("perDatas", getJSonFormatStringlist(wolfPerDatas));
        mav.addObject("sequence",sequence);
        mav.addObject("uniqueId",user);
        mav.addObject("matchNum",matchNum);
        mav.addObject("maxMatchNum",maxMatchNum);
        return mav;
    }
    
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView searchPlayer(HttpServletRequest request) {
        String playerUid = request.getParameter("player");
        ModelAndView mav = new ModelAndView("searchPlayer");
        List<String> wolfDatas = new ArrayList<String>();
        List<String> matchNums = wolfKillService.getMatchNums(playerUid);
        if (!playerUid.isEmpty() && playerUid != null) {
        	/**
        	 * 当季数据
        	 */
        	String data = wolfKillService.getPlayerMainDataByUid(playerUid, "0");
        	if (data != null) {
    			wolfDatas.add(data);
    		}
        	/**
        	 * 历史数据
        	 */
        	for (int i = 0; i < matchNums.size(); i++) {
        		String hisdata = wolfKillService.getPlayerMainDataByUid(playerUid, matchNums.get(i));
        		if (hisdata != null) {
        			wolfDatas.add(hisdata);
        		}
        	}       	
        }
        int maxMatchNum = wolfKillService.getMaxMatchNum() + 1;
        mav.addObject("maxMatchNum",maxMatchNum);
        mav.addObject("pMainDatas", getJSonFormatStringlist(wolfDatas));
        mav.addObject("matchNums",getJSonFormatStringlist(matchNums));
        return mav;
    }
    @RequestMapping(value = "queryPlayerId", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String queryPlayerId(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-queryPlayerId begin" );
    	List<PlayerInfoVo> players = wolfKillService.getPlayerBaseInfo();
    	return JsonConvertor.toJson(players);    	   	
    }
    @RequestMapping(value = "updataMemInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updataMemInfo(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-updataMemInfo begin" );
    	String pdata = null;
    	try {
			pdata = cMethod.convertStreamToString(request.getInputStream());
		} catch (IOException e) {
			String message = "error||pdata is null";
			logger.error("updataMemInfo is error"+ message); 
			return message;
		}
    	logger.info("the data from local is:" +pdata);
    	String message = "error||pdata is null";
        if (pdata != null && !pdata.isEmpty()) {
        	 message = wolfKillService.updataUserInfo(pdata);
        	 if (message.startsWith("success")) {
        		 logger.info("updataMemInfo is success");       		 
        	 } else {
        		 logger.error("updataMemInfo is error"+ message); 
        	 }        	 
        } else {
        	logger.error("updataMemInfo is error"+ message); 
        }
        return message;
    }
    @RequestMapping(value = "queryMemInfo", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String queryMemInfo(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-queryMemInfo begin" );
    	String message = "";
    	List<ImageResponseDataDetail> datas = new ArrayList<ImageResponseDataDetail>();
    	try {
			List<ImageResponseData> memInfos = pospalService.getMemInfoFormPospal();
			for (int i = 0; i < memInfos.size(); i++) {
				ImageResponseData memInfo = memInfos.get(i);
				if (!memInfo.isSuccess()) {
					int errorCode = 0;
					String strMsg = "";
					if (memInfo.getErrorCode() != null) {
						errorCode = memInfo.getErrorCode();
					}
					if (memInfo.getMessages() != null) {
						String[] msg = memInfo.getMessages();
						for (int j = 0; j < msg.length; j++) {
							strMsg = strMsg + msg[j];
						}
					}
					logger.error("queryMemInfo pospal return errorCode and Message:"+ errorCode+strMsg);
					message = "error||queryMemInfo pospal return errorCodeand Message:" + errorCode+strMsg;
					return message;
				} else {
					datas.add(memInfo.getData());
				}
			}
			if (datas.isEmpty()) {
				logger.error("queryMemInfo pospal return empty");
				message = "error||queryMemInfo pospal return empty";
			} else {
				wolfKillService.updatePospalMemInfo(datas);
				message = "queryMemInfo is success";
			}
		} catch (IOException e) {
			logger.error("queryMemInfo error:"+ e);
			message = "error||queryMemInfo" + e;
		}
    	return message;
    }
    @RequestMapping(value = "updatePospalPoint", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String updatePospalPoint(HttpServletRequest request) {
    	logger.info(cMethod.getIpAddr(request) + "-updatePospalPoint begin" );
    	List<PostPointParameter> points = wolfKillService.getPospalPointChange();
    	String message = "";
    	try {
    		message = pospalService.updatePospalPoint(points);
    		if ("success".equals(message)) {
    			logger.info("updatePospalPoint success!");
    		} else {
    			logger.error("updatePospalPoint error" + message);
    		}
    	} catch (IOException e) {
    		logger.error("updatePospalPoint error" + e);
    	}
    	return message;
    }
    private String getJSonFormatStringlist(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(LEFT_BRACKET);
        for (int i = 0; i < list.size(); i++) {
            sb.append(LABEL_ITEM);
            sb.append(list.get(i));
            sb.append(VAL_ITEM);
            sb.append(list.get(i));
            sb.append(RIGHT_QUOTE);
            if (i == list.size() - 1) {
                sb.append("}");
            } else {
                sb.append("},");
            }
        }
        sb.append(RIGHT_BRACKET);
        return sb.toString();
    }   
}
