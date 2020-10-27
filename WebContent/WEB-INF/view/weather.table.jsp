<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/include/view-lib.jsp"%>

<c:set var="locationMap" value="${locationMap}" />
<div class="table-responsive">
	
	<c:choose>
	    <c:when test="${locationMap == null}">
	        <tr class="bg">
			<td colspan="3" align="center">沒有資料</td>
		</tr>
	    </c:when>
	    <c:otherwise>
	        <div id="weatherTable">
	        	<c:forEach var="item" items="${locationMap}">
	        		<c:if test="${item.key eq '桃園區'}">
						<h1>${item.key}</h1>
						<div>
							<table border="1">
								<c:forEach var="itemType" items="${item.value.weatherTypeList}">
									<c:if test="${itemType.weatherType eq '天氣現象'}">
										<thead>
									        <tr>
									            <th colspan="100">${itemType.weatherType}</th>
									        </tr>
									    </thead>
										<tbody>
											<tr>
									        	<c:forEach var="itemTime" items="${itemType.weatherTimeList}">
													<td style="<c:if test="${fn:indexOf(itemTime.elementValue, '雨') >=0 }">background-color: #000000; color: #ffffff;</c:if>">
														<fmt:formatDate pattern="MM/dd HH:mm" value="${itemTime.startTime}" />
													</td>
												</c:forEach>
											</tr>
											<tr>
									        	<c:forEach var="itemTime" items="${itemType.weatherTimeList}">
													<td style="<c:if test="${fn:indexOf(itemTime.elementValue, '雨') >=0 }">background-color: #000000; color: #ffffff;</c:if>">
														${itemTime.elementValue}
													</td>
												</c:forEach>
											</tr>
									    </tbody>
									</c:if>
								</c:forEach>
							</table>
						</div>
					</c:if>
				</c:forEach>
	        </div>
	    </c:otherwise>
	</c:choose>
</div>