package com.test.service;

import java.io.IOException;
import java.util.List;

import com.pospal.vo.ImageResponseData;
import com.pospal.vo.PostPointParameter;

public interface IPospalService {
	/**
	 * 获得分页会员信息
	 */
	public List<ImageResponseData>  getMemInfoFormPospal()throws IOException;
	/**
	 * 更新会员积分信息
	 */
	public String updatePospalPoint(List<PostPointParameter> points)throws IOException;
}
