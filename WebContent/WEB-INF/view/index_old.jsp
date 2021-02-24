<!DOCTYPE html>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
	   	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	</head>

	<body role="document">

	    <div class="container theme-showcase page-width page-height" role="main">
			<div class="row row-offcanvas row-offcanvas-left">
				
				<!--/span-->
				<div class="col-xs-12 col-md-9 single_table">
					<h3><b>HR Manage</b></h3>
					<h4>版本:2021.2.20</h4>
					<hr style="margin-top:0px;">				
					<div id="dataContent">
						<div id="edit">
								<div class="form-group">
						            <label for="recipient-name" class="control-label col-sm-12">帳號</label>
						            <div class="col-sm-12">
							            <input type="text" class="form-control" id="userId" name="userId" onfocus="resetInput('userId')" placeholder=""/>
							            <span id="userId-error" class="error_text"></span>
						            </div>
								</div>
								<div class="form-group">
						            <label for="recipient-name" class="control-label col-sm-12">密碼</label>
						            <div class="col-sm-12">
							            <input type="password" class="form-control" id="password" name="password" onfocus="resetInput('password')" placeholder=""/>
							            <span id="password-error" class="error_text"></span>
						            </div>
								</div>
								<!-- 
								<div class="form-group">
						            <div class="col-sm-12" style="margin-top:10px">
							           <a style="cursor:pointer;" href="<c:url value='/forgot'/>"><span class="glyphicon glyphicon-lock" aria-hidden="true" style="font-size:11px;"></span>&nbsp;forgot password</a>
						            </div>
								</div>
								 -->
								<div class="form-group">
						            <div class="col-sm-12" style="margin-top:10px">
							          <button class="btn btn-success" style="" onclick="login();">登入</button>
						            </div>
								</div>
							</div>
					</div>
				</div>
			</div>
	    </div> <!-- /container -->
		<%@include file="/WEB-INF/view/include/footer.jsp"%>
	    <%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	    <script type="text/javascript">
		    $('#password').keypress(function( e ) {
		        if(e.which == 13) {
		        	login();
		        }
		    });
			function login(){
				var errorCode = {};
				errorCode["1"] = "Can not be empty";
				errorCode["2"] = "id or password incorrect";
				var errors = {};
				
				
				var userId = $('#userId').val();
				var password = $('#password').val();
				
				
				if(userId == ''){
					errors['userId'] = 1;
				}
	
				if(password == ''){
					errors['password'] = 1;
				}else{
					password = md5(password);
				}
				
				if(Object.keys(errors).length == 0){
					targetURL= "<c:url value='/login'/>";
				
					$.ajax({
						type : "POST",
						url : targetURL,
						data : {
							userId:userId,
							password:password
						},
						dataType: "json",
						success : function(data) {
							$("body").css("cursor", "auto");
							if (data.success) {
								location.href="<c:url value='/"+data.message+"'/>";
							}else{
								if(data.message != ''){
									$("#nonActive").show();
									alert(data.message);
								}else{
									$("#nonActive").hide();
									$('#password-error').text(errorCode[2]);
									$('#password-error').show();
								}
								
							}
						}
					});
				}
				for(var key in errors){
					//$("#"+key).css('width',90);
					$("#"+key+"-error").text(errorCode[errors[key]]);
					$("#"+key+"-error").show();
				}
			}			
		</script>
	</body>
</html>