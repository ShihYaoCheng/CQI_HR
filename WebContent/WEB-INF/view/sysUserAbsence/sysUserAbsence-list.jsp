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
					<h3><b>人員留職停薪管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							<input type="text" id="searchUserName" class="form-control" placeholder="請輸入人員名稱...">
							<span class="btn btn-default input-group-addon function_icon" onclick="queryData(1)">
								GO
							</span>
						</div>
						<div class="btn-group">
						</div>

					</div>
					<%@include file="../include/progressing.jsp"%>					
					<div id="dataContent">
					</div>
					
					<!-- 員工編號/姓名/cardId 畫面上不顯示-->
					<div id="test" style="display: none;">
						<table class="table table-striped">
							<tbody>
								<c:forEach var="item" items="${SysUserList}" varStatus="vs">
									<tr>
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
					        <div class="form-group row">
					            <label for="originalName" class="control-label col-sm-4">姓名:</label>
					            <div class="col-sm-8">
						            <input type="text" class="form-control" id="originalName" name="originalName" onfocus="resetInput('originalName')" disabled />
						            <span id="originalName-error" class="error_text"></span>
					            </div>
							</div>
							<div class="form-group row">
					            <label for="userName" class="control-label col-sm-4">暱稱:</label>
					            <div class="col-sm-8">
						            <input type="text" class="form-control" id="userName" name="userName" onfocus="resetInput('userName')" disabled/>
						            <span id="userName-error" class="error_text"></span>
					            </div>
							</div>

							<div class="form-group row">
								<label for="cardId" class="control-label col-sm-4">卡號:</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="cardId" name="cardId" />
								</div>
								<!-- <span>請前往卡號原始資料查詢無對應的卡號，離職人員請將卡號清除避免卡號重複</span> -->
								<span id="cardId-error" class="error_text" style="padding-left: 15px;"></span>

							</div>

							<div class="form-group row">
					            <label for="sysUserStatus" class="control-label col-sm-4">狀態</label>
					            <div class="col-sm-8">
					            	<select class="form-control" id="sysUserStatus" name="sysUserStatus">
					            		<option value="y">在職</option>
					            		<option value="l">留職停薪</option>
					            	</select>
						            <span id="status-error" class="error_text"></span>
					            </div>
							</div>
							
							<div class="form-group row">
					            <label for="inaugurationDate" class="control-label col-sm-4">報到日期:</label>
					            <div class="col-sm-8">
					            	<div class='input-group date' id='datepickerInaugurationDate'>
					                    <input type="text" class="form-control" id="inaugurationDate" name="inaugurationDate" size="10" type="text" value="" disabled />
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="inaugurationDate-error" class="error_text"></span>
					            </div>
							</div>
							
							
							
							
							<div class="form-group row">
					            <label for="EffectiveDate" class="control-label col-sm-4">留職停薪開始日期:</label>
					            <div class="col-sm-8">
					            	<div class='input-group date' id='datepickerEffectiveDate'>
					                    <input type="text" class="form-control" id="EffectiveDate" name="EffectiveDate" size="10" type="text" value=""/>
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="EffectiveDate-error" class="error_text"></span>
					            </div>
							</div>
							
							
							<div class="form-group row">
					            <label for="ExpirationDate" class="control-label col-sm-4">留職停薪結束日期:</label>
					            <div class="col-sm-8">
					            	<div class='input-group date' id='datepickerExpirationDate'>
					                    <input type="text" class="form-control" id="ExpirationDate" name="'ExpirationDate'" size="10" type="text" value=""/>
					                    <span class="input-group-addon">
					                        <span class="glyphicon glyphicon-calendar"></span>
					                    </span>
					                </div>
						            <span id="ExpirationDate-error" class="error_text"></span>
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
			
			$('#datepickerEffectiveDate').datetimepicker({
				format :'yyyy/mm/dd',
				autoclose: true, minView: 2
			});
			
			$('#datepickerExpirationDate').datetimepicker({
				format :'yyyy/mm/dd',
				autoclose: true, minView: 2
			});

			var minDateForStart = new Date();
			minDateForStart.setDate(minDateForStart.getDate() + 1);
	        $('#datepickerEffectiveDate').datetimepicker("setStartDate", minDateForStart);
	        $('#datepickerExpirationDate').datetimepicker("setStartDate", minDateForStart);

		    $("#datepickerEffectiveDate").on("changeDate", function (e) {		    			    
		    	$('#datepickerExpirationDate').datetimepicker("setStartDate", e.date);
	        });
			$("#datepickerExpirationDate").on("changeDate", function (e) {				
				$('#datepickerEffectiveDate').datetimepicker("setEndDate", e.date);
	        });
			queryData(1);
		});
		
		function queryData(page) {
			if(page == 0){
				return;
			}
			let data = {
				page:page
			};
			if($('#searchUserName').val() !== ''){
				data.searchUserName = $('#searchUserName').val()
			}

			$("body").css("cursor", "progress");
			$('#dataContent').hide();
			$("#progressing").show();
			$.ajax({
				type : "POST",
				url : "<c:url value='/security/sysUserAbsence/ajaxDataLoading'/>",
				data : data,
				success : function(data) {
					$("body").css("cursor", "auto");
					$("#progressing").hide();
					$('#dataContent').html(data);
					$('#dataContent').show();
				}
			});
		}

		/* 
		申請送出按鈕
		*/
		function submit(){
			var errorCode = {};
			errorCode["1"] = "Can not be empty";
			errorCode["2"] = "Id duplicate";
			var errors = {};
			// 如果填入的卡號與其他職員的卡號相同，則顯示提示字樣及該職員名
			if(tdCardId.includes($("#cardId").val()) && $("#cardId").val() != "NULL" && $("#cardId").val() != "" ) {
				let theIndex = tdCardId.indexOf($("#cardId").val());
				if(tdName[theIndex] !== $("#originalName").val()){
					errorCode["3"] = "此卡號與職員「"+ tdName[theIndex] +"」重複，請清除離職員工卡號以免系統判斷錯誤";
					errors['cardId'] = 3;
				}
			}else {
				$('#cardId-error').hide();
			}
			var targetURL= "<c:url value='/security/sysUserAbsence/updateUserInfo'/>";
			
			if(Object.keys(errors).length == 0){
				$("body").css("cursor", "progress");
				$.ajax({
					type : "POST",
					url: targetURL,
					data : {
					 	sysUserId : $("#sysUserId").val(),
						status : $("#sysUserStatus").val() ,
						cardid : $("#cardId").val(),
						effectiveDate : $("#EffectiveDate").val(),
						expirationDate : $("#ExpirationDate").val()
					},
					dataType: "json",
					success : function(data) {
						location.reload();
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

		/*
			申請畫面
		*/		
		function active(id){
			if(progressing == 1){
				return;
			}
			var text = "修改";
			if(typeof(id) == "undefined"){
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/sysUserAbsence/"+id+"'/>";
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
							$('#sysUserStatus').val(data.sysUser.status);
							$('#originalName').val(data.sysUser.originalName);
							$('#userName').val(data.sysUser.userName);
							//$('#email').val(data.sysUser.email);
							//$('#gender').val(data.sysUser.gender);
							//$('#department').val(data.sysUser.department);
							$('#cardId').val(data.sysUser.cardId);
							$('#defaultProjectId').val(data.sysUser.defaultProjectId);
							$('#inaugurationDate').val(formatJsonDate(data.sysUser.inaugurationDate, "y/M/d"));
							//$('#graduationDate').val(formatJsonDate(data.sysUser.graduationDate, "y/M/d"));
							$('#basicModal').find('.modal-title').text(text+"開發者");
							$('#basicModal').modal('toggle');
							if(data.SysUserAbsence != null)
							{
								$('#EffectiveDate').val(formatJsonDate(data.SysUserAbsence.effectiveDate, "y/M/d"));
								$('#ExpirationDate').val(formatJsonDate(data.SysUserAbsence.expirationDate, "y/M/d"));
							}else
							{
								$('#EffectiveDate').val("");
								$('#ExpirationDate').val("")
							}
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
