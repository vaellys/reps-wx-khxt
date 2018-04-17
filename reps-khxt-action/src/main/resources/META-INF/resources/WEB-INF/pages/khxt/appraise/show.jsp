<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
<title>月考核详情</title>
<reps:theme />
</head>
<body>
		<reps:container>
			<reps:panel id="myform" dock="top" action="add.mvc" formId="xform"
			validForm="true">
				<reps:formcontent >
					<reps:formfield label="名称" labelStyle="width:20%;"
					textStyle="width:20%;" >
						${sheet.name }
					</reps:formfield>

					<reps:formfield label="考核年月" labelStyle="width:5%"
					textStyle="width:30%;">
						${sheet.season}
					</reps:formfield>

					<reps:formfield label="填报开始时间" labelStyle="width:20%;"
					textStyle="width:27%;">
							${sheet.beginDate }
					</reps:formfield>

					<reps:formfield label="填报截止时间" labelStyle="width:5%"
					textStyle="width:30%;">
						${sheet.endEate }
					</reps:formfield>

					<reps:formfield label="考核人" labelStyle="width:20%;"
					textStyle="width:27%;">
						${sheet.levelDisplay }
					</reps:formfield>

					<reps:formfield label="考核对象" labelStyle="width:10%"
					textStyle="width:30%;">
						${sheet.bkhr.name }
					</reps:formfield>

					<reps:formfield label="考核权重" fullRow="true">
						${sheet.levelWeight.name }
					</reps:formfield>
					
					<reps:formfield label="考核指标" fullRow="true" >
						<select multiple="multiple" name="itemId" id="itemId"
							style="width: 200px; height: 100px;" disabled="disabled">
							<c:forEach var="item" items="${sheet.item}">
									<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select>
					</reps:formfield>
					
					<c:forEach var="file" items="${file}" varStatus="status">
					<reps:formfield label="考核材料${status.count }" fullRow="true" >
						<c:if test="${!empty file.fileName}">
								<input type="text" name="fileName" id="fileName"
									value="${file.fileName }" readonly="readonly" class="txtInput" style="width:195px;"/>
						 <reps:button cssClass="downloading-a" value="下载" 
						action="download.mvc?id=${file.id}&callbackUrl=${callbackUrl }">
					</reps:button>
						</c:if>
					</reps:formfield>
					</c:forEach>
				</reps:formcontent>
				</br>
				</br>
				<reps:formbar>
					<reps:button cssClass="btn_back" type="button" onClick="back('${callbackUrl }')"
					messageCode="manage.action.return" />
				</reps:formbar>
			</reps:panel>
		</reps:container>
	<script type="text/javascript">
	
		function back(url) {
			window.location.href= url || "#";
		}
		
	</script>

</body>
</html>