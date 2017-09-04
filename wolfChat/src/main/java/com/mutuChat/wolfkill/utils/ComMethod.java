package com.mutuChat.wolfkill.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ComMethod {
	
	private static Logger logger = Logger.getLogger(ComMethod.class);
	
	public String getIpAddr(HttpServletRequest request) {      
        String ip = request.getHeader("x-forwarded-for");      
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
           ip = request.getHeader("Proxy-Client-IP");      
       }      
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
           ip = request.getHeader("WL-Proxy-Client-IP");      
        }      
      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getRemoteAddr();      
       }      
      return ip;      
	}
	public String getNow() {      
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
       return df.format(day);      
	}
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /** 
     * getProperties:读取配置文件 <br/>
     * 
     * @param filename
     *            配置文件名
     * @return 配置 <br/>
     */
    public Properties getProperties(String filename) {
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream(filename);
        try {
            props.load(in);
        } catch (IOException e) {
            props = new Properties();
        } finally {
            if(in != null){
                SafeClose2(in);
            }
        }

        return props;
    }

    /**
     * 
     * SafeClose2:安全关闭流 <br/>
     * 
     * @param is
     *            流 <br/>
     */
    public static void SafeClose2(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("InputStream close Error."+e);
            }
        }
    }
    
    public String convertStreamToString(InputStream is) {   
    	   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
    	        StringBuilder sb = new StringBuilder();   
    	        String line = null;   
    	        try {   
    	            while ((line = reader.readLine()) != null) {   
    	                sb.append(line);   
    	            }   
    	        } catch (IOException e) {   
    	            e.printStackTrace();   
    	        } finally {   
    	            try {   
    	                is.close();   
    	            } catch (IOException e) {   
    	                e.printStackTrace();   
    	            }   
    	        }   
    	        return sb.toString();   
    	   }   
}
