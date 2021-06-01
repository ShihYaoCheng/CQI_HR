<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>
	<html>

		<head>
			<%@include file="/WEB-INF/view/include/view-html-head.jsp" %>
		</head>

		<body role="document">
			<%@include file="../include/menu.jsp" %>
				<div class="container theme-showcase page-width" role="main">
					<div class="row row-offcanvas row-offcanvas-left">

						<div class="col-xs-12 col-md-12 single_table">
							<form class="navbar-form">
								<h3><b>調班資料管理</b></h3>
								<br />
								<div class="form-group" style="display: inline;">
									<div class="input-group">

									</div>
									<div class="btn-group">
										<a href="#" class="btn btn-default function_icon" onclick="active()" title="新增" id="shiftSchedule">
											<i class="glyphicon glyphicon-plus"></i>
										</a>
									</div>
								</div>

								<%@include file="../include/progressing.jsp" %>
									<div id="overtimeQuotaContent">
									</div>
									<div id="dataContent">
									</div>
							</form>

						</div>
					</div>
			</div>
	
	


				<!-- dialog -->
				<div class="modal fade" id="basicModal" tabindex="-1" role="dialog" aria-labelledby="basicModal"
					aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">新增調班紀錄</h4>
							</div>
							<div class="modal-body" id="shiftScheduleDialog">
								<input type="hidden" id="unitType" name="unitType" />
								<form id="askForShiftForm" name="askForShiftForm">
									<input type="hidden" id="askForOvertimeId" name="askForOvertimeId" />
									<input type="hidden" id="sysUserId" name="sysUserId" />
									<input type="hidden" id="status" name="status" />
									<input type="hidden" id="asanaTaskId" name="asanaTaskId" />
									<div id="edit">
										<div class="form-group">
											<label for="recipient-name" class="control-label col-sm-12">調班種類:</label>
											<div class="col-sm-12 checkbox">
												<div id="roleTypeDiv" style="float:left;">
													<select class="form-control" id="overtimeId" name="overtimeId"
														onchange="selectedLeaveId(this.options[this.options.selectedIndex].label)">
														<option value="">請選擇</option>
														<c:forEach var="item" items="${cqiOvertimeList}" varStatus="vs">
															<option value="${item.leaveId}">${item.leaveName}，單位：
																<c:if test="${item.unitType == 1}">天</c:if>
																<c:if test="${item.unitType == 2}">小時</c:if>
															</option>
														</c:forEach>
													</select>
													<span id="overtimeId-error" class="error_text"></span>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="recipient-name" class="control-label col-sm-12"
												id="spendTimeStr" name="spendTimeStr">天數/時數：</label>
											<div class="col-sm-12">
												<select class="form-control" id="spendTime" name="spendTime">
													<option value="">請選擇</option>
													<option value="1">1</option>
													<option value="2">2</option>
													<option value="3">3</option>
													<option value="4">4</option>
													<option value="5">5</option>
													<option value="6">6</option>
													<option value="7">7</option>
													<option value="8">8</option>
													<option value="9">9</option>
													<option value="10">10</option>
													<option value="11">11</option>
													<option value="12">12</option>
													<option value="13">13</option>
													<option value="14">14</option>
													<option value="15">15</option>
													<option value="16">16</option>
												</select>
												<span id="spendTime-error" class="error_text"></span>
											</div>
										</div>
										<div class="form-group" style="margin-bottom: 0px;">
											<label for="recipient-name" class="control-label col-sm-12">調班起始時間：</label>
											<div class="col-sm-12">
												<div class="form-group">
													<div class='input-group date' id='datetimepickerStart'>
														<input id="startTime" name="startTime" type='text'
															class="form-control" value="" readonly="readonly" style="cursor: pointer;
															background-color: #fff;" />
														<span class="input-group-addon">
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</div>
												<span id="startTime-error" class="error_text"></span>
											</div>
										</div>
										<div class="form-group" style="margin-bottom: 0px;">
											<label for="recipient-name" class="control-label col-sm-12">調班結束時間：</label>
											<div class="col-sm-12">
												<div class="form-group">
													<div class='input-group date' id='datetimepickerEnd'
														style="width: 100%;pointer-events: none;">
														<input id="endTime" name="endTime" type='text'
															class="form-control" value="" readonly="readonly" />
														<span class="input-group-addon" style="display: none;">
															<span class="glyphicon glyphicon-calendar"
																style="display: none;"></span>
														</span>
													</div>
												</div>
												<span id="endTime-error" class="error_text"></span>
											</div>
										</div>



										<!-- -------------------排休-------------------- -->
										<div class="form-group" style="margin-bottom: 0px;">
											<label for="recipient-name" class="control-label col-sm-12">排休起始時間：</label>
											<div class="col-sm-12">
												<div class="form-group">
													<div class='input-group date' id='datetimepickerStartLeave'>
														<input id="startTimeLeave" name="startTimeLeave" type='text'
															class="form-control" value="" readonly="readonly" style="cursor: pointer;
															background-color: #fff;" />
														<span class="input-group-addon">
															<span class="glyphicon glyphicon-calendar"></span>
														</span>
													</div>
												</div>
												<span id="startTimeLeave-error" class="error_text"></span>
											</div>
										</div>


										<div class="form-group" style="margin-bottom: 0px;">
											<label for="recipient-name" class="control-label col-sm-12">排休結束時間：</label>
											<div class="col-sm-12">
												<div class="form-group">
													<div class='input-group date' id='datetimepickerEndLeave'
														style="width: 100%;pointer-events: none;">
														<input id="endTimeLeave" name="endTimeLeave" type='text'
															class="form-control" value="" readonly="readonly" />
														<span class="input-group-addon" style="display: none;">
															<span class="glyphicon glyphicon-calendar"
																style="display: none;"></span>
														</span>
													</div>
												</div>
												<span id="endTimeLeave-error" class="error_text"></span>
											</div>
										</div>
										<!-- -------------------排休-------------------- -->


										<div class="form-group">
											<label for="recipient-name" class="control-label col-sm-12">事由：</label>
											<div class="col-sm-12">
												<input type="text" class="form-control" id="description"
													name="description" />
												<span id="description-error" class="error_text"></span>
											</div>
										</div>
									</div>
								</form>
							</div>



							<div style="clear:both;"></div>
							<div class="modal-footer" style="margin-top:20px">
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
								<button type="button" style="cursor:pointer;" class="btn btn-primary btn-lg " id="save"
									data-loading-text="儲存中" onclick="submit()">儲存</button>
							</div>
						</div>
					</div>
				</div>



				<!-- dialog 調班剩餘額度-->
				<div class="modal fade" id="basicModalEdit" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">調班額度</h4>
							</div>
							<div class="modal-body">
								<form id="shiftForm" name="shiftForm">
									<input type="hidden" id="shiftId" name="shiftId"/>
									<input type="hidden" id="status" name="status"/>
									<div id="edit">
										<div class="form-group">
											<label for="recipient-name" class="control-label col-sm-12">成員：</label>
											<div class="col-sm-12">
												<div class="form-group">
													<select class="form-control" id="sysUserId" name="sysUserId" onchange="">
														<c:choose>
															<c:when test="${operator.roleId == '1'}">
																<option value="">請選擇</option>
																<c:forEach var="item" items="${mapEnableRule2User}" varStatus="vs">
																	<option value="${item['key']}">${item['value'].originalName}</option>
																</c:forEach>
															</c:when>    
															<c:otherwise>
																<option value="${operator.sysUserId}">${operator.originalName}</option>
															</c:otherwise>
														</c:choose>
														
													</select>
													<span id="sysUserId-error" class="error_text"></span>
												</div>
											</div>	
										</div>
										<div class="form-group">
											<label for="recipient-name" class="control-label col-sm-12">調班額度（每周）：</label>
											<div class="col-sm-12 ">
												<div class="form-group">
													<select class="form-control" id="shiftQuota" name="shiftQuota">
														<option value="">請選擇</option>
														<option value="1">1</option>
														<option value="2">2</option>
														<option value="3">3</option>
													</select>
													<span id="shiftQuota-error" class="error_text"></span>
												</div>
											</div>
										</div>
										
									</div>
								</form>
							</div>
							<div style="clear:both;"></div>
							<div class="modal-footer" style="margin-top:20px">
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
								<button type="button" class="btn btn-primary btn-lg " id="save" data-loading-text="儲存中" onclick="submitEdit()">儲存</button>
							</div>
						</div>
					</div>
				</div>











				<%@include file="/WEB-INF/view/include/view-html-bottom.jsp" %>
					<script type="text/javascript">
						var progressing = 0;
						jQuery().ready(function () {
							queryData(1);

							var today = new Date();
							var pickerStartDate = getFormattedDate(getCurrentFirstDay(), 'y/M/d H:m');
							console.log("pickerStartDate：" + pickerStartDate);
							// pickerStartDate：2021/05/01 00:00
							if (today.getDate() < 4) {
								pickerStartDate = getFormattedDate(getLastMonth(), 'y/M/d H:m');
							}


							
							console.log("今天：" + today);

							//抓取當前日期+30天
							var next30days = new Date(today.setDate(today.getDate() + 30));
							console.log('30天後：' + next30days);

							//-------------------調班時間--------------------

							$('#datetimepickerStart').datetimepicker({
								format: 'yyyy/mm/dd hh:ii',
								autoclose: true,
								minuteStep: 30,
								focusOnShow: false,
								allowInputToggle: true,
								// startDate: pickerStartDate,
								startDate: new Date(),    //設置開始時間，早於此刻的日期不能選擇
							});
							$('#datetimepickerStart').datetimepicker('setEndDate', next30days); //限制當前日期+30天

							
							$('#datetimepickerEnd').datetimepicker({
								format: 'yyyy/mm/dd hh:ii',
								autoclose: true, 
								minuteStep: 30,
								focusOnShow: false,
								allowInputToggle: true,
								useCurrent: false,
								startDate: new Date(),

							});
							$('#datetimepickerEnd').datetimepicker('setEndDate', next30days); //限制當前日期+30天
							



							$("#datetimepickerStart").on("changeDate", function (e) {
								$('#datetimepickerEnd').datetimepicker("setStartDate", e.date);
								if ($('#spendTime').val() != '') {
									var setEndDate = new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate());
									setEndDate.setHours(e.date.getHours());
									setEndDate.setMinutes(e.date.getMinutes());
									if ($('#unitType').val() == '小時') {
										setEndDate.setHours(setEndDate.getHours() + parseInt($('#spendTime').val()));
										if (e.date.getHours() < 12 && setEndDate.getHours() > 12) {
											setEndDate.setHours(setEndDate.getHours() + 1);
										}
									} else {
										setEndDate.setDate(setEndDate.getDate() + parseInt($('#spendTime').val()) - 1);
										setEndDate.setHours(19);
										setEndDate.setMinutes(0);
									}
									$('#endTime').val(getFormattedDate(setEndDate, "y/M/d H:m"));
								}
							});
							$("#datetimepickerEnd").on("changeDate", function (e) {
								$('#datetimepickerStart').datetimepicker("setEndDate", e.date);
							});


							//-------------------排休時間--------------------

							$('#datetimepickerStartLeave').datetimepicker({
								format: 'yyyy/mm/dd hh:ii',
								autoclose: true,
								minuteStep: 30,
								focusOnShow: false,
								allowInputToggle: true,
								// startDate: pickerStartDate,
								startDate: new Date(),    //設置開始時間，早於此刻的日期不能選擇'1986/12/08'
							});
							$('#datetimepickerStartLeave').datetimepicker('setEndDate', next30days); //限制當前日期+30天


							$('#datetimepickerEndLeave').datetimepicker({
								format: 'yyyy/mm/dd hh:ii',
								autoclose: true,
								minuteStep: 30,
								focusOnShow: false,
								allowInputToggle: true,
								useCurrent: false,
								startDate: new Date(),
							});
							$('#datetimepickerEndLeave').datetimepicker('setEndDate', next30days); //限制當前日期+30天



							$("#datetimepickerStartLeave").on("changeDate", function (e) {
								$('#datetimepickerEndLeave').datetimepicker("setStartDate", e.date);
								if ($('#spendTime').val() != '') {
									var setEndDate = new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate());
									setEndDate.setHours(e.date.getHours());
									setEndDate.setMinutes(e.date.getMinutes());
									if ($('#unitType').val() == '小時') {
										setEndDate.setHours(setEndDate.getHours() + parseInt($('#spendTime').val()));
										if (e.date.getHours() < 12 && setEndDate.getHours() > 12) {
											setEndDate.setHours(setEndDate.getHours() + 1);
										}
									} else {
										setEndDate.setDate(setEndDate.getDate() + parseInt($('#spendTime').val()) - 1);
										setEndDate.setHours(19);
										setEndDate.setMinutes(0);
									}
									$('#endTimeLeave').val(getFormattedDate(setEndDate, "y/M/d H:m"));
								}
							});
							$("#datetimepickerEndLeave").on("changeDate", function (e) {
								$('#datetimepickerStartLeave').datetimepicker("setEndDate", e.date);
							});

							//---------------------------------------


						});

						function selectedLeaveId(data) {
							console.log("data : " + data);
							$('#spendTimeStr').html(data.split("，")[1]);
							if (data.indexOf('天') >= 0) {
								$('#unitType').val('天');
							} else {
								$('#unitType').val('小時');
							}

							$("#spendTime option").remove();
							if (data.indexOf('災害處理') >= 0) {
								$("#spendTime").append($("<option></option>").attr("value", "3").text("C1狼級，3小時"));
								$("#spendTime").append($("<option></option>").attr("value", "6").text("C2虎級，6小時"));
								$("#spendTime").append($("<option></option>").attr("value", "9").text("C3鬼級，9小時"));
								$("#spendTime").append($("<option></option>").attr("value", "12").text("C4龍級，12小時"));
								$("#spendTime").append($("<option></option>").attr("value", "15").text("C5神級，15小時"));
							} else {
								$("#spendTime").append($("<option></option>").text("請選擇"));
								// $("#spendTime").append($("<option></option>").attr("value", "請選擇").text("請選擇"));

								for (var i = 1; i < 17; i++) {
									$("#spendTime").append($("<option></option>").attr("value", i).text(i));
								}
							}
						}

						function queryData(page) {
							if (page == 0) {
								return;
							}
							$("body").css("cursor", "progress");
							// $('#leaveContent').hide();
							$('#overtimeQuotaContent').hide();
							$('#dataContent').hide();
							$("#progressing").show();
							$.ajax({
								type: "POST",
								url: "<c:url value='/security/askShift/ajaxShiftQuota'/>",
								data: {},
								success: function (data) {
									$('#overtimeQuotaContent').html(data);
									$('#overtimeQuotaContent').show();
								}
							});
							$.ajax({
								type: "POST",
								url: "<c:url value='/security/askShift/ajaxDataLoading'/>",
								data: {
									page: page
								},
								success: function (data) {
									$("body").css("cursor", "auto");
									$("#progressing").hide();
									$('#dataContent').html(data);
									$('#dataContent').show();
								}
							});
						}





						// "班別：" 點選調班時，"單位：小時" 限制為2跟4小時
						$('#overtimeId').change(function () {

							$('#startTime').val('');
							$('#endTime').val('');

							if ($('#overtimeId').val() == '2') {


								$('#spendTime').children().each(function (index, el) {
									if ($(el).val() !== '' && $(el).val() !== '2' && $(el).val() !== '4') {
										$(el).hide()
									}
								})
							} else {
								$('#spendTime').children().each(function (i, el) {
									$(el).show()
								})
							}


							// 抓取選擇調班的時數
							$('#spendTime').change(function () {

								var spendTimeHour = $("#spendTime").val();
								console.log("小時.change function ", spendTimeHour);
							});

						});

						$('#overtimeId').change(function () {
							$('#spendTime').val('');
							$('#startTime').val('');
							$('#endTime').val('');
							$('#startTimeLeave').val('');
							$('#endTimeLeave').val('');
						});

						$('#spendTime').change(function () {

							$('#startTime').val('');
							$('#endTime').val('');
							$('#startTimeLeave').val('');
							$('#endTimeLeave').val('');
						});



						function submit() {

							var errorCode = {};
							errorCode["1"] = "請選擇班別";
							errorCode["2"] = "請選擇天數/時數";
							errorCode["3"] = "請輸入時間";
							errorCode["4"] = "請選擇成員";
							errorCode["5"] = "請選擇調班額度";
							var errors = {};

							if ($('#overtimeId :selected').val() == '') {
								errors['overtimeId'] = 1;
							} else {
								$('#overtimeId-error').hide();
							}
							if ($('#spendTime :selected').val() == '' || $('#spendTime :selected').val() == '請選擇') {
								errors['spendTime'] = 2;
							} else {
								$('#spendTime-error').hide();
							}
							if ($('#startTime').val() == '') {
								errors['startTime'] = 3;
							} else {
								$('#startTime-error').hide();
							}
							if ($('#endTime').val() == '') {
								errors['endTime'] = 3;
							} else {
								$('#endTime-error').hide();
							}
							if ($('#startTimeLeave').val() == '') {
								errors['startTimeLeave'] = 3;
							} else {
								$('#startTimeLeave-error').hide();
							}
							if ($('#endTimeLeave').val() == '') {
								errors['endTimeLeave'] = 3;
							} else {
								$('#endTimeLeave-error').hide();
							}

							if ($('#QuotasysUserId').val() == '') {
								errors['QuotasysUserId'] = 4;
							} else {
								$('#QuotasysUserId-error').hide();
							}
							/* if ($('#shiftQuota').val() == '') {
								errors['shiftQuota'] = 5;
							} else {
								$('#shiftQuota-error').hide();
							} */

							
							
							var targetURL = "<c:url value='/security/askShift/add'/>";
							
							console.log(Object.keys(errors).length);
							if (Object.keys(errors).length == 0) {
								var data = $('#askForShiftForm').serialize();
								if (typeof ($('#askForOvertimeId').val()) != 'undefined' && $('#askForOvertimeId').val() != '') {
									targetURL = "<c:url value='/security/askShift/update'/>";
								}
								console.log(targetURL);
								$('#save').button('loading');
								$("body").css("cursor", "progress");
								$.ajax({
									type: "POST",
									url: targetURL,
									data: data,
									dataType: "json",
									success: function (data) {
										location.reload(); //強制重整
										$("body").css("cursor", "auto");

										if (data.success) {
											queryData(1);

											$('#basicModal').modal('hide');
										} else {
											alert(data.message);
										}
										$('#save').button('reset');
									},
									error: function (jqXHR, exception) {
										var msg = "";
										if (jqXHR.status === 0) {
											msg = "資料新增失敗！\n\n請檢查您的網路之後，重新登入再試一次!";
										} else if (jqXHR.status == 401) {
											msg = "資料新增失敗！\n\n您太久未操作系統，系統會回到登入頁面，請重新登入!";
										} else if (jqXHR.status == 404) {
											msg = "資料新增失敗！\n\n系統無此功能404，系統會回到登入頁面，請重新登入!";
										} else if (jqXHR.status == 500) {
											msg = "資料新增失敗！\n\n伺服器錯誤500，系統會回到登入頁面，請重新登入!";
										} else if (exception === 'parsererror') {
											msg = "資料新增失敗！\n\n資料錯誤，系統會回到登入頁面，請重新登入!";
										} else if (exception === 'timeout') {
											msg = "資料新增失敗！\n\n連線逾時，系統會回到登入頁面，請重新登入!";
										} else if (exception === 'abort') {
											msg = "資料新增失敗！\n\n連線被取消，系統會回到登入頁面，請重新登入!";
										} else {
											msg = "資料新增失敗！\n\n系統錯誤!\n" + jqXHR.responseText + "，系統會回到登入頁面，，請重新登入!";
										}
										$('#basicModal').modal('hide');
										sessionOvertimeMessage(msg);
									}
								});
							}
							for (var key in errors) {
								//$("#"+key).css('width',90);
								$("#" + key + "-error").text(errorCode[errors[key]]);
								$("#" + key + "-error").show();
							}
						}

						function deleteData(id) {
							var targetURL = "<c:url value='/security/askShift/" + id + "'/>";

							$("body").css("cursor", "progress");
							$.ajax({
								type: "DELETE",
								url: targetURL,
								data: {
								},
								dataType: "json",
								success: function (data) {
									location.reload(true)
									$("body").css("cursor", "auto");
									if (data.success) {
										queryData(1);
									}

								}
							});
						}

						function active(id) {
							if (progressing == 1) {
								return;
							}
							// 1.如果點擊的id是askOvertimeEdit
							// 2.如果點擊的id不是askOvertimeEdit
							// 2-1. 如果active(id)中的id是未定義
							// if($(event.target).attr('id') ===  'askOvertimeEdit'){
								
							// 	console.log("修改")

							// 	$('#shiftScheduleDialog').hide();
							// 	$('#askOvertimeEditDialog').show();
							// 	$('#basicModal').modal('toggle');

							// } else {
							// 	$('#shiftScheduleDialog').show();
							// 	$('#askOvertimeEditDialog').hide();
								// console.log("新增")
							
								//$('input[name="mangaRoleTypes"]').attr("checked", false);  //原本就註解
								// $('#datetimepickerEnd').datetimepicker("setStartDate", new Date(-8639968443048000)); //0512註解
								// $('#datetimepickerStart').datetimepicker("setEndDate", new Date(8639968443048000));  //0512註解
								var text = "修改";
								if (typeof (id) == "undefined") {
									text = "新增";
									$('#askForOvertimeId').val("");
									$('#overtimeId').val("");
									$('#spendTime').val("");
									$('#startTime').val("");
									$('#sysUserId').val("");
									$('#endTime').val("");
									$('#description').val("");
									$('#asanaTaskId').val("");
									$('#status').val("");

									$('#basicModal').find('.modal-title').text(text + "調班紀錄");
									$('#basicModal').modal('toggle');

								} else {
									progressing = 1;
									$("body").css("cursor", "progress");
									var targetURL = "<c:url value='/security/askShift/" + id + "'/>";
									$.ajax({
										type: "GET",
										url: targetURL,
										data: {
											askForLeaveId: id
										},
										dataType: "json",
										success: function (data) {
											$("body").css("cursor", "auto");
											if (data.success) {
												$('#askForOvertimeId').val(data.userAskForOvertime.askForOvertimeId);
												$('#overtimeId').val(data.userAskForOvertime.overtimeId);
												$('#spendTime').val(data.userAskForOvertime.spendTime);
												$('#startTime').val(formatJsonDate(data.userAskForOvertime.startTime, "y/M/d H:m"));
												$('#endTime').val(formatJsonDate(data.userAskForOvertime.endTime, "y/M/d H:m"));
												$('#datetimepickerEnd').datetimepicker("setStartDate", $('#startTime').val());
												$('#datetimepickerStart').datetimepicker("setEndDate", $('#endTime').val());
												$('#datetimepickerEndLeave').datetimepicker("setStartDate", $('#startTimeLeave').val()); //排休
												$('#datetimepickerStartLeave').datetimepicker("setEndDate", $('#endTimeLeave').val()); //排休
												$('#sysUserId').val(data.userAskForOvertime.sysUserId);
												$('#description').val(data.userAskForOvertime.description);
												$('#asanaTaskId').val(data.userAskForOvertime.asanaTaskId);
												$('#status').val(data.userAskForOvertime.status);
												selectedLeaveId($('#overtimeId option:selected').html());
												$('#basicModal').find('.modal-title').text(text + "調班紀錄");
												$('#basicModal').modal('toggle');
												progressing = 0;
											} else {
												alert(data.message);
											}
										},
										error: function(err){
											alert('error')
											$("body").css("cursor", "auto");
										}
									});
								}
							// }

						}

						function resetInput(id) {
							$("#" + id + "-error").text('');
							$("#" + id + "-error").hide();
						}



						// 調班額度管理

						function edit(id){
							if(progressing == 1){
								return;
							}
							var text = "修改";
							if(typeof(id) == "undefined"){
								text = "新增";
								$('#shiftId').val("");
								$('#sysUserId').val("");
								$('#shiftQuota').val("");

								$('#status').val("");
								$('#basicModalEdit').find('.modal-title').text(text + "調班額度");
								$('#basicModalEdit').modal('toggle');
							}else{
								progressing = 1;
								$("body").css("cursor", "progress");
								var targetURL= "<c:url value='/security/UserShiftQuota/"+id+"'/>";
								$.ajax({
									type : "GET",
									url : targetURL,
									data : {
										shiftId:id
									},
									dataType: "json",
									success : function(data) {
										$("body").css("cursor", "auto");
										if (data.success) {
											$('#shiftId').val(data.sysUserShift.shiftId);
											$('#sysUserId').val(data.sysUserShift.sysUserId);
											$('#sysUserId').attr("disabled", true);
											$('#shiftQuota').val(data.sysUserShift.status);
											selectedShift(data.sysUserShift.boardTime, data.sysUserShift.finishTime);
											
											$('#basicModalEdit').find('.modal-title').text(text + "調班額度");
											$('#basicModalEdit').modal('toggle');
											progressing = 0;
										}else{
											alert(data.message);
										}
									}
								});				
							}	
						}


					</script>
		</body>
	</html>