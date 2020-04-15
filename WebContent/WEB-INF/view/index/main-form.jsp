
<!DOCTYPE html>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<html>
	<head>
	   	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	</head>
	<style type="text/css">
	html, body{
	    height:100%; /* important to vertically align the container */
	    margin:0;
	    padding:0;
	}
	.vertical-center {
		min-height: 100%;  /* Fallback for browsers do NOT support vh unit */
		min-height: 100vh; /* These two lines are counted as one 🙂       */
		display: flex;
		align-items: center;
	}
	</style>
	<body role="document">

    <%@include file="../include/menu.jsp"%>

    <div class="container theme-showcase page-width page-height" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			<!--/span-->
			<div class="col-lg-12 col-lg-12 single_table" style="margin-top:100px;">
				<form class="navbar-form">
					<h3><b></b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
						</div>
						<div class="btn-group">
							<c:if test="${sessionScope.__session_info.logInInfo.roleId == 2}">
								<button type="button" class="btn btn-primary btn-lg " id="gotoLeave" data-loading-text="前往請假資料中" onclick="goToLeave()">前往請假資料</button>
								<button type="button" class="btn btn-primary btn-lg " id="gotoOvertime" data-loading-text="前往調班資料中" onclick="goToOvertime()">前往調班資料</button>
							</c:if>
							<c:if test="${sessionScope.__session_info.logInInfo.roleId == 1}">
								<button type="button" class="btn btn-primary btn-lg " id="gotoMonthlySummary" data-loading-text="前往出勤月總計中" onclick="goToMonthlySummary()">前往出勤月總計</button>
								<button type="button" class="btn btn-primary btn-lg " id="gotoMonthlyQuota" data-loading-text="前往月結剩餘額度中" onclick="goToMonthlyQuota()">前往月結剩餘額度</button>
								<button type="button" class="btn btn-primary btn-lg " id="gotoCheckIn" data-loading-text="前往打卡紀錄" onclick="goToCheckIn()">前往打卡紀錄</button>
							</c:if>
						</div>
		
					</div>
					<iframe style="margin-top:20px;" width="100%" height="800px" src="<c:url value='/security/askLeave/calendar'/>">
					</iframe>
				</form>
			</div>
		</div>
		<hr>
		<footer>
			<p>&copy; CQI 2018</p>
		</footer>
    </div> <!-- /container -->
    <%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
    <script type="text/javascript">
		function goToLeave(){
			$('#gotoLeave').button('loading');
			location.href="<c:url value='/security/askLeave'/>";
		}
		
		function goToOvertime(){
			$('#gotoOvertime').button('loading');
			location.href="<c:url value='/security/askOvertime'/>";
		}
		
		function goToMonthlySummary(){
			$('#gotoMonthlySummary').button('loading');
			location.href="<c:url value='/security/userLeaveHistory/manager'/>";
		}
		
		function goToMonthlyQuota(){
			$('#gotoMonthlyQuota').button('loading');
			location.href="<c:url value='/security/userLeaveQuotaMonthly/manager'/>";
		}
		
		function goToCheckIn(){
			$('#gotoCheckIn').button('loading');
			location.href="<c:url value='/security/attendanceRecord'/>";
		}
	</script>
  </body>
</html>
