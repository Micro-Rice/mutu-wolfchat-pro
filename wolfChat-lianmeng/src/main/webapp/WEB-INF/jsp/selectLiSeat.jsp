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
<script src="./js/fastclick.js"></script>
<script src="./js/tool.js"></script>
<script type="text/javascript">
$(function (){
	FastClick.attach(document.body);
	var userInfo = '${userInfo}';
	var preInfo = '${preInfo}'
	var rMsg = "${rMsg}";
	var room = '${rooms}';
	if (!!userInfo) {
		userInfo = eval("("+userInfo+")");
	}
	if (!!preInfo) {
		preInfo = eval("("+preInfo+")");
	}
	if (!!room) {
		room = eval("("+room+")");
		$("#room").picker({
			title : "请选择您的房间号",
			cols : [{
				textAlign: 'center',
				values:room,
			}],
		});
	}
	var seats=new Array("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
	$("#seat").picker({
		title : "请选择您的座位号",
		cols : [{
			textAlign: 'center',
			values:seats,
		}],
	});
	var pathImage = "<%=basePath%>images/mutu.jpg";
	var $img = '<img style="width:120px;height:120px" src="'+pathImage+'"/>';
	$("#circleImg").append($img);
	if (!!rMsg) {
		$.alert("系统繁忙,请稍后再试!");
	} else {
		if (!!userInfo) {
			pathImage = userInfo.openImg;
			$img = '<img style="width:120px;height:120px" src="'+pathImage+'"/>';
			$("#circleImg").empty();
			$("#circleImg").append($img);
			var playerId = userInfo.playerId;
			$("#playerID").val(playerId);
			//与银豹会员号绑定,如果自己随机生成的会员号,则不需要.
			if (!!preInfo) {
					var roomId = preInfo.roomId;
					var seat = preInfo.seatId;
					$("#room").val(roomId);
					$("#seat").val(seat);
					$("#room").attr("disabled","disabled");
					$("#seat").attr("disabled","disabled");
					$.confirm("您已选过座位,是否需要重新选座?",function(){
						$("#room").removeAttr("disabled");
						$("#seat").removeAttr("disabled");
					});
			}			
		} else {
			$.alert("系统繁忙,请稍后再试!");
		}
	}
	
	$("#circleImg img").error(function(){
		$("#circleImg img").attr("src","<%=basePath%>images/mutu.jpg");
	});
	
	$("#loginBtn").click(function(){
		var openidEncode;
		if (!!userInfo) {
			var openid = userInfo.openId;
			openidEncode = base64encode(openid);			
		}
		var roomId = $("#room").val();
		var seat = $("#seat").val();
		var r = base64encode(utf16to8(roomId));
		var s = base64encode(seat);
		var pdata = {
				"pq" : openidEncode,
				"rz"  : r,
				"sw" : s,		
		};
		$.ajax({
			url:"<%=basePath%>gotoLiSelect",
			type:"GET",
			cache : false,
			//设置同步，避免和自动刷新冲突导致数据不同步
			async: false,
			data : pdata,
			success: function(data){
				if ("success" ==  data.message) {
					$.alert("选座成功!",function() {
						var r = base64encode((roomId));
						var url = "showResp?rz="+roomId+"&sw="+seat;
						window.location.href = url;
					});
				} else {
					$.alert("系统繁忙,请稍后再试!");
				} 
			},
			error: function(){
				$.alert("系统繁忙,请稍后再试!");
			}
		});			
	});	

	$("#userform").on("focus",".weui-input",function(){
		clearErrMsg($(this));
	});
	

	function validInput(id,reg,info) {
		var value = $("#"+ id).val();
		var sign = reg.test(value);
		if(!sign || +value > 20) {
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
	function checkSeat() {
		var id = "seat";
		var info = "请输入1-20座位号!";
		var reg = /^[0-9]{1,2}$/;
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
	<form id="userform">
		<div class="weui-cells weui-cells_form">
			<div class="weui-cell">
				<div class="weui-cell_hd">
					<label class="weui-label">玩家ID：</label>
				</div>
				<div class="weui-cell_bd">
					<input id="playerID" tabindex="0" class="weui-input" type="text" disabled=disabled>
				</div>				
			</div>
			<div class="weui-cell">
				<div class="weui-cell_hd">
					<label class="weui-label">房间名：</label>
				</div>
				<div class="weui-cell_bd">
					<input id="room" tabindex="1" class="weui-input" type="text">
				</div>				
			</div>
			<div class="weui-cell">
				<div class="weui-cell_hd">
					<label class="weui-label">座位号：</label>
				</div>
				<div class="weui-cell_bd weui-cell_primary">
					<input id="seat" tabindex="2" class="weui-input" type="text">
				</div>
				<div class="weui-cell__ft">
                     <i class="weui-icon-warn" id="seatErr"></i>
                     <i class="weui-icon-success" id="seatSuc" style="display:none"></i>
                 </div>
			</div>		   
		</div>
		<div id="errorMsg" class="weui-cells__tips" style="text-align:center;color:red;"></div>
		<div style="padding:15px;">
			<div class="weui-btn weui-btn_primary" id="loginBtn">确 定</div>			
		</div>		
	</form>
    <div class="weui-footer weui-footer_fixed-bottom">
      	<p class="weui-footer__text">Copyright ? 北京六维世纪文化传播有限公司</p>
    </div>
</body>
</html>