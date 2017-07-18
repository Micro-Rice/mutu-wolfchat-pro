/**
 * Copyright (c) 2016,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:WebAppTest
 * Package Name:com.test.core.entity.page
 * File Name:PageInfo.java
 * Date:2016年1月27日 下午2:12:26
 * 
 */
package com.test.core.entity.page;

import java.io.Serializable;

 /**
 * ClassName: PageInfo <br/>
 * Description: TODO <br/>
 * Date: 2016年1月27日 下午2:12:26 <br/>
 * <br/>
 * 
 * @author yzheng(邮箱)
 * 
 * 修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */

public class PageInfo implements Serializable {
    private static final long serialVersionUID = 7326349283768198763L;
    private int totalPageCount;
    private int totalRowCount;
    private int currentPageNum = 1;
    private int rowsOfPage = 10;
    private boolean countOfEverytime = true;

    public PageInfo() {
    }

    public PageInfo(String pageNum, String pageLength) {
        if ((null != pageNum) && (pageNum.length() > 0))
            this.currentPageNum = Integer.parseInt(pageNum);
        else {
            this.currentPageNum = 1;
        }
        if ((null != pageLength) && (pageLength.length() > 0))
            this.rowsOfPage = Integer.parseInt(pageLength);
        else
            this.rowsOfPage = 15;
    }

    public int getTotalPageCount() {
        return this.totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalRowCount() {
        return this.totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public int getCurrentPageNum() {
        return ((this.currentPageNum <= 0) ? 1 : this.currentPageNum);
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getRowsOfPage() {
        return ((this.rowsOfPage <= 0) ? 10 : this.rowsOfPage);
    }

    public void setRowsOfPage(int rowsOfPage) {
        this.rowsOfPage = rowsOfPage;
    }

    public boolean isCountOfEverytime() {
        return this.countOfEverytime;
    }

    public void setCountOfEverytime(boolean countOfEverytime) {
        this.countOfEverytime = countOfEverytime;
    }

    public int getStartIndex() {
        return ((getCurrentPageNum() - 1) * getRowsOfPage());
    }
}