<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>
		<div>
			<h4>
			<br>
				<b>調班額度</b>
			</h4>
			<p style="font-size:16px">＊修改調班額度後，系統下周起將套用新設定額度＊</p>
			<br>
		</div>

		<!-- 調班剩餘額度 -->
		<table class="table table-striped" id="userLeaveDataArea">

			<thead>
				<tr style="background-color: #edf8ff; font-weight: bold;">
					<td width="30%">成員</td>
					<td width="30%">每周額度</td>
					<td width="30%">本周剩餘額度</td>
					<td width="10%">修改</td>
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
							${item.quota.intValue()}
						</td>
						<td>
							${item.count.intValue()}
						</td>
						<td>
							<c:if test="${operator.roleId == '1'}">

								<a href="#" id="askOvertimeEdit" class="btn btn-default function_icon" title="修改"
									onclick="edit('${item.userShiftQuotaId}')" style="background: #76bcff;">
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