<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>月考核列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel title="" id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
	
		<reps:formcontent parentLayout="true" style="width:80%;" columns="2">
			<reps:formfield label="考核名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${sheet.name}</reps:input>
			</reps:formfield>
			<reps:formfield label="考核对象" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${levelMap}" name="bkhrId" >${sheet.bkhr.name}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核年月" labelStyle="width:20%;" textStyle="width:27%;" >
				<reps:select dataSource="${season}" name="season" >${sheet.season}</reps:select>
			</reps:formfield>
				
			<reps:formfield label="考核状态" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${levelMap}" name="bkhr.id" ></reps:select>
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
		<reps:grid id="itemList" items="${list}" form="queryForm" var="sheet" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="名称" width="15" align="center">${sheet.name}</reps:gridfield>
				<reps:gridfield title="考核年月" width="20" align="center">${sheet.season}</reps:gridfield>
				<reps:gridfield title="考核对象" width="25" align="center">${sheet.bkhr.name}</reps:gridfield>
				<reps:gridfield title="考核状态" width="10" align="center">${sheet.name}</reps:gridfield>
				<reps:gridfield title="考核人权重" width="25" align="center">${sheet.levelWeight.name}</reps:gridfield>
				
				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="show.mvc?id=${sheet.id }" value="详细"></reps:button>
					<reps:button cssClass="add-table" value="上报" action="../appraise/personlist.mvc?levelId=${sheet.bkhr.id}"></reps:button>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${sheet.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
						callBack="my" url="delete.mvc?id=${sheet.id}">
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
