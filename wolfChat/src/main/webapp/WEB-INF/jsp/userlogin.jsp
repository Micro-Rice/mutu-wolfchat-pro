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
<style type="text/css">
.circle-img {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    overflow: hidden;
    border: 4px solid rgba(191, 191, 191, .24);
}
</style>
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
<script type="text/javascript">
$(function (){
	
	var userInfo = '${userInfo}';
	var rMsg = "${rMsg}";
	var openid = "${openid}";
	if (!!userInfo) {
		userInfo = eval("("+userInfo+")");
	}	
	var pathImage = "<%=basePath%>images/mutu.jpg";
	var $img = '<img style="width:120px;height:120px" src="'+pathImage+'"/>';
	$("#circleImg").append($img);
	
	$("#circleImg img").error(function(){
		$("#circleImg img").attr("src","<%=basePath%>images/mutu.jpg");
	});
	
	if (openid == "") {
		$.alert("系统繁忙,请稍后再试!");
	} else {
		if (!!rMsg) {
			$.alert("系统繁忙,请稍后再试!");
		} else {
			if (!!userInfo) {
				pathImage = userInfo.openImg;
				$img = '<img style="width:120px;height:120px" src="'+pathImage+'"/>';
				$("#circleImg").empty();
				$("#circleImg").append($img);
				if (!!userInfo.playerId && !!userInfo.playerPhone) {
					$("#username").val(userInfo.playerId);
					$("#username").attr("disabled","disabled");
					$("#phone").val(userInfo.playerPhone);
					$("#phone").attr("disabled","disabled");
					$.alert("已绑定会员卡号!",function(){
						window.location.href = "http://www.mutuclub.com";
					})
				}
			} else {
				$.alert("系统繁忙,请稍后再试!");
			}
		}
	}
		
	$("#loginBtn").click(function(){
		var name = $("#username").val();
		var phone = $("#phone").val();
		if (checkName() && checkPhone()) {
			var user = {
					"username" : name,
					"phone" : phone,
					"openid" : openid
			};
			$.ajax({
				url:"userBind",
				type:"GET",
				cache : false,
				//设置同步，避免和自动刷新冲突导致数据不同步
				async: false,
				data : user,
				success: function(data){
					if ("error1" ==  data.loginResult) {
						$.alert("会员号手机信息有误!");
					} else if ("error2" ==  data.loginResult) {
						$.alert("会员号不存在,请先注册!");
					} else if ("error3" ==  data.loginResult) {
						$.alert("绑定失败,请稍后再试!");
					} else {
						$.alert("绑定会员卡成功!",function(){
							window.location.href = "http://www.mutuclub.com";
						});						
					}
				},
				error: function(){
					$.alert("系统繁忙,请稍后再试!");
				}
			});
		}			
	});	
	
	$("#findusername").click(function(){
		$.alert("请联系MUTU管理员,联系电话010-56123556!");
	});
	$("#userform").on("focus",".weui-input",function(){
		clearErrMsg($(this));
	});
	
	function validInput(id,reg,info) {
		var value = $("#"+ id).val();
		var sign = reg.test(value);
		if(!sign) {
			$("#"+id+"Suc").css("display","none");
			$("#"+id+"Err").css("display","inline-block");
			$("#errorMsg").empty();
			$("#errorMsg").append(info);
			return false;
		} else {
			$("#errorMsg").empty();
			$("#"+id+"Err").css("display","none");
			$("#"+id+"Suc").css("display","inline-block");
			return true;
		}
	}
	function checkName() {
		var id = "username";
		var reg = /^[1-9]\d*$/;
		var info = "请输入数字!"
		//var reg = /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/;
		//var info = "账号格式错误！请以字母开头，输入5-16位字符，只允许字母数字下划线";
		var flag = validInput(id,reg,info);
		return flag;
	}
	function checkPhone() {
		var id = "phone";
		var reg = /^[1-9]\d{10}$/;
		var info = "请输入11位手机号码!";
		var flag = validInput(id,reg,info);
		return flag;
	}
	function checkPassword() {
		var id = "password";
		var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/;
		var info = "密码格式错误! 请输入6-16位字母与数字组合";
		var flag = validInput(id,reg,info);
		return flag;
	}
	function clearErrMsg($input) {
		var id = $input.attr("id");
		$("#"+id+"Err").css("display","none");
		$("#"+id+"Suc").css("display","none");
		$("#errorMsg").empty();
	}
});
</script>
<title>Mutu狼人杀</title>
</head>
<body>
	<div style="margin:40px auto 80px auto;" class="circle-img" id="circleImg"></div>
	<form id="userform" action="doMemberInfo" method="post" id="loginform" name="loginform">
		<div class="weui-cells weui-cells_form">
			<div class="weui-cell">
				<div class="weui-cell_hd">
					<label class="weui-label">会员号：</label>
				</div>
				<div class="weui-cell_bd weui-cell_primary">
					<input id="username" name="username" tabindex="1" class="weui-input" type="text" placeholder="请输入会员号">
				</div>
				<div class="weui-cell__ft">
                     <i class="weui-icon-warn" id="usernameErr"></i>
                     <i class="weui-icon-success" id="usernameSuc" style="display:none"></i>
                 </div>
			</div>
			<div class="weui-cell">
				<div class="weui-cell_hd">
					<label class="weui-label">手机号：</label>
				</div>
				<div class="weui-cell_bd weui-cell_primary">
					<input id="phone" name="phone" tabindex="2" class="weui-input" type="text" placeholder="请输入手机号">
				</div>
				<div class="weui-cell__ft">
                     <i class="weui-icon-warn" id="phoneErr"></i>
                     <i class="weui-icon-success" id="phoneSuc" style="display:none"></i>
                 </div>
			</div>		   
		</div>
		<div id="errorMsg" class="weui-cells__tips" style="text-align:center;color:red;"></div>
		<div style="padding:15px;">
			<div class="weui-btn weui-btn_primary" id="loginBtn">绑定会员卡</div>		
		</div>
		<div id="findusername" class="weui-cells__tips" style="text-align:right;color:blue;cursor:pointer;">忘记会员号？</div>		
	</form>
</body>
</html>