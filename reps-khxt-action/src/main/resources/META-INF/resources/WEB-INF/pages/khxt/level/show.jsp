<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>级别详情</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="myform" dock="none" formId="xform" validForm="false"
			action="" title="级别详细信息">
			<reps:detail id="itemInfo" borderFlag="true"
				textBackgroundFlag="false" style="width:800px">
				<reps:detailfield label="名称" fullRow="true" labelStyle="width:20%;">${level.name}</reps:detailfield>
				<reps:detailfield label="级别" fullRow="true" labelStyle="width:20%;">
					<c:if test="${level.level == '1'}">一级</c:if>
					<c:if test="${level.level == '2' }">二级</c:if>
					<c:if test="${level.level == '3' }">三级</c:if>
					<c:if test="${level.level == '4' }">四级</c:if>
					<c:if test="${level.level == '5' }">五级</c:if>
					<c:if test="${level.level == '6' }">六级</c:if>
				</reps:detailfield>
				<reps:detailfield label="级别权限" fullRow="true"
					labelStyle="width:20%;">
					<c:if test="${level.power == '1'}">是考核人</c:if>
					<c:if test="${level.power == '2'}">是被考核人</c:if>
					<c:if test="${level.power == '3'}">是考核人，也是被考核人</c:if>
				</reps:detailfield>
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
			window.location.href = "list.mvc";
		}
	</script>
</body>
</html>
