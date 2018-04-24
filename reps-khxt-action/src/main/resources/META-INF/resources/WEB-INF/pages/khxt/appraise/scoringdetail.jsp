<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>得分详情</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="myform" dock="none" formId="xform" validForm="false"
			action="" title="${sheet.season }${sheet.name }">
			<reps:detail id="itemInfo" borderFlag="true"
				textBackgroundFlag="false" >
				<reps:detailfield label="被考核人" fullRow="true"
					labelStyle="width:20%;">${member.bkhrPersoName}</reps:detailfield>
				<c:forEach var="item" items="${items}" varStatus="status">
					<c:forEach var="point" items="${member.performancePoints}" varStatus="s">
						<c:if test="${item.id == point.itemId }">
						<c:set var="sum" value="${sum + item.point}" />
						<reps:detailfield label="${item.name }（${item.point }分）" fullRow="true"
							labelStyle="width:20%;"><fmt:formatNumber value="${point.point}" pattern="#.#"/>分</reps:detailfield>
						</c:if>
					</c:forEach>
				</c:forEach>
				<reps:detailfield label="总分（${sum}分）" fullRow="true"
							labelStyle="width:20%;"><fmt:formatNumber value="${member.totalPoints }" pattern="#.#"/>分</reps:detailfield>
			</reps:detail>
			<reps:formbar>
				<reps:button cssClass="btn_back" type="button" onClick="back()"
					messageCode="manage.action.return" />
			</reps:formbar>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
		function back() {
			//返回列表页
			window.location.href = "${ctx}/reps/khxt/appraise/bkhrlist.mvc";
		}
	</script>
</body>
</html>
