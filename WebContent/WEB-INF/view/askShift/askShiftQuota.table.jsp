<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>

		<div>
			<br>
			<h4>
				<b>調班額度管理</b>
			</h4>
		</div>


		<!-- 調班剩餘額度 -->
		<table class="table table-striped" id="userLeaveDataArea">

			<thead>
				<tr style="background-color: #edf8ff; font-weight: bold;">
					<td width="30%">成員</td>
					<td width="20%">剩餘額度</td>
					<td width="20%">調班額度（每周）</td>
					<td width="15%">修改</td>
				</tr>
			</thead>
			<tbody>

				<c:if test="${userShiftQuotaList == null || userShiftQuotaList.size()<1}">
					<tr class="bg">
						<td colspan="5" align="center">沒有資料</td>
					</tr>
				</c:if>



				<c:forEach var="item" items="${userShiftQuotaList}">
					<tr>
						<td>
							${mapEnableRule2User.get(item.sysUserId).originalName}
						</td>
						<td>
							${item.count.intValue()}
						</td>
						<td>
							${item.quota.intValue()}
						</td>
						<td>
							<c:if test="${operator.roleId == '1'}">
								<a href="#" id="askOvertimeEdit" class="btn btn-default function_icon" title="修改"
									onclick="edit(item.userShiftQuotaId)" style="background: #76bcff;">
									<i class="glyphicon glyphicon-pencil"></i>
								</a>
							</c:if>

						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>


		<br />
		<br />