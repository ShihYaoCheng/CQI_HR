<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	<!-- jQuery v1.9.1 -->
	<script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
	<!-- DataTables v1.10.16 -->
	<link href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css" rel="stylesheet" />
	<script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
</head>
<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>日結紀錄</b></h3>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<select id="historyYear" name="historyYear">
							</select>年
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
							</select>月
						</div>
						<br/>
						<div class="btn-group">
							<button type="button" style="cursor:pointer;" class="btn btn-primary btn-lg " data-loading-text="查詢中" onclick="queryData()">查詢</button>
						</div>
					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
					
				</form>

			</div>
		</div>
	</div>
	
	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		
		function queryData() {
			
			$("body").css("cursor", "progress");
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/DailyAttendanceRecords/manager/ajaxDataLoading'/>",
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
		jQuery().ready(function (){
			var startYear = 2021;
			var nowYear = new Date().getFullYear();
			var nowDate = new Date();
			nowDate.setMonth(nowDate.getMonth());
			for(var i=0;i<=(nowYear - startYear);i++){
				var insertYear = startYear + i;
				$("#historyYear").append($("<option></option>").attr("value", insertYear).text(insertYear));
				if(nowDate.getFullYear()==insertYear){
					$("#historyYear option").get(i).selected = true;
				}
			}
			$("#historyMonth option").get(nowDate.getMonth()).selected = true;
		});
	</script>
</body>	
</html>