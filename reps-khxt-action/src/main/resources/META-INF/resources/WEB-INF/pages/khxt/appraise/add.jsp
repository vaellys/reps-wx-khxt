<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>添加月考核</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="add.mvc" formId="xform"  >
		<reps:formcontent style="margin-left:100px;" columns="2">
			<reps:formfield label="名称">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:220px"></reps:input>
			</reps:formfield>
		
			<reps:formfield label="考核年月" >
				<reps:select dataSource="${season}" id="phase" name="season" required="true" style="width:220px">${current}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核人" >
			 <c:forEach  var="khr"   items="${khr}">
			 	<c:if test="${!empty khr}">  
					<input type="checkbox" name="khrids" value="${khr.id}" id="khrids"/>${khr.name}</input>
             	</c:if>
             </c:forEach>
			</reps:formfield>
			
			<reps:formfield label="考核对象" >
				<reps:select dataSource="${bkhrMap}" id="bkhr" name="bkhr.id" required="true" style="width:220px"></reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核权重">
				<reps:select dataSource="${weightMap}" id="weight.id" name="levelWeight.id" required="true" style="width:220px"></reps:select>
			</reps:formfield>
			<reps:formfield label="备注">
				<reps:input name="phase" minLength="2" maxLength="20" style="width:220px"></reps:input>
			
			</reps:formfield>
			<reps:formfield label="考核指标">
				<select multiple="multiple" name="itemId" id="itemId" style="width: 220px;height: 150px;" >
					<c:forEach  var="itemMap"   items="${itemMap}">
					 	<c:if test="${!empty khr}">  
							<option value="${itemMap.id}">${itemMap.name}</option>
		             	</c:if>
		             </c:forEach>
				</select>
			</reps:formfield>
			<reps:formfield label="已选指标">
				<select multiple="multiple" name="itemIds" id="itemIds" style="width: 220px;height: 150px;" ></select>
			</reps:formfield>
			<reps:formfield label="考核材料" fullRow="true">
					<reps:upload id="titlePicPath" flagAbsolute="true" coverage="true" cssClass="uploading-a" fileType="txt,doc" value="开始上传" size="10" callBack="fileUpload" path="${uploadpath}"></reps:upload>
					<reps:input name="titlePicPath" maxLength="100" required="false" readonly="true" ></reps:input>
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
		/*选中双击去右边*/
		$("#itemId:first").dblclick(function(){
			$("#itemId:first>option:selected").appendTo($("#itemIds"));
		})
		/*选中双击去左边*/
		$("#itemIds:first").dblclick(function(){
			$("#itemIds:first>option:selected").appendTo($("#itemId"));
		})
	})
	var my = function(data){
		messager.message(data, function(){ back(); });
	};
	
	
	function back() {
		window.location.href= "list.mvc";
	}
	
	var fileUpload = function(filename, fileType, fileSize, path, id) {
		path=path.replace(/\\/g,"/");
		var configpath="${uploadConfigPath}";
		var index=path.indexOf(configpath);
		var configp=path.substring(index);
		$("input[name=titlePicPath]").val(configp);
	};
</script>
</html>