
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
					<h3><b>請假資料管理</b></h3>
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
					<div id="leaveQuatoContent">
					</div>
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
					<input type="hidden" id="unitType" name="unitType"/>
					<form id="askForLeaveForm" name="askForLeaveForm">
						<input type="hidden" id="askForLeaveId" name="askForLeaveId"/>
						<input type="hidden" id="sysUserId" name="sysUserId"/>
						<input type="hidden" id="status" name="status"/>
						<input type="hidden" id="asanaTaskId" name="asanaTaskId"/>
						<div id="edit">
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">假別:</label>
					            <div class="col-sm-12 checkbox">
					            	<div id="roleTypeDiv" style="float:left;">
						            	<select class="form-control" id="leaveId" name="leaveId" onchange="selectedLeaveId(this.options[this.options.selectedIndex].label)">
						            		<option value="">請選擇</option>
							            	<c:forEach var="item" items="${cqiLeaveList}" varStatus="vs">
												<option value="${item.leaveId}">${item.leaveName}，單位：
													<c:if test="${item.unitType == 1}">天</c:if>
													<c:if test="${item.unitType == 2}">小時</c:if>
												</option>
											</c:forEach>
										</select>
										<span id="leaveId-error" class="error_text"></span>
									</div>
								</div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12" id="spendTimeStr" name="spendTimeStr">
					            	天數/時數：
					            </label>
					            <div class="col-sm-12">
						            <!-- <input type="text" class="form-control" id="spendTime" name="spendTime"/> -->
						            <select class="form-control" id="spendTime" name="spendTime">
						            	<option value="">請選擇</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
										
									</select>
						            <span id="spendTime-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">公告開始時間：</label>
					            <div class="col-sm-12">
						            <div class="form-group">
						                <div class="input-group date" id='datetimepickerStart'>
						                    <input id="startTime" name="startTime" class="form-control" size="16" type="text" value=""/>
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						                </div>
						            </div>
						            <span id="startTime-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">公告結束時間：</label>
					            <div class="col-sm-12">
						            <input id="endTime" name="endTime" class="form-control" size="16" type="text" value="" readonly="readonly"/>
						            <span id="endTime-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">事由：</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="description" name="description" />
						            <span id="description-error" class="error_text"></span>
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
			var today = new Date();
			var pickerStartDate = getFormattedDate(getCurrentFirstDay(), 'y/M/d H:m');
			if(today.getDate()<4){
				pickerStartDate = getFormattedDate(getLastMonth(), 'y/M/d H:m');
			}
			$('#datetimepickerStart').datetimepicker({
				format :'yyyy/mm/dd hh:ii',
				autoclose: true,
				minuteStep:30,
		        focusOnShow: false,
		        allowInputToggle: true,
		        startDate: pickerStartDate
			});
			$('#datetimepickerEnd').datetimepicker({
				format :'yyyy/mm/dd hh:ii',
				autoclose: true,
				minuteStep:30,
				focusOnShow: false,
		        allowInputToggle: true
			});
			$('#datetimepickerEnd').datetimepicker("setStartDate", pickerStartDate);
			$("#datetimepickerStart").on("changeDate", function (e) {
	            $('#datetimepickerEnd').datetimepicker("setStartDate", e.date);
	            if($('#spendTime').val()!=''){
	            	var setEndDate = new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate());
	            	setEndDate.setHours(e.date.getHours());
	            	setEndDate.setMinutes(e.date.getMinutes());
	            	if($('#unitType').val()=='小時'){
	            		setEndDate.setHours(setEndDate.getHours() + parseInt($('#spendTime').val()));
		            	if(e.date.getHours()<12 && setEndDate.getHours()>12){
		            		setEndDate.setHours(setEndDate.getHours() + 1);
		            	}
	            	}else if($('#unitType').val()=='天'){
	            		setEndDate.setDate(setEndDate.getDate() + parseInt($('#spendTime').val()) - 1);
	            		setEndDate.setHours(19);
	            		setEndDate.setMinutes(0);
		            }
	            	$('#endTime').val(getFormattedDate(setEndDate, "y/M/d H:m"));
	            }
	        });
	        $("#datetimepickerEnd").on("changeDate", function (e) {
	            $('#datetimepickerStart').datetimepicker("setEndDate", e.date);	            
	        });
		});
		
		function selectedLeaveId(data){
			$('#spendTimeStr').html(data.split("，")[1]);
			if(data.indexOf('天')>=0){
				$('#unitType').val('天');
			}else{
				$('#unitType').val('小時');
			}
		}
		
		function queryData(page) {
			if(page == 0){
				return;
			}
			$("body").css("cursor", "progress");
			$('#leaveQuatoContent').hide();
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/askLeave/ajaxLeaveQuota'/>",
				data : {},
				success : function(data) {
					$('#leaveQuatoContent').html(data);
					$('#leaveQuatoContent').show();
				}
			});
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/askLeave/ajaxDataLoading'/>",
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
			errorCode["1"] = "請選擇假別";
			errorCode["2"] = "請選擇天數/時數";
			errorCode["3"] = "請輸入時間";
			errorCode["4"] = "時間似乎怪怪的，請確認";
			var errors = {};
			
			if($('#leaveId :selected').val() == ''){
				errors['leaveId'] = 1;
			}
			if($('#spendTime :selected').val() == ''){
				errors['spendTime'] = 2;
			}
			if($('#startTime').val() == ''){
				errors['startTime'] = 3;
			}
			if($('#endTime').val() == ''){
				errors['endTime'] = 3;
			}
			var targetURL= "<c:url value='/security/askLeave/add'/>";
			
			if(Object.keys(errors).length == 0){
				var data = $('#askForLeaveForm').serialize();
				if(typeof($('#askForLeaveId').val()) != 'undefined' && $('#askForLeaveId').val() != ''){//easier
					targetURL= "<c:url value='/security/askLeave/update'/>";
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
				//$("#"+key).css('width',90);
				$("#"+key+"-error").text(errorCode[errors[key]]);
				$("#"+key+"-error").show();
			}
		}

		function deleteData(id){
			var targetURL= "<c:url value='/security/askLeave/"+id+"'/>";
			
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
		
		function active(id){
			if(progressing == 1){
				return;
			}
			//$('input[name="mangaRoleTypes"]').attr("checked", false);
			$('#datetimepickerEnd').datetimepicker("setStartDate", new Date(-8639968443048000));
			$('#datetimepickerStart').datetimepicker("setEndDate", new Date(8639968443048000));
			var text = "修改";
			if(typeof(id) == "undefined"){
				text = "新增";
				$('#askForLeaveId').val("");
				$('#leaveId').val("");
				$('#spendTime').val("");
				$('#startTime').val("");
				$('#sysUserId').val("");
				$('#endTime').val("");
				$('#description').val("");
				$('#asanaTaskId').val("");
				$('#status').val("");
				$('#basicModal').find('.modal-title').text(text + "請假紀錄");
				$('#basicModal').modal('toggle');
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/askLeave/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						askForLeaveId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#askForLeaveId').val(data.userAskForLeave.askForLeaveId);
							$('#leaveId').val(data.userAskForLeave.leaveId);
							$('#spendTime').val(data.userAskForLeave.spendTime);
							$('#startTime').val(formatJsonDate(data.userAskForLeave.startTime, "y/M/d H:m"));
							$('#endTime').val(formatJsonDate(data.userAskForLeave.endTime, "y/M/d H:m"));
							$('#datetimepickerEnd').datetimepicker("setStartDate", $('#startTime').val());
							$('#datetimepickerStart').datetimepicker("setEndDate", $('#endTime').val());
							$('#sysUserId').val(data.userAskForLeave.sysUserId);
							$('#description').val(data.userAskForLeave.description);
							$('#asanaTaskId').val(data.userAskForLeave.asanaTaskId);
							$('#status').val(data.userAskForLeave.status);
							selectedLeaveId($('#leaveId option:selected').html());
							$('#basicModal').find('.modal-title').text(text + "請假紀錄");
							$('#basicModal').modal('toggle');
							progressing = 0;
						}else{
							alert(data.message);
						}
					}
				});				
			}	
		}
	</script>
</body>
</html>
