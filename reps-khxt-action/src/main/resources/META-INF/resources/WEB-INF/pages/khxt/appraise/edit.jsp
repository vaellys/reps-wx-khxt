<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
	<title>修改月考核</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="myform" dock="center" action="edit.mvc" formId="xform"  >
		<reps:formcontent style="margin-left:100px;" columns="2">
			<reps:formfield label="名称">
				<input name="sheetId" type="hidden" value="${sheet.id}">
				<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:220px">${sheet.name }</reps:input>
			</reps:formfield>
		
			<reps:formfield label="考核年月" >
				<reps:select dataSource="${season}" id="phase" name="season" required="true" style="width:220px">${sheet.season}</reps:select>
			</reps:formfield>
			
			<reps:formfield label="填报开始时间" >
				<reps:datepicker name="beginDate" format="yyyy-MM-dd" value="${sheet.beginDate }" />
			</reps:formfield>
			
			<reps:formfield label="填报截止时间" >
				<reps:datepicker name="endEate" format="yyyy-MM-dd" value="${sheet.endEate }"/>
			</reps:formfield>
			
			<reps:formfield label="考核人" >
				 <c:forEach  var="khr"   items="${khr}">
				 	<c:if test="${!empty khr}"> 
						<input type="checkbox" name="khrids" value="${khr.id}" id="${khr.id}"/>${khr.name}</input>
	             	</c:if>
	             </c:forEach>
			</reps:formfield>
			
			<reps:formfield label="考核对象" >
				<reps:select dataSource="${bkhrMap}" id="bkhr" name="bkhr.id" required="true" style="width:220px">${sheet.bkhr.id }</reps:select>
			</reps:formfield>
			
			<reps:formfield label="考核权重">
				<reps:select dataSource="${weightMap}" id="weight.id" name="levelWeight.id" required="true" style="width:220px">${sheet.levelWeight.id }</reps:select>
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
				<select multiple="multiple" name="itemIds" id="itemIds" style="width: 220px;height: 150px;" >
					<%-- <c:forEach  var="item"   items="${item}">
						 	<c:if test="${!empty item}">  
								<option value="${item.id}">${item.name}</option>
			             	</c:if>
		             </c:forEach> --%>
				</select>
			</reps:formfield>
			<reps:formfield label="考核材料" fullRow="true">
					<input type="text" name="fileName" id="fileName" value="${file.fileName }"/>
					<input type="hidden" name="fileUrl" id="fileUrl" value="${file.fileUrl }"/>
					<input type="hidden" name="fileType" id="fileType" value="${file.fileType }"/>
					<input type="hidden" name="fileSize" id="fileSize" value="${file.fileSize }"/>
					<input type="hidden" name="fileid" id="fileid" value="${file.id }"/>
					<reps:upload id="file1" callBack="getPathNameOne"  value="上传文件"  flagAbsolute="true"  path="${uploadPath}" cssClass="uploading-a" fileType="txt,doc" coverage="true" size="2"></reps:upload>
			</reps:formfield>
		</reps:formcontent>
		</br></br>
		<reps:formbar>
			<reps:ajax  messageCode="add.button.save" formId="xform" callBack="my" type="button" cssClass="btn_save"></reps:ajax>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back()"></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
	<script type="text/javascript">
			
	 	$(function () {
	 		<c:forEach  items="${listkhrId}" var="listkhrId">
	 			<c:if test="${!empty listkhrId}">  
					var a ="${listkhrId}";
					$("#"+a).prop("checked",true);
	         	</c:if>
         </c:forEach>
	 	});

		var my = function(data){
			messager.message(data, function(){ back(); });
		};
		
		
		function back() {
			window.location.href= "list.mvc";
		}
		
		var getPathNameOne = function(filename, fileType, fileSize, path, id) {
				$("#fileName").val(filename);
				$("#fileType").val(fileType);
				$("#fileSize").val(fileSize);
				$("#fileUrl").val(path);
				$("#fileid").val("");
		};
		$(function(){
			/*选中双击去右边*/
			$("#itemId:first").dblclick(function(){
				
				$("#itemId:first>option:selected").appendTo($("#itemIds"));
			});
			/*选中双击去左边*/
			$("#itemIds:first").dblclick(function(){
				$("#itemIds:first>option:selected").appendTo($("#itemId"));
			}); 
		});
	</script>

</body>
</html>