<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="20%">班別</td>
				<td width="20%">天數/時數</td>
				<td width="20%">起訖時間</td>
				<td width="20%">修改</td>
			</tr>
		</thead>
		<tbody>
			<c:if test="${totalRecord == 0}">
				<tr class="bg">
					<td colspan="5" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${dataList}" varStatus="vs">
				<tr>
					<td>
						${mappingOvertime.get(item.overtimeId).leaveName}
					</td>
					<td>
						${item.spendTime.intValue()}
						<c:if test="${mappingOvertime.get(item.overtimeId).unitType == 1}">天</c:if>
						<c:if test="${mappingOvertime.get(item.overtimeId).unitType == 2}">小時</c:if>
					</td>
					<td>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.startTime}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.startTime}" />
						</div>
						<div>至</div>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.endTime}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.endTime}" />
						</div>
					</td>
					<td>
						<a href="#" class="btn btn-default function_icon" onclick="active('${item.askForOvertimeId}')" title="修改"> 
							<i class="glyphicon glyphicon-pencil"></i>
						</a>
						<a href="#" class="btn btn-default function_icon" onclick="deleteData('${item.askForOvertimeId}')" title="刪除"> 
							<i class="glyphicon glyphicon-remove"></i>
						</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<nav style="text-align: center;">
	<ul class="pagination">
		<li><a href="javascript:queryData(${currentPage-1})">&laquo;</a></li>
		<c:forEach var="number" begin="${startPage}" end="${endPage}" >
			<li class="${currentPage == number ? 'active':''}"><a href="javascript:queryData(${number})">${number} <span class="sr-only">(current)</span></a></li>
		</c:forEach>
		<li><a href="javascript:queryData(${currentPage+1 > totalPage ? '0' :currentPage+1})">&raquo;</a></li>
	</ul>
</nav>