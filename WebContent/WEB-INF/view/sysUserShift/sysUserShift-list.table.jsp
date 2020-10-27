<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<div><h4><b>班別紀錄</b></h4></div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="20%">班別</td>
				<td width="20%">排班月份</td>
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
				<tr >
					<td>
						${item.boardTime} ~ ${item.finishTime}
					</td>
					<td>
						<fmt:formatDate pattern="yyyy/MM/dd" value="${item.enableMonth}" />
					</td>
					<td>
						<c:set var="today" value="<%=new Date()%>"/>
						<fmt:formatDate var="year" value="${today}" pattern="yyyy" />
						<fmt:formatDate var="month" value="${today}" pattern="MM" />
						<fmt:formatDate var="dataYear" value="${item.enableMonth}" pattern="yyyy" />
						<fmt:formatDate var="dataMonth" value="${item.enableMonth}" pattern="MM" />
						<c:if test="${dataYear > year || (dataYear==year && dataMonth > month)}">
							<a href="#" class="btn btn-default function_icon" onclick="active('${item.shiftId}')" title="修改"> 
								<i class="glyphicon glyphicon-pencil"></i>
							</a>
						</c:if>
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