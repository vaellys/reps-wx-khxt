<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>新增级别</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="add.mvc" formId="xform" validForm="true"  >
		<reps:formcontent style="margin-left:250px;">
			<reps:formfield label="名称" fullRow="true">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:300px"></reps:input>
			</reps:formfield>
			<reps:formfield label="级别" fullRow="true">
				<reps:select jsonData="{'':'请选择','1':'一级','2':'二级','3':'三级','4':'四级','5':'五级','6':'六级'}" name="level" required="true" style="width:304px"></reps:select>
			</reps:formfield>
			<reps:formfield label="级别权限" fullRow="true">
				<reps:select jsonData="{'':'请选择','1':'是考核人','2':'是被考核人','3':'是考核人，也是被考核人'}" name="power" required="true" style="width:304px"></reps:select>
			</reps:formfield>
		</reps:formcontent>
		</br></br>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="xform" callBack="my" type="button" cssClass="btn_save"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back()"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
	var my = function(data){
		messager.message(data, function(){ back(); });
	};
	
	function back() {
		window.location.href= "list.mvc";
	}
</script>
</html>