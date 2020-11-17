<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container page-width">
		<div class="navbar-header">
		
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url value='/security/index'/>">CQI</a>
		</div>
		<div class="self-function">
			<ul class="nav navbar-nav basic-set-nav" style="">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle basic-set" data-toggle="dropdown" style="">Hello, ${userName}<span class="caret color-white"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li>
							<a class="color-white" style="cursor:pointer;" href="<c:url value='/security/sysUser/u/${userId }'/>">我的帳號</a>
						</li>
						<li>
							<a class="color-white" style="cursor:pointer;" href="<c:choose><c:when test='${sessionScope.__session_info.logInInfo.roleId == 1}'><c:url value='/logoutManager'/></c:when><c:otherwise><c:url value='/logout'/></c:otherwise></c:choose>">登出</a>
			    		</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="navbar-collapse collapse"><!--/.nav-collapse -->
			<ul class="nav navbar-nav">
				<c:forEach var="menu" items="${userMenu}" varStatus="vs">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">${menu.key}<span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<c:forEach var="functionMap" items="${menu.value}" varStatus="vs">
								<c:if test="${vs.index != 0}">
									<li class="divider"></li>
								</c:if>
								<li class="dropdown-header">${functionMap.key}</li>
								<c:forEach var="menuList" items="${functionMap.value}" varStatus="vs">
									<li><a href="<c:url value='${menuList.functionUrl}'/>">${menuList.functionName}</a></li>
								</c:forEach>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
			</ul>
			
		</div>
		
	</div>
	
</nav>

<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">基本資料設定</h4>
			</div>
			<div class="modal-body">
				<form id="userForm" name="userForm">
					<div id="edit">
						<div class="form-group">
				            <label for="recipient-name" class="control-label col-sm-12">使用者名稱:</label>
				            <div class="col-sm-12">
					            <input type="text" class="form-control" id="selfUserName" name="selfUserName" onfocus="resetInput('selfUserName')" value="${userName}"/>
					            <span id="selfUserName-error" class="error_text"></span>
				            </div>
						</div>
					</div>
		        </form>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="margin-top:20px">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" onclick="updateUserInfo('name')">Save</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog" aria-labelledby="passwordModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">修改密碼</h4>
			</div>
			<div class="modal-body">
				<form id="passwordForm" name="passwordForm">
					<div id="edit">
						<div class="form-group">
				            <div class="col-sm-12 control-label">
				            	<input type="password" class="form-control" id="selfPassword" name="selfPassword" onfocus="resetInput('selfPassword')" placeholder="輸入目前密碼"/>
				            	<input type="password" class="form-control" style="margin-top:15px;" id="newPassword" name="selfPassword" onfocus="resetInput('newPassword')" placeholder="輸入新密碼"/>
				            	<input type="password" class="form-control" style="margin-top:15px;" id="checkPassword" name="selfPassword" onfocus="resetInput('checkPassword')" placeholder="再次輸入新密碼"/>
				            	<span id="selfPassword-error" class="error_text"></span>
				            </div>
			            </div>
					</div>
		        </form>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="margin-top:20px">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" onclick="updateUserInfo('password')">Save</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	function updateUserInfo(actionType){
		var errorCode = {};
		errorCode["1"] = "Can not be empty";
		errorCode["2"] = "new password and re-entered new password are not equal";
		errorCode["3"] = "old password is incorrect";
		var errors = {};
		
		
		var selfUserName = $('#selfUserName').val();
		var selfPassword = $('#selfPassword').val();
		var newPassword = $('#newPassword').val();
		var checkPassword = $('#checkPassword').val();
		
		if(actionType == "name"){
			if(selfUserName == ''){
				errors['selfUserName'] = 1;
			}
		}else{
			if(selfPassword == '' || newPassword == '' || checkPassword == ''){
				errors['selfPassword'] = 1;
			}
			else if(newPassword != checkPassword){
				errors['selfPassword'] = 2;
			}
		}
		if(Object.keys(errors).length == 0){
			targetURL= "<c:url value='/security/sysUser/updateUserInfo'/>";
		
			$.ajax({
				type : "POST",
				url : targetURL,
				data : {
					userName:selfUserName,
					password:selfPassword,
					newPassword:newPassword
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					
					if(actionType == "name"){
						if (data.success) {
							$('#menuUserName').text(selfUserName);
							$('#userModal').modal('hide');
						}else{
							errorCode["3"] = data.message;
							$("#selfUserName-error").text(errorCode['3']);
							$("#selfUserName-error").show();
						}
					}else{
						if (data.success) {
							$('#passwordModal').modal('hide');
						}else{
							errorCode["3"] = data.message;
							$("#selfPassword-error").text(errorCode['3']);
							$("#selfPassword-error").show();
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

	function resetInput(id){
		$("#"+id+"-error").text('');
		$("#"+id+"-error").hide();		
	}
</script>