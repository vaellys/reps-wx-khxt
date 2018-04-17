<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>被考核人查询列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel  id="top" dock="top" method="post" action="bkhrquery.mvc" formId="queryForm">
	
		<reps:formcontent parentLayout="true" style="width:80%;" columns="2">
			
			<reps:formfield label="填报时间" >
					<reps:datepicker name="appraiseSheet.beginDate" format="yyyy年MM月dd日" />
				</reps:formfield>
			<reps:formfield label="结束时间" >
					<reps:datepicker name="appraiseSheet.endEate" format="yyyy年MM月dd日" />
			</reps:formfield>
			
			<reps:formfield label="被考核人姓名" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="bkhrPerson.name" maxLength="20">${sheet.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="itemList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		
		
	</reps:panel>
	
	<reps:panel id="mybody" dock="center">
		<reps:grid id="itemList" items="${list}" form="queryForm" var="members" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="月考核报表名称" width="15" align="center">${members.appraiseSheet.name}</reps:gridfield>
				
				<reps:gridfield title="姓名" width="10" align="center">${members.bkhrPerson.name}</reps:gridfield>
				
				<reps:gridfield title="性别" width="10" align="center">
					<sys:dictionary src="sex">${members.bkhrPerson.sex}</sys:dictionary>
				</reps:gridfield>
				
				<reps:gridfield title="工作单位" width="15" align="center">${members.personOrganize}</reps:gridfield>
					
				<reps:gridfield title="得分" width="25" align="center">${members.totalPoints}</reps:gridfield>

				<reps:gridfield title="操作" width="25">
					<reps:button cssClass="detail-table" action="scoringdetails.mvc?bkhrPersonId=${members.bkhrPerson.id }&&callbackUrl=bkhrquery.mvc&&sheetId=${members.appraiseSheet.id }" value="得分详情" ></reps:button>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "bkhrquery.mvc"; });
	};
	
</script>
</body>
</html>
