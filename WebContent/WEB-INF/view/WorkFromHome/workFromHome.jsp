
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	<link href="<c:url value='/resources/datepicker/bootstrap-datepicker.min.css'/>" rel="stylesheet" />
</head>

<body role="document">
	<%@include file="../include/menu.jsp"%>
	<div class="container theme-showcase page-width" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>遠端作業管理</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div class="input-group">
							
						</div>
						<div class="btn-group">
							<a href="#" class="btn btn-default function_icon" onclick="active()" title="新增"> 
								<i class="glyphicon glyphicon-plus"></i>
							</a>
						</div>
					</div>
					<br>
					<table class="table table-striped">
						<h4>
							<b>*** 因 COVID-19 或政府宣告之傳染病，於當地政府規範下，得由公司公布或成員申請遠端工作佈署 ***</b>
							<br><br><br>
							<b>遠端作業說明</b>
						</h4>
						<thead>
							<tr style="background-color: #edf8ff; font-weight: bold;">
								<td width="10%">警戒程度</td>
								<td width="10%">遠端日數</td>
								<td width="30%">A 地區關聯</td>
								<td width="30%">B 親友關聯</td>
								<td width="20%">C 個人</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>一級</td>
								<td>三天</td>
								<td>A1.1 居住於確診熱區(萬華5/17)(台北新北5/20)
									<br>
									A1.2 使用跨縣市大眾通勤, 所跨縣市政府宣布三級防疫(新北台北5/17)(全國5/20)
									<br>
									A1.3 所居住社區出現確診案例, 未共用通風管道或共用電梯
								</td>
								<td>B1.1 直系子女因防疫停課, 經主管核定業務急需以居家工作代替防疫照顧假者</td>
								<td>C1.1 個人因防疫考量, 請長假超過兩周, 經主管核定業務急需以居家工作代替長假者</td>
							</tr>
							<tr>
								<td>二級</td>
								<td>七天</td>
								<td>A2.1 所居住大樓出現確診案例, 有共用通風管道或共用電梯</td>
								<td>A2.2 本司辦公所在位於確診熱區(板橋中和5/25)</td>
								<td>B2.1 一周內同室(同車)社交過的親友確診, 過程未戴口罩超過15分鐘</td>
								<td></td>
							</tr>
							<tr>
								<td>三級</td>
								<td>十四天</td>
								<td>A3.1 本司辦公所在大樓出現確診案例, 或由公司行政統一發布</td>
								<td>B3.1 同居親友確診或收到自主通知單</td>
								<td>C3.1 本人, 配偶, 同居直系確診或收到自主通知單</td>
							</tr>
							<tr>
								<td>四級</td>
								<td>三十天</td>
								<td></td>
								<td>B4.1 同居親友確診, 因故無法在院隔離, 須在家隔離者</td>
								<td>C4.1 本人確診</td>
							</tr>
							<tr>
								<td>五級</td>
								<td>超過一個月</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tbody>
					</table>


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
					<h4 class="modal-title" id="myModalLabel">新增遠端作業紀錄</h4>
				</div>
				<div class="modal-body">
					<form id="wfhForm" name="wfhForm">
						<input type="hidden" id="workFromHomeId" name="workFromHomeId"/>
						<input type="hidden" id="status" name="status"/>
						<div id="edit">
							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">成員：</label>
								<div class="col-sm-12">
						            <div class="form-group">
										<select class="form-control" id="sysUserId" name="sysUserId" onchange="">
							            	
									    	<option value="">請選擇</option>
									        <c:forEach var="item" items="${mapEnableRule2User}" varStatus="vs">
												<option value="${item['key']}">${item['value'].originalName}</option>
											</c:forEach>
							            	
										</select>
										<span id="sysUserId-error" class="error_text"></span>
									</div>
								</div>	
							</div>

							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12"
									id="levelStr" name="levelStr">
									遠端級別：
								</label>
								<div class="col-sm-12" id="levelForm">
									<select class="form-control" id="level" name="level">
										<option value="">請選擇</option>
										<option value="1">遠端一級 (３天)</option>
										<option value="2">遠端二級 (７天)</option>
										<option value="3">遠端三級 (１４天)</option>
										<option value="4">遠端四級 (３０天)</option>
										<option value="5">遠端五級 (超過１個月)</option>
									</select>
									<span id="level-error" class="error_text"></span>
								</div>
							</div>

							<div class="form-group" id="levelFive" style="display: none;">
								<label for="recipient-name" class="control-label col-sm-12">五級遠端天數：</label>
								<div class="col-sm-12">
									<input type="text" class="form-control" id="levelFiveDay"
										name="levelFiveDay" />
									<span id="levelFiveDay-error" class="error_text"></span>
								</div>
							</div>


							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12" id="workDateTitle">公告開始時間：</label>
								<div class="col-sm-12">
									<div class="form-group" style="margin-bottom: 0px;" id="workDateForm">
										<div class="input-group date" id='datetimepickerStart'>
											<input id="workDate" name="workDate" class="form-control"
												size="16" type="text" value="" readonly="readonly"  placeholder="請點選右方日曆"/>
											<span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span></span>
										</div>
									</div>
									<span id="workDate-error" class="error_text"></span>
								</div>
							</div>
			 				<div class="form-group" id="endTimeForm">
								<label for="recipient-name" class="control-label col-sm-12">公告結束時間：</label>
								<div class="col-sm-12">
									<input id="endTime" name="endTime" class="form-control" size="16"
										type="text" value="" readonly="readonly" />
									<span id="endTime-error" class="error_text"></span>
								</div>
							</div>


							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">
									事由：
								</label>
								<div class="col-sm-12">
									<!-- <input type="text" class="form-control" id="spendTime" name="spendTime"/> -->
									<select class="form-control" id="description" name="description">
										<option value="">請選擇</option>
										<optgroup label="A 地區關聯">
											<option>A 一級-1 居住於確診熱區(萬華5/17)(台北新北5/20)</option>
											<option>A 一級-2 使用跨縣市大眾通勤, 所跨縣市政府宣布三級防疫(新北台北5/17)(全國5/20)</option>
											<option>A 一級-3 所居住社區出現確診案例, 未共用通風管道或共用電梯</option>
											<option>A 二級-1 所居住大樓出現確診案例, 有共用通風管道或共用電梯</option>
											<option>A 二級-2 本司辦公所在位於確診熱區(板橋中和5/25)</option>
											<option>A 三級-1 本司辦公所在大樓出現確診案例, 或由公司行政統一發布</option>
										</optgroup>
									
										<optgroup label="B 親友關聯">
											<option>B 一級-1 直系子女因防疫停課, 經主管核定業務急需以居家工作代替防疫照顧假者</option>
											<option>B 二級-1 一周內同室(同車)社交過的親友確診, 過程未戴口罩超過15分鐘</option>
											<option>B 三級-1 同居親友確診或收到自主通知單</option>
											<option>B 四級-1 同居親友確診, 因故無法在院隔離, 須在家隔離者</option>
										</optgroup>

										<optgroup label="C 個人">
											<option>C 一級-1 個人因防疫考量, 請長假超過兩周, 經主管核定業務急需以居家工作代替長假者</option>
											<option>C 三級-1 本人, 配偶確診或收到自主通知單</option>
											<option>C 四級-1 本人確診</option>
										</optgroup>

									</select>
									<span id="description-error" class="error_text"></span>
								</div>
							</div>


							<div class="form-group">
								<label for="recipient-name" class="control-label col-sm-12">主管：</label>
								<div class="col-sm-12">
									<input type="text" class="form-control" id="approvalBy"
										name="approvalBy" />
									<span id="approvalBy-error" class="error_text"></span>
								</div>
							</div>

						</div>
			        </form>
				</div>
				<div style="clear:both;"></div>
				<div class="modal-footer" style="margin-top:20px">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary btn-lg " id="save" data-loading-text="儲存中" onclick="submit()">儲存</button>
				</div>
			</div>
		</div>
 	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	<script src="<c:url value='/resources/datepicker/bootstrap-datepicker.js'/>" type="text/javascript"></script>
	
	<script type="text/javascript">
		var progressing = 0;
		jQuery().ready(function (){
			queryData(1);
			var today = new Date();
			var ruleId = ${operator.roleId} ;
			var pickerStartDate = getNextFirstDay();
			if(ruleId == '1'){
				pickerStartDate = getFormattedDate(getCurrentFirstDay(), 'y/M/d H:m');
			}

			$('#datetimepickerStart').datetimepicker({
				minView: "month",  //只顯示到日期，不顯示分秒
				format: 'yyyy/mm/dd',
				autoclose: true,
				// minuteStep: 30,
				focusOnShow: false,
				allowInputToggle: true,
				startDate: pickerStartDate,
				
			});

			$('#datetimepickerEnd').datetimepicker({
				minView: "month",  //只顯示到日期，不顯示分秒
				format: 'yyyy/mm/dd',
				autoclose: true,
				// minuteStep: 30,
				focusOnShow: false,
				allowInputToggle: true,
				startDate: pickerStartDate
			});
			$("#datetimepickerStart").on("changeDate", function (e) {
				$('#datetimepickerEnd').datetimepicker("setStartDate", e.date);
				calEndDateTime(e);

			});
		});


		var levelWFHDays;

		// level轉換天數
		$('#level').change(function () {

			if ($('#level').val() == '1') {
				levelWFHDays = '3';
				console.log("一級：" + levelWFHDays + "天");
			}
			if ($('#level').val() == '2') {
				levelWFHDays = '7';
				console.log("二級：" + levelWFHDays + "天");
			}
			if ($('#level').val() == '3') {
				levelWFHDays = "14";
				console.log("三級：" + levelWFHDays + "天");
			}
			if ($('#level').val() == '4') {
				levelWFHDays = "30";
				console.log("四級：" + levelWFHDays + "天");
			}

			// 五級時手動輸入天數
			if ($('#level').val() == '5') {
				$('#levelFive').show();

				$('#levelFiveDay').blur(function () {
					if (($('#levelFiveDay').val()) !== "") {
						levelWFHDays = $('#levelFiveDay').val();
						console.log("五級：" + levelWFHDays+ "天");

						$('#levelFiveDay').change(function(){
							$('#workDate').val('');
							$('#endTime').val('');
						});
					}
				});

			}else{
				$('#levelFive').hide();
				$('#levelFiveDay').val("");
			}
		});

		

		function calEndDateTime(e) {

			if ($('#level').val() != '' && $('#workDate').val() != '') {
				var setEndDate = new Date(e.date.getFullYear(), e.date.getMonth(), e.date.getDate());
				setEndDate.setHours(e.date.getHours());
				setEndDate.setMinutes(e.date.getMinutes());
				// setEndDate.setDate(setEndDate.getDate() + parseInt($('#level').val()) - 1);
				setEndDate.setDate(setEndDate.getDate() + parseInt(levelWFHDays) - 1);
				setEndDate.setHours(19);
				setEndDate.setMinutes(0);
				$('#endTime').val(getFormattedDate(setEndDate, "y/M/d"));

			}
		}


		$('#level').change(function () {
			$('#workDate').val('');
			$('#endTime').val('');
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
				url : "<c:url value='/security/WorkFromHome/ajaxDataLoading'/>",
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
			errorCode["1"] = "請選擇成員";
			errorCode["2"] = "請選擇遠端級別";
			errorCode["3"] = "請輸入時間";
			errorCode["4"] = "請輸入事由";
			errorCode["5"] = "請輸入主管";
			errorCode["6"] = "請輸入五級遠端天數";
			var errors = {};
			
			if($('#sysUserId :selected').val() == ''){
				errors['sysUserId'] = 1;
			}else {
				$('#sysUserId-error').hide();
			}
			if ($('#level :selected').val() == '') {
				errors['level'] = 2;
			} else {
				$('#level-error').hide();
			}
			if ($('#workDate').val() == '') {
				errors['workDate'] = 3;
			} else {
				$('#workDate-error').hide();
			}
			/*
			if ($('#endTime').val() == '') {
				errors['endTime'] = 3;
			} else {
				$('#endTime-error').hide();
			}
			*/
			if ($('#description').val() == '') {
				errors['description'] = 4;
			} else {
				$('#description-error').hide();
			}
			if ($('#approvalBy').val() == '') {
				errors['approvalBy'] = 5;
			} else {
				$('#approvalBy-error').hide();
			}

			if ($('#level').val() == '5'){
				if ($('#levelFiveDay').val() == '') {
					errors['levelFiveDay'] = 6;
				} else {
					$('#levelFiveDay-error').hide();
				}
			}if ($('#level').val() !== '5'){
				$('#levelFiveDay-error').hide();
			}

			


			
			var targetURL= "<c:url value='/security/WorkFromHome/add'/>";
			
			if(Object.keys(errors).length == 0){
				
				var data = $('#wfhForm').serialize();
				if(typeof($('#workFromHomeId').val()) != 'undefined' && $('#workFromHomeId').val() != ''){//easier
					targetURL= "<c:url value='/security/WorkFromHome/update'/>";
				}//yeah 
				$('#save').button('loading');
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
			for(var key in errors){
				//$("#"+key).css('width',90);
				$("#"+key+"-error").text(errorCode[errors[key]]);
				$("#"+key+"-error").show();
			}
		}
		
		function deleteData(id) {
			if(confirm("您確定要刪除此筆紀錄嗎？")) {
				var targetURL = "<c:url value='/security/WorkFromHome/" + id + "'/>";
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
		}
		
		function active(id){
			if(progressing == 1){
				return;
			}
			var text = "修改";
			if(typeof(id) == "undefined"){
				text = "新增";
				$('#workFromHomeId').val("");
				$('#sysUserId').val("");
				$('#level').val("");
				$('#levelFiveDay').val("");
				$('#levelFive').hide();
				$('#workDate').val("");
				$('#endTime').val("");
				$('#description').val("");
				$('#approvalBy').val("");
				$('#status').val("");
				$('#basicModal').find('.modal-title').text(text + "遠端作業紀錄");
				$('#sysUserId').attr("disabled", false);
				$('#level').css({"background-color": "#fff","pointer-events":"all"});
				$('#levelForm').css({"cursor": "auto"});
				$('#workDate').css({"background-color": "#fff","cursor": "auto"});
				$('#workDateForm').css({"cursor": "auto"});
				$('#datetimepickerStart').css({"pointer-events":"auto"});
				$('#basicModal').find('#workDateTitle').text("公告開始時間：");
				$('#endTimeForm').show();
				$('#levelFive').hide();
				$('#levelFiveDay-error').hide();
				$('#basicModal').modal('toggle');
			}else{
				progressing = 1;
				$("body").css("cursor", "progress");
				var targetURL= "<c:url value='/security/WorkFromHome/"+id+"'/>";
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
							$('#workFromHomeId').val(data.workFromHome.workFromHomeId);
							$('#sysUserId').val(data.workFromHome.sysUserId);
							$('#sysUserId').attr("disabled", true); //後端已擋，其他欄位後端未設定不能用disabled
							$('#level').val(data.workFromHome.level);
							$('#level').css({"background-color": "#eee","pointer-events":"none"});
							$('#levelForm').css({"cursor": "not-allowed"});
							$('#workDate').val(formatJsonDate(data.workFromHome.workDate, "y/M/d"));
							$('#workDate').css({"background-color": "#eee","cursor": "not-allowed","pointer-events":"none"});
							$('#workDateForm').css({"cursor": "not-allowed"});
							$('#datetimepickerStart').css({"pointer-events":"none"});
							$('#description').val(data.workFromHome.description);
							$('#approvalBy').val(data.workFromHome.approvalBy);
							$('#status').val(data.workFromHome.status);
							$('#basicModal').find('#workDateTitle').text("日期：");
							$('#basicModal').find('.modal-title').text(text + "遠端作業紀錄");
							$('#endTimeForm').hide();
							$('#levelFive').hide();


							$('#basicModal').modal('toggle');


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
