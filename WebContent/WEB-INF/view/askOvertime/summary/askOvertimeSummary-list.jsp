
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
</head>

<body role="document">
	<%@include file="../../include/menu.jsp"%>
	<div class="container theme-showcase page-width" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-9 single_table">
				<form class="navbar-form">
					<h3><b>調班總計</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div>
							<h3><fmt:formatDate pattern="yyyy年MM月" value="${nowCalendar.getTime()}" /></h3>
						</div>
					</div>
					<%@include file="../../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
				</form>

			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData();
			
		});
		
		function queryData() {
			$("body").css("cursor", "progress");
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/askOvertime/summary/ajaxDataLoading'/>",
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
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
