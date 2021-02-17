<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead >
			<tr>
				<th style="width:3%">年</th>
				<th style="width:2%">月</th>
				<th style="width:5%">樓層</th>
				<th style="width:6%">成員</th>
				<th style="width:5%">班別</th>
				<th style="width:4%">缺班計算基數</th>
				<th style="width:4%">加班計算基數</th>
				<th style="width:4%">勤務計算基數</th>
				<th style="width:4%">加班</th>
				<th style="width:4%">排班</th>
				<th style="width:4%">作業時數</th>
				<th style="width:4%">缺席(曠職)</th>
				<th style="width:4%">特休</th>
				<th style="width:4%">事假</th>
				<th style="width:4%">病假</th>
				<th style="width:4%">生理假</th>
				<th style="width:4%">婚假</th>
				<th style="width:4%">公假</th>
				<th style="width:4%">產假</th>
				<th style="width:4%">陪產假</th>
				<th style="width:4%">喪假</th>
				<th style="width:4%">無薪假</th>
				<th style="width:4%">防疫假</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
			    <c:when test="${monthlyReportList == null || monthlyReportList.size()<1}">
			        <tr class="bg">
						<td colspan="3" align="center">沒有資料</td>
					</tr>
			    </c:when>    
			    <c:otherwise>
			        <c:forEach var="item" items="${monthlyReportList}" varStatus="vs">
						<tr>
							<td>${item.dateOfYear.intValue()}</td>
							<td>${item.dateOfMonth.intValue()}</td>
							<td>${item.department }</td>
							<td>${item.originalName }</td>
							<td>${item.userShift }</td>
							<td>${item.absentBase }</td>
							<td>加班</td>
							<td>勤務</td>
							<!-- 加班 -->
							<c:choose>
								<c:when test="${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() > 46 }">
									<td> <span style="color:red">${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() }/46時 </span></td>
								</c:when>
								<c:otherwise>
									<td>${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() }/46時</td>
								</c:otherwise>
							</c:choose>
							<!-- 排班 -->
							<c:choose>
								<c:when test="${item.schedulingHours.intValue() > 40 }">
									<td> <span style="color:red">${item.schedulingHours.intValue() }/40時 </span></td>
								</c:when>
								<c:otherwise>
									<td>${item.schedulingHours.intValue()}/40時</td>
								</c:otherwise>
							</c:choose>
							<!-- 作業時數 -->
							<c:choose>
								<c:when test="${item.workHours.intValue() > item.needWorkHours.intValue() }">
									<td> <span style="color:red">${item.workHours.intValue()}/${item.needWorkHours.intValue()}時</span></td>
								</c:when>
								<c:otherwise>
									<td>${item.workHours.intValue()}/${item.needWorkHours.intValue()}時</td>
								</c:otherwise>
							</c:choose>
							<!-- 缺席 -->
							<c:choose>
								<c:when test="${item.absenceHours.intValue() > 24 }">
									<td> <span style="color:red">${item.absenceHours}/24時 </span></td>
								</c:when>
								<c:otherwise>
									<td>${item.absenceHours}/24時</td>
								</c:otherwise>
							</c:choose>
							<!-- 特休 -->
							<c:choose>
								<c:when test="${item.l4.intValue() > item.ql4.intValue() }">
									<td> <span style="color:red">${item.l4.intValue()}/${item.ql4.intValue()}時</span></td>
								</c:when>
								<c:otherwise>
									<td>${item.l4.intValue()}/${item.ql4.intValue()}時</td>
								</c:otherwise>
							</c:choose>
							<!-- 事假 -->
							<c:choose>
								<c:when test="${item.l1.intValue() > item.ql1.intValue() }">
									<td> <span style="color:red">${item.l1.intValue()}/${item.ql1.intValue()}時</span></td>
								</c:when>
								<c:otherwise>
									<td>${item.l1.intValue()}/${item.ql1.intValue()}時</td>
								</c:otherwise>
							</c:choose>
							<!-- 病假 -->
							<c:choose>
								<c:when test="${item.l5.intValue() > item.ql5.intValue() }">
									<td> <span style="color:red">${item.l5.intValue()}/${item.ql5.intValue()}時</span></td>
								</c:when>
								<c:otherwise>
									<td>${item.l5.intValue()}/${item.ql5.intValue()}時</td>
								</c:otherwise>
							</c:choose>
							<!-- 生理假 -->
							<c:choose>
								<c:when test="${item.l3.intValue() > 1 }">
									<td> <span style="color:red">${item.l3.intValue() }天</span></td>
								</c:when>
								<c:otherwise>
									<td>${item.l3.intValue() }天</td>
								</c:otherwise>
							</c:choose>
							<td>婚假</td>
							<td>${item.l6.intValue() }時</td>
							<td>${item.l8.intValue() }天</td>
							<td>${item.l9.intValue() }天</td>
							<td>${item.l7.intValue() }天</td>
							<td>無薪</td>
							<td>防疫</td>
						</tr>
					</c:forEach>
			    </c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>