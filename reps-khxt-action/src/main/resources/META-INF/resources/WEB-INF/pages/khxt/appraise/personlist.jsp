<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>级别人员列表</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="mybody" dock="center">
			<reps:grid id="levelPersonList" items="${list}" form="queryForm" var="levelPerson"  flagSeq="true">
				<reps:gridrow>
					<reps:gridfield title="姓名" width="15" align="center">${levelPerson.personName}</reps:gridfield>
					<reps:gridfield title="性别" width="15" align="center">
						<sys:dictionary src="sex">${levelPerson.personSex}</sys:dictionary>
					</reps:gridfield>
					<reps:gridfield title="单位部门" width="25" align="center">${levelPerson.organize.name}</reps:gridfield>
					<reps:gridfield title="操作" width="25">
						<reps:button cssClass="add-table" value="填写工作计划" action="toWorkPlan.mvc?personId=${levelPerson.personId}"></reps:button>
					</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
		var back = function() {
			window.location.href = "list.mvc?levelId=${levelId }";
		}
	</script>
</body>
</html>
