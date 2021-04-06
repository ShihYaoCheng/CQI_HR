<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<div class="table-responsive">
	<table class="table table-striped">
		<thead >
			<tr>
				<th style="width:3%">年</th>
				<th style="width:2%">月</th>
				<th style="width:4%">樓層</th>
				<th style="width:7%">成員</th>
				<th style="width:5%">班別</th>
				<th style="width:4%">缺班計算基數</th>
				<th style="width:4%"><span style="color:lightgray">加班計算基數</span></th>
				<th style="width:4%"><span style="color:lightgray">勤務計算基數</span></th>
				<th style="width:4%"><span style="color:lightgray">加班</span></th>
				<th style="width:4%"><span style="color:lightgray">排班</span></th>
				<th style="width:4%">作業時數</th>
				<th style="width:4%">缺席(曠職)</th>
				<th style="width:4%">特休/額度</th>
				<th style="width:4%">事假/額度</th>
				<th style="width:4%">病假/額度</th>
				<th style="width:4%">生理假</th>
				<th style="width:4%">公假</th>
				<th style="width:4%">產假</th>
				<th style="width:4%">陪產假</th>
				<th style="width:4%">喪假</th>
				<th style="width:4%"><span style="color:lightgray">婚假</span></th>
				<th style="width:4%"><span style="color:lightgray">無薪假</span></th>
				<th style="width:4%"><span style="color:lightgray">防疫假</span></th>
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
							<td><span style="color:lightgray">加班 </span></td>
							<td><span style="color:lightgray">勤務 </span></td>
							<!-- 加班 -->
							<td> 
								<c:choose>
									<c:when test="${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() > 46 }">
										<span style="color:red">${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() }/46時 </span>
									</c:when>
									<c:otherwise>
										<span style="color:lightgray">${item.o1.intValue()+item.o2.intValue() +item.o3.intValue() +item.o4.intValue() }/46時 </span>
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 排班 -->
							<td> 
								<c:choose>
									<c:when test="${item.schedulingHours.intValue() > 40 }">
										<span style="color:red">${item.schedulingHours.intValue() }/40時 </span>
									</c:when>
									<c:otherwise>
										<!--${item.schedulingHours.intValue()}/40時 -->
										<span style="color:lightgray">排/40時 </span>
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 作業時數 -->
							<td> 
								<c:choose>
									<c:when test="${item.workHours.intValue() > item.needWorkHours.intValue() }">
										<span style="color:red">${item.workHours.intValue()}/${item.needWorkHours.intValue()}時</span>
									</c:when>
									<c:otherwise>
										${item.workHours.intValue()}/${item.needWorkHours.intValue()}時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 缺席 -->
							<td> 
								<c:choose>
									<c:when test="${item.absenceHours.intValue() > 24 }">
										<span style="color:red">${item.absenceHours}/24時 </span>
									</c:when>
									<c:when test="${item.absenceHours.intValue() == 0 }">
										<span style="color:gray">${item.absenceHours}/24時 </span>
									</c:when>
									<c:otherwise>
										${item.absenceHours}/24時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 特休 -->
							<td> 
								<c:choose>
									<c:when test="${item.l4.intValue() > item.ql4.intValue() }">
										<span style="color:red">${item.l4.intValue()}/${item.ql4.intValue()}時</span>
									</c:when>
									<c:when test="${item.l4.intValue() == 0 }">
										<span style="color:gray">${item.l4.intValue()}/${item.ql4.intValue()}時</span>
									</c:when>
									<c:otherwise>
										${item.l4.intValue()}/${item.ql4.intValue()}時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 事假 -->
							<td> 
								<c:choose>
									<c:when test="${item.l1.intValue() > item.ql1.intValue() }">
										<span style="color:red">${item.l1.intValue()}/${item.ql1.intValue()}時</span>
									</c:when>
									<c:when test="${item.l1.intValue() == 0 }">
										<span style="color:gray">${item.l1.intValue()}/${item.ql1.intValue()}時</span>
									</c:when>
									<c:otherwise>
										${item.l1.intValue()}/${item.ql1.intValue()}時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 病假 -->
							<td> 
								<c:choose>
									<c:when test="${item.l5.intValue() > item.ql5.intValue() }">
										<span style="color:red">${item.l5.intValue()}/${item.ql5.intValue()}時</span>
									</c:when>
									<c:when test="${item.l5.intValue() == 0 }">
										<span style="color:gray">${item.l5.intValue()}/${item.ql5.intValue()}時</span>
									</c:when>
									<c:otherwise>
										${item.l5.intValue()}/${item.ql5.intValue()}時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 生理假 -->
							<td> 
								<c:choose>
									<c:when test="${item.l3.intValue() > 1 }">
										<span style="color:red">${item.l3.intValue() }天</span>
									</c:when>
									<c:when test="${item.l3.intValue() == 0 }">
										<span style="color:gray">${item.l3.intValue()}天</span>
									</c:when>
									<c:otherwise>
										${item.l3.intValue()}天
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 公假 -->
							<td>
								<c:choose>
									<c:when test="${item.l6.intValue() == 0}">
										<span style="color:gray">${item.l6.intValue() }時</span>
									</c:when>
									<c:otherwise>
										${item.l6.intValue()}時
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 產假 -->
							<td>
								<c:choose>
									<c:when test="${item.l8.intValue() == 0}">
										<span style="color:gray">${item.l8.intValue() }天</span>
									</c:when>
									<c:otherwise>
										${item.l8.intValue()}天
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 陪產假 -->
							<td>
								<c:choose>
									<c:when test="${item.l9.intValue() == 0}">
										<span style="color:gray">${item.l9.intValue() }天</span>
									</c:when>
									<c:otherwise>
										${item.l9.intValue()}天
									</c:otherwise>
								</c:choose>
							</td>
							<!-- 喪假 -->
							<td>
								<c:choose>
									<c:when test="${item.l7.intValue() == 0}">
										<span style="color:gray">${item.l7.intValue() }天</span>
									</c:when>
									<c:otherwise>
										${item.l7.intValue()}天
									</c:otherwise>
								</c:choose>
							</td>
							<td><span style="color:lightgray">婚假</span></td>
							<td><span style="color:lightgray">無薪</span></td>
							<td><span style="color:lightgray">防疫</span></td>
						</tr>
					</c:forEach>
			    </c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>