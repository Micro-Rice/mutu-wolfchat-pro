/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.utils
 * File Name:StringUtils.java
 * Date:2016年1月26日 下午5:29:22
 * 
 */
package com.test.utils;

 /**
 * ClassName: StringUtils <br/>
 * Description: TODO <br/>
 * Date: 2016年1月26日 下午5:29:22 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils{
    
    public static boolean isNull(String value) {
        return ((value == null) || (value.trim().length() == 0));
    }
    public static boolean basicInputValidate(String str) {
        String regex = "^[0-9a-zA-Z\\u4E00-\\u9FA5\\s\\-_:#@\\?\\*,\\.\\/]{0,}$";
        return validate(regex, str);
    }
    public static boolean checkPassword(String password){
        String regex = "^[a-zA-Z]\\w{5,17}$";
        return validate(regex, password);
    }
    public static boolean checkEmail(String email) {
        String regex = "^[a-zA-Z0-9_\\-\\.]+@([a-zA-Z0-9]+\\.[a-zA-Z0-9]+)+$";
        return validate(regex, email);
    }
    public static boolean checkIsNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9]{1,}$");
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }
    private static boolean validate(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }
}
