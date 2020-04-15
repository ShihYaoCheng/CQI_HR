
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	
	<link href="<c:url value='/resources/fullcalendar/fullcalendar.min.css'/>" rel="stylesheet" />
	<link href="<c:url value='/resources/fullcalendar/fullcalendar.print.min.css'/>" rel="stylesheet" media="print" />
	
	<style>
	
	  body {
	    margin: 0;
	    padding: 0;
	    font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
	    font-size: 14px;
	  }
	
	  #script-warning {
	    display: none;
	    background: #eee;
	    border-bottom: 1px solid #ddd;
	    padding: 0 10px;
	    line-height: 40px;
	    text-align: center;
	    font-weight: bold;
	    font-size: 12px;
	    color: red;
	  }
	
	  #loading {
	    display: none;
	    position: absolute;
	    top: 10px;
	    right: 10px;
	  }
	
	  #calendar {
	    max-width: 900px;
	    margin: 40px auto;
	    padding: 0 10px;
	  }
	
	</style>
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>國定假日資料管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							國定假日資料匯入(https://data.gov.tw/dataset/14718)：
							<input type="file" id="excelFile" />
							<button type="button" id="uploadExcelFile" class="btn btn-primary" data-loading-text="檔案上傳中" onclick="ajaxUploadFile()"> 
								<i class="glyphicon glyphicon-upload"></i>上傳
							</button>
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id='loading'>loading...</div>

					<div id='calendar'></div>
					</div>
					
					<div id="dataContent">
					</div>
				</form>

			</div>
		</div>
	</div>
	
	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	
	<!-- dialog -->
	<div class="modal fade" id="basicModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">新增請假紀錄</h4>
				</div>
				<div class="modal-body">
					<form id="specialDateAboutWorkForm" name="specialDateAboutWorkForm">
						<input type="hidden" id="dateId" name="dateId"/>
						<input type="hidden" id="status" name="status"/>
						<div id="edit">
							<div class="form-group">
					            <label for="dayDesc" class="control-label col-sm-12">
					            	說明：
					            </label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="dayDesc" name="dayDesc" />
						            <span id="getIntoOfficesTime-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="theDay" class="control-label col-sm-12">
					            	日期：
					            </label>
					            <div class="col-sm-12">
					            	<div class='input-group date' id='datepickerTheDay'>
					                    <input id="theDay" name="theDay" class="form-control" size="10" type="text" value=""/>
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="theDay-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="getIntoOfficesTime" class="control-label col-sm-12">
					            	工作日/放假日
					            </label>
					            <div class="col-sm-12">
						            <select class="form-control" id="isWorkDay" name="isWorkDay">
						            	<option value="0" selected>放假日</option>
										<option value="1">工作日</option>
						            </select>
						            <span id="theDay-error" class="error_text"></span>
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

	<!-- javascript -->		
	<script src="<c:url value='/resources/fullcalendar/moment.min.js'/>"></script>
	<!-- <script src="<c:url value='/resources/fullcalendar/jquery.min.js'/>"></script> -->
	<script src="<c:url value='/resources/fullcalendar/fullcalendar.min.js'/>"></script>
	<script src="<c:url value='/resources/fullcalendar/zh-tw.js'/>"></script>
	
	<script>
		$(document).ready(function() {
			
			$('#datepickerTheDay').datetimepicker({
				format :'yyyy/mm/dd',
				autoclose: true, minView: 2
			});
			
			$('#calendar').fullCalendar({
				header : {
					left : 'prev,next today',
					center : 'title',
					right : 'month,agendaWeek,agendaDay'
				},
				editable : false,
				navLinks : true, // can click day/week names to navigate views
				eventLimit : true, // allow "more" link when too many events
				events : {
					url : "<c:url value='/security/specialDateAboutWork/ajaxCalendarDataLoading'/>",
					data : function() { // a function that returns an object
						return {
							
						};
					},
					error : function() {
						$('#script-warning').show();
					}
				},
				loading : function(bool) {
					$('#loading').toggle(bool);
				},
				timeFormat : 'H(:mm)' // uppercase H for 24-hour clock
			});
			
			queryData(1);
		});
	</script>
	
	<script type="text/javascript">
		
		function ajaxUploadFile(){
			var targetURL= "<c:url value='/security/specialDateAboutWork/uploadFile'/>";
			var formdata = new FormData();
			formdata.append("excelFile", excelFile.files[0]);
			$("#uploadExcelFile").button('loading');
			$.ajax({
				type : "POST",
				url : targetURL,
				data : formdata,
				contentType:false, //- 必须false才会自动加上正确的Content-Type
	            processData: false, //- 必须false才会避开jQuery对 formdata 的默认处理,XMLHttpRequest会对 formdata 进行正确的处理
				dataType: "json",
	            success : function(data) {
					if (data.success) {
						//queryData(1);
						alert("上傳成功");
					}else{
						alert(data.message);
					}
					$("#uploadExcelFile").button('reset');
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
			        sessionOvertimeMessage(msg);
			    }
			});
		}
		
		function queryData(page) {
			if(page == 0){
				return;
			}
			$('#dataContent').hide();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/specialDateAboutWork/ajaxDataLoading'/>",
				data : {
					page:page
				},
				success : function(data) {
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}
		
		function submit(){
			var errorCode = {};
			errorCode["1"] = "請輸入說明";
			errorCode["2"] = "請輸入日期";
			var errors = {};
			
			if($('#dayDesc').val() == ''){
				errors['dayDesc'] = 1;
			}
			if($('#theDay').val() == ''){
				errors['theDay'] = 2;
			}
			var targetURL= "<c:url value='/security/specialDateAboutWork/add'/>";
			
			if(Object.keys(errors).length == 0){
				var data = $('#specialDateAboutWorkForm').serialize();
				if(typeof($('#dateId').val()) != 'undefined' && $('#dateId').val() != ''){//easier
					targetURL= "<c:url value='/security/specialDateAboutWork/update'/>";
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
			var targetURL= "<c:url value='/security/specialDateAboutWork/"+id+"'/>";
			
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
			var targetURL= "<c:url value='/security/specialDateAboutWork/"+id+"'/>";
			
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
				$('#dateId').val("");
				$('#dayDesc').val("");
				$('#theDay').val("");
				$('#isWorkDay').val("");
				$('#status').val("");
				$('#basicModal').find('.modal-title').text(text + "國定假日資料設定");
				$('#basicModal').modal('toggle');
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/specialDateAboutWork/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						dateId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#dateId').val(data.specialDateAboutWork.dateId);
							$('#dayDesc').val(data.specialDateAboutWork.dayDesc);
							$('#theDay').val(formatJsonDate(data.specialDateAboutWork.theDay, "y/M/d"));
							$('#isWorkDay').val(data.specialDateAboutWork.isWorkDay);
							$('#status').val(data.specialDateAboutWork.status);
							$('#basicModal').find('.modal-title').text(text + "國定假日資料設定");
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
