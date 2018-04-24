<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>工作详情</title>
<reps:theme />
</head>
<body>
	<reps:container>
		<reps:panel  id="top" dock="top" >
		<reps:footbar>
			<span style="margin-right:120px;">姓名：${levelPerson.personName }</span><span>单位部门：${levelPerson.organize.name }</span>
		</reps:footbar>
	</reps:panel>
		<reps:panel id="mybody" dock="center" style="height:200px;">
			<reps:grid id="workList" items="${workList}" form="queryForm" var="work"
				pagination="${pager}" flagSeq="true">
				<reps:gridrow>
					<reps:gridfield title="本月工作计划" width="15" align="center">${work.planning}</reps:gridfield>
					<reps:gridfield title="本月工作纪实" width="20" align="center">${work.execution}</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>
	</reps:container>
</body>
</html>