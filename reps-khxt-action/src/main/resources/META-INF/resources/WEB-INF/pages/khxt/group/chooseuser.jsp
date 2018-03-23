<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>人员选择</title>
<reps:theme />
<style type="text/css">
.selecteUser {
	width: 500px;
	height: 30px;
	overflow: auto;
	white-space: pre-wrap;
	word-wrap: break-word;
	float: left;
	margin-left: 10px;
	text-align: left;
}

.submit_btn {
	float: left;
	margin-left: 20px;
}

.hk {
	margin-right: 10px;
	align-content: right;
}
</style>
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="top" dock="top" method="post" action="listlevel.mvc" formId="queryForm">
			<reps:formcontent parentLayout="true" style="width:80%;">
				<reps:formfield label="姓名">
					<reps:input name="account.person.name">${user.account.person.name}</reps:input>
				</reps:formfield>
				<reps:formfield label="工作单位">
					<reps:input name="organize.name">${user.organize.name}</reps:input>
				</reps:formfield>
			</reps:formcontent>
			<reps:querybuttons style="margin-right:20px;">
				<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="mygrid" cssClass="search-form-a"></reps:ajaxgrid>
			</reps:querybuttons>
		</reps:panel>
		<reps:panel id="mybody" dock="center" border="true">
			<reps:grid id="mygrid" items="${list}" form="queryForm" var="u" >
				<reps:gridrow>
					<reps:gridfield title="姓名" width="15">${u.personName}</reps:gridfield>
					
					<reps:gridfield title="性别" width="10">
						<sys:dictionary src="sex">${u.personSex}</sys:dictionary>
					</reps:gridfield>
					<reps:gridfield title="工作单位" width="20">${u.organize.name}</reps:gridfield>
					
					<reps:gridfield title="选择" width="10" align="center">
					<c:if test="${empty u.personId}">
						<input type="checkbox" disabled="disabled"/>
					</c:if>
					<c:if test="${not empty u.personId}">
						<input type="checkbox" name="user" value="${u.personId}" nameAttr="${u.personName}" data-options="{'id':'${u.personId}','name':'${u.personName}'}" />
					</c:if>
					</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
			<reps:formbar>
				<div class="dialogButtons" style="height:100px;margin-top: 10px">
					<div id="selecteUser" class="selecteUser"></div>
					<div class="submit_btn">
						<ul>
							<div class="small_btn_save">
								<input type="button" onclick="okCallback()" value="确定">
							</div>
							<div class="small_btn_cancel hk">
								<input type="button" onclick="cancelCallback()" value="取消">
							</div>
						</ul>
					</div>
				</div>
			</reps:formbar>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
	
	$(function(){
		 var okCallback=function(){
			 var hideNameValue = $('input[name=hideNameValue]').val();
			if(hideNameValue.length == 0){
				alert('请选择人员！');
				return false;
			}  
			var hideName = $('input[name=hideName]').val();
			window.parent.$('input[name='+hideName+']').val(hideNameValue);
			var call = "${callBack}";
			if(call){
				var callBack = null;
				if(call.indexOf("(") > 0){
					callBack = "window.parent.${callBack}";
				}else{
					callBack = "window.parent.${callBack}()";
				}
				eval("("+callBack+")");
			}
			var hideIdMulCallback = function(){
			};
			cancelCallback(); 
		
	};
	
	//取消关闭窗口
	var cancelCallback=function(){
		//关闭弹出框
		$.pdialog.pdialogClose();
	};

</script>
</body>
</html>
