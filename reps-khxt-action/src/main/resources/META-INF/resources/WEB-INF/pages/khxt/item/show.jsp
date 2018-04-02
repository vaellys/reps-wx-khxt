<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>指标详情</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="myform" dock="none" formId="xform" validForm="false"
			action="" title="指标详细信息">
			<reps:detail id="itemInfo" borderFlag="true"
				textBackgroundFlag="false" style="width:800px">
				<reps:detailfield label="指标名称" fullRow="true"
					labelStyle="width:20%;">${item.name}</reps:detailfield>
				<reps:detailfield label="所属类别" fullRow="true"
					labelStyle="width:20%;">${item.khxtCategory.name }</reps:detailfield>
				<reps:detailfield label="所需积分" fullRow="true"
					labelStyle="width:20%;"><fmt:formatNumber value="${item.point}" pattern="#.#"/></reps:detailfield>
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
