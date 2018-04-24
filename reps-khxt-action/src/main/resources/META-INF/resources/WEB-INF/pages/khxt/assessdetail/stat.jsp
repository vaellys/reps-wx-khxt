<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>考核结果汇总</title>
<reps:theme />
<style type="text/css">

table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 80%;
	margin-left: 30px;
	text-align:center;
	margin-top: 5px;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #d0d0d0;
	background-color: #EEE;
}

table.gridtable td {
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #ededed;
	background-color: #ffffff;
}
</style>
</head>
<body>
	<reps:container>
		<reps:panel  id="top" dock="top" method="post"
			action="${ctx }/reps/khxt/assessdetail/stat.mvc" formId="queryForm">
			
			<%-- <reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="被考核人" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="bkhrPersonName" maxLength="20">${member.bkhrPersonName }</reps:input>
			</reps:formfield>
			<reps:formfield label="考核人" labelStyle="width:20%;" textStyle="width:27%;" >
				<reps:input name="khrPersonName" maxLength="20">${member.khrPersonName }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm"  cssClass="search-form-a"></reps:ajaxgrid>
			<reps:button cssClass="search-form-a" id="search" value="查询" onClick="query();"></reps:button>
		</reps:querybuttons> --%>
		
			<reps:footbar>
				<input type="hidden" name="sheetId" value="${sheetId }">
				<reps:button cssClass="export-a" id="exportExcel" value="导出" onClick="exportExcel();"></reps:button>
				<reps:button cssClass="return-a"
					action="${ctx}/reps/khxt/appraise/assessslist.mvc" value="返回" />
			</reps:footbar>
		</reps:panel>
		<reps:panel id="myform" dock="center">
					<table class="gridtable" id="gridTable">
					<caption style="padding: 8px;font-weight: bold;">${sheet.season }${sheet.name }</caption>
						<tbody>
							<tr>
								<th>序号</th>
								<th>被考核人</th>
								<th>考核人</th>
								<c:forEach var="item" items="${items}" varStatus="status">
								<c:set var="count" value="${count+ item.point}" />
								<th>
									${item.name}
									（<fmt:formatNumber value="${item.point}" pattern="#.#" />）
								</th>
								</c:forEach>
								<th>
									合计
									（<fmt:formatNumber value="${count}" pattern="#.#" />）
								</th>
							</tr>
							
							<c:forEach var="member" items="${assessItems}" varStatus="status">
							<c:forEach var="i" items="${member.itemList}" varStatus="s">
								<tr>
									<c:if test="${s.count == 1 }">
									<td rowspan="${member.itemList.size() + 1}">${status.count }</td>
									<td rowspan="${member.itemList.size() + 1}">
									<reps:dialog cssClass="" id="detail" iframe="true"
											width="600" height="200"
											url="${ctx }/reps/khxt/member/workdetail.mvc?sheetId=${member.sheetId }&personId=${member.bkhrPersonId }"
											value="${member.bkhrPersonName }" title="${sheet.season }份工作人员日常考核量化评分细则"></reps:dialog>
									</td>
									</c:if>
									
									<td>${i.khrPersonName }</td>
									<c:forEach var="item" items="${items}" >
										<td><c:forEach var="p" items="${i.itemPoints}"><c:if test="${p.itemId == item.id }"><fmt:formatNumber value="${p.point }" pattern="#.#" /></c:if></c:forEach></td>
									</c:forEach>
									<td><fmt:formatNumber value="${i.sumOfPerRow }" pattern="#.#" /></td>
								</tr>
							</c:forEach>
							<tr>
								<td></td>
								<c:forEach var="m" items="${items}" varStatus="status">
								<td>
									<c:forEach var="map" items="${member.itemWeightList}">
									<c:if test="${m.id == map.id}"><fmt:formatNumber value="${map.point }" pattern="#.#" /></c:if>
									</c:forEach>
								</td>
								</c:forEach>
								<td><c:if test="${member.sum != 0}"><fmt:formatNumber value="${member.sum }" pattern="#.#" /></c:if><c:if test="${member.sum == 0}">&nbsp;</c:if></td>
								
							</tr>
							</c:forEach>
							
						</tbody>
					</table>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
	var exportExcel = function() {
		$("#queryForm").attr("action", "export.mvc");
		$("#queryForm").submit();
		$("#queryForm").attr("action", "${ctx }/reps/khxt/assessdetail/stat.mvc?sheetId=${sheetId}");
	}
	
	var query = function() {
		$("#queryForm").attr("action", "${ctx }/reps/khxt/assessdetail/stat.mvc");
		$("#queryForm").submit();
	}
</script>
</body>
</html>