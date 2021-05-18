
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
					<h3><b>遠端作業管理</b></h3>
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


					<table class="table table-striped">
						<h4>
							<b>遠端作業模式</b>
						</h4>
						<thead>
							<tr style="background-color: #edf8ff; font-weight: bold;">
								<td width="50%">程度</td>
								<td width="50%">實施日數</td>
								
							</tr>
						</thead>
						<tbody>
			
							<c:forEach var="userLeave" items="${userLeaveList}">
								<tr>
									<td>
										1
									</td>
									<td>
										3天
									</td>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>


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
					<h4 class="modal-title" id="myModalLabel">新增遠端作業紀錄</h4>
				</div>
				<div class="modal-body">
					<form id="wfhForm" name="wfhForm">
						<input type="hidden" id="workFromHomeId" name="workFromHomeId"/>
						<input type="hidden" id="status" name="status"/>
						<div id="edit">
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">成員：</label>
								<div class="col-sm-12">
						            <div class="form-group">
										<select class="form-control" id="sysUserId" name="sysUserId" onchange="">
							            	
									    	<option value="">請選擇</option>
									        <c:forEach var="item" items="${mapEnableRule2User}" varStatus="vs">
												<option value="${item['key']}">${item['value'].originalName}</option>
											</c:forEach>
							            	
										</select>
										<span id="sysUserId-error" class="error_text"></span>
									</div>
								</div>	
							</div>

							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12"
									id="levelStr" name="levelStr">
									遠端級別：
								</label>
								<div class="col-sm-12">
									<select class="form-control" id="level" name="level">
										<option value="">請選擇</option>
										<option value="1">遠端一級 (３天)</option>
										<option value="2">遠端二級 (７天)</option>
										<option value="3">遠端三級 (１４天)</option>
										<option value="4">遠端四級 (１個月)</option>
										<option value="5">遠端五級 (超過１個月)</option>
									</select>
									<span id="level-error" class="error_text"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">公告開始時間：</label>
								<div class="col-sm-12">
									<div class="form-group">
										<div class="input-group date" id='datetimepickerStart'>
											<input id="workDate" name="workDate" class="form-control"
												size="16" type="text" value="" readonly="readonly" style="cursor: pointer;
												background-color: #fff;" />
											<span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span></span>
										</div>
									</div>
									<span id="workDate-error" class="error_text"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">公告結束時間：</label>
								<div class="col-sm-12">
									<input id="endTime" name="endTime" class="form-control" size="16"
										type="text" value="" readonly="readonly" />
									<span id="endTime-error" class="error_text"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">事由：</label>
								<div class="col-sm-12">
									<input type="text" class="form-control" id="description"
										name="description" />
									<span id="description-error" class="error_text"></span>
								</div>
							</div>

							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">主管：</label>
								<div class="col-sm-12">
									<input type="text" class="form-control" id="approvalBy"
										name="approvalBy" />
									<span id="approvalBy-error" class="error_text"></span>
								</div>
							</div>


							<!-- <div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">排班月份：</label>
					            <div class="col-sm-12">
						            <div class="form-group">
						                <div class="input-group date" id='datetimepickerEnable'>
						                    <input id="enableMonth" name="enableMonth" class="form-control" size="16" type="text" value=""/>
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						                </div>
						            </div>
						            <span id="enableMonth-error" class="error_text"></span>
					            </div>
							</div> -->
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
	<script src="<c:url value='/resources/datepicker/bootstrap-datepicker.js'/>" type="text/javascript"></script>
	
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData(1);
			var today = new Date();
			var ruleId = ${operator.roleId} ;
			var pickerStartDate = getNextFirstDay();
			if(ruleId == '1'){
				pickerStartDate = getFormattedDate(getCurrentFirstDay(), 'y/M/d H:m');
			}
			
			// $('#datetimepickerEnable').datepicker({
			// 	viewMode: "months", 
			//     minViewMode: "months",
	        //     format: 'yyyy/mm',
	        //     autoclose: true,
		    //     startDate: pickerStartDate
			// });


			$('#datetimepickerStart').datetimepicker({
				minView: "month",  //只顯示到日期，不顯示分秒
				format: 'yyyy/mm/dd',
				autoclose: true,
				// minuteStep: 30,
				focusOnShow: false,
				allowInputToggle: true,
				startDate: pickerStartDate,
				
			});

			$('#datetimepickerEnd').datetimepicker({
				minView: "month",  //只顯示到日期，不顯示分秒
				format: 'yyyy/mm/dd',
				autoclose: true,
				// minuteStep: 30,
				focusOnShow: false,
				allowInputToggle: true,
				startDate: pickerStartDate
			});
			$("#datetimepickerStart").on("changeDate", function (e) {
				$('#datetimepickerEnd').datetimepicker("setStartDate", e.date);
				calEndDateTime(e);

			});
		});

		function calEndDateTime(e) {
			if ($('#level').val() != '' && $('#workDate').val() != '') {
				var setEndDate = new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate());
				setEndDate.setHours(e.date.getHours());
				setEndDate.setMinutes(e.date.getMinutes());
				setEndDate.setDate(setEndDate.getDate() + parseInt($('#level').val()) - 1);
				setEndDate.setHours(19);
				setEndDate.setMinutes(0);
				// $('#endTime').val(getFormattedDate(setEndDate, "y/M/d H:m"));
				$('#endTime').val(getFormattedDate(setEndDate, "y/M/d"));
			}
		}

		$('#level').change(function () {
			$('#workDate').val('');
			$('#endTime').val('');
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
				url : "<c:url value='/security/WorkFromHome/ajaxDataLoading'/>",
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
			errorCode["1"] = "請選擇成員";
			errorCode["2"] = "請選擇遠端級別";
			errorCode["3"] = "請輸入時間";
			errorCode["4"] = "請輸入主管";
			var errors = {};
			
			if($('#sysUserId :selected').val() == ''){
				errors['sysUserId'] = 1;
			}else {
				$('#sysUserId-error').hide();
			}
			if ($('#level :selected').val() == '') {
				errors['level'] = 2;
			} else {
				$('#level-error').hide();
			}
			if ($('#workDate').val() == '') {
				errors['workDate'] = 3;
			} else {
				$('#workDate-error').hide();
			}
			if ($('#endTime').val() == '') {
				errors['endTime'] = 3;
			} else {
				$('#endTime-error').hide();
			}
			if ($('#approvalBy').val() == '') {
				errors['approvalBy'] = 4;
			} else {
				$('#approvalBy-error').hide();
			}


			
			var targetURL= "<c:url value='/security/WorkFromHome/add'/>";
			
			if(Object.keys(errors).length == 0){
				
				var data = $('#wfhForm').serialize();
				if(typeof($('#workFromHomeId').val()) != 'undefined' && $('#workFromHomeId').val() != ''){//easier
					targetURL= "<c:url value='/security/WorkFromHome/update'/>";
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
		
		function deleteData(id) {
			var targetURL = "<c:url value='/security/WorkFromHome/" + id + "'/>";

			$("body").css("cursor", "progress");
			$.ajax({
				type: "DELETE",
				url: targetURL,
				data: {
				},
				dataType: "json",
				success: function (data) {
					location.reload(true)
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
				$('#workFromHomeId').val("");
				$('#sysUserId').val("");
				$('#level').val("");
				$('#workDate').val("");
				$('#description').val("");
				$('#approvalBy').val("");
				$('#status').val("");
				$('#basicModal').find('.modal-title').text(text + "遠端作業紀錄");
				$('#basicModal').modal('toggle');
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/WorkFromHome/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						shiftId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#shiftId').val(data.sysUserShift.shiftId);
							$('#sysUserId').val(data.sysUserShift.sysUserId);
							$('#sysUserId').attr("disabled", true);
							$('#enableMonth').val(formatJsonDate(data.sysUserShift.enableMonth, "y/M"));
							$('#status').val(data.sysUserShift.status);
							$('#boardTime').val(data.sysUserShift.boardTime);
							$('#finishTime').val(data.sysUserShift.finishTime);
							selectedShift(data.sysUserShift.boardTime, data.sysUserShift.finishTime);
							
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
