<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th style="width:25%">成員</th>
				<th style="width:25%">卡號</th>
				<th style="width:25%">打卡時間</th>
				<th style="width:25%">建立時間</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
			    <c:when test="${dataList == null || dataList.size()<1}">
			        <tr class="bg">
					<td colspan="3" align="center">沒有資料</td>
				</tr>
			    </c:when>    
			    <c:otherwise>
			        <c:forEach var="item" items="${dataList}" varStatus="vs">
						<tr>
							<td>
								${cardMap.get(item.cardId).originalName}
							</td>
							<td>
								${item.cardId}
							</td>
							<td>
								<fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${item.time}" />
							</td>
							<td>
								<fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${item.createDate}" />
							</td>
						</tr>
					</c:forEach>
			    </c:otherwise>
			</c:choose>
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