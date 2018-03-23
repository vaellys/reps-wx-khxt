<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>新增评分权重</title>
<reps:theme />
<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width:68%;
	margin-left:0;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}

table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
</head>
<body>
	<reps:container>
		<reps:panel id="myform" dock="top" action="add.mvc" formId="xform"
			validForm="true">
			<reps:formcontent>
				<reps:formfield label="权重名称" labelStyle="width:20%;"
					textStyle="width:20%;">
					<reps:input name="name" minLength="2" maxLength="20"
						required="true"></reps:input>
				</reps:formfield>
				<reps:formfield label="适用年度" labelStyle="width:5%"
					textStyle="width:30%;">
					<reps:select dataSource="${applyYearMap}" id="year" name="year"
						required="true"></reps:select>
				</reps:formfield>
				<reps:formfield label="权重条件" fullRow="true" >
					<input type="hidden" name="weight" id="weight" value="">
					<table class="gridtable" id="gridTable">
						<tbody>
							<tr>
								<th>级别</th>
								<th>级别名称</th>
								<th>人员</th>
								<th>权重(%)</th>
							</tr>
							<c:forEach var="level" items="${levelList}" varStatus="status">
								<tr>
									<td><c:if test="${level.level == '1'}">一级</c:if><c:if test="${level.level == '2'}">二级</c:if><c:if test="${level.level == '3'}">三级</c:if><c:if test="${level.level == '4'}">四级</c:if><c:if test="${level.level == '5'}">五级</c:if><c:if test="${level.level == '6'}">六级</c:if></td>
									<td>${level.name }</td>
									<td>${level.personNames }</td>
									<td><input class="txtInput integernum required" name="levelWeight${status.count }" style="width:176px" value="" levelId="${level.id }"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</reps:formfield>
				
				<reps:formfield label="公开打分明细" fullRow="true">
					<input type="radio" name="visible" value="1" checked />可见<span
						style="margin-right: 50px;"></span>
					<input type="radio" name="visible" value="0" />不可见
				</reps:formfield>
			</reps:formcontent>
			</br>
			</br>
			<reps:formbar>
				<reps:ajax messageCode="add.button.save" formId="xform"
					callBack="my" type="button" cssClass="btn_save" beforeCall="buildLevelWeightJson()"></reps:ajax>
				<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel"
					onClick="back()"></reps:button>
			</reps:formbar>
		</reps:panel>
	</reps:container>
</body>
<script type="text/javascript">
	var my = function(data) {
		messager.message(data, function() {
			back();
		});
	};

	function back() {
		window.location.href = "list.mvc";
	}

	$(function() {
		$("#year").val(new Date().getFullYear());
	});
	
	function buildLevelWeightJson() {
		var levelWeightArray = new Array();
		$("#gridTable input[name^='levelWeight']").each(function(){
			var levelId = $(this).attr("levelId");
			var weight = $(this).val();
			levelWeightArray.push(new LevelWeight(levelId, weight));
		});
		var jsonStr = JSON.stringify(levelWeightArray);
		$("input[name='weight']").val(jsonStr);
		return true;
	}
	
	function LevelWeight(levelId, weight) {
		this.levelId = levelId;
		this.weight = weight;
	}
</script>
</html>