
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
			<div class="col-xs-12 col-md-9 single_table">
				<form class="navbar-form">
					<h3><b>請假總計</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div>
							<h3><fmt:formatDate pattern="yyyy年MM月" value="${nowCalendar.getTime()}" /></h3>
						</div>
					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
					
					<h3><b>月結統計搜尋</b></h3>
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
						</div>
						<div class="btn-group">
							<button type="button" style="cursor:pointer;" class="btn btn-primary btn-lg " data-loading-text="查詢中" onclick="queryHistory()">查詢</button>
						</div>

					</div>				
					<div id="dataContentHistory">
					</div>
				</form>

			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
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
			
			queryData(1);
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
				url : "<c:url value='/security/userLeaveNew/ajaxDataLoading'/>",
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}
		
		function queryHistory(){
			$("body").css("cursor", "progress");
			$('#dataContentHistory').hide();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/userLeaveNew/ajaxDataLoadingHistory'/>",
				data : {
					year:$('#historyYear').val(),
					month:$('#historyMonth').val()
				},
				success : function(data) {
					$("body").css("cursor", "auto");
					$('#dataContentHistory').html(data);
					$('#dataContentHistory').show();
				}
			});
		}
	</script>
</body>
</html>
