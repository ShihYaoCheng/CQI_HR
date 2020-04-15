<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th style="width:20%">成員</th>
				<th style="width:20%">假別</th>
				<th style="width:25%">剩餘額度</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
			    <c:when test="${userLeaveQuotaList == null || userLeaveQuotaList.size()<1}">
			        <tr class="bg">
					<td colspan="3" align="center">沒有資料</td>
				</tr>
			    </c:when>    
			    <c:otherwise>
			        <c:forEach var="item" items="${userLeaveQuotaList}" varStatus="vs">
						<tr>
							<td>
								${userMap.get(item.sysUserId).originalName}
							</td>
							<td>
								${mappingLeave.get(item.leaveId).leaveName}
							</td>
							<td>
								${item.monthlySummaryQuota.intValue()}
								<c:if test="${mappingLeave.get(item.leaveId).unitType == 1}">天</c:if>
								<c:if test="${mappingLeave.get(item.leaveId).unitType == 2}">小時</c:if>
							</td>
						</tr>
					</c:forEach>
			    </c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>