
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
					<h3><b>人員管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<input type="text" id="searchUserName" class="form-control" placeholder="請輸入人員名稱...">
							<span class="btn btn-default input-group-addon function_icon" onclick="queryData(1)">
								GO!
							</span>
						</div>
						<div class="btn-group">
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
				</form>

			</div>
		</div>
	</div>
	
	<!-- dialog -->
	<div class="modal fade" id="basicModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">修改人員</h4>
				</div>
				<div class="modal-body">
					<form id="sysUserForm" name="sysUserForm">
						<div id="edit">
							<input type="hidden" id="status" name="status"/>
					        <input type="hidden" id="sysUserId" name="sysUserId"/>
					        <div class="form-group">
					            <label for="originalName" class="control-label col-sm-12">姓名:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="originalName" name="originalName" onfocus="resetInput('originalName')"/>
						            <span id="originalName-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="userName" class="control-label col-sm-12">暱稱:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="userName" name="userName" onfocus="resetInput('userName')"/>
						            <span id="userName-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="email" class="control-label col-sm-12">暱稱:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="email" name="email" onfocus="resetInput('email')"/>
						            <span id="email-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="gender" class="control-label col-sm-12">性別:</label>
					            <div class="col-sm-12">
					            	<select class="form-control" id="gender" name="gender">
					            		<option value=""></option>
					            		<option value="M">男</option>
					            		<option value="F">女</option>
					            	</select>
						            <span id="gender-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="department" class="control-label col-sm-12">樓層:</label>
					            <div class="col-sm-12">
					            	<select class="form-control" id="department" name="department">
					            		<option value=""></option>
					            		<option value="26F">26F</option>
					            		<option value="25F">25F</option>
					            	</select>
						            <span id="department-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="inaugurationDate" class="control-label col-sm-12">報到日期:</label>
					            <div class="col-sm-12">
					            	<div class='input-group date' id='datepickerInaugurationDate'>
					                    <input type="text" class="form-control" id="inaugurationDate" name="inaugurationDate" size="10" type="text" value=""/>
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="inaugurationDate-error" class="error_text"></span>
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
				url : "<c:url value='/security/sysUser/ajaxDataLoading'/>",
				data : {
					page:page,
					searchUserName:$('#searchUserName').val()
				},
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}
		
		function submit(){
			alert(1)
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
							queryData(1);
							
							$('#basicModal').modal('hide');
						}else{
							$("#sysUserId-error").text(errorCode['2']);
							$("#sysUserId-error").show();
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

		function deleteData(id){
			var targetURL= "<c:url value='/security/sysUser/"+id+"'/>";
			
			$("body").css("cursor", "progress");
			$.ajax({
				type : "DELETE",
				url : targetURL,
				data : {
					
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						queryData(1);
					}
					
				}
			});
		}
				
		function active(id){
			if(progressing == 1){
				return;
			}
			//$('input[name="mangaRoleTypes"]').attr("checked", false);
			
			var text = "修改";
			if(typeof(id) == "undefined"){
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/sysUser/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						sysUserId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#sysUserId').val(data.sysUser.sysUserId);
							$('#status').val(data.sysUser.status);
							$('#originalName').val(data.sysUser.originalName);
							$('#userName').val(data.sysUser.userName);
							$('#email').val(data.sysUser.email);
							$('#gender').val(data.sysUser.gender);
							$('#department').val(data.sysUser.department);
							$('#inaugurationDate').val(formatJsonDate(data.sysUser.inaugurationDate, "y/M/d"));
							$('#basicModal').find('.modal-title').text(text+"開發者");
							$('#basicModal').modal('toggle');
							progressing = 0;
						}else{
							alert(data.message);
						}
						
					}
				});				
			}	
			
		}
		
		function resetInput(id){
			$("#"+id+"-error").text('');
			$("#"+id+"-error").hide();		
		}
	</script>
</body>
</html>
