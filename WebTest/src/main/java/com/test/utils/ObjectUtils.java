/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.utils
 * File Name:ObjectUtils.java
 * Date:2016年1月27日 下午2:50:29
 * 
 */
package com.test.utils;

import java.io.Serializable;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.SerializationUtils;

 /**
 * ClassName: ObjectUtils <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:50:29 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public final class ObjectUtils {
    public static String toXml(Object o) {
        JSON jsonObject = JSONSerializer.toJSON(o);
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.write(jsonObject);
    }

    public static String toJSON(Object o) {
        JSON jsonObject = JSONSerializer.toJSON(o);
        return jsonObject.toString();
    }

    public static Object serializaClone(Serializable o) {
        return SerializationUtils.clone(o);
    }
}
