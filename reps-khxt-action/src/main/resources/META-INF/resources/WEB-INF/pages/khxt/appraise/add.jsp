<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
	<title>添加月考核</title>
	<reps:theme />
	<style type="text/css">
		div#b2{  
	    position:absolute;
	    }
	    .font_01 { font-size:50px;}
    </style>

</head>
<body>
	<div style="widht:1100px ;">
<reps:container>
		<reps:panel id="myform" dock="center" action="add.mvc" formId="xform"  >
			<reps:formcontent style="margin-left:100px;" columns="2">
				<reps:formfield label="名称">
					<reps:input name="name" minLength="2" maxLength="20" required="true" style="width:220px"></reps:input>
				</reps:formfield>
			
				<reps:formfield label="考核年月" >
					<reps:select dataSource="${season}" id="phase" name="season" required="true" style="width:220px">${current}</reps:select>
				</reps:formfield>
				<reps:formfield label="填报开始时间" >
					<reps:datepicker name="beginDate" format="yyyy-MM-dd" />
				</reps:formfield>
				<reps:formfield label="填报截止时间" >
					<reps:datepicker name="endDate" format="yyyy-MM-dd" />
				</reps:formfield>
				
				<reps:formfield label="考核人" >
				 <c:forEach  var="khr"   items="${khr}">
				 	<c:if test="${!empty khr}">  
						<input type="checkbox" name="khrids" value="${khr.id}" id="khrids" />${khr.name}</input>
	             	</c:if>
	             </c:forEach>
				</reps:formfield>
				
				<reps:formfield label="考核对象" >
					<reps:select dataSource="${bkhrMap}"  headerValue="" headerText="" id="bkhr" name="bkhr.id" required="true" style="width:220px"></reps:select>
				</reps:formfield>
				
				<reps:formfield label="考核权重">
					<reps:select dataSource="${weightMap}"  headerValue="" headerText="" id="weight.id" name="levelWeight.id" required="true" style="width:220px"></reps:select>
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
				<div style="margin-left:600px;margin-top:150px;z-index:9999" id="b2">
					<div>
						<a href="#" class="font_01">></a>
					</div>
					<div>
						<a href="#" class="font_01">&lt;</a>
					</div>
				</div>
				<reps:formfield label="已选指标">
					<select multiple="multiple" name="itemIds" id="itemIds" style="width: 220px;height: 150px;" ></select>
				</reps:formfield>
				<reps:formfield label="考核材料" fullRow="true">
						<input type="text" name="fileName" id="fileName"/>
						<input type="hidden" name="fileUrl" id="fileUrl"/>
						<input type="hidden" name="fileType" id="fileType"/>
						<input type="hidden" name="fileSize" id="fileSize"/>
						<reps:upload id="file1" callBack="getPathNameOne"  value="上传文件"  flagAbsolute="true"  path="${uploadPath}/khxt/assess/teacher/" cssClass="uploading-a" fileType="txt,doc" coverage="true" size="2"></reps:upload>
				</reps:formfield>
			</reps:formcontent>
			</br></br>
			<reps:formbar>
				<reps:ajax  messageCode="add.button.save" formId="xform" callBack="my" type="button" cssClass="btn_save"></reps:ajax>
				<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back()"></reps:button>
			</reps:formbar>
		</reps:panel>
</reps:container>
	</div>
	<script type="text/javascript">
		
		var my = function(data){
			messager.message(data, function(){ back(); });
		};
		
		
		function back() {
			window.location.href= "khrlist.mvc";
		}
		
		var getPathNameOne = function(filename, fileType, fileSize, path, id) {
				$("tr:last #fileName").val(filename);
				$("tr:last #fileType").val(fileType);
				$("tr:last #fileSize").val(fileSize);
				$("tr:last #fileUrl").val(path);
				
				var tempTr = $("tr:last").clone(true); 
				$("tr:last").after(tempTr); 
				$("tr:last #fileName").val(""); 
				$("tr:last #fileType").val(""); 
				$("tr:last #fileSize").val(""); 
				$("tr:last #fileUrl").val("");
				$("tr:last a").remove(); 
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
			
			//单击去右边
			$("a:eq(0)").click(function(){
				$("#itemId:first>option:selected").appendTo($("#itemIds"));
				
			});
			//单击去左边
			$("a:eq(1)").click(function(){
				$("#itemIds:first>option:selected").appendTo($("#itemId"));
			});
			
			$("#itemIds").click(function(){
				var sel = document.getElementById("itemIds");
	            for (var i = 0; i < sel.options.length; i++) {
	                sel.options[i].selected = true;
	            }
			});
		});
	</script>

</body>
</html>