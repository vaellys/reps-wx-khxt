<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>级别人员列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel title="" id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${level.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="levelPersonList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<sys:user id="user" hideId="hideId" hideName="hideName" hideNameValue="" nameValue="${personName }" url="${ctx }/reps/sys/userrole/multiple.mvc" multiple="true" name="选择用户" cssClass="add-a" callBack="userCallBack">新增</sys:user>
			<reps:button cssClass="return-a" onClick="back()" value="返回" />
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="levelPersonList" items="${list}" form="queryForm" var="levelPerson" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="姓名" width="15" align="center">${levelPerson.personName}</reps:gridfield>
				<reps:gridfield title="性别" width="15" align="center">${levelPerson.personName}</reps:gridfield>
				<reps:gridfield title="单位部门" width="25" align="center">${levelPerson.personName}</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">

var userCallBack = function() {
	var levelId = '${levelId}';
	var ids = $("#hideId").val();
	$.ajax({
		url : "save.mvc",
		type : "POST",
		dataType : "json",
		data : {
			"userIds" : ids,
			"levelId" : levelId
		},
		async : false,
		success : function(data) {
			messager.message(data, function() {
				window.location.reload();
			});
		}
	});
};


	var my = function(data){
		messager.message(data, function(){ back(); });
	};
	
	var back = function() {
		window.location.href= "list.mvc";
	}
	
</script>
</body>
</html>
