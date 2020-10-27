
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
			<div class="col-xs-12 col-md-12 single_table" style="margin-top: 100px;">
				<h3><b>打卡資料</b></h3>
				<br/>
				<div class="form-group" style="display: inline;">
					
				</div>
				<%@include file="../include/progressing.jsp"%>					
				<div id='loading'>loading...</div>

				<div id='calendar'></div>
				</div>
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
							sysUserId : "",
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
		});
	</script>
</body>
</html>
