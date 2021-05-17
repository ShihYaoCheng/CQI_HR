<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>
		<div id="userLeaveDataText">
			<h4><b>
					***為方便結算出勤時數，每月四日零時零分起將無法更動上個月的出勤資料。感謝配合。***
					<br />*特休功能上線，有問題請回報。目前特休以小時計算。一天工時8小時。
					<br /><br /><br />調班額度</b>
			</h4>
		</div>
		<table class="table table-striped" id="userLeaveDataArea">
			<thead>
				<tr style="background-color: #edf8ff; font-weight: bold;">
					<td>假別</td>
					<td>剩餘額度</td>
				</tr>
			</thead>
			<tbody>
				<c:if test="${userLeaveList == null || userLeaveList.size()<1}">
					<tr class="bg">
						<td colspan="5" align="center">沒有資料</td>
					</tr>
				</c:if>
				<c:forEach var="userLeave" items="${userLeaveList}" varStatus="vs">
					<tr>
						<td>
							${mappingLeave.get(userLeave.leaveId).leaveName}
						</td>
						<td>
							${userLeave.count.intValue()}
							<c:if test="${mappingLeave.get(userLeave.leaveId).unitType == 1}">天</c:if>
							<c:if test="${mappingLeave.get(userLeave.leaveId).unitType == 2}">小時</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br />
		<br />