/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.applicationexception
 * File Name:ApplicationException.java
 * Date:2016年1月28日 上午9:30:52
 * 
 */
package com.test.core.exception;

 /**
 * ClassName: ApplicationException <br/>
 * Description: TODO <br/>
 * Date: 2016年1月28日 上午9:30:52 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class ApplicationException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errCode;
    private Object[] params;

    public ApplicationException(String errCode) {
        this(errCode, null, null, null);
    }

    public ApplicationException(String errCode, String message) {
        this(errCode, message, null, null);
    }

    public ApplicationException(String errCode, Object[] params) {
        this(errCode, null, params, null);
    }

    public ApplicationException(String errCode, String message, Object[] params) {
        this(errCode, message, params, null);
    }

    public ApplicationException(String errCode, String message, Object[] params, Throwable e) {
        super(message, e);
        this.params = params;
        this.errCode = errCode;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
