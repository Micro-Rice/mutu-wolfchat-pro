<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>会员管理界面</title>
<link rel="shortcut icon" href="./images/favicon.ico"/>
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/jAlert-v3.css" rel="stylesheet">
<link href="./css/signin.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/jAlert-v3.min.js"></script>
<script type="text/javascript" src="./js/jAlert-functions.min.js"></script>
<script type="text/javascript" src="./js/tool.js"></script>
<script type="text/javascript">
$(function(){
	$("#loginBtn").click(function(){
/* 		if ($("#memName").val() == "" || $("#pass").val() == "") {
			return false;
		} */
		var param = {
				memName : $("#memName").val(),
				password : $("#pass").val()
		};
		$.ajax({
			url:"<%=basePath%>vertifyMem",
			type:"post",
			cache : false,
			//设置同步，避免和自动刷新冲突导致数据不同步
			async: false,
			data : param,
			success: function(data){				
				var msg = data.message.split("|")[1];
				var memName = data.memName;
				var memUid = data.mUid;
				if (data.message.indexOf("success") > -1) {
					var memNameEncode = base64encode(memName);
					var memUidEncode = base64encode(memUid);
					var url = "forwardMemInfo?mna=" + memNameEncode + "&uid=" + memUidEncode;
					window.location.href = url;
				} else {
					$.jAlert({
						'title': 'ERROR',
						'content': msg,
					    'theme': 'red',
					    'replaceOtherAlerts' : 'true',
					    'size': 'sm',
					});
				}	
			},
			error: function(){
				$.jAlert({
					'title': 'ERROR',
					'content': '登录失败，请重试!',
				    'theme': 'red',
				    'replaceOtherAlerts' : 'true',
				    'size': 'sm',
				});
			},
		});
	});
	
	 $("body").keyup(function () {  
         if (event.which == 13){  
             $("#loginBtn").trigger("click");  
         }  
     }); 
});
</script>
</head>
<body>
<div style="margin:0 auto;">
<div class="signin">
  <div class="signin-head"><img src="./images/mutu.jpg" width="120px" height="120px"class="img-circle"></div>
  <form class="form-signin">
    <input type="text" class="form-control" id="memName" placeholder="用户名" required autofocus />
    <input type="password" class="form-control" id="pass" placeholder="密码" required />
    <input class="btn btn-lg btn-warning btn-block"  id="loginBtn" type="button" value="登录"/>
  </form>
</div>
</div>
</body>
</html>