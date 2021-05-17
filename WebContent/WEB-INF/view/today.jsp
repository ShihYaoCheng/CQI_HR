<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>
<html>
<head>
	<%@include file="/WEB-INF/view/include/view-html-head.jsp"%>
	<link href="<c:url value='/resources/css/today.css?version=3'/>" rel="stylesheet">
</head>

<body role="document">
	<div class="container theme-showcase" role="main">
		<div class="row row-offcanvas row-offcanvas-left">
			<div class="col-xs-12 col-md-12 single_table">
				<div id="weatherData"></div>
			</div>
			<!--/span-->
			<div class="col-xs-12 col-md-12 single_table">
				<form class="navbar-form">
					<h3><b>當日出勤狀況</b></h3>
					<br/>
					<div class="form-group" style="display: inline;">
						<div>
							<h3><fmt:formatDate pattern="yyyy年MM月dd日" value="${nowCalendar.getTime()}" /></h3>
						</div>
						<table class="table table-striped table-bordered">
							<tr>
								<td class="absence-time" style="width:15%;">缺席</td>
								<td class="ask-leave-time" style="width:15%;">請假</td>
								<td class="working-time" style="width:15%;">上班</td>
								<td class="rest-time" style="width:15%;">休息</td>
								<td class="has-overtime" style="width:15%;">調班</td>
								<td class="no-attendance-time" style="width:15%;">待更新</td>
							</tr>
						</table>
					</div>
				</form>
				<div class="table-responsive">
					<table class="table table-striped table-bordered" id="userLeaveDataArea">
						<thead>
							<tr>
								<td colspan="2" align="center">樓層</td>
								<td colspan="11" align="center">時間</td>
								<td align="center">調班</td>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${isWorkDay}">
									<c:if test="${userList == null || userList.size()<1}">
										<tr class="bg">
											<td colspan="12" align="center">沒有資料</td>
										</tr>
									</c:if>
									<c:forEach var="user" items="${userList}" varStatus="vs">
										<c:if test="${user.roleId==2 && user.userName ne 'JZ'}">
											<tr>
												<td class="text-center">
													${user.department}
												</td>
												<td class="text-center">
													${user.userName} | <c:if test="${attendance.get(user.sysUserId)!=null}">${attendance.get(user.sysUserId).arriveTime} ~ ${attendance.get(user.sysUserId).leaveTime}</c:if>
												</td>
												<c:forEach var="i" begin="8" end="18" step="1">
													<c:choose>
														<c:when test="${i==12 || shift.get(user.sysUserId)==null}">
															<td class="text-center rest-time">
																${i}
															</td>
														</c:when>
														<c:otherwise>
															<c:set var="isLeaveThrough" value="false" />
															<c:if test="${todayLeave.get(user.sysUserId)!=null}">
																<c:set var="isLeaveThrough" value="false" />
																<c:forEach var="userLeave" items="${todayLeave.get(user.sysUserId)}" varStatus="vs">
																	<c:if test="${(startWork.timeInMillis + (i * 3600000)) >= userLeave.startTime.time && (startWork.timeInMillis + (i * 3600000)) < userLeave.endTime.time }">
																		<c:set var="isLeaveThrough" value="true" />
																	</c:if>
																</c:forEach>
															</c:if>
															<c:set var="today" value="<%=new Date()%>"/>
															<fmt:formatDate var="nowHour" value="${today}" pattern="HH" />
															<fmt:parseNumber var="nowHourNumber" type="number" value="${nowHour}" />
															<fmt:formatDate var="nowMinute" value="${today}" pattern="mm" />
															<c:choose>
																<c:when test="${attendance.get(user.sysUserId)==null}">
																	
																	<td class="text-center 
																	<!-- 123 -->
																		<c:choose>
																			
																			<c:when test="${ i==8  && shift.get(user.sysUserId).boardTime=='09:00' }">rest-time</c:when>
																			<c:when test="${ i==8  && shift.get(user.sysUserId).boardTime=='10:00' }">rest-time</c:when>
																			<c:when test="${ i==9  && shift.get(user.sysUserId).boardTime=='10:00' }">rest-time</c:when>
																			<c:when test="${ i==17 && shift.get(user.sysUserId).finishTime=='17:00' }">rest-time</c:when>
																			<c:when test="${ i==18 && shift.get(user.sysUserId).finishTime=='17:00' }">rest-time</c:when>
																			<c:when test="${ i==18 && shift.get(user.sysUserId).finishTime=='18:00' }">rest-time</c:when>
																			<c:when test="${isLeaveThrough}">ask-leave-time</c:when>
																			<c:when test="${nowHourNumber<i || (nowHourNumber==i && nowMinute<'15')}">working-time</c:when>
																			<c:otherwise>absence-time</c:otherwise></c:choose>">
																		${i}
																	</td>

																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${attendance.get(user.sysUserId).arriveTime!=null && attendance.get(user.sysUserId).arriveTime!=''}" >
																			<fmt:parseNumber var="arriveHour" type="number" value="${fn:split(attendance.get(user.sysUserId).arriveTime, ':')[0]}" />
																			<fmt:parseNumber var="arriveMinute" type="number" value="${fn:split(attendance.get(user.sysUserId).arriveTime, ':')[1]}" />
																			<fmt:parseNumber var="leaveHour" type="number" value="${fn:split(attendance.get(user.sysUserId).leaveTime, ':')[0]}" />
																			<td class="text-center 
																				<c:choose>
																					<!--/span WFH-->
																					<c:when test="${ i==8  && shift.get(user.sysUserId).boardTime=='09:00' }">rest-time</c:when>
																					<c:when test="${ i==8  && shift.get(user.sysUserId).boardTime=='10:00' }">rest-time</c:when>
																					<c:when test="${ i==9  && shift.get(user.sysUserId).boardTime=='10:00' }">rest-time</c:when>
																					<c:when test="${ i==17 && shift.get(user.sysUserId).finishTime=='17:00' }">rest-time</c:when>
																					<c:when test="${ i==18 && shift.get(user.sysUserId).finishTime=='17:00' }">rest-time</c:when>
																					<c:when test="${ i==18 && shift.get(user.sysUserId).finishTime=='18:00' }">rest-time</c:when>
																					<c:when test="${isLeaveThrough}">ask-leave-time</c:when>
																					<c:when test='${arriveHour==i && arriveMinute > 15}'>absence-time</c:when>
																					<c:when test='${(arriveHour!=null && arriveHour <= i) && (leaveHour==null || leaveHour>=i)}'>working-time</c:when>
																					<c:when test="${isLeaveThrough}">ask-leave-time</c:when>
																					<c:when test="${attendance.get(user.sysUserId).arriveTime==attendance.get(user.sysUserId).leaveTime ||nowHourNumber<i || (nowHourNumber==i && nowMinute<'15')}">working-time</c:when>
																					<c:when test="${arriveHour < i && attendance.get(user.sysUserId).arriveTime!=null && attendance.get(user.sysUserId).leaveTime!=''}">no-attendance-time</c:when>
																					<c:otherwise>absence-time</c:otherwise>
																				</c:choose>">
																				${i}
																			</td>

																		</c:when>
																		<c:otherwise>
																			<td class="text-center working-time">
																				${i}
																			</td>
																		</c:otherwise>
																	</c:choose>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:forEach>
												<c:choose>
													<c:when test="${todayOvertime.get(user.sysUserId)!=null}">
														<c:set var="overtimeSum" value="0" />
														<c:forEach var="userOvertime" items="${todayOvertime.get(user.sysUserId)}" varStatus="vs">
															<c:set var="overtimeSum" value="${overtimeSum + userOvertime.spendTime}" />
														</c:forEach>
														<td class="text-center has-overtime">
															+${overtimeSum.intValue()}H
														</td>
													</c:when>
													<c:otherwise>
														<td class="text-center rest-time">
															0
														</td>
													</c:otherwise>
												</c:choose>
											</tr>
										</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="bg">
										<td colspan="13" align="center"><h1>放假啦~~~~~~!</h1></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/view/include/view-html-bottom.jsp"%>
	
	<script>
	jQuery().ready(function (){
		queryData();
	});
	
	function queryData() {
		$.ajax({
			type : "GET",
			url : "<c:url value='/weather'/>",
			data : {
			},
			contentType: "application/json",
			success : function(data) {
				console.log(data);
				$("#weatherData").html(data);
			}
		});
	}
	
	</script>
</body>
</html>
