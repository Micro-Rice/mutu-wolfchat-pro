package com.mutuChat.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pospal.main.PospalApiSerivce;
import com.pospal.utils.http.ResponseData;
import com.pospal.utils.tools.JsonConvertor;
import com.pospal.vo.ImageResponseData;
import com.pospal.vo.PostMemParameter;
import com.pospal.vo.PostPointParameter;
import com.pospal.vo.UserCertificate;
import com.mutuChat.service.IPospalService;
@Service("pospalService")
public class PospalServiceImpl implements IPospalService{
	// 用你的凭证信息替换以下三个变量的内容
	public static final String urlPreFix = "https://area8-win.pospal.cn:443";
	public static final String appId = "EB4D27D7BF20D886F29FD7BB4B6F7CF5";
	public static final String appKey = "335275524016846949";
	@Override
	public List<ImageResponseData> getMemInfoFormPospal() throws IOException {
		
		List<ImageResponseData> resultInfos = new ArrayList<ImageResponseData>();
		
					 
		String urlString = urlPreFix+"/pospal-api2/openapi/v1/customerOpenApi/queryCustomerPages";
		//String urlString = "http://localhost:8080/pospal-api2/openapi/v1/productOpenApi/queryProductImagePages";
		// 银豹收银系统开放接口的访问凭证
		UserCertificate certificate = new UserCertificate(appId, appKey);
					
					
					//设置代码服务器，java发送的数需要手动设置代理,fiddler才能抓到包。
					//HttpUtil.setCustomProxy("127.0.0.1", 8888);
					
					//是否需要进行下一页数据的查询。
					boolean queryNextPage = true;
					//Date now = new Date();
					//String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now);
					PostMemParameter requestData = new PostMemParameter();				
					do {
						// 发送请求参数并从服务端取得返回的数据
						ResponseData sendRequestData = PospalApiSerivce.sendRequestData(urlString, requestData, certificate);
						String responseContent = sendRequestData.getResponseContent();
						System.out.println(responseContent);
						// 将服务端返回的数据转换成java 实例来处理
						ImageResponseData imageResponseData = JsonConvertor.fromJson(responseContent, ImageResponseData.class);
						
						// 是否需要进行下一页数据的查询。
						// wantQuerySize > realQuerySize说明数据已经全部取出下来了。
						if (imageResponseData.getData() == null) {
							resultInfos.add(imageResponseData);
							return resultInfos;
						}
						int wantQuerySize = imageResponseData.getData().getPageSize();
						int realQuerySize = imageResponseData.getData().getResult().size();
						queryNextPage = !(wantQuerySize > realQuerySize);
						
						//System.out.println("wantQuerySize="+wantQuerySize+",realQuerySize="+realQuerySize);
						//System.out.println(responseContent);
						
						// 设置查询下一页所需要的回传参数 postBackParameter
						requestData.setPostBackParameter(imageResponseData.getData().getPostBackParameter());
						
						resultInfos.add(imageResponseData);
						
					} while(queryNextPage);
		return resultInfos;
	}

	@Override
	public String updatePospalPoint(List<PostPointParameter> points) throws IOException {
		String urlString = urlPreFix+"/pospal-api2/openapi/v1/customerOpenApi/updateBalancePointByIncrement";
		UserCertificate certificate = new UserCertificate(appId, appKey);
		Date now = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now);
		String message = "success";
		int size = points.size();
		if (size > 150) {
			size = 150;
		}
		for (int i = 0; i < size; i++) {
			PostPointParameter requestData = points.get(i);
			requestData.setDataChangeTime(dateStr);
			// 发送请求参数并从服务端取得返回的数据
			ResponseData sendRequestData = PospalApiSerivce.sendRequestData(urlString, requestData, certificate);
			String responseContent = sendRequestData.getResponseContent();
			ImageResponseData imageResponseData = JsonConvertor.fromJson(responseContent, ImageResponseData.class);			
			if (!imageResponseData.isSuccess()) {
				String strMsg = "";
				if (imageResponseData.getMessages() != null) {
					for (int j = 0; j < imageResponseData.getMessages().length; j++) {
						strMsg = strMsg + imageResponseData.getMessages()[j];
					}
				}
				message = strMsg;
				break;
			}			
		}
		return message;
	}
}
