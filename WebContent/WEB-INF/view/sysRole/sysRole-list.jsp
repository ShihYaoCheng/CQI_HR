
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-9 single_table">
				<form class="navbar-form">
					<h3><b>系統權限管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<input type="text" id="searchRoleName" class="form-control" placeholder="請輸入開發者名稱...">
							<span class="btn btn-default input-group-addon function_icon" onclick="queryData(1)">
								GO!
							</span>
						</div>
						<div class="btn-group">
							<a href="#" class="btn btn-default function_icon" onclick="active('insert')" title="新增"> 
								<i class="glyphicon glyphicon-plus"></i>
							</a>
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
					<h4 class="modal-title" id="myModalLabel">新增角色</h4>
				</div>
				<div class="modal-body">
					<form id="sysUserForm" name="sysUserForm">
						<div id="edit">
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">角色名稱:</label>
					            <div class="col-sm-12">
						            <input type="hidden" class="form-control" id="sysRoleId" name="sysRoleId"/>
						            <input type="text" class="form-control" id="roleName" name="roleName" onfocus="resetInput('roleName')"/>
						            <span id="roleName-error" class="error_text"></span>
					            </div>
							</div>
							
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">功能列表:</label>
					            <div class="col-sm-12">
					            	<div class="table-responsive">
										<table class="table table-striped">
											<thead>
												<tr>
													<th style="width:35%">模組名稱</th>
													<th style="width:45%">功能代稱</th>
													<th style="width:20%">權限狀況</th>
												</tr>
											</thead>
											<tbody>
											<c:forEach var="item" items="${sysFunctionList}" varStatus="vs">
												<tr >
													<td>
														${item.moduleName}
													</td>
													<td>
														${item.functionName}
													</td>
													<td>
														<input type="checkbox" name="sysFunctions" value="${item.functionId}">
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
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
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">刪除角色</h4>
				</div>
				<div class="modal-body">
					<span>are you sure??</span>
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
			queryData(1);
			$('.selectpicker').selectpicker({
			      style: 'btn-info',
			      size: 8
			  });
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
				url : "<c:url value='/security/sysRole/ajaxDataLoading'/>",
				data : {
					page:page,
					searchRoleName:$('#searchRoleName').val()
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
			var errorCode = {};
			errorCode["1"] = "Can not be empty";
			errorCode["2"] = "請輸入角色名稱";
			errorCode["3"] = "請選擇功能模組";
			var errors = {};
			var targetURL= "<c:url value='/security/sysRole/"+$("#sysRoleId").val()+"'/>";
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
							$("#roleName-error").text(data.message);
							$("#roleName-error").show();
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
		
		function resetPassword(id){
			var targetURL= "<c:url value='/security/sysUser/ajaxResetPasswrod'/>";
			
			$("body").css("cursor", "progress");
			$.ajax({
				type : "POST",
				url : targetURL,
				data : {
					sysUserId:id
				},
				dataType: "json",
				success : function(data) {
					$("body").css("cursor", "auto");
					if (data.success) {
						queryData(1);
						alert(data.message);
					}
					
				}
			});
		}
		
		function active(actionType, id){
			if(progressing == 1){
				return;
			}
			//$('input[name="mangaRoleTypes"]').attr("checked", false);
			
			var text = "修改";
			$("input[name='sysFunctions']").attr("checked",false);
			$('#roleName').val("");
			if(actionType == "insert"){
				$('#sysUserId').val("");
				$('#userName').val("");
				$('#password').val("");
				$('#status').val("");
				text = "新增";
				$('#basicModal').find('.modal-title').text(text+"角色");
				$('#basicModal').modal('toggle');
			}else if(actionType == "update"){
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/sysRole/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						sysRoleId:id
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#sysRoleId').val(data.sysRole.sysRoleId);
							$('#roleName').val(data.sysRole.roleName);
							for(var i = 0; i < data.sysRole.sysPrivilegeSet.length; i++){
								$(":checkbox[value="+data.sysRole.sysPrivilegeSet[i].functionId+"]").prop("checked", true);
							}
							$('#basicModal').find('.modal-title').text(text+"角色");
							$('#basicModal').modal('toggle');
							progressing = 0;
						}else{
							alert(data.message);
						}
						
					}
				});				
			}else if(actionType == "delete"){
				$('#deleteModal').modal('toggle');
			}
			
		}
		
		function resetInput(id){
			$("#"+id+"-error").text('');
			$("#"+id+"-error").hide();		
		}
	</script>
</body>
</html>
