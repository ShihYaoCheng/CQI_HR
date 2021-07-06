
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
					
					<!-- 員工編號/姓名/cardId -->
					<div id="test" style="display: none;">
						<table class="table table-striped">
							<tbody>
								<c:forEach var="item" items="${SysUserList}" varStatus="vs">
									<tr >
										<td>
											${item.sysUserId}
										</td>
										<td class="tdName">
											${item.originalName}
										</td>
										<td class="tdCardId">
											${item.cardId}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
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
					            <label for="email" class="control-label col-sm-12">Email:</label>
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
								<label for="cardId" class="control-label col-sm-12">卡號:</label>
								<div class="col-sm-12">
									<input type="text" class="form-control" id="cardId" name="cardId" />
								</div>
								<!-- <span>請前往卡號原始資料查詢無對應的卡號，離職人員請將卡號清除避免卡號重複</span> -->
								<span id="cardId-error" class="error_text" style="padding-left: 15px;"></span>

							</div>

							<div class="form-group">
					            <label for="department" class="control-label col-sm-12">樓層:</label>
					            <div class="col-sm-12">
					            	<select class="form-control" id="department" name="department">
					            		<option value=""></option>
					            		<option value="26F">26F</option>
					            		<option value="25F">25F</option>
					            		<option value="5F">5F</option>
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
							<div class="form-group">
					            <label for="graduationDate" class="control-label col-sm-12">離職日期:</label>
					            <div class="col-sm-12">
					            	<div class='input-group date' id='datepickerGraduationDate'>
					                    <input type="text" class="form-control" id="graduationDate" name="graduationDate" size="10" type="text" value=""/>
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="graduationDate-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group">
					            <label for="defaultProjectId" class="control-label col-sm-12">Asana Project:</label>
					            <div class="col-sm-12">
					            	<select class="form-control" id="defaultProjectId" name="defaultProjectId" onchange="checkProjectPermission()">
					            		<option value="">請選擇</option>
						            	<c:forEach var="map" items="${projectMap}" varStatus="vs">
											<option value="${map.key}">${map.value}</option>
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
			</div>
		</div>
 	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script type="text/javascript">

		// const NULL = '';
		// const SysUserCardId = [];
		// const SysUserName = [];
		// <c:forEach var="item" items="${SysUserList}" varStatus="vs">
		// 	<c:if test="${item.cardId != 'NULL'}">
		// 		SysUserCardId.push("${item.cardId}");
		// 		SysUserName.push("${item.originalName}");
		// 	</c:if>
		// </c:forEach>

		// 取得全部cardID
		const tdCardId = $('.tdCardId').map(function(){
			return $(this).text().trim();
		}).get();

		// 取得全部Name
		const tdName = $('.tdName').map(function(){
			return $(this).text().trim();
		}).get();

		console.log('tdCardId',tdCardId);
		console.log('tdName',tdName);

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
					page:page
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

			// 如果填入的卡號與其他職員的卡號相同，則顯示提示字樣及該職員名
			// if (SysUserCardId.includes($("#cardId").val())) {
			// 	let theIndex = SysUserCardId.indexOf($("#cardId").val());
			// 	errorCode["3"] = "11此卡號與職員「"+ SysUserName[theIndex] +"」重複，請清除離職員工卡號以免系統判斷錯誤";
			// 	errors['cardId'] = 3;
			// }else {
			// 	$('#cardId-error').hide();
			// }

			// 如果填入的卡號與其他職員的卡號相同，則顯示提示字樣及該職員名
			if (tdCardId.includes($("#cardId").val()) && $("#cardId").val() != "NULL") {
				let theIndex = tdCardId.indexOf($("#cardId").val());
				errorCode["3"] = "此卡號與職員「"+ tdName[theIndex] +"」重複，請清除離職員工卡號以免系統判斷錯誤";
				errors['cardId'] = 3;
			}else {
				$('#cardId-error').hide();
			}

			

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
							$('#cardId').val(data.sysUser.cardId);
							$('#defaultProjectId').val(data.sysUser.defaultProjectId);
							$('#inaugurationDate').val(formatJsonDate(data.sysUser.inaugurationDate, "y/M/d"));
							$('#graduationDate').val(formatJsonDate(data.sysUser.graduationDate, "y/M/d"));
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
