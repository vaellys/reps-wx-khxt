<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel title="" id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${item.name }</reps:input>
			</reps:formfield>
			<reps:formfield label="所属类别" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${categoryMap}" name="categoryId" >${item.categoryId }</reps:select>
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
		<reps:grid id="itemList" items="${list}" form="queryForm" var="item" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="指标名称" width="15" align="center">${item.name}</reps:gridfield>
				<reps:gridfield title="所属类别" width="15" align="center">${item.khxtCategory.name}</reps:gridfield>
				<reps:gridfield title="分数" width="15" align="center"><fmt:formatNumber value="${item.point}" pattern="#.#"/></reps:gridfield>
				<reps:gridfield title="操作" width="15">
					<reps:button cssClass="detail-table" action="show.mvc?id=${item.id }" value="查看"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${item.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
						callBack="my" url="delete.mvc?id=${item.id}">
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
