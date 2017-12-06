package com.test.wolfChat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mutuChat.service.IWolfKillServive;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml","classpath:spring-mvc.xml","classpath:spring-hibernate.xml"})
public class UpdateMemInfoTest {
	
	
	@Autowired
    private IWolfKillServive wolfKillService;
	
	@Test
	
	public void updateUseTest() {
		String msg = "[{\"achieve\":0,\"camp\":1,\"id\":\"336\",\"mvp\":0,\"name\":\"??\",\"result\":1,\"role\":1,\"seat\":1},{\"achieve\":0,\"camp\":0,\"id\":\"946\",\"mvp\":0,\"name\":\"Jeffery\",\"result\":2,\"role\":2,\"seat\":2},{\"achieve\":0,\"camp\":0,\"id\":\"577\",\"mvp\":0,\"name\":\"??\",\"result\":2,\"role\":2,\"seat\":3},{\"achieve\":0,\"camp\":0,\"id\":\"600\",\"mvp\":0,\"name\":\"??\",\"result\":2,\"role\":2,\"seat\":4},{\"achieve\":1,\"camp\":1,\"id\":\"123\",\"mvp\":0,\"name\":\"??\",\"result\":1,\"role\":1,\"seat\":5},{\"achieve\":0,\"camp\":1,\"id\":\"125\",\"mvp\":0,\"name\":\"??\",\"result\":1,\"role\":1,\"seat\":6},{\"achieve\":0,\"camp\":0,\"id\":\"126\",\"mvp\":0,\"name\":\"Allen\",\"result\":2,\"role\":17,\"seat\":7},{\"achieve\":0,\"camp\":1,\"id\":\"94\",\"mvp\":0,\"name\":\"?????\",\"result\":1,\"role\":1,\"seat\":8},{\"achieve\":0,\"camp\":0,\"id\":\"1041\",\"mvp\":0,\"name\":\"?\",\"result\":2,\"role\":2,\"seat\":9},{\"achieve\":14,\"camp\":0,\"id\":\"491\",\"mvp\":0,\"name\":\"6?\",\"result\":2,\"role\":4,\"seat\":10}]";
		String message = wolfKillService.updataUserInfo(msg);
		System.out.println(msg);
	}
}
