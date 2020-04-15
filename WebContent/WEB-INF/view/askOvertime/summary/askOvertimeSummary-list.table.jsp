<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="50%">班別</td>
				<td width="50%">天數/時數</td>
			</tr>
		</thead>
		<tbody>
			<c:if test="${dataList == null || dataList.size()<1}">
				<tr class="bg">
					<td colspan="5" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${dataList}" varStatus="vs">
				<tr>
					<td>
						${mappingOvertime.get(item[2]).leaveName}
					</td>
					<td>
						已調${item[0].intValue()}
						<c:if test="${mappingOvertime.get(item[2]).unitType == 1}">天</c:if>
						<c:if test="${mappingOvertime.get(item[2]).unitType == 2}">小時</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>