<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th style="width:20%">假別</th>
				<th style="width:25%">天數/時數</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${totalRecord == 0}">
				<tr class="bg">
					<td colspan="2" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${dataList}" varStatus="vs">
				<tr >
					<td>
						${mappingLeave.get(item.leaveId).leaveName}
					</td>
					<td>
						還有　 ${item.count.intValue()}
						<c:if test="${mappingLeave.get(item.leaveId).unitType == 1}">天</c:if>
						<c:if test="${mappingLeave.get(item.leaveId).unitType == 2}">小時</c:if>
						　可以請
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>