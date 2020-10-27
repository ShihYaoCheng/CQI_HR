<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="10%">人員</td>
				<td width="10%">級別</td>
				<td width="10%">起訖時間</td>
				<td width="10%">勤務核定主管簽核</td>
				<td width="10%">部門主管簽核</td>
				<td width="10%">財務簽核</td>
				<td width="10%">行政簽核</td>
				<td width="10%">總經理簽核</td>
				<td width="10%">狀態</td>
			</tr>
		</thead>
		<tbody>
			<c:if test="${emergenceOvertimeList == null || emergenceOvertimeList.size() < 1}">
				<tr class="bg">
					<td colspan="9" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${emergenceOvertimeList}" varStatus="vs">
				<tr>
					<td>
						<div>
							${userMap.get(askOvertimeMap.get(item.askForOvertimeId).sysUserId).originalName}
						</div>
						<div>
							<button type="button" class="btn btn-primary" onclick="showCalendar('${askOvertimeMap.get(item.askForOvertimeId).sysUserId}')">查看出勤資料</button>
						</div>
					</td>
					<td>
						<c:if test="${askOvertimeMap.get(item.askForOvertimeId).spendTime == 3}">C1狼級</c:if>
						<c:if test="${askOvertimeMap.get(item.askForOvertimeId).spendTime == 6}">C2虎級</c:if>
						<c:if test="${askOvertimeMap.get(item.askForOvertimeId).spendTime == 9}">C3鬼級</c:if>
						<c:if test="${askOvertimeMap.get(item.askForOvertimeId).spendTime == 12}">C4龍級</c:if>
						<c:if test="${askOvertimeMap.get(item.askForOvertimeId).spendTime == 15}">C5神級</c:if>
					</td>
					<td>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${askOvertimeMap.get(item.askForOvertimeId).startTime}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${askOvertimeMap.get(item.askForOvertimeId).startTime}" />
						</div>
						<div>至</div>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${askOvertimeMap.get(item.askForOvertimeId).endTime}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${askOvertimeMap.get(item.askForOvertimeId).endTime}" />
						</div>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.projectSignTime == null && item.status > 0}">
								
							</c:when>
							<c:otherwise>
								<div>
									${userMap.get(item.projectSignerId).originalName}
								</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd" value="${item.projectSignTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm" value="${item.projectSignTime}" />
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.departmentSignTime == null && item.status > 0}">
								<c:if test="${sessionScope.__session_info.logInInfo.departmentMaster==1}">
									<a href="#" class="btn btn-default function_icon" onclick="sign('department', 'confirm', '${item.signId}')" title="簽核"> 
										<i class="glyphicon glyphicon-check"></i>
									</a>
									<a href="#" class="btn btn-default function_icon" onclick="sign('department', 'reject', '${item.signId}')" title="退回"> 
										<i class="glyphicon glyphicon-remove-sign"></i>
									</a>
								</c:if>
							</c:when>
							<c:otherwise>
								<div>
									${userMap.get(item.departmentSignerId).originalName}
								</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd" value="${item.departmentSignTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm" value="${item.departmentSignTime}" />
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.financeSignTime == null && item.status > 0}">
								<c:if test="${sessionScope.__session_info.logInInfo.financeMaster==1}">
									<a href="#" class="btn btn-default function_icon" onclick="sign('finance', 'confirm', '${item.signId}')" title="簽核"> 
										<i class="glyphicon glyphicon-check"></i>
									</a>
									<a href="#" class="btn btn-default function_icon" onclick="sign('finance', 'reject', '${item.signId}')" title="退回"> 
										<i class="glyphicon glyphicon-remove-sign"></i>
									</a>
								</c:if>
							</c:when>
							<c:otherwise>
								<div>
									${userMap.get(item.financeSignerId).originalName}
								</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd" value="${item.financeSignTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm" value="${item.financeSignTime}" />
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.administrationSignTime == null && item.status > 0}">
								<c:if test="${sessionScope.__session_info.logInInfo.administrationManager==1}">
									<a href="#" class="btn btn-default function_icon" onclick="sign('administration', 'confirm', '${item.signId}')" title="簽核"> 
										<i class="glyphicon glyphicon-check"></i>
									</a>
									<a href="#" class="btn btn-default function_icon" onclick="sign('administration', 'reject', '${item.signId}')" title="退回"> 
										<i class="glyphicon glyphicon-remove-sign"></i>
									</a>
								</c:if>
							</c:when>
							<c:otherwise>
								<div>
									${userMap.get(item.administrationSignerId).originalName}
								</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd" value="${item.administrationSignTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm" value="${item.administrationSignTime}" />
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.companySignTime == null && item.status > 0}">
								<c:if test="${sessionScope.__session_info.logInInfo.companyGod==1}">
									<a href="#" class="btn btn-default function_icon" onclick="sign('company', 'confirm', '${item.signId}')" title="簽核"> 
										<i class="glyphicon glyphicon-check"></i>
									</a>
									<a href="#" class="btn btn-default function_icon" onclick="sign('company', 'reject', '${item.signId}')" title="退回"> 
										<i class="glyphicon glyphicon-remove-sign"></i>
									</a>
								</c:if>
							</c:when>
							<c:otherwise>
								<div>
									${userMap.get(item.companySignerId).originalName}
								</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd" value="${item.companySignTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm" value="${item.companySignTime}" />
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.status == 1}">
								<div>
									正常
								</div>
							</c:when>
							<c:when test="${item.status == -1}">
								<div style="color: red;">
									勤務核定主管退審
								</div>
							</c:when>
							<c:when test="${item.status == -2}">
								<div style="color: red;">
									部門主管退審
								</div>
							</c:when>
							<c:when test="${item.status == -3}">
								<div style="color: red;">
									財務退審
								</div>
							</c:when>
							<c:when test="${item.status == -4}">
								<div style="color: red;">
									總經理退審
								</div>
							</c:when>
							<c:otherwise>
								<div>
									未知
								</div>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>