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
<style type="text/css">
	body {
				padding-top: 50px;				
			}
	table {
				margin-top: 20px;
			}
			.starter-template {
				padding: 40px 15px;
				text-align: center;
			}			
	td.alt { background-color: #ffc; background-color: rgba(230, 127, 34, 0.2); }
	
		
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>会员管理界面</title>
<link rel="shortcut icon" href="./images/favicon.ico"/>
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/jAlert-v3.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/jquery.filtertable.min.js"></script>
<script type="text/javascript" src="./js/jAlert-v3.min.js"></script>
<script type="text/javascript" src="./js/jAlert-functions.min.js"></script>
<script type="text/javascript" src="./js/tool.js"></script>
<script type="text/javascript">
$(function(){
	
	
	
	var memInfos = '${memInfos}';
	var account = "${account}";
	var accuid = "${accuid}";
	memInfos = eval("("+memInfos+")");
	
	$("#memUid").val(accuid);
	$("#memName").val(account);
	$("#infospan").text("Welcome  "+account);
	creatTableInfo(memInfos);
	
	$("table").filterTable({
	    inputSelector: '#input-filter',
	    minRows:2,
	});
	
	$("#checkAll").click(function(){
		if ($(this).attr("checked") == "checked") {
			$("#mainTable tbody").find("input[id^='check_']").each(function(){
				$(this).attr("checked","checked");
			});
		} else {
			$("#mainTable tbody").find("input[id^='check_']").each(function(){
				$(this).removeAttr("checked");
			});
		}
	});
	
	
	
	
	$("#backBtn").click(function(){
		$("html,body").animate({scrollTop:0},200);
        return false;
	});
	$("#quitBtn").click(function(){
		window.location.href = "Memlogin";
	});
	$(".addBtn").on("click",function(e){
		var $div = $("<div></div>");
		var $form = $('<form class="form-signin" id="addform"></form>');
		var $input1 = $('<div>会员卡号</div><input id="cNum" type="text" class="form-control" maxlength="10"/>');
		$input1.appendTo($form);
		var $input2 = $('<div>会员ID</div><input id="iNum" type="text" class="form-control" />');
		$input2.appendTo($form);
		var $input3 = $('<div>手机号码</div><input id="pNum" type="text" class="form-control" maxlength ="11"/>');
		$input3.appendTo($form);
		$div.append($form);
		$.jAlert({
		    'title': '新增会员',
		    'content': $div.html(),
		    'theme': 'green',
		    'size': 'md',
		    'replaceOtherAlerts' : 'true',
		    'btns':[
		           {'text':'保存', 'closeAlert':false, 'theme': 'green', 'onClick': function(){ 
		        	   checkSubmit("ADD");}},
		           {'text':'取消', 'closeAlert': true, 'theme': 'red'}
		        ]
		  });	  
	});
	$(".editBtn").on("click",function(e){
		var num = 0;
		var idnum;
		$("input[id^='check_']").each(function(){
			if ($(this).attr('checked') == "checked") {
				num++;
				var id = $(this).attr("id");
				idnum = id.split("_")[1];
			}
		});
		if (num != 1) {
			$.jAlert({
				'title': 'ERROR',
				'content': '请选中一条数据!',
			    'theme': 'red',
			    'size': 'sm',
			}); 
			return false;
		}
		var mName = $("#mName_"+idnum).text();
		var mid = $("#mid_"+idnum).text();
		var mphone = $("#mphone_"+idnum).text();
		var $div = $("<div></div>");
		var $form = $('<form class="form-signin" id="editform"></form>');
		var $input1 = $('<div>会员卡号</div><input id="cNum" type="text" class="form-control" disabled=disabled value='+mid+'>');
		$input1.appendTo($form);
		var $input2 = $('<div>会员ID</div><input id="iNum" type="text" class="form-control" value='+mName+'>');
		$input2.appendTo($form);
		var $input3 = $('<div>手机号码</div><input id="pNum" type="text" class="form-control" maxlength ="11" value='+mphone+'>');
		$input3.appendTo($form);
		$div.append($form);
		
		$.jAlert({
		    'title': '编辑会员',
		    'content': $div.html(),
		    'theme': 'green',
		    'size': 'md',
		    'replaceOtherAlerts' : 'true',
		    'btns':[
		           {'text':'保存', 'closeAlert':false, 'theme': 'green', 'onClick': function(){ 
		        	   checkSubmit("EDIT");}},
		           {'text':'取消', 'closeAlert': true, 'theme': 'red'}
		        ]
		  });
	});
	$(".delBtn").on("click",function(){
		var dataform =[];
		var num = 0;
		$("input[id^='check_']").each(function(){
			if ($(this).attr('checked') == "checked") {
				var id = $(this).attr("id");
				idnum = id.split("_")[1];
				dataObj = {
						memId : $("#mid_"+idnum).text(),
						memName : $("#mName_"+idnum).text(),
						memPhone : $("#mphone_"+idnum).text()
				};
				dataform.push(dataObj);
				num++;
			}
		});
		if (num == 0) {
			$.jAlert({
				'title': 'ERROR',
				'content': '请选中至少一条数据!',
			    'theme': 'red',
			    'size': 'sm',
			});
			return false;
		}
		$.jAlert({
		    'title': '删除会员',
		    'content': '确定要删除选中的会员信息?',
		    'theme': 'green',
		    'size': 'md',
		    'replaceOtherAlerts' : 'true',
		    'btns':[
		           {'text':'确定', 'closeAlert':true, 'theme': 'green', 'onClick': function(){ 
		        	   deleteMem(dataform);}},
		           {'text':'取消', 'closeAlert': true, 'theme': 'red'}
		        ]
		  });
		
	});
	$("#passBtn").click(function(){
		
		var $div = $("<div></div>");
		var $form = $('<form class="form-signin" id="passform"></form>');
		var $input1 = $('<div>旧密码</div><input id="oldpass" type="password" class="form-control"/>');
		$input1.appendTo($form);
		var $input2 = $('<div>新密码</div><input id="newpass" type="password" class="form-control" maxlength="16" />');
		$input2.appendTo($form);
		var $input3 = $('<div>确认新密码</div><input id="dulpass" type="password" class="form-control"/>');
		$input3.appendTo($form);
		$div.append($form);
		$.jAlert({
		    'title': '修改密码',
		    'content': $div.html(),
		    'theme': 'green',
		    'size': 'md',
		    'replaceOtherAlerts' : 'true',
		    'btns':[
		           {'text':'保存', 'closeAlert':false, 'theme': 'green', 'onClick': function(){ 
		        	   changePass();}},
		           {'text':'取消', 'closeAlert': true, 'theme': 'red'}
		        ]
		  });
	});
	
});
function changePass() {
	var memName = $("#memName").val();
	var oldpass = $("#oldpass").val();
	var newpass = $("#newpass").val();
	if ($("#newpass").val().length < 6) {
		$.jAlert({
			'title': 'ERROR',
			'content': '密码最小长度必须大于等于6位!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	}
	if ($("#newpass").val() != $("#dulpass").val()) {
		$.jAlert({
			'title': 'ERROR',
			'content': '两次密码输入不一致，请重新输入!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	}
	var param = {
			mName : memName,
			opass : oldpass,
			npass : newpass,
	};
	$.ajax({
		url:"<%=basePath%>modifyAccount",
		type:"post",
		cache : false,
		//设置同步，避免和自动刷新冲突导致数据不同步
		async: false,
		data : param,
		success: function(data){
			var msg = data.message;
			if (msg.indexOf("success") > -1) {
				$.jAlert({
					'title': 'SUCCESS',
					'content': '修改成功!',
				    'theme': 'green',
				    'size': 'sm',
				    'replaceOtherAlerts' : 'true'
				});
			} else {				
				$.jAlert({
					'title': 'ERROR',
					'content': '旧密码与用户不一致!',
				    'theme': 'red',
				    'size': 'sm',
				});
			}	
		},
		error: function(){
			$.jAlert({
				'title': 'ERROR',
				'content': '修改失败，请重试!',
			    'theme': 'red',
			    'size': 'sm',
			});
		},
	});
}
function deleteMem(dataform) {
	var delMems = {
			param : JSON.stringify(dataform)
			};
	$.ajax({
		url:"<%=basePath%>deletedMem",
		type:"post",
		cache : false,
		//设置同步，避免和自动刷新冲突导致数据不同步
		async: false,
		data : delMems,
		success: function(data){
			var msg = data.message;
			if (msg.indexOf("success") > -1) {
				$.jAlert({
					'title': 'SUCCESS',
					'content': '删除成功!',
				    'theme': 'green',
				    'size': 'sm',
				    'replaceOtherAlerts' : 'true',
				    'closeBtn':'false',
				    'btns':[{
				    	'text':'确认',
				    	'closeAlert':true,
				    	'theme':'green',
				    	'onClick':function(){
				    		refresh();
				    	}
				    }]

				});
			} else {
				$.jAlert({
					'title': 'ERROR',
					'content': msg,
				    'theme': 'red',
				    'size': 'sm',
				});
			}	
		},
		error: function(){
			$.jAlert({
				'title': 'ERROR',
				'content': '删除失败，请重试!',
			    'theme': 'red',
			    'size': 'sm',
			});
		},
	});
}
function checkSubmit(type) {
	if ($("#cNum").val() == "" || $("#iNum").val() == "") {
		$.jAlert({
			'title': 'ERROR',
			'content': '会员卡号、会员ID为必填项!',
		    'theme': 'red',
		    'size': 'sm',
		}); 
		return false;
	};
	if (isNaN($("#cNum").val())) {
		$.jAlert({
			'title': 'ERROR',
			'content': '会员卡号必须为数字!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	};
	if (isNaN($("#pNum").val()) || $("#pNum").val().length != 11) {
		$.jAlert({
			'title': 'ERROR',
			'content': '请填写正确的手机号!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	};
	if ($("#iNum").val().indexOf("-") > -1 || ($("#iNum").val().indexOf("_") > -1)) {
		$.jAlert({
			'title': 'ERROR',
			'content': '会员ID不能包含"-"或"_"!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	};
	var msg = "success";
	if (type == "ADD") {
		var numdata = {
				mNum : $("#cNum").val(),
		};
		$.ajax({
			url:"<%=basePath%>vertifyMemNum",
			type:"post",
			cache : false,
			//设置同步，避免和自动刷新冲突导致数据不同步
			async: false,
			data : numdata,
			success: function(data){
				msg = data.message;	
			},
			error: function(){
				$.jAlert({
					'title': 'ERROR',
					'content': '保存失败，请重试!',
				    'theme': 'red',
				    'size': 'sm',
				});
			},
		});
	}
	if (msg != "success") {
		$.jAlert({
			'title': 'ERROR',
			'content': '该会员号已存在,请重新输入!',
		    'theme': 'red',
		    'size': 'sm',
		});
		return false;
	}
	var userdata = {
			mNum : $("#cNum").val(),
			mName : $("#iNum").val(),
			mPhone : $("#pNum").val()
	};	
	$.ajax({
		url:"<%=basePath%>insertNewMem",
		type:"post",
		cache : false,
		//设置同步，避免和自动刷新冲突导致数据不同步
		async: false,
		data : userdata,
		success: function(data){
			var msg = data.message;
			if (msg.indexOf("success") > -1) {
				$.jAlert({
					'title': 'SUCCESS',
					'content': '保存成功!',
				    'theme': 'green',
				    'size': 'sm',
				    'replaceOtherAlerts' : 'true',
				    'closeBtn':'false',
				    'btns':[{
				    	'text':'确认',
				    	'closeAlert':true,
				    	'theme':'green',
				    	'onClick':function(){
				    		refresh();
				    	}
				    }]

				});
			} else {
				$.jAlert({
					'title': 'ERROR',
					'content': msg,
				    'theme': 'red',
				    'size': 'sm',
				});
			}	
		},
		error: function(){
			$.jAlert({
				'title': 'ERROR',
				'content': '保存失败，请重试!',
			    'theme': 'red',
			    'size': 'sm',
			});
		},
	});		 
}
function creatTableInfo(memInfos) {
	var length = memInfos.length;
	for (var i = 0; i < length; i++) {
		var $tr = $('<tr id=Ftr_'+i+'></tr>'); 
		var temp='<td style="width: 60px; min-width: 60px; max-width: 60px;"><input type="checkbox" id=check_'+i+'></td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mName_'+i+'>'+memInfos[i].memName+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mid_'+i+'>'+memInfos[i].memId+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mphone_'+i+'>'+memInfos[i].memPhone+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		$("#mainTable tbody").append($tr);
		
	};
};
function refresh() {
	var account = $("#memName").val();
	var accuid = $("#memUid").val();
	var memNameEncode = base64encode(account);
	var memUidEncode = base64encode(accuid);
	var url = "forwardMemInfo?mna=" + memNameEncode + "&uid=" + memUidEncode;
	window.location.href = url;
};
</script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="btn btn-success addBtn" style="margin-top:8px;">新增</button>
				<button type="button"class="btn btn-info editBtn"style="margin-top:8px;">编辑</button>
				<button type="button" class="btn btn-danger delBtn"style="margin-top:8px;">删除</button>
				<button type="button" class="btn btn-primary" id="backBtn"style="margin-top:8px;">返回顶部</button>
			</div>
			<div class="navbar-header navbar-right">
				<span class="navbar-brand" id="infospan"></span>
				<button type="button"  class="btn btn-success" style="margin-top:8px;float:left"id ="passBtn">修改密码</button>
				<button type="button"  class="btn btn-danger" style="margin-top:8px;margin-left:5px;float:left" id="quitBtn">退出</button>
			</div>
		</div>
	</nav>
<div class="container">
		<input type="hidden" id="memUid"/>
		<input type="hidden" id="memName"/>
			<div class="input-group" style="margin-top:30px">
				<div class="input-group-addon">查  询</div>
				<input class="form-control" type="search" id="input-filter" maxlength="15" placeholder="输入关键字">
			</div>
			<table class="table table-striped" id="mainTable">
				<thead style="visibility: visible;">
					<tr class="success">
						<td style="width: 60px; min-width: 60px; max-width: 60px;"><input type="checkbox" id="checkAll"></td>
						<td>会员ID</td>
						<td>会员卡号</td>
						<td>电话号码</td>
					</tr>
				</thead>
				<tbody>			
				</tbody>
			</table>

		</div>
</body>
</html>