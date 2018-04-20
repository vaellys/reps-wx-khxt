<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>创建月考核列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel  id="top" dock="top" method="post" action="khrlist.mvc" formId="queryForm">
	
		<reps:formcontent parentLayout="true" style="width:80%;" columns="2">
			<reps:formfield label="考核名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${sheet.name }</reps:input>
			</reps:formfield>
			<reps:formfield label="考核对象" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${levelMap}" name="bkhrId"  headerValue="" headerText="">${sheet.bkhr.id }</reps:select>
			</reps:formfield>
			<reps:formfield label="考核年月" labelStyle="width:20%;" textStyle="width:27%;" >
				<reps:select dataSource="${season}" name="season"   headerValue="" headerText="">${sheet.season }</reps:select>
			</reps:formfield>
				
			 <reps:formfield label="考核进度" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${status}" name="progress"  headerValue="" headerText="">${sheet.progress }</reps:select>
			</reps:formfield> 
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add" value="新增"></reps:button>
			<reps:button cssClass="add-a" action="count.mvc" messageCode="manage.action.add" value="统计进度"></reps:button>
		</reps:footbar>
		
	</reps:panel>
	
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemList" items="${list}" form="queryForm" var="sheet" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="名称" width="20" align="center">${sheet.name}</reps:gridfield>
				
				<reps:gridfield title="考核年月" width="10" align="center">${sheet.season}</reps:gridfield>
				
				<reps:gridfield title="考核对象" width="10" align="center">${sheet.bkhr.name}</reps:gridfield>
				
				<reps:gridfield title="考核人权重" width="35" align="center">${sheet.weight}</reps:gridfield>
				
				<reps:gridfield title="填报截止时间" width="15" align="center">${sheet.endDate}</reps:gridfield>
				
				<reps:gridfield title="考核对象人数" width="13" align="center">${sheet.bkhrCount}</reps:gridfield>
				
				<reps:gridfield title="进度情况" width="25" align="center">${sheet.result}</reps:gridfield>
				
				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="show.mvc?sheetId=${sheet.id }&&callbackUrl=khrlist.mvc" value="详情"></reps:button>
					<c:if test="${sheet.progress == '1'}">
						<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
							callBack="my" url="delete.mvc?id=${sheet.id}">
						</reps:ajax>
					</c:if>
					<c:if test="${sheet.progress == '0'}">
						<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?sheetId=${sheet.id}"></reps:button>
						<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗？"
							callBack="my" url="delete.mvc?id=${sheet.id}">
						</reps:ajax>
					</c:if>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "khrlist.mvc"; });
	};
	
</script>
</body>
</html>
