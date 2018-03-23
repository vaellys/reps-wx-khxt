<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>人员选择</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel title="" id="top" dock="top" method="post" action="listlevel.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="姓名" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${level.personName }</reps:input>
			</reps:formfield>
			
			<reps:formfield label="工作单位" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:input name="name" maxLength="20">${level.organize.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="levelList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="levelList" items="${list}" form="queryForm" var="level" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="姓名" width="15" align="center">${level.personName}</reps:gridfield>
				<reps:gridfield title="性别" width="15" align="center"><c:if test="${level.personSex == '1'}">男性</c:if><c:if test="${level.personSex == '2'}">女性</c:if></reps:gridfield>
				<reps:gridfield title="工作单位" width="25" align="center">${level.organize.name}</reps:gridfield>
				<reps:gridcheckboxfield title="选择" checkboxName="ids" align="center" width="10">${level.id}</reps:gridcheckboxfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "listlevel.mvc"; });
	};
	
</script>
</body>
</html>