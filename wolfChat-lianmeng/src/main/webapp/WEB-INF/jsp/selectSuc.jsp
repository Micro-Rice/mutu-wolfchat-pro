<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh_cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache,must-revalidate">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="format-detection" content="telephone=no, address=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="./css/weui.css">
<link rel="stylesheet" href="./css/jquery-weui.css">
<script src="./js/jquery-1.11.0.js"></script>
<script src="./js/jquery-weui.js"></script>
<script src="./js/tool.js"></script>
<title>Mutu狼人杀</title>
<script type="text/javascript">
$(function(){
	var room = "${room}";
	var seat = "${seat}";
	var msg = "您的房间是" +room+ "<br/>座位号是" + seat;
	$("#backMsg").append(msg);
});
</script>
</head>
<body>
<div class="weui-msg" style="margin-top:100px">
  <div class="weui-msg__icon-area"><i class="weui-icon-success weui-icon_msg"></i></div>
  <div class="weui-msg__text-area">
    <h2 id="backMsg" class="weui-msg__title"></h2>
  </div>
   <div class="weui-footer weui-footer_fixed-bottom">
      <p class="weui-footer__text">Copyright © 北京六维世纪文化传播有限公司</p>
   </div>
</div>
</body>
</html>