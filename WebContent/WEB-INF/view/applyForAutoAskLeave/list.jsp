
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase page-width" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>自動請假設定管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							
						</div>
						<div class="btn-group">
							<a href="#" class="btn btn-default function_icon" onclick="active()" title="新增"> 
								<i class="glyphicon glyphicon-plus"></i>
							</a>
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
				</form>

			</div>
		</div>
	</div>
	
	<!-- dialog -->
	<div class="modal fade" id="basicModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">新增請假紀錄</h4>
				</div>
				<div class="modal-body">
					<form id="applyForAutoAskLeaveForm" name="applyForAutoAskLeaveForm">
						<input type="hidden" id="autoId" name="autoId"/>
						<input type="hidden" id="sysUserId" name="sysUserId"/>
						<input type="hidden" id="status" name="status"/>
						<div id="edit">
							<div class="form-group">
					            <label for="dateOfYear" class="control-label col-sm-12">
					            	年份：
					            </label>
					            <div class="col-sm-12">
						            <select class="form-control" id="dateOfYear" name="dateOfYear"></select>
						            <select class="form-control" id="dateOfMonth" name="dateOfMonth">
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
										<option value="9">9</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</select>
						            <span id="date-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="getIntoOfficesTime" class="control-label col-sm-12">
					            	上班時間：
					            </label>
					            <div class="col-sm-12">
						            <select class="form-control" id="getIntoOfficesTime" name="getIntoOfficesTime">
										<option value="09:00">09:00</option>
										<option value="10:00">10:00</option>
									</select>
						            <span id="getIntoOfficesTime-error" class="error_text"></span>
					            </div>
							</div>
						</div>
			        </form>
				</div>
				<div style="clear:both;"></div>
				<div class="modal-footer" style="margin-top:20px">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary btn-lg " id="save" data-loading-text="儲存中" onclick="submit()">儲存</button>
				</div>
			</div>
		</div>
 	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData(1);
			
			var startYear = 2020;
			var nowYear = new Date().getFullYear();
			var nowDate = new Date();
			for(var i=0;i<=(nowYear - startYear + 1);i++){
				var insertYear = startYear + i;
				$("#dateOfYear").append($("<option></option>").attr("value", insertYear).text(insertYear));
				if(nowDate.getFullYear()==insertYear){
					$("#dateOfYear option").get(i).selected = true;
				}
			}
			$("#dateOfMonth option").get((nowDate.getMonth()+1)).selected = true;
		});
		
		function queryData(page) {
			if(page == 0){
				return;
			}
			$("body").css("cursor", "progress");
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/applyForAutoAskLeave/ajaxDataLoading'/>",
				data : {
					page:page
				},
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}
		
		function submit(){
			var errorCode = {};
			errorCode["1"] = "請選擇年月份";
			errorCode["2"] = "請選擇上班時間";
			var errors = {};
			
			if($('#dateOfYear :selected').val() == ''){
				errors['date'] = 1;
			}
			if($('#dateOfMonth :selected').val() == ''){
				errors['date'] = 1;
			}
			if($('#getIntoOfficesTime :selected').val() == ''){
				errors['getIntoOfficesTime'] = 2;
			}
			var targetURL= "<c:url value='/security/applyForAutoAskLeave/add'/>";
			
			if(Object.keys(errors).length == 0){
				var data = $('#applyForAutoAskLeaveForm').serialize();
				if(typeof($('#autoId').val()) != 'undefined' && $('#autoId').val() != ''){//easier
					targetURL= "<c:url value='/security/applyForAutoAskLeave/update'/>";
				}//yeah 
				$('#save').button('loading');
				$("body").css("cursor", "progress");
				$.ajax({
					type : "POST",
					url : targetURL,
					data : data,
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							queryData(1);
							
							$('#basicModal').modal('hide');
						}else{
							alert(data.message);
						}
						$('#save').button('reset');
					},
				    error: function (jqXHR, exception) {
				        var msg = "";
				        if (jqXHR.status === 0) {
				            msg = "資料新增失敗！\n\n請檢查您的網路之後，重新登入再試一次!";
				        } else if (jqXHR.status == 400) {
				            msg = "資料新增失敗！\n\n請檢查看看!";
				        } else if (jqXHR.status == 401) {
				            msg = "資料新增失敗！\n\n您太久未操作系統，系統會回到登入頁面，請重新登入!";
				        } else if (jqXHR.status == 404) {
				            msg = "資料新增失敗！\n\n系統無此功能404，系統會回到登入頁面，請重新登入!";
				        } else if (jqXHR.status == 500) {
				            msg = "資料新增失敗！\n\n伺服器錯誤500，系統會回到登入頁面，請重新登入!";
				        } else if (exception === 'parsererror') {
				            msg = "資料新增失敗！\n\n資料錯誤，系統會回到登入頁面，請重新登入!";
				        } else if (exception === 'timeout') {
				            msg = "資料新增失敗！\n\n連線逾時，系統會回到登入頁面，請重新登入!";
				        } else if (exception === 'abort') {
				            msg = "資料新增失敗！\n\n連線被取消，系統會回到登入頁面，請重新登入!";
				        } else {
				            msg = "資料新增失敗！\n\n系統錯誤!\n" + jqXHR.responseText + "，系統會回到登入頁面，，請重新登入!";
				        }
				        $('#basicModal').modal('hide');
				        sessionOvertimeMessage(msg);
				    }
				});
			}
			for(var key in errors){
				$("#"+key+"-error").text(errorCode[errors[key]]);
				$("#"+key+"-error").show();
			}
		}

		function disableData(id){
			var targetURL= "<c:url value='/security/applyForAutoAskLeave/"+id+"'/>";
			
			$("body").css("cursor", "progress");
			$.ajax({
				type : "DELETE",
				url : targetURL,
				data : {
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						queryData(1);
					}
					
				}
			});
		}
		
		function enableData(id){
			var targetURL= "<c:url value='/security/applyForAutoAskLeave/"+id+"'/>";
			
			$("body").css("cursor", "progress");
			$.ajax({
				type : "PATCH",
				url : targetURL,
				data : {
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						queryData(1);
					}
					
				}
			});
		}
		
		function active(id){
			if(progressing == 1){
				return;
			}
			var text = "修改";
			if(typeof(id) == "undefined"){
				text = "新增";
				$('#autoId').val("");
				$('#sysUserId').val("");
				$('#dateOfYear').val("");
				$('#dateOfMonth').val("");
				$('#getIntoOfficesTime').val("");
				$('#status').val("");
				$('#basicModal').find('.modal-title').text(text + "自動請假設定");
				$('#basicModal').modal('toggle');
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/applyForAutoAskLeave/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						autoId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#autoId').val(data.applyForAutoAskLeave.autoId);
							$('#sysUserId').val(data.applyForAutoAskLeave.sysUserId);
							$('#dateOfYear').val(data.applyForAutoAskLeave.dateOfYear);
							$('#dateOfMonth').val(data.applyForAutoAskLeave.dateOfMonth);
							$('#getIntoOfficesTime').val(data.applyForAutoAskLeave.getIntoOfficesTime);
							$('#status').val(data.userAskForLeave.status);
							$('#basicModal').find('.modal-title').text(text + "自動請假設定");
							$('#basicModal').modal('toggle');
							progressing = 0;
						}else{
							alert(data.message);
						}
						
					}
				});				
			}	
			
		}
		
		function resetInput(id){
			$("#"+id+"-error").text('');
			$("#"+id+"-error").hide();		
		}
	</script>
</body>
</html>
