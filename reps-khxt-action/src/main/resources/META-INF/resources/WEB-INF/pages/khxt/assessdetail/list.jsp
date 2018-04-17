<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>月考核表情况列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel  id="top" dock="top" method="post" action="assessslist.mvc" formId="queryForm">
	
		<reps:formcontent parentLayout="true" style="width:80%;" columns="2">
			<reps:formfield label="考核名称" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name" maxLength="20">${sheet.name }</reps:input>
			</reps:formfield>
			<reps:formfield label="考核年月" labelStyle="width:20%;" textStyle="width:27%;" >
				<reps:select dataSource="${season}" name="season"   headerValue="" headerText="">${sheet.season }</reps:select>
			</reps:formfield>
			<reps:formfield label="考核人级别" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${khrMap}" name="khrId"  headerValue="" headerText="">${sheet.khrId }</reps:select>
			</reps:formfield>
				
			 <reps:formfield label="考核对象级别" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:select dataSource="${bkhrMap}" name="bkhrId"  headerValue="" headerText="">${sheet.bkhr.id }</reps:select>
			</reps:formfield> 
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
	</reps:panel>
	
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemList" items="${list}" form="queryForm" var="sheet" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="考核年月" width="10" align="center">${sheet.season}</reps:gridfield>
				
				<reps:gridfield title="名称" width="20" align="center">${sheet.name}</reps:gridfield>
				
				<reps:gridfield title="考核人" width="10" align="center">${sheet.khrName}</reps:gridfield>
				
				<reps:gridfield title="考核对象" width="10" align="center">${sheet.bkhr.name}</reps:gridfield>
				
				<reps:gridfield title="填报截止时间" width="15" align="center">${sheet.endEate}</reps:gridfield>
				
				<reps:gridfield title="完成情况" width="25" align="center">
					<c:if test="${sheet.progress == '0'}">未完成</c:if>
					<c:if test="${sheet.progress == '1'}">已完成</c:if>
				</reps:gridfield>
				
				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="assessshow.mvc?sheetId=${sheet.id }&&callbackUrl=assessslist.mvc" value="考核表详情"></reps:button>					
					
					<reps:button cssClass="detail-table" action="${ctx }/reps/khxt/assessdetail/stat.mvc?sheetId=${sheet.id }" value="考核结果汇总"></reps:button>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "assessslist.mvc"; });
	};
	
</script>
</body>
</html>
