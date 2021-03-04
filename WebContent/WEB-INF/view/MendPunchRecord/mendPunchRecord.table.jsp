<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<div><h4><b>補卡紀錄</b></h4></div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="10%">成員</td>
				<td width="15%">補卡時間</td>
				<td width="15%">申請原因</td>
				<td width="10%">審核狀態</td>
				<td width="10%">審核人員</td>
				<td width="15%">審核時間</td>
				<td width="15%">操作</td>
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
						${mapEnableRule2User.get(item.sysUserId).originalName}
					</td>
					<td>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.mendPunchTime}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.mendPunchTime}" />
						</div>
					</td>
					<td>
						${item.mendReason}
					</td>
					<td>
						<c:choose>
							<c:when test="${item.approvedStatus == '0'}">
								取消
							</c:when>
							<c:when test="${item.approvedStatus == 'P'}">
								審核中 
							</c:when>
							<c:when test="${item.approvedStatus == 'N'}">
								審核不通過 
							</c:when>
							<c:when test="${item.approvedStatus == 'Y'}">
								審核通過 
							</c:when>
							<c:otherwise>
								狀態不明
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						${userMap.get(item.modifyUser).userName}
					</td>
					<td>
						<div>
							<fmt:formatDate pattern="yyyy/MM/dd" value="${item.modifyDate}" />
						</div>
						<div>
							<fmt:formatDate pattern="HH:mm" value="${item.modifyDate}" />
						</div>
					</td>
					
					<td>
						<c:set var="today" value="<%=new Date()%>"/>
						<fmt:formatDate var="day" value="${today}" pattern="dd" />
						<fmt:formatDate var="month" value="${today}" pattern="MM" />
						<fmt:formatDate var="dataMonth" value="${item.mendPunchTime}" pattern="MM" />
						<c:if test="${dataMonth >= month || (day<4 && (dataMonth + 1) == month )}">
							<c:if test="${item.approvedStatus == 'P' }">
								<c:if test="${operator.roleId == '1' }">
									<a href="#" class="btn btn-default function_icon" onclick="provedData('${item.mendId}')" title="審核通過"> 
										<i class="glyphicon glyphicon-ok"></i>
									</a>
								</c:if>
								
								<a href="#" class="btn btn-default function_icon" onclick="deleteData('${item.mendId}')" title="取消"> 
									<i class="glyphicon glyphicon-remove"></i>
								</a>
							</c:if>
								
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