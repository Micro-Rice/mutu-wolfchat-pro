/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.entity.page
 * File Name:Page.java
 * Date:2016年1月27日 下午2:11:41
 * 
 */
package com.test.core.entity.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.test.core.entity.page.PageInfo;

 /**
 * ClassName: Page <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:11:41 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    private PageInfo pageInfo;
    private List data;

    public Page() {
        this.pageInfo = new PageInfo();

        this.data = new ArrayList();
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List getData() {
        return this.data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
