<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>修改</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="add.mvc" formId="xform" validForm="true"  >
		<reps:formcontent style="margin-left:100px;">
			<reps:formfield label="	分组名称" fullRow="true">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:300px"></reps:input>
			</reps:formfield>
		
			<reps:formfield label="考核人级别" fullRow="true">
				<reps:select dataSource="${khrLevelMap}"  headerValue="" headerText="" id="khrlevelid" name="khxtLevel.id" required="true" style="width:304px" ></reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核人姓名" fullRow="true">
				<input name="khrIds" type="hidden" />
				<reps:input name="khr"  required="true" readonly="true" style="width:300px;height:100px"></reps:input>

				<sys:user id="user" hideId="hideId" hideName="hideName"
					hideNameValue="" nameValue=""
					url="khrlistlevel.mvc" multiple="true" 
					cssClass="add-a" callBack="userCallBack"></sys:user>
			</reps:formfield>
			
			<reps:formfield label="被考核人级别" fullRow="true">
				<reps:select dataSource="${bkhrLevelMap}"  headerValue="" headerText="" id="bkhrlevelid" name="bkhxtLevel.id" required="true" style="width:304px"></reps:select>
			</reps:formfield>
			
			<reps:formfield label="被核人姓名" fullRow="true">
				<input name="bkhrIds" type="hidden" />
				<reps:input name="bkhr"  required="true" readonly="true" style="width:300px;height:100px"></reps:input>
				
				<sys:user id="buser" hideId="bhideId" hideName="bhideName"
					hideNameValue="" nameValue="" multiple="true"
					url="bkhrlistlevel.mvc"
					cssClass="add-a" callBack="buserCallBack"></sys:user>
				
			</reps:formfield>
		
			<reps:formfield label="是否参与考核" fullRow="true">
				<reps:select jsonData="{'1':'参与','0':'暂时不参与'}" name="isEnable" required="true" style="width:304px"></reps:select>
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
		$(function(){
			$("#khrlevelid").click(function(){
				var a =$("#khrlevelid").val();
				$('#hideId').val(a);
				
			})
			$("#bkhrlevelid").click(function(){
				var a =$("#bkhrlevelid").val();
				$('#bhideId').val(a);
				
			})
		});

	var userCallBack = function() {
		
		 $("input[name=khrIds]").val(" ");
		 $("input[name=khr]").val(" ");
		 var person = $("#hideId").val();
		 var p = person.split("=");
		
		 $("input[name=khrIds]").val(p[0]);
		 $("input[name=khr]").val(p[1]);
		 
		 var a =$("#khrlevelid").val();
			$('#hideId').val(a);
	};
	var buserCallBack = function() {
		
		
		 $("input[name=bkhrIds]").val(" ");
		 $("input[name=bkhr]").val(" ");
		 var person = $("#bhideId").val();
		 var p = person.split("=");
		
		 $("input[name=bkhrIds]").val(p[0]);
		 $("input[name=bkhr]").val(p[1]);
		 
		 var a =$("#bkhrlevelid").val();
			$('#bhideId').val(a);
	};
	
	var my = function(data){
		messager.message(data, function(){ back(); });
	};
	
	
	function back() {
		window.location.href= "list.mvc";
	}
	$(function(){
		$("a:eq(0)").attr('title','选择人员');
		$("a:eq(0) span").text('选择人员');
		$("a:eq(1)").attr('title','选择人员');
		$("a:eq(1) span").text('选择人员');
	});
</script>
</html>