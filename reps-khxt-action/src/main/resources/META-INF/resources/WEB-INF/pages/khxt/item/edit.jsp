<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>修改指标</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="edit.mvc" formId="xform" validForm="true">
		<reps:formcontent style="margin-left:250px;">
			<input type="hidden" value="${item.id }" name="id">
			<reps:formfield label="所属类别" fullRow="true">
				<reps:select dataSource="${categoryMap}" id="categoryId" name="khxtCategory.id" required="true" style="width:304px">${item.khxtCategory.id }</reps:select>
			</reps:formfield>
			<reps:formfield label="指标名称" fullRow="true">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:300px" readonly="true">${item.name }</reps:input>
			</reps:formfield>
			<reps:formfield label="分数" fullRow="true">
				<reps:input name="point" dataType="moneyvalidate" maxLength="6" required="true" style="width:300px">${item.point }</reps:input>
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