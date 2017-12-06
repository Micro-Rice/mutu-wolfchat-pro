/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.utils
 * File Name:SqulUtil.java
 * Date:2016年1月27日 下午2:14:54
 * 
 */
package com.mutuChat.wolfkill.utils;

 /**
 * ClassName: SqulUtil <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:14:54 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public final class SqlUtil {
    public static String parseLikeString(String str) {
        if (str == null) {
            return null;
        }

        return str.replaceAll("([\\\\])", "$1$1").replaceAll("([_%＿％])", "\\\\$1");
    }
}
