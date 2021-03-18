<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	
	<div><h4><b>全體公假紀錄</b></h4></div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="10%">假別</td>
				<td width="20%">開始時間</td>
				<td width="20%">結束時間</td>
				<td width="20%">編輯准假成員</td>
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
					<td>${mappingLeave.get(item.leaveId).leaveName}</td>
					<td><div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.officialLeaveStartDate}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.officialLeaveStartDate}" />
						</div>
					</td>
					<td><div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.officialLeaveEndDate}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.officialLeaveEndDate}" />
						</div>
					</td>
					<td>
						<c:set var="today" value="<%=new Date()%>"/>
						<fmt:formatDate var="day" value="${today}" pattern="dd" />
						<fmt:formatDate var="month" value="${today}" pattern="MM" />
						<fmt:formatDate var="dataMonth" value="${item.officialLeaveStartDate}" pattern="MM" />
						<c:if test="${dataMonth >= month || (day<4 && (dataMonth + 1) == month )}">
							<a href="#" class="btn btn-default function_icon" onclick="queryUserList('${item.giveOfficialLeaveId}')" title="成員清單"> 
								<i class="glyphicon glyphicon-user"></i>
							</a>
							<a href="#" class="btn btn-default function_icon" onclick="deleteData('${item.giveOfficialLeaveId}')" title="刪除"> 
								<i class="glyphicon glyphicon-remove"></i>
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