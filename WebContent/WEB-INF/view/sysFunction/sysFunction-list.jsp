
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
</head>

<body role="document">
	<%@include file="/WEB-INF/view/include/menu.jsp"%>
	<div class="container">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-9 single_table">
				<form class="navbar-form">
					<h3><b>功能管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<input type="text" id="searchRoleName" class="form-control" placeholder="請輸入功能名稱...">
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
					<h4 class="modal-title" id="myModalLabel">新增功能</h4>
				</div>
				<div class="modal-body">
					<form id="sysFunctionForm" name="sysFunctionForm">
						<div id="edit">
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">模組名稱:</label>
					            <div class="col-sm-12">
						            <input type="hidden" class="form-control" id="functionId" name="functionId"/>
						            <input type="text" class="form-control" id="moduleName" name="moduleName" onfocus="resetInput('moduleName')"/>
						            <span id="moduleName-error" class="error_text"></span>
					            </div>
							</div>
							
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">次模組名稱:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="subModuleName" name="subModuleName" onfocus="resetInput('subModuleName')"/>
						            <span id="subModuleName-error" class="error_text"></span>
					            </div>
							</div>
							
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">功能名稱:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="functionName" name="functionName" onfocus="resetInput('functionName')"/>
						            <span id="functionName-error" class="error_text"></span>
					            </div>
							</div>
							
							<div class="form-group">
					            <label for="recipient-name" class="control-label col-sm-12">功能連結:</label>
					            <div class="col-sm-12">
						            <input type="text" class="form-control" id="functionUrl" name="functionUrl" onfocus="resetInput('functionUrl')"/>
						            <span id="functionUrl-error" class="error_text"></span>
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
	<%@include file="/WEB-INF/view/include/deleteModal.jsp"%>
	

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">
		var progressing = 0;
		var url = "sysFunction";
		
		jQuery().ready(function (){
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
				url : "<c:url value='/security/sysFunction/ajaxDataLoading'/>",
				data : {
					page:page,
					searchFunctioname:$('#searchFunctioname').val()
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
			errorCode["2"] = "Id duplicate";
			var errors = {};
			
			var targetURL= "<c:url value='/security/sysFunction/"+$("#functionId").val()+"'/>";
			
			if(Object.keys(errors).length == 0){
				var data = $('#sysFunctionForm').serialize();
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
							$("#roleName-error").text(errorCode['2']);
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

		function active(actionType, id){
			if(progressing == 1){
				return;
			}
			//$('input[name="mangaRoleTypes"]').attr("checked", false);
			
			var text = "修改";
			$("input[name='sysFunctions']").attr("checked",false);
			$('#roleName').val("");
			if(actionType == "insert"){
				$('#moduleName').val("");
				$('#subModuleName').val("");
				$('#functionName').val("");
				$('#functionUrl').val("");
				text = "新增";
				$('#basicModal').find('.modal-title').text(text+"功能");
				$('#basicModal').modal('toggle');
			}else if(actionType == "update"){
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/sysFunction/"+id+"'/>";
				$.ajax({
					type : "GET",
					url : targetURL,
					data : {
						
					},
					dataType: "json",
					success : function(data) {
						$("body").css("cursor", "auto");
						if (data.success) {
							$('#functionId').val(data.sysFunction.functionId);
							$('#moduleName').val(data.sysFunction.moduleName);
							$('#subModuleName').val(data.sysFunction.subModuleName);
							$('#functionName').val(data.sysFunction.functionName);
							$('#functionUrl').val(data.sysFunction.functionUrl);
							$('#basicModal').find('.modal-title').text(text+"功能");
							$('#basicModal').modal('toggle');
							progressing = 0;
						}else{
							alert(data.message);
						}
						
					}
				});				
			}else if(actionType == "delete"){
				$('#deleteId').val(id);
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
