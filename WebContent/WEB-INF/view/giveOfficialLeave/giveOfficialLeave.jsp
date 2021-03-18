
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	<link href="<c:url value='/resources/datepicker/bootstrap-datepicker.min.css'/>" rel="stylesheet" />
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase page-width" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>全體公假管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							
						</div>
						<div class="btn-group">
							<a href="#" class="btn btn-default function_icon" onclick="initAdd()" title="新增"> 
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
					<h4 class="modal-title" id="myModalLabel">新增公假</h4>
				</div>
				<div class="modal-body">
					<form id="giveOfficialLeaveForm" name="giveOfficialLeaveForm">
						<input type="hidden" class="form-control" id="giveOfficialLeaveId" name="giveOfficialLeaveId"/>
						<input type="hidden" class="form-control" id="leaveId" name="leaveId"/>
						<input type="hidden" class="form-control" id="status" name="status"/>
						<div id="edit">
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">公告開始時間：</label>
					            <div class="col-sm-12">
						            <div class="form-group">
						                <div class="input-group date" id='datetimepickerStart'>
						                    <input id="officialLeaveStartDate" name="officialLeaveStartDate" class="form-control" size="16" type="text" value=""/>
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						                </div>
						            </div>
						            <span id="officialLeaveStartDate-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">公告結束時間：</label>
					            <div class="col-sm-12">
						            <div class="form-group">
						                <div class="input-group date" id='datetimepickerEnd'>
						                    <input id="officialLeaveEndDate" name="officialLeaveEndDate" class="form-control" size="16" type="text" value=""/>
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						                </div>
						            </div>
						            <span id="officialLeaveEndDate-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">事由：</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="description" name="description" />
						            <span id="description-error" class="error_text"></span>
					            </div>
							</div>
							<label for="recipient-name" class="control-label col-sm-12">請新增公假紀錄後，編輯准假成員</label>
						</div>
			        </form>
				</div>
				<div style="clear:both;"></div>
				<div class="modal-footer" style="margin-top:20px">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary btn-lg " id="addSave" data-loading-text="儲存中" onclick="addSubmit()">新增</button>
				</div>
			</div>
		</div>
 	</div>
 	
 	<!-- dialog -->
	<div class="modal fade" id="userListModal" tabindex="-1" role="dialog" aria-labelledby="userListModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<form id="userListForm" name="userListForm">
						<input type="hidden" class="form-control" id="giveOfficialLeaveUserListId" name="giveOfficialLeaveUserListId"/>
						<span id="userListForm-error" class="error_text"></span>
						
						<button type="button" class="btn btn-primary" onclick="selectAll()">全選</button>
						<button type="button" class="btn btn-primary" onclick="removeSelectAll()">全不選</button>
						<div class="table-responsive">
							<table class="table table-striped">
								<thead>
									<tr>
										<th style="width:40%">成員</th>
										<th style="width:40%">公假</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach var="item" items="${mapEnableRule2User}" varStatus="vs">
									<tr >
										<td>
											${item['value'].originalName}
										</td>
										<td>
											<input type="checkbox" name="sysUserIds" value="${item['value'].sysUserId}">
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
					</form>
				</div>
				<div style="clear:both;"></div>
				<div class="modal-footer" style="margin-top:20px">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" onclick="userListSave()">Save</button>
				</div>
			</div>
		</div>
 	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script src="<c:url value='/resources/datepicker/bootstrap-datepicker.js'/>" type="text/javascript"></script>
	
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData(1);
			var today = new Date();
			var ruleId = ${operator.roleId} ;
			var pickerStartDate = getFormattedDate(getLastMonth(), 'y/M/d H:m');
			
			
			$('#datetimepickerStart').datetimepicker({
				format :'yyyy/mm/dd hh:ii',
				autoclose: true,
				minuteStep:30,
		        focusOnShow: false,
		        allowInputToggle: true,
		        daysOfWeekDisabled: [0],
		        todayHighlight: true, 
		        startDate: pickerStartDate
			});
			$('#datetimepickerEnd').datetimepicker({
				format :'yyyy/mm/dd hh:ii',
				autoclose: true,
				minuteStep:30,
				focusOnShow: false,
		        allowInputToggle: true,
		        daysOfWeekDisabled: [0],
		        todayHighlight: true, 
		        startDate: pickerStartDate
			});
			$("#datetimepickerStart").on("changeDate", function (e) {
	            $('#datetimepickerEnd').datetimepicker("setStartDate", e.date);

				var maxDateForEnd = new Date(e.date.valueOf()+(1000*60*60*10));
	            $('#datetimepickerEnd').datetimepicker("setEndDate", maxDateForEnd);
	        });
			$("#datetimepickerEnd").on("changeDate", function (e) {
	            $('#datetimepickerStart').datetimepicker("setEndDate", e.date);

				var minDateForStart = new Date(e.date.valueOf()-(1000*60*60*10));
	            $('#datetimepickerStart').datetimepicker("setStartDate", minDateForStart);
	        });
			
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
				url : "<c:url value='/security/GiveOfficialLeaveManage/ajaxDataLoading'/>",
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
		function initAdd(){
			if(progressing == 1){
				return;
			}
			var pickerStartDate = getFormattedDate(getLastMonth(), 'y/M/d H:m');
			$('#datetimepickerStart').datetimepicker("setStartDate", pickerStartDate);
			$('#datetimepickerStart').datetimepicker("setEndDate", new Date(8639968443048000));
			$('#datetimepickerEnd').datetimepicker("setStartDate", pickerStartDate);
			$('#datetimepickerEnd').datetimepicker("setEndDate", new Date(8639968443048000));

			$('#officialLeaveStartDate').val("");
			$('#officialLeaveEndDate').val("");
			$('#giveOfficialLeaveId').val("");
			$('#leaveId').val("6");//公假
			$('#status').val("1");
			$('#description').val("");
			
			$('#basicModal').find('.modal-title').text("新增全體公假");
			$('#basicModal').modal('toggle');
		}
		
		function addSubmit(){
			var errorCode = {};
			errorCode["1"] = "請選擇時間";
			errorCode["2"] = "請輸入事由";
			errorCode["3"] = "時間似乎怪怪的，請確認";
			var errors = {};
			
			if($('#officialLeaveStartDate').val() == ''){
				errors['officialLeaveStartDate'] = 1;
			}
			if($('#officialLeaveEndDate').val() == ''){
				errors['officialLeaveEndDate'] = 1;
			}
			if($('#description').val() == ''){
				errors['description'] = 2;
			}
			
			
			var targetURL= "<c:url value='/security/GiveOfficialLeaveManage/add'/>";
			
			if(Object.keys(errors).length == 0){
				
				var data = $('#giveOfficialLeaveForm').serialize();
				$('#addSave').button('loading');
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
						$('#addSave').button('reset');
					},
				    error: function (jqXHR, exception) {
				        var msg = "";
				        if (jqXHR.status === 0) {
				            msg = "資料新增失敗！\n\n請檢查您的網路之後，重新登入再試一次!";
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
		
		function queryUserList(giveOfficialLeaveId){
			progressing = 1;
			$("body").css("cursor", "progress");
			$("input[name='sysUserIds']").attr("checked",false);
			var targetURL= "<c:url value='/security/GiveOfficialLeaveManage/"+giveOfficialLeaveId+"'/>";
			$.ajax({
				type : "GET",
				url : targetURL,
				data : {
					giveOfficialLeaveId:giveOfficialLeaveId
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						$('#giveOfficialLeaveUserListId').val(giveOfficialLeaveId);
						for(var i = 0; i < data.giveOfficialLeaveUserList.length; i++){
							//alert(data.giveOfficialLeaveUserList[i].sysUserId);
							$(":checkbox[value="+data.giveOfficialLeaveUserList[i].sysUserId+"]").prop("checked", true);
						}
						$('#userListModal').find('.modal-title').text("成員清單");
						$('#userListModal').modal('toggle');
						progressing = 0;
					}else{
						alert(data.message);
					}
				}
			});	
		}
		
		function userListSave(){
			var targetURL= "<c:url value='/security/GiveOfficialLeaveManage/save/"+$("#giveOfficialLeaveUserListId").val()+"'/>";
			var data = $('#userListForm').serialize();
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
						
						$('#userListModal').modal('hide');
					}else{
						$("#userListForm-error").text(data.message);
						$("#userListForm-error").show();
					}
					
				}
			});
		}
		
		function deleteData(id){
			var targetURL= "<c:url value='/security/GiveOfficialLeaveManage/"+id+"'/>";
			
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
		
		function selectAll(){
			$("input[name='sysUserIds']").attr("checked",true);
		}
		function removeSelectAll(){
			$("input[name='sysUserIds']").attr("checked",false);
		}
		
		
		
		
		
	</script>
</body>
</html>
