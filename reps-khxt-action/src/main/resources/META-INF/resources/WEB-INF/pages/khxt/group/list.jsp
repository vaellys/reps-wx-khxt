<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>考核分组列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel title="" id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${khxtgroup.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add" value="新增"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemList" items="${list}" form="queryForm" var="khxtgroup" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="分组名称" width="15" align="center">${khxtgroup.name}</reps:gridfield>
				<reps:gridfield title="考核人" width="30" align="center">${khxtgroup.khxtLevel.name}</reps:gridfield>
				<reps:gridfield title="被考核人" width="15" align="center">${khxtgroup.bkhxtLevel.name}</reps:gridfield>
				
				<reps:gridfield title="本组是否参与" width="15" align="center"><c:if test="${khxtgroup.isEnable == '1'}">参与</c:if><c:if test="${khxtgroup.isEnable == '2'}">不参与</c:if></reps:gridfield>
				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="show.mvc?id=${khxtgroup.id }" value="详细"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${item.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
						callBack="my" url="delete.mvc?id=${khxtgroup.id}">
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
