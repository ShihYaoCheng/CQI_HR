
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	<link href="<c:url value='/resources/fullcalendar/fullcalendar.min.css'/>" rel="stylesheet" />
	<link href="<c:url value='/resources/fullcalendar/fullcalendar.print.min.css'/>" rel="stylesheet" media="print" />
</head>

<body role="document">
	<div class="container theme-showcase" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>災害處理單簽核</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<select id="historyYear" name="historyYear">
							</select>
							年
							<select id="historyMonth" name="historyMonth">
								<option value="1" selected>1</option>
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
							月
						</div>
						<div class="btn-group">
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
					
					<div id='calendar'></div>
				</form>

			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	
	<script src="<c:url value='/resources/fullcalendar/moment.min.js'/>"></script>
	<script src="<c:url value='/resources/fullcalendar/jquery.min.js'/>"></script>
	<script src="<c:url value='/resources/fullcalendar/fullcalendar.min.js'/>"></script>
	<script src="<c:url value='/resources/fullcalendar/zh-tw.js'/>"></script>
	
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData();
			var startYear = 2019;
			var nowYear = new Date().getFullYear();
			var nowDate = new Date();
			nowDate.setMonth(nowDate.getMonth()-1);
			for(var i=0;i<=(nowYear - startYear);i++){
				var insertYear = startYear + i;
				$("#historyYear").append($("<option></option>").attr("value", insertYear).text(insertYear));
				if(nowDate.getFullYear()==insertYear){
					$("#historyYear option").get(i).selected = true;
				}
			}
			$("#historyMonth option").get(nowDate.getMonth()).selected = true;
			
			$("#historyYear").change(function() {
	            queryData();
	        });
	        $("#historyMonth").change(function() {
	        	queryData();
	        });
		});
		
		function queryData() {
			$("body").css("cursor", "progress");
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/emergence/ajaxDataLoading'/>",
				data : {
					year:$('#historyYear').val(),
					month:$('#historyMonth').val()
				},
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}
		
		function sign(level, sign, id){
			if(progressing == 1){
				return;
			}
			progressing = 1;
			$("body").css("cursor", "progress");
			var targetURL= "<c:url value='/security/emergence/" + sign + "/" + level + "/" + id+"' />";
			$.ajax({
				type : "POST",
				url : targetURL,
				data : {},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						progressing = 0;
						queryData();
					}else{
						alert(data.message);
					}
					
				}
			});
		}
		
		function showCalendar(id){
			if($('#calendar').children().length<1){
				$('#calendar').fullCalendar({
					header: {
				        left: 'prev,next today',
				        center: 'title',
				        right: 'month,agendaWeek,agendaDay'
					},
					editable: false,
					navLinks: true, // can click day/week names to navigate views
					eventLimit: true, // allow "more" link when too many events
					events: {
				        url: "<c:url value='/security/askLeave/calendar/ajaxDataLoading'/>",
				        method: 'POST',
				        data: {
				            userId: id
						},
				        error: function() {
							$('#script-warning').show();
						}
					},
					loading: function(bool) {
			        	$('#loading').toggle(bool);
					},
					timeFormat: 'H(:mm)' // uppercase H for 24-hour clock
				});
			}else{
				$('#calendar').fullCalendar('refetchEventSources', {
			        url: "<c:url value='/security/askLeave/calendar/ajaxDataLoading'/>",
			        method: 'POST',
			        data: {
			            userId: id
					},
			        error: function() {
						$('#script-warning').show();
					}
				} )
			}
			
		}
	</script>
</body>
</html>
