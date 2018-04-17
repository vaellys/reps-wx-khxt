<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>评分月考核列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel  id="top" dock="top" method="post" action="listpoint.mvc" formId="queryForm">
	
		<reps:formcontent parentLayout="true" style="width:80%;" columns="2">
			<reps:formfield label="考核名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${sheet.name}</reps:input>
			</reps:formfield>
			<reps:formfield label="考核对象" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${levelMap}" name="bkhrId"  headerValue="" headerText="">${sheet.bkhr.name}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核年月" labelStyle="width:20%;" textStyle="width:27%;" >
				<reps:select dataSource="${season}" name="season"  headerValue="" headerText="">${sheet.season}</reps:select>
			</reps:formfield>
				
			<c:if test="${'00' != identity }">
			<reps:formfield label="打分情况" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select jsonData="{'0':'未完成打分','1':'已完成打分'}" name="status" headerValue="" headerText="" >${sheet.status }</reps:select>
			</reps:formfield>
			</c:if>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<%-- <reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add" value="新增"></reps:button> --%>
		</reps:footbar>
		
	</reps:panel>
	
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemList" items="${list}" form="queryForm" var="sheet" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="名称" width="15" align="center">${sheet.season}${sheet.name}</reps:gridfield>
				<reps:gridfield title="考核年月" width="20" align="center">${sheet.season}</reps:gridfield>
				<reps:gridfield title="考核对象" width="25" align="center">${sheet.bkhr.name}</reps:gridfield>
				<reps:gridfield title="考核人权重" width="25" align="center">${sheet.weightDisplay}</reps:gridfield>
				<c:if test="${'00' != identity }">
				<reps:gridfield title="打分情况" width="10" align="center"><c:if test="${sheet.checkCompletedMarking == true }">已完成打分</c:if><c:if test="${sheet.checkCompletedMarking == false }">未完成打分</c:if></reps:gridfield>
				</c:if>
				<reps:gridfield title="操作" width="15" align="center">
					<reps:button cssClass="detail-table" action="show.mvc?sheetId=${sheet.id }&callbackUrl=listpoint.mvc" value="详情"></reps:button>
					<c:if test="${sheet.checkKhr == true }">
						<reps:button cssClass="audit-table" value="打分" action="${ctx }/reps/khxt/member/appraisepoint.mvc?sheetId=${sheet.id}"></reps:button>
					</c:if>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "listpoint.mvc"; });
	};
	
</script>
</body>
</html>
