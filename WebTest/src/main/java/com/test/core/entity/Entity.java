/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.entity
 * File Name:Entity.java
 * Date:2016年1月27日 下午2:47:40
 * 
 */
package com.test.core.entity;

import java.io.Serializable;

 /**
 * ClassName: Entity <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:47:40 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public abstract interface Entity extends Serializable, Cloneable {
    public abstract long getId();

    public abstract void setId(int paramLong);

    public abstract boolean equals(Object paramObject);

    public abstract int hashCode();

    public abstract String toString();

    public abstract String toXml();

    public abstract String toJSON();

    public abstract Object clone();
}
