<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>考核级别列表</title>
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
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="levelList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add" value="新增"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="levelList" items="${list}" form="queryForm" var="level" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="级别名称" width="15" align="center">${level.name}</reps:gridfield>
				<reps:gridfield title="级别" width="15" align="center"><c:if test="${level.level == '1'}">一级</c:if><c:if test="${level.level == '2'}">二级</c:if><c:if test="${level.level == '3'}">三级</c:if><c:if test="${level.level == '4'}">四级</c:if><c:if test="${level.level == '5'}">五级</c:if><c:if test="${level.level == '6'}">六级</c:if></reps:gridfield>
				<reps:gridfield title="人员" width="25" align="center">${level.level}</reps:gridfield>
				<reps:gridfield title="权限" width="15" align="center"><c:if test="${level.level == '1'}">是考核人</c:if><c:if test="${level.level == '2'}">是被考核人</c:if><c:if test="${level.level == '3'}">是考核人，也是被考核人</c:if></reps:gridfield>
				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="show.mvc?id=${level.id }" value="详细"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${level.id}"></reps:button>
					<reps:button cssClass="add-table" value="添加人员" action="../levelperson/list.mvc?levelId=${level.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
						callBack="my" url="delete.mvc?id=${level.id}">
					</reps:ajax>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "list.mvc"; });
	};
	
</script>
</body>
</html>
