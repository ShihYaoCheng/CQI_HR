<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead >
			<tr>
				<th style="width:6%">人員</th>
				<th style="width:8%">日期</th>
				<th style="width:6%">星期</th>
				<th style="width:8%">班別</th>
				<th style="width:6%">上班</th>
				<th style="width:6%">下班</th>
				<th style="width:8%">打卡時數</th>
				<th style="width:8%">休息時數</th>
				<th style="width:8%">缺席時數</th>
				<th style="width:8%">請假時數</th>
				<th style="width:8%">加班時數</th>
		
			</tr>
		</thead>
		<tbody>
			<c:choose>
			    <c:when test="${dailyAttendanceRecordList == null || dailyAttendanceRecordList.size()<1}">
			        <tr class="bg">
						<td colspan="3" align="center">沒有資料</td>
					</tr>
			    </c:when>    
			    <c:otherwise>
			        <c:forEach var="item" items="${dailyAttendanceRecordList}" varStatus="vs">
						<tr>
							<td>${userMap.get(item.sysUserId).originalName}</td>
							<td><fmt:formatDate pattern="yyyy/MM/dd" value="${item.attendanceDate}" /></td>
							<td><fmt:formatDate pattern="E" value="${item.attendanceDate}" /> </td>
							<td>${shiftMap.get(item.sysUserId).boardTime} ~ ${shiftMap.get(item.sysUserId).finishTime}</td>
							<td>${item.arriveTime}</td>
							<td>${item.leaveTime}</td>
							<td>${item.attendHours.intValue()}時</td>
							<td>1 時</td>
							<c:choose>
								<c:when test="${item.absenceHours.intValue() > 0 }">
									<td> <span style="color:red"> ${item.absenceHours.intValue()}時 </span></td>
								</c:when>
								<c:otherwise>
									<td>${item.absenceHours.intValue()}時</td>
								</c:otherwise>
							</c:choose>
							<td>${item.leaveHours.intValue()}時</td>
							<td>${item.overtimeHours.intValue()}時</td>
						
						</tr>
					</c:forEach>
			    </c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>