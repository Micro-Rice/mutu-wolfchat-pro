<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css">
.backgroundBar {
	width:100%;
	height:160px;
	background-image:url(<%=basePath%>images/back_new.jpg);
	background-repeat:repeat-x;
	background-position:center;
}
.xing-img {
	height:20px;
	vertical-align: middle;
    margin-right: 3px;
}
.nav-head li{border: 0;}
#top-nav .nav-right{display: table;float: right;width: 320px;height: 46px;font-size: 12px;}
#top-nav .nav-right *{box-sizing: border-box;}
#top-nav .nav-right .account-wrap{display: table-cell;vertical-align: middle;}
#top-nav .nav-right .account-wrap .btn{
			    float: right;
			    width: 70px;
			    text-align: center;
			    border: 0;
			    border-radius: 0;
			    cursor: pointer;
			    background: transparent;
			    box-shadow: none;
			    text-shadow: none;
			    padding: 0;
			    color: #d2d2d2;
			    line-height: 46px;
			    -webkit-transition: all 300ms ease;
			    -moz-transition: all 300ms ease;
			    transition: all 300ms ease;
			}
#top-nav .nav-right .account-wrap .btn:hover{color: #fff;background: #913232;}
#top-nav .nav-right .account-wrap .btn.sign-in{border-color: transparent;}
#top-nav .nav-right .account-wrap .btn.login{border-color: #913232;}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Mutu Club</title>
<link rel="shortcut icon" href="./images/favicon.ico"/>
<link href="http://cdn.maxjia.com/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=basePath%>css/wolfKillMax.css" rel="stylesheet">
<link href="http://v3.jiathis.com/code/css/jiathis_share.css" rel="stylesheet">
<script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/sorttable.js"></script>
<script type="text/javascript" src="./js/tool.js"></script>
<script type="text/javascript">
var matchNum = "0";
$(function (){	
	getLocalMatchData(matchNum);		
	$("#mainTable").on('click','.infoTr',function(){
		var id = $(this).attr("id");
		var num = id.split("_")[1];
		var palyerName = $("#uid_"+num).text();
		var sequence = $("#squence_"+num).text();		
		var palyerNameEncode = base64encode(palyerName);
		var sequenceEncode = base64encode(sequence);
		var matchNumEncode = base64encode(matchNum);
		var url = "forwardPerInfo?playerName=" + palyerNameEncode + "&seq=" + sequenceEncode + "&mnum=" + matchNumEncode;
		window.location.href = url;
	});
	$("#seconudbarOuter").on('click','.matchLi',function(){
		if ($(this).hasClass("active-s")) {
			return;
		}
		$("#seconudbarOuter").find("li").each(function() {
			if ($(this).hasClass("active-s")) {
				$(this).removeClass("active-s");
			}
		});
		var id = $(this).attr("id");
		matchNum = id.split("_")[1];	
		getLocalMatchData(matchNum);
		$(this).addClass("active-s");
	});
});
function getLocalMatchData(matchNum) {
	$.ajax({
		url:"<%=basePath%>getWolfkillData",
		type:"GET",
		cache : false,
		//设置同步，避免和自动刷新冲突导致数据不同步
		async: false,
		data: {matchNum : matchNum}	,
		success: function(data){
			var playerInfoObj = eval("("+data.playerInfo+")");
			$("#mainTable tbody").empty();
			if (playerInfoObj.length != 0) {
				createInfoTable(playerInfoObj);
			}			
		},
		error: function(){
			createFailerInfo();
		}
	});
	$("#mainTable img").each(function(i,e){
		$(e).error(function(){
			$(e).attr("src","<%=basePath%>images/mutu.jpg");
		});
	});
}
function findMax(dataObj,mode) {
	var length = dataObj.length;
	var max;
	if (mode == "achiveNum") {
		max = dataObj[0].pAchiveNum;
		for (var i = 1; i < length; i++) {
			var tg = dataObj[i].pAchiveNum;
			if (+max < +tg) {
				max = tg;
			}
		}
	} else if (mode == "mvp") {
		max = dataObj[0].mvp;
		for (var i = 1; i < length; i++) {
			var tg = dataObj[i].mvp;
			if (+max < +tg) {
				max = tg;
			}
		}
	} else if (mode == "levelnum") {
		max = dataObj[0].pLevelNum;
		for (var i = 1; i < length; i++) {
			var tg = dataObj[i].pLevelNum;
			if (+max < +tg) {
				max = tg;
			}
		}
	}
	return max;
}
function createInfoTable(dataObj) {	
	var length = dataObj.length;
	var maxAchive = findMax(dataObj,"achiveNum");
	var maxMvp = findMax(dataObj,"mvp");
	var maxLevel = findMax(dataObj,"levelnum");
	for (var i = 0; i < length; i++) {
		var killdata = dataObj[i];
		var $tr = $('<tr id=Ftr_'+i+' class="infoTr"></tr>');
		
		var temp = '<td id=squence_'+i+'>'+killdata.pOrder+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		var pathImage;
		if (!!killdata.pTag) {
			pathImage = killdata.pTag;
		} else {
			pathImage = "<%=basePath%>images/mutu.jpg";
		}
		var temp='<td id=name_'+i+'><a href="javascript:void(0);"><img class="match-avatars-img" style="height:36px;width:36px;margin: 4px;" src="'+pathImage+'"/>'
		+''+killdata.pName+'</a></td>';
		$td = $(temp);
		$td.appendTo($tr);
				
		temp = '<td><div style="font-weight:bold">'+killdata.pLevel+'</div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		var percent = ((killdata.pLevelNum/maxLevel)*100).toFixed(2);
		temp = '<td><div>'+killdata.pLevelNum+'</div><div class="segment segment-gold" style="width:'+percent+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
						
		var wrate = (killdata.pWrate*100).toFixed(2);
		temp = '<td><div>'+wrate+'%</div><div class="segment segment-green" style="width:'+wrate+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		percent = ((killdata.mvp/maxMvp)*100).toFixed(2);
		temp = '<td><div>'+killdata.mvp+'</div><div class="segment segment-red" style="width:'+percent+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp = '<td></td>';
		$td = $(temp);
		var str = '<img class="xing-img" src="<%=basePath%>images/xingxing.png"> x ' + killdata.pAchiveNum;
		$td.append(str);	
		/* percent = ((killdata[1]/maxAchive)*100).toFixed(2);
		temp = '<td><div>'+killdata[1]+'</div><div class="segment segment-white" style="width:'+percent+'%;"></div></td>';
		$td = $(temp); */
		$td.appendTo($tr);
			
		
		temp = '<td style="display:none" id="uid_'+i+'">'+killdata.playerId+'</td>';
		$td = $(temp);
		$td.appendTo($tr);	
		
		$("#mainTable tbody").append($tr);
	}	
}
function createFailerInfo() {
	$("#maincontainer").empty();
	$("#maincontainer").append("<div class='stitle'>读取数据错误，请刷新！</div>");
}
function validAndOrder() {
	var text = $("#player")();
	if (/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/g.test(text)) {
		return true;
	} else {
		return false;
	}
}
</script>
</head>
<body>
	<div class="maxtopbar">
		<div class="nav-head">
		<div id="top-nav" class="nav-container">
			<a href="http://www.mutuclub.com">
				<img style="text-align:center"src="<%=basePath%>images/logo1.jpg">
			</a>
			<!-- <a href="#"><li>首页</li></a>
			<a href="#"><li>数据</li></a>
			<a href="#"><li>职业</li></a>
			<a href="#"><li class="h-active">玩家</li></a>
			<a href="#"><li>天梯</li></a>
			<a href="#"><li>直播</li></a>
			<a href="#"><li>商城</li></a> -->
			<!-- <a href="javascript:void(0);"><li class="h-active">玩家</li></a> -->
			<!-- <div class="nav-right">
				<div class="account-wrap">
					<div class="not-login-wrap">
						<a href="javascript:void(0);" class="btn sign-in">注册</a>
						<a href="javascript:void(0);" class="btn login">登录</a>
					</div>
				</div>
			</div> -->
		</div>
		</div>
	</div>
	<div class="maxtopmainbar">
		<div class="backgroundBar"></div>
		 <!-- <div style="position: absolute; top:19px;width:100%;display:table;height:130px">
			<div style="margin-left:auto;margin-right:auto;position:relative;text-align:center;width:1080px;padding-left:20px;">
				<div style="position: absolute;top:40px;left:20px;text-align: left;font-size:28px;color:white;font-weight: 500;width:450px;text-shadow:2px 3px 2px white;font-family:Arial;">MUTU</div>
			</div>
		</div> -->
		<div id="search-box">
			<form action="search" method="get" onsubmit="return validAndOrder()">
				<div class="large-search">
					<div style="margin-left: auto;margin-right:auto;position: relative;width: 341px;">
						<input class="large-search-text" type="text" id="player" name="player" placeholder="请输入昵称或会员号">
						<input type="submit" value="Go" class="large-search-btn ">
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="main-shadow-box">
		<div class="main-container" style="margin-top:0px;margin-bottom:1px;padding-bottom:1px;">
			 <div id= "seconudbarOuter" class="secoundbar-outer">
				<ul class="secoundbar" style="margin-top: 20px;margin-bottom: 0px">
					<li id = "mutuS_1" class="matchLi" style="cursor:pointer;">
						MUTU S1赛季
					</li>
					<li id = "mutuS_2" class="matchLi" style="cursor:pointer;">
						MUTU S2赛季
					</li>
					<li  id = "mutuS_3" class="matchLi" style="cursor:pointer;">
						MUTU S3赛季
					</li>
					<li  id = "mutuS_0" class="matchLi active-s" style="cursor:pointer;">
						MUTU S4赛季
					</li>
				</ul>
			</div> 
			<div id= "maincontainer" class="container" style="width:1000px;">
				<table id="mainTable" class="table table-hover table-striped table-list table-thead-left" style="width: 95%; margin-left: auto;margin-right: auto;margin-bottom: 20px;margin-top: 10px;">
					<thead>
						<tr>
							<th style="width:5%">名次</th>
							<th style="width:25%">玩家ID</th>
							<th style="width:15%">玩家等级</th>	
							<th style="width:15%">玩家积分</th>						
							<th style="width:15%">总胜率</th>							
							<th style="width:10%">MVP</th>	
							<th style="width:15%">成就点数</th>					
						</tr>
					</thead>
					<tbody class="table-player-detail">						
					</tbody>
				</table>
			</div>
		</div>
		<div style="width:100%;min-width:1100px;text-align: center; display: table;position: absolute;">
			<div style="background-color: rgb(0,0,0);padding-top: 15px;padding-bottom: 15px;position: relative;">
				<!-- <div style="width:1100px;margin: 0 auto;line-height: 20px;position: relative;height: 30px;display:none">
					<span style="line-height: 30px;position: absolute;right:25px;top:0px;">
						<img src="http://cdn.maxjia.com/image/zh-cn.gif" style="margin-right: 3px;height: 15px;">
						<a href="javascript:void(0);"><img src="http://cdn.maxjia.com/image/en.gif" style="margin-right: 3px;"></a>
						<a href="javascript:void(0);"><img src="http://cdn.maxjia.com/image/ru.gif" style="margin-right: 3px;"></a>
						<a href="javascript:void(0);"><img src="http://cdn.maxjia.com/image/ko.gif" style="margin-right: 3px;"></a>
					</span>
				</div> -->
			</div>
			<div class="footbar" style="padding-bottom:10px;">
				<div style="position: relative;width: 1100px;margin: 0 auto;">
					<img class="max_logo breath" style="width:40px;"src="<%=basePath%>images/logo_01.png">
					<div style="position: absolute;left:0px;bottom:10px;">
						<div style="float:left;margin-left: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-envelope-o" style="font-size: 16px;padding-top: 1px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-heartbeat" style="font-size: 18px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-qq" style="font-size: 16px;padding-top: 2px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-question-circle" style="font-size: 16px;padding-top: 2px;"></i>
							</a>
						</div>
						<!-- <div style="float:left;margin-left: 20px">wolfKillmax.com</div>
						<div style="float:left;margin-left: 20px">友情链接</div> -->
					</div>
					<div style="position: absolute;right:0px;bottom:10px;">
						<div style="float:right;margin-right: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-bar-chart" style="font-size: 18px"></i>
							</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-weibo" style="font-size: 18px"></i>
							</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href="javascript:void(0);">
								<i class="fa fa-tencent-weibo" style="font-size: 18px"></i>
							</a>
						</div>
						<!-- <div style="float:right;margin-right: 20px">
							<a href="javascript:void(0);">录入数据</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href="javascript:void(0);">加入我们</a>
						</div> -->
					</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>