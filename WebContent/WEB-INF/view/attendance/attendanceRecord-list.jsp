
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
	<div class="container theme-showcase page-width" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>打卡資料管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							打卡紀錄匯入(每個檔案請以每個月的資料匯入)：
							<input type="file" id="excelFile" />
							<button type="button" id="uploadExcelFile" class="btn btn-primary" data-loading-text="檔案上傳中" onclick="ajaxUploadFile()"> 
								<i class="glyphicon glyphicon-upload"></i>上傳
							</button>
						</div>
						<div class="btn-group">
							資料查詢：
							<select class="form-control" id="sysUserId" name="sysUserId" onchange="">
				            	<option value="">請選擇</option>
				            	<c:forEach var="item" items="${userList}" varStatus="vs">
									<option value="${item.sysUserId}">${item.originalName}</option>
								</c:forEach>
							</select>
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id='loading'>loading...</div>

					<div id='calendar'></div>
					</div>
				</form>

			</div>
		</div>
	</div>
	
	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>

	<!-- javascript -->		
	<script src="<c:url value='/resources/fullcalendar/moment.min.js'/>"></script>
	<!-- <script src="<c:url value='/resources/fullcalendar/jquery.min.js'/>"></script> -->
	<script src="<c:url value='/resources/fullcalendar/fullcalendar.min.js'/>"></script>
	<script src="<c:url value='/resources/fullcalendar/zh-tw.js'/>"></script>
	
	<script>
		$(document).ready(function() {
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
					url : "<c:url value='/security/attendanceRecord/ajaxDataLoading'/>",
					data : function() { // a function that returns an object
						return {
							sysUserId : $('#sysUserId').val(),
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
			
			$('#sysUserId').change(function () {
		        $('#calendar').fullCalendar('refetchEvents');
		    });
		});
	</script>
	
	<script type="text/javascript">
		
		function ajaxUploadFile(){
			var targetURL= "<c:url value='/security/attendanceRecord/uploadFile'/>";
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
		
		function resetInput(id){
			$("#"+id+"-error").text('');
			$("#"+id+"-error").hide();		
		}
	</script>
</body>
</html>
