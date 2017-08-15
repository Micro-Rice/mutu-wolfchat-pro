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
$(function(){
	var playerInfos = '${jsonChatPlayer}';
	var playerId = "${playerId}";
	playerInfos = eval("("+playerInfos+")");
	console.log(playerInfos);
})
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
					<li  id = "mutuS_0" class="matchLi active-s" style="cursor:pointer;">
						MUTU S2赛季
					</li>
				</ul>
			</div> 
			<div id= "maincontainer" class="container" style="width:1000px;">
				<div style="margin:0px auto 5px auto;" class="circle-img" id="circleImg"></div>
				<div id="playerid">账号ID:</div>
				<div id="playerlevel">段位:</div>
				<div id="playertag">个性签名:</div>
				<div id="levelnum">实时积分:</div>
				<div id="levelorder">实时排名:</div>				
				<div id="levelmax">最高积分:</div>
				<div id="ordermax">最高排名</div>
				<div id="wrate">综合胜率:</div>
				<div id="mnum">比赛场次:</div>
				<div id="mvpnum">MVP点数:</div>
				<div id="achivenum">成就点数</div>
				<div id="achivede">成就详情</div>
				<table id="groupTable">
					<thead>
						<tr>
							<th style="width:34%">阵营</th>
							<th style="width:33%">比赛次数</th>
							<th style="width:33%">胜率</th>				
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<table id="roleTable">
					<thead>
						<tr>
							<th>角色</th>
							<th>比赛次数</th>
							<th>比赛胜率</th>
							<th>比赛任务</th>
							<th>角色排名</th>
						</tr>					
					</thead>
					<tbody>
					</tbody>
				</table>
				<table id="hisTable">
					<thead>
						<tr>
							<th>角色</th>
							<th>胜负情况</th>
							<th>时间</th>
							<th>积分变化</th>
							<th>达成成就</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
</html>