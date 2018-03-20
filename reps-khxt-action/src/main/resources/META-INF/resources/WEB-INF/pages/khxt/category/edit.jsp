<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>修改类别</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="edit.mvc" formId="xform" validForm="true">
		<reps:formcontent style="margin-left:250px;">
			<input type="hidden" value="${category.id}" name="id">
			<reps:formfield label="名称" fullRow="true">
				<reps:input name="name" required="true" minLength="2" maxLength="20" style="width:300px" readonly="true">${category.name }</reps:input>
			</reps:formfield>
			<reps:formfield label="备注" fullRow="true">
				<reps:input name="remark" maxLength="100" multiLine="true" style="width:300px;height:100px;">${category.remark }</reps:input>
			</reps:formfield>
		</reps:formcontent>
		</br></br>
		<reps:formbar>
			<reps:ajax messageCode="edit.button.save" formId="xform" callBack="skip" type="link"
				confirm="确定要提交修改？" cssClass="btn_save_a">
			</reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back()"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
<script type="text/javascript">
	var skip = function(data) {
		messager.message(data, function(){ back(); });
	};
	
	function back() {
		window.location.href= "list.mvc";
	}
	
</script>
</body>
</html>