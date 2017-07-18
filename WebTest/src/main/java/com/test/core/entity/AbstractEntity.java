/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.entity
 * File Name:AbstractEntity.java
 * Date:2016年1月27日 下午2:48:54
 * 
 */
package com.test.core.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.test.core.entity.Entity;
import com.test.utils.ObjectUtils;

 /**
 * ClassName: AbstractEntity <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:48:54 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

@MappedSuperclass
public abstract class AbstractEntity implements Entity {
    private static final long serialVersionUID = -3922123931186043789L;

    @Column(name = "id", unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public String toXml() {
        return ObjectUtils.toXml(this);
    }

    public String toJSON() {
        return ObjectUtils.toJSON(this);
    }

    public String toString() {
        return toJSON();
    }

    public Object clone() {
        return ObjectUtils.serializaClone(this);
    }

    public final int hashCode() {
        return (int) getId();
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass().equals(super.getClass())) {
            return (obj.hashCode() == hashCode());
        }

        return false;
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
