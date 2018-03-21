<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>新增指标</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="add.mvc" formId="xform" validForm="true"  >
		<reps:formcontent style="margin-left:250px;">
			<reps:formfield label="	分组名称" fullRow="true">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:300px"></reps:input>
			</reps:formfield>
		
			<reps:formfield label="考核人级别" fullRow="true">
				<reps:select dataSource="${khrlevelMap}" id="khrlevelid" name="khxtLevel.id" required="true" style="width:304px"></reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核人姓名" fullRow="true">
				<reps:input name="khr" minLength="2" maxLength="20" required="true" style="width:300px"></reps:input>
				<input type="button" value="选择">
			</reps:formfield>
			
			<reps:formfield label="被考核人级别" fullRow="true">
				<reps:select dataSource="${bkhrlevelMap}" id="bkhrlevelid" name="bkhxtLevel.id" required="true" style="width:304px"></reps:select>
			</reps:formfield>
			
			<reps:formfield label="考被核人姓名" fullRow="true">
				<reps:input name="bkhr" minLength="2" maxLength="20" required="true" style="width:300px"></reps:input>
			</reps:formfield>
		
			<reps:formfield label="是否参与考核" fullRow="true">
				<reps:select jsonData="{'':'请选择','1':'参与','0':'暂时不参与'}" name="isEnable" required="true" style="width:304px"></reps:select>
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