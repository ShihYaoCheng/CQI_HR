<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>

		<div class="table-responsive">
			<div>
				<h4><b>排班紀錄</b></h4>
			</div>
			<table class="table table-striped">
				<thead>
					<tr style="background-color: #edf8ff; font-weight: bold;">
						<td width="10%">成員</td>
						<td width="10%">班別</td>
						<td width="15%">天數/時數</td>
						<td width="15%">排班時間</td>
						<td width="15%">排休時間</td>
						<td width="15%">事由</td>
						<td width="10%">修改</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${totalRecord == 0}">
						<tr class="bg">
							<td colspan="9" align="center">沒有資料</td>
						</tr>
					</c:if>
					<c:forEach var="item" items="${dataList}" varStatus="vs">
						<tr>
							<td>
								${mapEnableRule2User.get(item.sysUserId).originalName}
							</td>
							<td>
								${mappingOvertime.get(item.overtimeId).leaveName}
							</td>

							<td>
								${item.spendTime.intValue()}
								<c:if test="${mappingOvertime.get(item.overtimeId).unitType == 1}">天</c:if>
								<c:if test="${mappingOvertime.get(item.overtimeId).unitType == 2}">小時</c:if>
							</td>

							<!-- 排班時間 -->
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


							<!-- 排休時間 -->
							<td>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd"
										value="${mapUserAskForLeaveByOvertimeId.get(item.askForOvertimeId).startTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm"
										value="${mapUserAskForLeaveByOvertimeId.get(item.askForOvertimeId).startTime}" />
								</div>
								<div>至</div>
								<div>
									<fmt:formatDate pattern="yyyy/MM/dd"
										value="${mapUserAskForLeaveByOvertimeId.get(item.askForOvertimeId).endTime}" />
								</div>
								<div>
									<fmt:formatDate pattern="HH:mm"
										value="${mapUserAskForLeaveByOvertimeId.get(item.askForOvertimeId).endTime}" />
								</div>
							</td>

							<td>
								${item.description}
							</td>

							<td>
								<c:set var="today" value="<%=new Date()%>" />
								<fmt:formatDate var="day" value="${today}" pattern="dd" />
								<fmt:formatDate var="month" value="${today}" pattern="MM" />
								<fmt:formatDate var="dataMonth" value="${item.startTime}" pattern="MM" />
								<c:if test="${ dataMonth >= month || (day<4 && (dataMonth + 1) == month )}">
									<c:if test="${operator.roleId == '2'}">
										<a href="#" class="btn btn-default function_icon"
											onclick="deleteData('${item.askForOvertimeId}')" title="刪除">
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
				<c:forEach var="number" begin="${startPage}" end="${endPage}">
					<li class="${currentPage == number ? 'active':''}"><a
							href="javascript:queryData(${number})">${number} <span class="sr-only">(current)</span></a>
					</li>
				</c:forEach>
				<li><a href="javascript:queryData(${currentPage+1 > totalPage ? '0' :currentPage+1})">&raquo;</a></li>
			</ul>
		</nav>