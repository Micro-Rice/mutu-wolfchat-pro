<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>会员信息界面</title>
<link rel="shortcut icon" href="./images/favicon.ico"/>
<link href="./css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/jquery.filtertable.min.js"></script>
<script type="text/javascript">
$(function(){

	var memInfos = "${pInfo}";
	memInfos = eval("("+memInfos+")");
	
	if (memInfos.length != 0) {
		creatTableInfo(memInfos);
	}
	$("table").filterTable({
	    inputSelector: '#input-filter',
	    minRows:2,
	});
});

function creatTableInfo(memInfos) {
	var length = memInfos.length;
	for (var i = 0; i < length; i++) {
		var killdata = memInfos[i].val.split("-");
		
		var $tr = $('<tr id=Ftr_'+i+' class="infoTr"></tr>');
		
		var temp = '<td id=squence_'+i+'>'+killdata[7]+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mName_'+i+'>'+killdata[0]+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mid_'+i+'>'+killdata[4]+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mlevel_'+i+'>'+killdata[5]+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		temp='<td id=mlevelNum_'+i+'>'+killdata[6]+'</td>';
		$td = $(temp);
		$td.appendTo($tr);
		
		$("#mainTable tbody").append($tr);
		
	};
};
</script>
</head>
<body>
<div class="container">
			<div class="input-group" style="margin-top:30px">
				<div class="input-group-addon">查  询</div>
				<input class="form-control" type="search" id="input-filter" size="15" placeholder="输入关键字">
			</div>
			<table class="table table-striped" id="mainTable">
				<thead style="visibility: visible;">
					<tr>
						<td>名次</td>
						<td>玩家ID</td>
						<td>玩家卡号</td>
						<td>玩家等级</td>
						<td>玩家积分</td>
					</tr>
				</thead>
				<tbody>			
				</tbody>
			</table>
		</div>
</body>
</html>