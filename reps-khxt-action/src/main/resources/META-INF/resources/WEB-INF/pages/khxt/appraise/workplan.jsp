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
		</reps:formcontent>
	</reps:panel>
</reps:container>
	
</head>
<body>
<table border="1" width="500">
    
</table>

</body>
</html>