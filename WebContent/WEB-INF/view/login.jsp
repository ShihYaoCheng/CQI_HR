<!DOCTYPE html>
<%@include file="include/view-lib.jsp"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
.loader {
  border: 16px solid #f3f3f3;
  border-radius: 50%;
  border-top: 16px solid #3498db;
  width: 120px;
  height: 120px;
  -webkit-animation: spin 2s linear infinite; /* Safari */
  animation: spin 2s linear infinite;
}

/* Safari */
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
<body>
	
	<div class="vertical-center">
		<div class="container" style="text-align:center;">
			<h1 class="form-heading">CQI出勤系統</h1>
			<div class="login-form">
				<br>
				<div class="main-div">
					<button type="button" class="btn btn-primary btn-lg " id="load" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> 登入中" onclick="loginAsana()" >使用Asana登入</button>
			    </div>
			    <br>
			</div>
			<%@include file="/WEB-INF/view/include/footer.jsp"%>
	    	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
		</div>
	</div>
		
	<script type="text/javascript">
		function loginAsana(){
			$('#load').button('loading');
			
			targetURL= "<c:url value='/loginAsana'/>";
			
			$.ajax({
				type : "GET",
				dataType: 'json',
				url : targetURL,
				success : function(data) {
					if (data.success) {
						location.href=data.message;
					}else{
						if(data.message != ''){
							alert(data.message);
						}else{
							alert("未知錯誤");
						}
						$('#load').button('reset');
					}
				}
			});
		}
	</script>
</body>
</html>
