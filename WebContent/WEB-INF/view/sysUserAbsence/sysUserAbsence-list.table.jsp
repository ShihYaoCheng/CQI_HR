<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead>
			<tr>
				<th style="width:20%">人員</th>
				<th style="width:25%">人員名稱</th>
				<th style="width:30%">啟用狀況</th>
				<th style="width:25%"></th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${totalRecord == 0}">
				<tr class="bg">
					<td colspan="4" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${dataList}" varStatus="vs">
				<tr >
					<td>
						${item.sysUserId}
					</td>
					<td>
						${item.originalName} : ${item.userName}
					</td>
					<td>
						<i class="glyphicon ${item.status eq 'y' ? 'glyphicon-ok' : item.status eq 'l' ? 'glyphicon-remove-sign' : 'glyphicon-remove'}" 					
							style=" color:${item.status eq 'y' ? 'green':'red'}"></i>
							${item.status eq 'y' ? '在職' : item.status eq 'l' ? '留職停薪' : '離職'}
						
					</td>
					<td>
						<div>
							<a href="#" class="btn btn-default function_icon" onclick="active('${item.sysUserId}')" title="申請留職停薪"> 
								<i class="glyphicon glyphicon-pencil"></i>申請留職停薪
							</a>
						</div>
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






<div  class="table-responsive" style="margin: 30px;" > 
	<table id="tableSysUserAbsence"  class="table table-striped">
		<thead>
			<tr>
				<th style="width:20%">人員</th>
				<th style="width:20%">人員名稱</th>
				<th style="width:20%">留職停薪開始日期</th>
				<th style="width:20%">留職停薪結束日期</th>
				<th style="width:20%">申請日期</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${totalRecord == 0}">
				<tr class="bg">
					<td colspan="2" align="center">沒有資料</td>
				</tr>
			</c:if>
			<c:forEach var="item" items="${SysUserAbsenceList}" varStatus="vs">
				<tr >
					<td>
						${item.sysUserId}
					</td>
					<td>
						${userMap.get(item.sysUserId).originalName}
					</td>
					<td>
						${item.effectiveDate}
					</td>
					<td>
						${item.expirationDate}
					</td>
					<td>
						${item.createDate}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>