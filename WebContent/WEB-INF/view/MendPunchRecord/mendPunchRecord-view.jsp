
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
					<h3><b>補卡系統</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							
						</div>
						<div class="btn-group">
							<a href="#" class="btn btn-default function_icon" onclick="add()" title="新增"> 
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
					<h4 class="modal-title" id="myModalLabel">補卡紀錄申請</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" id="unitType" name="unitType"/>
					<form id="mendForm" name="mendForm">
						<input type="hidden" id="mendId" name="mendId"/>
						<div id="edit">
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">補卡人員：</label>
								<div class="col-sm-12">
						            <div class="form-group">
										<select class="form-control" id="sysUserId" name="sysUserId" onchange="">
							            	<c:choose>
											    <c:when test="${operator.roleId == '1'}">
											    	<option value="">請選擇</option>
											        <c:forEach var="item" items="${mapEnableRule2User}" varStatus="vs">
														<option value="${item['key']}">${item['value'].originalName}</option>
													</c:forEach>
											    </c:when>    
											    <c:otherwise>
											        <option value="${operator.sysUserId}">${operator.originalName}</option>
											    </c:otherwise>
											</c:choose>
							            	
										</select>
										<span id="sysUserId-error" class="error_text"></span>
									</div>
								</div>	
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">申請時間：</label>
					            <div class="col-sm-12">
						            <div class="form-group">
						                <div class="input-group date" id='datetimepickerMend'>
						                    <input id="mendPunchTime" name="mendPunchTime" class="form-control" size="16" type="text" value=""/>
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
						                </div>
						                <span id="mendPunchTime-error" class="error_text"></span>
						            </div>
						            
					            </div>
							</div>
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">申請事由：</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="mendReason" name="mendReason" />
						            <span id="mendReason-error" class="error_text"></span>
					            </div>
					            
							</div>
						</div>
			        </form>
				</div>
				<div style="clear:both;"></div>
				<div class="modal-footer" style="margin-top:20px">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary btn-lg " id="save" data-loading-text="儲存中" onclick="submit()">申請</button>
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
			$('#datetimepickerMend').datetimepicker({
				format :'yyyy/mm/dd hh:ii',
				autoclose: true,
				minuteStep:5,
		        focusOnShow: false,
		        allowInputToggle: true,
		        startDate: pickerStartDate,
		        endDate: new Date()
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
				url : "<c:url value='/security/MendPunchRecord/ajaxDataLoading'/>",
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
			errorCode["1"] = "請選擇補卡人員";
			errorCode["2"] = "請輸入申請時間";
			errorCode["3"] = "請輸入申請事由";
			errorCode["4"] = "時間似乎怪怪的，請確認";
			var errors = {};
			
			if($('#sysUserId :selected').val() == ''){
				errors['sysUserId'] = 1;
			}
			if($('#mendPunchTime').val() == ''){
				errors['mendPunchTime'] = 2;
			}
			if($('#mendReason').val() == ''){
				errors['mendReason'] = 3;
			}
			var targetURL= "<c:url value='/security/MendPunchRecord/add'/>";
			
			if(Object.keys(errors).length == 0){
				var data = $('#mendForm').serialize();
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
			var targetURL= "<c:url value='/security/MendPunchRecord/"+id+"'/>";
			
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
		
		function provedData(id){
			var targetURL= "<c:url value='/security/MendPunchRecord/"+id+"'/>";
			
			$("body").css("cursor", "progress");
			$.ajax({
				type : "GET",
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
		
		function add(){
			if(progressing == 1){
				return;
			}
			
			var text = "新增";
			$('#mendId').val("");
			$('#sysUserId').val("");
			$('#mendPunchTime').val("");
			$('#mendReason').val("");
			$('#approvedStatus').val("");
			$('#basicModal').find('.modal-title').text(text + "補卡紀錄");
			$('#basicModal').modal('toggle');
			
		}
	</script>
</body>
</html>
