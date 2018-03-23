<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>级别人员列表</title>
<reps:theme />
</head>
<body>
	<reps:container layout="true">
		<reps:panel title="" id="top" dock="top" method="post"
			action="list.mvc?levelId=${levelId }" formId="queryForm">
			<reps:formcontent parentLayout="true" style="width:80%;">
				<reps:formfield label="名称" labelStyle="width:20%;"
					textStyle="width:27%;">
					<reps:input name="personName" maxLength="20">${levelPerson.personName }</reps:input>
				</reps:formfield>
			</reps:formcontent>
			<reps:querybuttons>
				<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm"
					gridId="levelPersonList" cssClass="search-form-a"></reps:ajaxgrid>
			</reps:querybuttons>
			<reps:footbar>
				<input type="hidden" name="levelPersonIds" />
				<sys:user id="user" hideId="hideId" hideName="hideName"
					hideNameValue="" nameValue=""
					url="multiple.mvc?levelId=${levelId}" multiple="true" name="人员选择"
					cssClass="add-a" callBack="userCallBack">新增</sys:user>
				<reps:ajax cssClass="delete-a" confirm="确认批量删除吗?"
					beforeCall="checkChecked" formId="queryForm" callBack="my"
					value="批量删除" />
				<reps:button cssClass="return-a"
					action="${ctx }/reps/khxt/level/list.mvc" value="返回" />
			</reps:footbar>
		</reps:panel>
		<reps:panel id="mybody" dock="center">
			<reps:grid id="levelPersonList" items="${list}" form="queryForm"
				var="levelPerson" pagination="${pager}" flagSeq="true">
				<reps:gridrow>
					<reps:gridcheckboxfield checkboxName="ids" align="center" title="选择" width="5">${levelPerson.id}</reps:gridcheckboxfield>
					<reps:gridfield title="姓名" width="15" align="center">${levelPerson.personName}</reps:gridfield>
					<reps:gridfield title="性别" width="15" align="center">
						<sys:dictionary src="sex">${levelPerson.personSex}</sys:dictionary>
					</reps:gridfield>
					<reps:gridfield title="单位部门" width="25" align="center">${levelPerson.organize.name}</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
		var userCallBack = function() {
			var levelId = '${levelId}';
			var ids = $("#hideId").val();
			$.ajax({
				url : "save.mvc",
				type : "POST",
				dataType : "json",
				data : {
					"personIds" : ids,
					"levelId" : levelId
				},
				async : false,
				success : function(data) {
					messager.message(data, function() {
						window.location.reload();
					});
				}
			});
		};

		var checkChecked = function() {
			if ($("input[type=checkbox][name=ids]:checked").length == 0) {
				messager.info("请选择要批量删除的账号");
				return false;
			}
			var levelPersonObj = $("input[type=hidden][name=levelPersonIds]");
			levelPersonObj.val("");
			$.each($("input[type=checkbox][name=ids]:checked"),
					function(i, obj) {
						if (levelPersonObj.val() == "") {
							levelPersonObj.val($(obj).val());
						} else {
							levelPersonObj.val(levelPersonObj.val() + ","
									+ $(obj).val());
						}
					});
			$("#queryForm").attr("action", "delete.mvc");
			return true;
		};

		var my = function(data) {
			messager.message(data, function() {
				back();
			});
		};

		var back = function() {
			window.location.href = "list.mvc?levelId=${levelId }";
		}
	</script>
</body>
</html>
