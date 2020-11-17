
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-9 single_table">
				<form class="navbar-form">
					<h3><b>個人資料管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
						</div>
						<div class="btn-group">
						</div>

					</div>
					<div class="modal-body">
						<form id="sysUserForm" name="sysUserForm">
							<div id="edit">
								<input type="hidden" id="status" name="status" value="${sysUser.status}"/>
						        <input type="hidden" id="sysUserId" name="sysUserId" value="${sysUser.sysUserId}"/>
						        <div class="row" style="margin-top: 10px;">
						            <label for="originalName" class="control-label col-sm-12">姓名:</label>
						            <div class="col-sm-12">
							            <input type="text" class="form-control" id="originalName" name="originalName" onfocus="resetInput('originalName')" value="${sysUser.originalName}"/>
							            <span id="originalName-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="userName" class="control-label col-sm-12">暱稱:</label>
						            <div class="col-sm-12">
							            <input type="text" class="form-control" id="userName" name="userName" onfocus="resetInput('userName')" value="${sysUser.userName}"/>
							            <span id="userName-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="email" class="control-label col-sm-12">Email:</label>
						            <div class="col-sm-12">
							            <input type="text" class="form-control" id="email" name="email" onfocus="resetInput('email')" value="${sysUser.email}"/>
							            <span id="email-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="gender" class="control-label col-sm-12">性別:</label>
						            <div class="col-sm-12">
						            	<select class="form-control" id="gender" name="gender">
						            		<option value=""></option>
						            		<option value="M" ${sysUser.gender=='M'?'selected':''}>男</option>
						            		<option value="F" ${sysUser.gender=='F'?'selected':''}>女</option>
						            	</select>
							            <span id="gender-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="cardId" class="control-label col-sm-12">卡號:</label>
						            <div class="col-sm-12">
						            	<input type="text" class="form-control" id="cardId" name="cardId" value="${sysUser.cardId}"/>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="department" class="control-label col-sm-12">樓層:</label>
						            <div class="col-sm-12">
						            	<select class="form-control" id="department" name="department">
						            		<option value=""></option>
						            		<option value="26F" ${sysUser.department=='26F'?'selected':''}>26F</option>
						            		<option value="25F" ${sysUser.department=='25F'?'selected':''}>25F</option>
						            	</select>
							            <span id="department-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="inaugurationDate" class="control-label col-sm-12">報到日期:</label>
						            <div class="col-sm-12">
						            	<div class='input-group date' id='datepickerInaugurationDate'>
						                    <input type="text" class="form-control" id="inaugurationDate" name="inaugurationDate" size="10" type="text" value="${sysUser.inaugurationDate}"/>
						                    <span class="input-group-addon">
						                        <span class="glyphicon glyphicon-calendar"></span>
						                    </span>
						                </div>
							            <span id="inaugurationDate-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="graduationDate" class="control-label col-sm-12">離職日期:</label>
						            <div class="col-sm-12">
						            	<div class='input-group date' id='datepickerGraduationDate'>
						                    <input type="text" class="form-control" id="graduationDate" name="graduationDate" size="10" type="text" value="${sysUser.graduationDate}"/>
						                    <span class="input-group-addon">
						                        <span class="glyphicon glyphicon-calendar"></span>
						                    </span>
						                </div>
							            <span id="graduationDate-error" class="error_text"></span>
						            </div>
								</div>
								<div class="row" style="margin-top: 10px;">
						            <label for="defaultProjectId" class="control-label col-sm-12">Asana Project:</label>
						            <div class="col-sm-12">
						            	<select class="form-control" id="defaultProjectId" name="defaultProjectId" onchange="checkProjectPermission()">
						            		<option value="">請選擇</option>
							            	<c:forEach var="map" items="${projectMap}" varStatus="vs">
												<option value="${map.key}" ${sysUser.defaultProjectId==map.value?'selected':''}>${map.value}</option>
											</c:forEach>
						            	</select>
							            <span id="defaultProjectId-error" class="error_text"></span>
						            </div>
								</div>
							</div>
				        </form>
					</div>
					<div style="clear:both;"></div>
					<div class="modal-footer" style="margin-top:20px">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary" onclick="submit()">Save</button>
					</div>
				</form>

			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			
			$('#datepickerInaugurationDate').datetimepicker({
				format :'yyyy/mm/dd',
				autoclose: true, minView: 2
			});
			
			$('#datepickerGraduationDate').datetimepicker({
				format :'yyyy/mm/dd',
				autoclose: true, minView: 2
			});
		});
		
		
		function submit(){
			var errorCode = {};
			errorCode["1"] = "Can not be empty";
			errorCode["2"] = "Id duplicate";
			var errors = {};
			
			var userId = "";
			if($("#sysUserId").val() != ''){
				userId = $("#sysUserId").val();
			}
			var targetURL= "<c:url value='/security/sysUser/"+userId+"'/>";
			
			var userName = $("#userName").val();
			
			if(userName == ""){
				errors['userName'] = 1;
			}
			if($("#sysUserId").val() == ""){
				errors['sysUserId'] = 1;
			}
			if(Object.keys(errors).length == 0){
				var data = $('#sysUserForm').serialize();
				$("body").css("cursor", "progress");
				$.ajax({
					type : "POST",
					url : targetURL,
					data : data,
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							alert("修改成功");
						}else{
							alert(data.message);
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
		
		function checkProjectPermission(){
			resetInput("defaultProjectId");
			var targetURL= "<c:url value='/security/sysUser/checkProjectPermission'/>";
			$.ajax({
				type : "POST",
				url : targetURL,
				data : {
					sysUserId: $('#sysUserId').val(),
					projectId: $('#defaultProjectId').val()
				},
				dataType: "json",
				success : function(data) {
					console.log("Response : " +data);
					if (data.success) {
						$("#defaultProjectId-error").text('使用者有權限');
						$("#defaultProjectId-error").hide();
					}else{
						$("#defaultProjectId-error").text('使用者無權限');
						$("#defaultProjectId-error").show();
					}
				}
			});
		}
	</script>
</body>
</html>
