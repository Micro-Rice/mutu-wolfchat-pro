package com.test.wolfkill;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.test.service.IWolfKillServive;
@ContextConfiguration({"/resources/spring.xml"})
public class updateMemInfoTest {
	
	@Autowired
    private IWolfKillServive wolfKillService;
	
	@Test
	
	public void updateUseTest() {
		String msg = "小米-003-1-1-0-0_青青-002-1-1-1-1";
		wolfKillService.updataUserInfo(msg);
	}
	
}
