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
		
		<reps:formcontent style="margin-left:100px;">
			<input type="hidden" value="${group.id }" name="id">
			<reps:formfield label="	分组名称" fullRow="true">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:300px">${group.name }</reps:input>
			</reps:formfield>
		
			<reps:formfield label="考核人级别" fullRow="true">
				<reps:select dataSource="${LevelMap}" id="khrlevelid" name="khxtLevel.id" required="true" style="width:304px">${group.khrId}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核人姓名" fullRow="true">
				<input name="khrIds" type="hidden" value="${group.khrIds }"/>
				<reps:input name="khr" minLength="2" maxLength="20" required="true" readonly="true" style="width:300px;height:100px">${group.khr}</reps:input>

				<sys:user id="user" hideId="hideId" hideName="hideName"
					hideNameValue="" nameValue=""
					url="khrlistlevel.mvc" multiple="true" name="人员选择"
					cssClass="add-a" callBack="userCallBack">新增</sys:user>
			</reps:formfield>
			
			<reps:formfield label="被考核人级别" fullRow="true">
				<reps:select dataSource="${LevelMap}" id="bkhrlevelid" name="bkhxtLevel.id" required="true" style="width:304px">${group.bkhrId}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考被核人姓名" fullRow="true">
				<input name="bkhrIds" type="hidden" value="${group.bkhrIds }"/>
			<reps:input name="bkhr" minLength="2" maxLength="20" required="true" readonly="true" style="width:300px;height:100px">${group.bkhr}</reps:input>
				
				<sys:user id="buser" hideId="bhideId" hideName="bhideName"
					hideNameValue="" nameValue=""
					url="bkhrlistlevel.mvc" multiple="true" name="人员选择"
					cssClass="add-a" callBack="buserCallBack">新增</sys:user>
			</reps:formfield>
		
			<reps:formfield label="是否参与考核" fullRow="true">
				<reps:select jsonData="{'':'请选择','1':'参与','0':'暂时不参与'}" name="isEnable" required="true" style="width:304px"><c:if test="${khxtgroup.isEnable == '1'}">参与</c:if><c:if test="${khxtgroup.isEnable == '0'}">不参与</c:if></reps:select>
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

	var userCallBack = function() {
		
		 $("input[name=khrIds]").val("");
		 $("input[name=khr]").val("");
		 var person = $("#hideId").val();
		 var p = person.split("=");
		
		 $("input[name=khrIds]").val(p[0]);
		 $("input[name=khr]").val(p[1]);
	};
	var buserCallBack = function() {
		
		 $("input[name=bkhrIds]").val("");
		 $("input[name=bkhr]").val("");
		 var person = $("#bhideId").val();
		 var p = person.split("=");
		
		 $("input[name=bkhrIds]").val(p[0]);
		 $("input[name=bkhr]").val(p[1]);
	};

	var skip = function(data) {
		messager.message(data, function(){ back(); });
	};
	
	function back() {
		window.location.href= "list.mvc";
	}
	
</script>
</body>
</html>