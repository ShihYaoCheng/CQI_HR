<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
<br/>
	<div><h4><b>**自動請假設定將於設定的當月份每個上班日的上班時間前十分鐘內隨機自動事假申請**</b></h4></div>
	<div><h4><b>**僅申請上午時段，所以會根據上班時間來請兩個小時/三個小時的事假**</b></h4></div>
	<div><h4><b>**如果當天後來有上班請自行上系統調整修正**</b></h4></div>
	
	<div><h4><b>自動請假設定</b></h4></div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td width="30%">年月份</td>
				<td width="30%">上班時間</td>
				<td width="20%">狀態</td>
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
						${item.dateOfYear}/${item.dateOfMonth}
					</td>
					<td>
						${item.getIntoOfficesTime}
					</td>
					<td>
						<c:if test="${item.status == 1}">啟用</c:if>
						<c:if test="${item.status == 0}">停用</c:if>
					</td>
					<td>
						<a href="#" class="btn btn-default function_icon" onclick="active('${item.autoId}')" title="修改"> 
							<i class="glyphicon glyphicon-pencil"></i>
						</a>
						<c:if test="${item.status == 1}">
							<a href="#" class="btn btn-default function_icon" onclick="disableData('${item.autoId}')" title="停用"> 
								<i class="glyphicon glyphicon-ban-circle"></i>
							</a>
						</c:if>
						<c:if test="${item.status == 0}">
							<a href="#" class="btn btn-default function_icon" onclick="enableData('${item.autoId}')" title="啟用"> 
							<i class="glyphicon glyphicon-flash"></i>
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