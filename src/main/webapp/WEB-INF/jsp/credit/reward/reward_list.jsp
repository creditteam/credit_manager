<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="gvtv" uri="http://www.gvtv.com.cn/jsp/jstl/common"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<gvtv:navigater path="reward"></gvtv:navigater>
</section>
<style>
</style>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div class="box">
				<div class="box-header">
					<button type="button" data-url="reward/saveReward?userId=${sessionUser.userId}" data-model="dialog" class="btn btn-sm btn-primary">
						<i class="fa fa-fw fa-plus"></i>新增
					</button>
				</div>
				<!-- /.box-header -->
				<div class="box-body">
					<table id="default_table" class="table table-primary table-hover table-striped">
						<thead>
							<tr>
								<th width="10px" style="padding-right: 12px;"><input type='checkbox' id="defaultCheck" /></th>
								<th width="20px" style="padding-right: 12px;"></th>
								<th>悬赏类型</th>
								<th>悬赏人</th>
								<th>悬赏金额</th>
								<th>地址</th>
								<th>发布日期</th>
								<th>悬赏有效日期</th>
								<th>悬赏状态</th>
							</tr>
						</thead>
					</table>
				</div>
				<!-- /.box-body -->
			</div>
			<!-- /.box -->
		</div>
		<!-- /.col -->
	</div>
	<!-- /.row -->
</section>

<script type="text/javascript">
	var defTable;
	$(document).ready(function() {
		defTable = $('#default_table').DataTable({
			"ordering" : false,
			"pagingType" : "simple_numbers",
			"autoWidth": false,
			"processing" : true,
			"serverSide" : true,
			"ajax" : {
				"url" : "reward/list",
				"type" : "post",
				"data" : function(data) {
					data.keyword = $("#keyword").val();
				}
			},
			"language" : {
				"url" : "<%=basePath%>static/AdminLTE/plugins/datatables/cn.txt"
			},
			"createdRow" : function(row, data, index) {
				$('td:eq(0)', row).html("<input type='checkbox' name='chx_default' value='" + data.id + "'/>");
			},
			"lengthMenu": [[10, 15, 20], [10, 15, 20]],
			"columns" : [ 
			              {"data" : "id"},
			              {"data" : null},
			              {"data" : "rewardType"},
			              {"data" : "rewardName"}, 
			              {"data" : "rewardAmount"},
			              {"data" : "province"},
			              {"data" : "createTime"},
			              {"data" : "endTime"},
			              {"data" : "rewardStatus"}
			            ],
			"columnDefs" : [ {
				"targets" : 1,
				"render" : function(data, type, row) {
					var html = htmlTpl.dropdown.prefix
		            	  + '  <li><a href="reward/saveReward?id='+row.id+'&userId=${sessionUser.userId}" data-model="dialog"><i class="fa fa-pencil"></i>编辑</a></li>'
		            	  + '  <li><a href="reward/delete?id='+row.id+'" data-msg="确定删除吗？" data-model="ajaxToDo" data-callback="refreshTable"><i class="fa fa-trash-o"></i>删除</a></li>'
		            	  + '  <li class="divider"></li>'
		            	  if(row.rewardStatus == '0'){
		            		  html += '<li><a href="reward/updStatus?rewardStatus=1&id='+row.id+'" data-msg="确定发布吗？" data-model="ajaxToDo" data-callback="refreshTable">发布</a></li>'
		            	  }else if(row.rewardStatus == '1'){
		            		  html += '<li><a href="reward/updStatus?rewardStatus=0&id='+row.id+'" data-msg="确定取消发布吗？" data-model="ajaxToDo" data-callback="refreshTable">取消发布</a></li>'
		            	  }
		            	  + htmlTpl.dropdown.suffix;
					return html;
				}
			},
			{
				"targets" : 2,
				"render" : function(data, type, row) {
					if(data == '0'){
						return "找人";
					}else if(data == '1'){
						return "找车辆";
					}else if(data == '2'){
						return "找房产";
					}else if(data == '3'){
						return "找应收账款";
					}else if(data == '4'){
						return "其它";
					}
				}
			},
			{
				"targets" : 3,
				"render" : function(data, type, row) {
					return "<a href='reward/details?id="+row.id+"' target='_blank'>"+data+"</a>";
				}
			},
			{
				"targets" : 4,
				"render" : function(data, type, row) {
					return (data > 0 ? data : 0) + " 元";
				}
			},
			{
				"targets" : 5,
				"render" : function(data, type, row) {
					return data + " " + row.city;
				}
			},
			{
				"targets" : 6,
				"render" : function(data, type, row) {
					return FormatDate(data);
				}
			},
			{
				"targets" : 7,
				"render" : function(data, type, row) {
					return FormatDate(data);
				}
			},
			{
				"targets" : 8,
				"render" : function(data, type, row) {
					if(data == '0'){
						return "<font color='red'>未发布</font>";
					}else if(data == '1'){
						return "<font color='blue'>发布中</font>";
					}
				}
			}],
			"drawCallback": function (settings) {
				drawICheck('defaultCheck', 'chx_default');
	      	},
			"initComplete": function () {
				initSearchForm("", "搜索悬赏人、省份");
				$("#startTime").datetimepicker({
					format : 'yyyy-mm-dd hh:ii',
					language : 'zh',
					weekStart : 1,
					todayBtn : 1,
					autoclose : 1,
					todayHighlight : 1,
					startView : 2,
					minView : 0,
					forceParse : 0,
					showMeridian : 0,
					pickerPosition : "bottom-left"
				});
			}
		});
	});
	function FormatDate (strTime) {
		if(strTime != ''){
		    var date = new Date(strTime);
		    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		}else{
			return "";
		}
	}
	FormatDate("Tue Jul 16 01:07:00 CST 2013");
	function refreshTable(toFirst) {
		//defaultTable.ajax.reload();
		if(toFirst){//表格重绘，并跳转到第一页
			defTable.draw();
		}else{//表格重绘，保持在当前页
			defTable.draw(false);
		}
	}
</script>