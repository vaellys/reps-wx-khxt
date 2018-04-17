<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>权重设置列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel  id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="考核名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${levelWeight.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="levelWeightList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add" value="新增"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="levelWeightList" items="${list}" form="queryForm" var="levelWeight" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="考核名称" width="15" align="center">${levelWeight.year}年${levelWeight.name}</reps:gridfield>
				<reps:gridfield title="适用年度" width="15" align="center">${levelWeight.year}</reps:gridfield>
				<reps:gridfield title="公开打分明细" width="15" align="center"><c:if test="${levelWeight.visible == '1'}">可见</c:if><c:if test="${levelWeight.visible == '0'}">不可见</c:if></reps:gridfield>
				<reps:gridfield title="考核权重" width="30" align="center">${levelWeight.weightDisplay}</reps:gridfield>
				<reps:gridfield title="操作" width="25">
					<reps:ajax cssClass="copy-table" value="复制" confirm="您确定要复制吗？"
						callBack="my" url="copy.mvc?id=${levelWeight.id}">
					</reps:ajax>
					<reps:button cssClass="detail-table" action="show.mvc?id=${levelWeight.id }" value="查看"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${levelWeight.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
						callBack="my" url="delete.mvc?id=${levelWeight.id}">
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
