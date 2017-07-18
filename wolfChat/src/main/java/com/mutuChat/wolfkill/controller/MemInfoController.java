/**
 * Copyright (c) 2017,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebTest
 * Package Name:com.test.wolfkill.controller
 * File Name:MemInfoController.java
 * Date:2017年4月26日 下午2:01:14
 * 
 */
package com.mutuChat.wolfkill.controller;

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

import com.mutuChat.service.IMemInfoService;
import com.mutuChat.wolfkill.model.MutuMemInfo;
import com.mutuChat.wolfkill.utils.JsonUtils;
import com.mutuChat.wolfkill.utils.StringUtils;


 /**
 * ClassName: MemInfoController <br/>
 * Description: TODO <br/>
 * Date: 2017年4月26日 下午2:01:14 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Controller
public class MemInfoController {
    private static Logger logger = Logger.getLogger(MemInfoController.class);
    @Autowired
    private IMemInfoService memInfoService;
    
    @RequestMapping(value = "Memlogin", method = RequestMethod.GET)
    public ModelAndView forwardMemLogin(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }
    
    @RequestMapping(value = "vertifyMem", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> vertifyMem(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String memName = request.getParameter("memName");
        String password = request.getParameter("password");
        String msg = memInfoService.vertifyMemInfo(memName, password);
        String muid = null;
        if (msg.startsWith("success")) {
            muid = msg.split("\\|")[1];
        }
        map.put("message", msg);
        map.put("memName",memName);
        map.put("mUid", muid);
        return map;
    }
    
    @RequestMapping(value = "vertifyMemNum", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> vertifyMemNum(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String memNum = request.getParameter("mNum");
        String msg = "error|mNum is null!";
        if (memNum != null && StringUtils.checkIsNum(memNum)) {
            int mId = Integer.parseInt(memNum);
            msg = memInfoService.vertifyMemNum(mId);
        }
        map.put("message", msg);
        return map;
    }
    
    @RequestMapping(value = "forwardMemInfo", method = RequestMethod.GET)
    public ModelAndView forwardMemInfo(HttpServletRequest request) {
        String userEncode = request.getParameter("mna");
        String uidEncode = request.getParameter("uid");
        List<MutuMemInfo> memInfos = memInfoService.queryMemInfos();        
        ModelAndView mav = new ModelAndView("memInfo");
        String account = "";
        String accuid = "";
        if (userEncode != null && !userEncode.isEmpty()) {          
            try {
                byte[] byteData = Base64.decodeBase64(userEncode);
                byte[] uidData = Base64.decodeBase64(uidEncode);                
                account = new String(byteData, "utf-8");
                accuid =  new String(uidData,"utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("DecodeBase64 is" +e);
            }                   
        }
        mav.addObject("memInfos",JsonUtils.toJson(memInfos));
        mav.addObject("account", account);
        mav.addObject("accuid", accuid);       
        return mav;
    }
    @RequestMapping(value = "insertNewMem", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertNewMem(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String mNum = request.getParameter("mNum");
        String mName = request.getParameter("mName");
        String mPhone = request.getParameter("mPhone");
        
        String msg = memInfoService.updateMemInfo(mNum, mName, mPhone);
        map.put("message", msg);
        return map;
    }
    @RequestMapping(value = "deletedMem", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deletedMem(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String param = request.getParameter("param");
        String msg = "";
        List<MutuMemInfo> delMems = new ArrayList<MutuMemInfo>();
        try {
            delMems = JsonUtils.jsonToArrayList(param, MutuMemInfo.class);
            msg = memInfoService.deleteMemInfos(delMems);
        } catch (Exception e) {
            msg = "删除失败，请重试!";
            map.put("message", msg);
            return map;           
        }              
        map.put("message", msg);
        return map;
    }
    @RequestMapping(value = "modifyAccount", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyAccount(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String mName = request.getParameter("mName");
        String opass = request.getParameter("opass");
        String npass = request.getParameter("npass");
        String msg = memInfoService.vertifyMemInfo(mName, opass);
        if (msg.startsWith("success")) {
            String muid = msg.split("\\|")[1];
            msg = memInfoService.updateAccInfo(muid, mName, npass,opass);
        }
        map.put("message", msg);
        return map;
    }
}
