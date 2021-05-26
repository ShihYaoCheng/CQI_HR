<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@include file="/WEB-INF/view/include/view-lib.jsp" %>
		<div>
			<h4>
				<b>調班額度管理</b>
			</h4>
		</div>
		<div class="btn-group">
			<a href="#" class="btn btn-default function_icon" onclick="edit()" title="新增" id="edit" style="background: #76bcff;">
				<i class="glyphicon glyphicon-plus"></i>
			</a>
		</div>
		<br><br>

		<!-- 調班剩餘額度 -->
		<table class="table table-striped" id="userLeaveDataArea">

			<thead>
				<tr style="background-color: #edf8ff; font-weight: bold;">
					<td width="50%">成員</td>
					<td width="35%">調班額度（每周）</td>
					<td width="15%">修改</td>
				</tr>
			</thead>
			<tbody>

				<c:if test="${userLeaveList == null || userLeaveList.size()<1}">
					<tr class="bg">
						<td colspan="5" align="center">沒有資料</td>
					</tr>
				</c:if>



				<c:forEach var="userLeave" items="${userLeaveList}">
					<tr>
						<td>
							yuri
						</td>
						<td>

							1
						</td>
						<td>
							<a href="#" id="askOvertimeEdit" class="btn btn-default function_icon"  title="修改" onclick="edit(id)" style="background: #76bcff;"> 
								<i class="glyphicon glyphicon-pencil"></i>
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>


		<br />
		<br />


		