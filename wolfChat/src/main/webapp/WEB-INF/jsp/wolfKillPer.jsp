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
<script type="text/javascript">
$(function (){
	var matchNum = "${matchNum}";
	var maxMatchNum = "${maxMatchNum}";
	var playerData = "${perDatas}";
	var seq = "${sequence}";
	var uid = "${uniqueId}";
	playerData = eval("("+playerData+")");
	if (playerData.length != 0) {
		var mainData = playerData[0].val;
		creatTablePer(mainData);
		$("#sequence").text(seq);
		if (playerData.length > 1) {
			creatTableInfo(playerData);
		}		
	} else {
		createFailerInfo();
	}
	$("#circleImg img").error(function(){
		$("#circleImg img").attr("src","<%=basePath%>images/mutu.jpg");
	});
	if (+matchNum > 0) {
		var str = "MUTU S"+matchNum+ "赛季";
		$("#perInfoBtn").html(str);
	} else {
		$("#perInfoBtn").html("MUTU S"+maxMatchNum+"赛季");
	}
	$("#roleTable").on('hover','.cjrole',function(){
		var roleName = $(this).attr("val");
		var cgName = getCgName(roleName);
		var $span = $('<span></span>');
		$span.append(cgName);
		$(this).parent().append($span);		
	});
	$("#roleTable").on('mouseleave','.cjrole',function(){		
		$(this).parent().find("span").each(function(){
			$(this).remove();
		});		
	});
});
function findMax(playerData) {
	var length = playerData.length;
	var max = playerData[1].val.split("-")[1];
	for (var i = 1; i < length; i++) {
		var tg = playerData[i].val.split("-")[1];
		if (+max < +tg) {
			max = tg;
		}
	}
	return max;
}
function creatImg(user,sequence) {
	var pathImage = "<%=basePath%>images/pic"+user+".jpg";
	var $img = '<img style="width:90px;" src="'+pathImage+'"/>'
	$("#circleImg").append($img);
}
function createFailerInfo() {
	$("#maincontainer").empty();
	$("#maincontainer").append("<div class='stitle'>没有该玩家的详细数据！</div>");
}
function creatTablePer(mainData) {	
	var name = mainData.split("-")[0];
	$("#nameSpan").text(name);
	var level = mainData.split("-")[11];
	$("#leveltext").text(level);
	var tg = mainData.split("-")[1];
	$("#sumNum").text(tg);
	var tr = mainData.split("-")[2];
	tr = (tr*100).toFixed(2)+"%";
	$("#winRatio").text(tr);
	var mvpNum = mainData.split("-")[9];
	$("#mvpNum").text(mvpNum);
	var levelNum = mainData.split("-")[12];
	$("#levelNum").text(levelNum);
	var maxLevelNum = mainData.split("-")[13];
	$("#maxLevelNum").text(maxLevelNum);
	var achiveNum = mainData.split("-")[14];
	$("#achiveNum").text(achiveNum);
	var urlImg =  mainData.split("-")[15];
	var pathImg;
	if (urlImg != "null") {
		pathImg = urlImg;
	} else {
		pathImg = "<%=basePath%>images/mutu.jpg";
	}
	var $img = '<img style="width:90px;height:90px" src="'+pathImg+'"/>'
	$("#circleImg").append($img);
	
	var pg = mainData.split("-")[3];
	var pr = mainData.split("-")[4];
	var wg = mainData.split("-")[5];
	var wr = mainData.split("-")[6];
	var og = mainData.split("-")[7];
	var or = mainData.split("-")[8];	
	var max = pg;
	if (+max < +wg) {
		max = wg;
	} 
	if (+max < +og) {
		max = og;
	}	
	var thobj=[];
	thobj.push("好人阵营");
	thobj.push("狼人阵营");
	thobj.push("其他阵营");
	var tgobj=[];
	tgobj.push(pg);
	tgobj.push(wg);
	tgobj.push(og);
	var trobj=[];
	trobj.push(pr);
	trobj.push(wr);
	trobj.push(or);
	for (var i = 0; i < thobj.length; i++) {
		var $tr = $('<tr id=Str_'+i+' class="SerTr"></tr>'); 
		var temp='<td><span>'+thobj[i]+'</span></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		var tempRate = ((tgobj[i]/max)*100).toFixed(2);
		temp =  '<td><div>'+tgobj[i]+'</div><div class="segment segment-white" style="width:'+tempRate+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		var rate = (trobj[i]*100).toFixed(2);
		temp = '<td><div>'+rate+'%</div><div class="segment segment-green" style="width:'+rate+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		$("#sideTable tbody").append($tr);
	}	
}
function creatTableInfo(playerData) {
	var length = playerData.length;
	var maxTotal = findMax(playerData);
	for (var i = 1; i < length; i++) {
		var roleName = playerData[i].val.split("-")[0];
		var displayName = getDisName(roleName);
		var cgName = getCgName(roleName);
		var total = playerData[i].val.split("-")[1];
		var rate = playerData[i].val.split("-")[2];
		var achiveFre = playerData[i].val.split("-")[3];
		var $tr = $('<tr id=Ftr_'+i+' class="perTr"></tr>'); 
		var temp='<td id=name_'+i+'><a href="javascript:void(0);"><img class="hero-img-list"src="<%=basePath%>images/'+roleName+'.jpg">'
		+''+displayName+'</a></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		var tempRate = ((total/maxTotal)*100).toFixed(2);
		temp = '<td><div>'+total+'</div><div class="segment segment-white" style="width:'+tempRate+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		rate = (rate*100).toFixed(2);
		temp = '<td><div>'+rate+'%</div><div class="segment segment-green" style="width:'+rate+'%;"></div></td>';
		$td = $(temp);
		$td.appendTo($tr);
				
		if (+achiveFre > 0 && !!cgName) {									
			temp = '<td id=cj_'+i+'><img class="hero-img-list cjrole"src="<%=basePath%>images/cj_'+roleName+'.png" val='+roleName+'></td>';
			//+''+cgName+'</td>';
			$td = $(temp);
			appendImg($td,+achiveFre);
		} else {
			temp = '<td></td>';
			$td = $(temp);
		}				
		$td.appendTo($tr);
		
		$("#roleTable tbody").append($tr);
	}	
}
function appendImg($td,freNum) {
	if (freNum >= 74) {
		var str = '<img class="xing-img" src="<%=basePath%>images/xingxing.png"> x ' + freNum;
		$td.append(str);		
	} else {
		var hg = parseInt(freNum/25);
		var hgadd = freNum%25;
		var zs = parseInt(hgadd/5);
		var zsadd = hgadd%5;
		var strImg = "";
		for (var i = 0; i < hg; i++) {
			strImg = strImg + '<img class="xing-img" src="<%=basePath%>images/huangguan.png">';
		}
		for (var i = 0; i < zs; i++) {
			strImg = strImg + '<img class="xing-img" src="<%=basePath%>images/zuanshi.png">';
		}
		for (var i = 0; i < zsadd; i++) {
			strImg = strImg + '<img class="xing-img" src="<%=basePath%>images/xingxing.png">';
		}
		$td.append(strImg);	
	}
}
function getCgName(name) {
	var cgName;
	if (name == "prophet") {
		cgName = "火眼金睛";
	} else if (name == "witch") {
		cgName = "药剂大师";
	} else if (name == "hunter") {
		cgName = "弹无虚发";
	} else if (name == "guard") {
		cgName = "坚不可摧";
	} else if (name == "cupid") {
		cgName = "爱神永恒";
	} else if (name == "walker") {
		cgName = "片甲不留";
	} else if (name == "whitewolf") {
		cgName = "邪恶领袖";
	} else if (name == "beautywolf") {
		cgName = "神之魅惑";
	} else if (name == "knight") {
		cgName = "正义审判";
	} else if (name == "blackman") {
		cgName = "等价交换";
	}
	return cgName;
}
function getDisName(name) {
	var disName;
	if (name == "prophet") {
		disName = "预言家";
	} else if (name == "witch") {
		disName = "女巫";
	} else if (name == "hunter") {
		disName = "猎人";
	} else if (name == "idiot") {
		disName = "白痴";
	} else if (name == "guard") {
		disName = "守卫";
	} else if (name == "silent") {
		disName = "禁言长老";
	} else if (name == "cupid") {
		disName = "丘比特";
	} else if (name == "walker") {
		disName = "潜行者";
	} else if (name == "villager") {
		disName = "村民";
	} else if (name == "wolf") {
		disName = "狼人";
	} else if (name == "whitewolf") {
		disName = "白狼王";
	} else if (name == "beautywolf") {
		disName = "狼美人";
	} else if (name == "knight") {
		disName = "圣光骑士";
	} else if (name == "devil") {
		disName = "恶魔";
	}
	return disName;
}
</script>
</head>
<body>
	<div class="maxtopbar">
		<div class="nav-head">
		<div id="top-nav" class="nav-container">
			<a href="http://www.mutuclub.com" style="margin-right:0px">
				<img style="text-align:center"src="<%=basePath%>images/logo1.jpg">
			</a>
			<!-- <a href="#"><li>首页</li></a>
			<a href="#"><li>数据</li></a>
			<a href="#"><li>职业</li></a>
			<a href="#"><li class="h-active">玩家</li></a>
			<a href="#"><li>天梯</li></a>
			<a href="#"><li>直播</li></a>
			<a href="#"><li>商城</li></a> -->
			<!-- <a href="http://mutuclub.com/WebTest"><li class="h-active">玩家</li></a> -->
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
		<div style="position: absolute; top:19px;width:100%;display:table;height:130px">
			<div style="margin-left:auto;margin-right:auto;position:relative;text-align:center;width:1080px;padding-left:20px;">
				<div class="new-box" style="width:300px;margin:0 auto;">
					<div style="margin:0px auto 5px auto;" class="circle-img" id="circleImg">
						<%-- <img src="<%=basePath%>images/mutu.jpg" style="width:90px"> --%>
					</div>
					<span style="font-size:16px;font-weight: bold;color:#fff;text-shadow: rgb(17, 17, 17) 1px 1px 1px;display:block;width:300px;">
						<span style="color:white">Mutu.</span><span id="nameSpan"></span>
						<span class="glyphicon glyphicon-ok-sign" style="color: rgb(44,139,199);font-size:14px;margin-right:5px;"></span>
						<!-- <a href="javascript:void(0);">
							<span style="position:absolute;top:40px;right:610px;" id="ajax_box" class="follow-circle">
							 <span class="glyphicon glyphicon-plus" style="font-size: 12px;padding-right:3px;">
							</span> 
								关注
							</span>
						</a> -->
					</span>
				</div>
				<!-- <div style="position: absolute;left:10px;top:10px;font-size: 10px;color:#999;padding: 3px 8px;text-align: left;">
					<div style="float: left;text-align: center;padding: 0px 10px;">
						<span style="font-size: 14px;font-weight: bold;color: #ccc;">
							<div class="jiathis_style_24x24" style="display: table;margin-top: 8px;">
								<a class="jiathis_button_tsina">
									<span class="jiathis_txt jtico jtico_tsina"></span>
								</a>
								<a class="jiathis_button_tqq">
									<span class="jiathis_txt jtico jtico_tqq"></span>
								</a>
								<a class="jiathis_button_weixin">
									<span class="jiathis_txt jtico jtico_weixin"></span>
								</a>
							</div>
						</span>
					</div>
				</div> -->
				<!-- <div style="position: absolute;left:800px;top:40px;font-size: 25px;padding: 3px 8px;text-align: center;">
					<span style="color:#decb00;text-shadow:2px 3px 2px #decb00;" id="leveltext"> </span>
					<span style="color:#decb00;text-shadow:2px 3px 2px #decb00;margin-left:60px">名次</span>
					<span style="color:#decb00;text-shadow:2px 3px 2px #decb00;margin-left:10px" id="sequence"></span>
				</div> -->
			</div>
		</div>
	</div>
	<div class="main-shadow-box">
		<div class="main-container" style="margin-top:0px;margin-bottom:1px;padding-bottom:1px;">
			<div class="secoundbar-outer">
				<ul class="secoundbar" style="margin-top: 20px;margin-bottom: 0px">
						<li class="active-s" style="cursor:pointer" id="perInfoBtn">
							个人信息
						</li>
				</ul>
			</div>
			<div  id="maincontainer"class="container">
				<div style="width:814px;height: 57px;margin-left: auto;margin-right: auto;color:#777;margin-top: 20px;background-color: rgb(25,25,25);padding-top: 8px;">
					<div style="float: left;text-align: center;width: 100px; margin-top:5px;">
						<span style="font-size: 22px;font-weight: bold;color: #ff3399;" id="sequence"></span>
					<div style="font-size: 14px;color: #ff3399;">比赛排名</div>
					</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px; margin-top:5px;">
						<span style="font-size: 22px;font-weight: bold;color: #a9cf54;" id="sumNum"></span>
					<div style="font-size: 14px;color: #a9cf54;">比赛场次</div>
					</div>					
					<div class="v-separator" style="float: left; height:40px;"></div>
						<div style="float: left;text-align: center;width: 100px;margin-top:6px;">
							<span style="font-size: 22px;font-weight: bold;color: #c23c2a;" id="winRatio"></span>
							<div style="font-size: 14px;color: #c23c2a;">总胜率</div>
						</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px; margin-top:5px;">
						<span style="font-size: 22px;font-weight: bold;color: #ffe600;" id="achiveNum"></span>
					<div style="font-size: 14px;color: #ffe600;">成就点数</div>
					</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px;margin-top:6px;">
						<span style="font-size: 22px;font-weight: bold;color: #e99129;"id="mvpNum"></span>
						<div style="font-size: 14px;color: #e99129;">MVP</div>
					</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px;margin-top:6px;">
						<span style="font-size: 22px;font-weight: bold;color: #999;"id="levelNum"></span>
						<div style="font-size: 14px;">实时积分</div>
					</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px;margin-top:6px;">
						<span style="font-size: 22px;font-weight: bold;color: #999;"id="maxLevelNum"></span>
						<div style="font-size: 14px;">最高积分</div>
					</div>
					<div class="v-separator" style="float: left;height:40px;"></div>
					<div style="float: left;text-align: center;width: 100px; margin-top:13px;">
						<span style="font-size: 22px;font-weight: bold;color: #ff6600;" id="leveltext"></span>
					<!-- <div style="font-size: 14px;color: #a9cf54;">玩家等级</div> -->
					</div>
				</div>
				<div class="stitle"> 阵营统计 </div>
				<table id="sideTable" class="table table-hover table-striped table-list table-thead-left" style="width: 95%; margin-left: auto;margin-right: auto;margin-bottom: 20px;margin-top: 10px;">
					<thead>
						<tr>
							<th style="width:34%">阵营</th>
							<th style="width:33%">比赛次数</th>
							<th style="width:33%">胜率</th>
						</tr>
					</thead>
					<tbody class="table-player-detail">
					</tbody>
				</table>
				<div class="stitle"> 角色统计 </div>
				<table id="roleTable" class="table table-hover table-striped sortable table-list table-thead-left" style="width: 95%; margin-left: auto;margin-right: auto;margin-bottom: 20px;margin-top: 10px;">
					<thead>
						<tr>
							<th style="width:25%">角色</th>
							<th style="width:20%">比赛次数</th>
							<th style="width:20%">胜率</th>
							<th style="width:35%">达成成就</th>
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
							<a href=javascript:void(0);>
								<i class="fa fa-envelope-o" style="font-size: 16px;padding-top: 1px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-heartbeat" style="font-size: 18px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-qq" style="font-size: 16px;padding-top: 2px;"></i>
							</a>
						</div>
						<div style="float:left;margin-left: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-question-circle" style="font-size: 16px;padding-top: 2px;"></i>
							</a>
						</div>
						<!-- <div style="float:left;margin-left: 20px">wolfKillmax.com</div>
						<div style="float:left;margin-left: 20px">友情链接</div> -->
					</div>
					<div style="position: absolute;right:0px;bottom:10px;">
						<div style="float:right;margin-right: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-bar-chart" style="font-size: 18px"></i>
							</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-weibo" style="font-size: 18px"></i>
							</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href=javascript:void(0);>
								<i class="fa fa-tencent-weibo" style="font-size: 18px"></i>
							</a>
						</div>
						 <!-- <div style="float:right;margin-right: 20px">
							<a href=javascript:void(0);>录入数据</a>
						</div>
						<div style="float:right;margin-right: 20px">
							<a href=javascript:void(0);>加入我们</a> 
						</div> -->
					</div>
			</div>
		</div>
		</div>
	</div>
</body>
</html>