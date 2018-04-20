<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>考核人查询列表</title>
	<reps:theme/>
</head>
<body>
<reps:container layout="true">
	<reps:panel id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="填报时间"  labelStyle="width:20%;" textStyle="width:27%;">
					<reps:datepicker name="beginDate" format="yyyy年MM月dd日" />
			</reps:formfield>
			<reps:formfield label="结束时间" labelStyle="width:23%;" textStyle="width:30%;">
					<reps:datepicker name="endDate" format="yyyy年MM月dd日" />
			</reps:formfield>
			<reps:formfield label="考核人姓名" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="khrPerson.name" maxLength="20">${sheet.name }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="khrList" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
	</reps:panel>
	
	<reps:panel id="mybody" dock="center">
		<reps:grid id="khrList" items="${list}" form="queryForm" var="khr" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="月考核报表名称" width="25" align="center">${khr.appraiseSheet.name}</reps:gridfield>
				<reps:gridfield title="姓名" width="15" align="center">${khr.khrPerson.name}</reps:gridfield>
				<reps:gridfield title="工作单位" width="25" align="center">${khr.organizeName}</reps:gridfield>
				<reps:gridfield title="打分情况" width="15" align="center"><c:if test="${khr.appraiseSheet.checkCompletedMarking == true }">已完成打分</c:if><c:if test="${khr.appraiseSheet.checkCompletedMarking == false }">未完成打分</c:if></reps:gridfield>
				<reps:gridfield title="操作" width="15" align="center">
					<reps:button cssClass="detail-table" action="" value="评分详情" onClick="goToUrl('${ctx }/reps/khxt/member/show.mvc?sheetId=${khr.appraiseSheet.id }&khrPersonId=${khr.khrPerson.id }', 'khrPersonName', '${khr.khrPerson.name}')"></reps:button>
				</reps:gridfield>
			</reps:gridrow>
		</reps:grid>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ window.location.href= "list.mvc"; });
	};
	
	function goToUrl(url, name, value){
		url = addURLParam(url, name, value)
		window.location.href = url;
	}
	
	function addURLParam(url, name, value){
		url += (url.indexOf('?') == -1 ? "?" : "&");
		url += encodeURIComponent(encodeURIComponent(name)) + "=" + encodeURIComponent(encodeURIComponent(value));
		return url;
	}
	
</script>
</body>
</html>
