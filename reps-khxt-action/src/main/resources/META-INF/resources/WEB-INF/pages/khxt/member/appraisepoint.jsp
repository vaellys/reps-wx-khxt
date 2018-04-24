<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>评分</title>
<reps:theme />
<style type="text/css">

table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 80%;
	margin-left: 30px;
	text-align:center;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #d0d0d0;
	background-color: #EEE;
}

table.gridtable td {
	border-width: 1px;
	padding: 3px;
	border-style: solid;
	border-color: #ededed;
	background-color: #ffffff;
	/*text-decoration: underline;*/
}
</style>
</head>
<body>
	<reps:container>
		<reps:panel id="myform" dock="top" action="add.mvc" formId="xform"
			validForm="true">
					<div style="margin-bottom: 5px;margin-top: 10px;"><span style="margin-left: 30px; margin-right: 700px;font-size:14px;font-weight:700">${khxtAppraiseSheet.season }${khxtAppraiseSheet.name }</span> <span style="font-size:14px;font-weight:700;">考核人：${khrPersonName }</span></div>
					<input type="hidden" name="memberJson" id="memberJson" value="">
					<input type="hidden" name="itemPointJson" id="itemPointJson"
						value="">
					<input type="hidden" name="sheetId" value="${member.sheetId }">
					<input type="hidden" name="khrPersonId" value="${member.khrPersonId }">
					<table class="gridtable" id="gridTable">
						<tbody>
							<tr>
								<th>被考核人</th>
								<c:forEach var="item" items="${items}" varStatus="status">
									<c:set var="count" value="${count+ item.point}" />
									<th>${item.name}（<fmt:formatNumber
											value="${item.point}" pattern="#.#" />）
									</th>
								</c:forEach>
								<th>合计（<fmt:formatNumber value="${count}"
										pattern="#.#" />）
								</th>
							</tr>
							<c:forEach var="member" items="${performanceMembers}"
								varStatus="status">
								<tr>
									<td><reps:dialog cssClass="" id="detail" iframe="true"
											width="600" height="200"
											url="workdetail.mvc?sheetId=${member.appraiseSheet.id }&personId=${member.bkhrPersonId }"
											value="${member.bkhrPersonName }" title="${khxtAppraiseSheet.season }份工作人员日常考核量化评分细则"></reps:dialog><br>
									<c:if test="${member.status == 0}"><span> (未上报工作计划)</span></c:if>
									</td>
									<c:forEach var="item" items="${items}" varStatus="s">
										<td>
											<input class="txtInput moneyvalidate required" style="width:80%"
											name="point${status.count }${s.count }" 
											<c:forEach var="p" items="${member.performancePoints}" varStatus="s">
												<c:if test="${item.id == p.itemId}">	value='<fmt:formatNumber
											value="${p.point}" pattern="#.#" />' pointId="${p.id }" </c:if>
											</c:forEach>
											itemId="${item.id }" memberId="${member.id }" 
											max="${item.point }" min="0"
											onkeyup="return calculateTotalPoint($('#totalPoint${status.count }'), '${status.count }')" <c:if test="${member.status == 0}">disabled="disabled"</c:if>>
										</td>
									</c:forEach>
									<td><input class="txtInput moneyvalidate"
										name="totalPoint${status.count }"
										id="totalPoint${status.count }" memberId="${member.id }"
										value="<fmt:formatNumber
											value="${member.totalPoints}" pattern="#.#" />" <c:if test="${member.status == 0}">disabled="disabled"</c:if> readonly="readonly" style="width:80%"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

			</br>
			</br>
			<reps:formbar>
				<reps:ajax messageCode="add.button.save" formId="xform"
					callBack="my" type="button" cssClass="btn_save"
					beforeCall="buildMemberAndItemJson()"></reps:ajax>
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
		window.location.href = "${ctx}/reps/khxt/appraise/listpoint.mvc";
	}

	function calculateTotalPoint($obj, index) {
		var totalPoint = 0;
		$("input[name^='point" + index + "']").each(function() {
			var val = parseFloat($(this).val());
			if (!isNaN(val)) {
				totalPoint += val;
			}
		});
		$obj.val(totalPoint);
	}

	function buildMemberAndItemJson() {
		var memberArray = new Array();
		$("#gridTable input[name^='totalPoint']:not(:disabled)").each(function() {
			var memberId = $(this).attr("memberId");
			var totalPoint = $(this).val();
			memberArray.push(new Member(memberId, totalPoint));
		});
		var memberJsonStr = JSON.stringify(memberArray);
		$("input[name='memberJson']").val(memberJsonStr);

		var itemPointArray = new Array();
		$("#gridTable input[name^='point']:not(:disabled)").each(function() {
			var memberId = $(this).attr("memberId");
			var itemId = $(this).attr("itemId");
			var pointId = $(this).attr("pointId");
			var point = $(this).val();
			itemPointArray.push(new ItemPoint(memberId, itemId, point, pointId));
		});
		var itemPointJsonStr = JSON.stringify(itemPointArray);
		$("input[name='itemPointJson']").val(itemPointJsonStr);

		return true;
	}

	function Member(memberId, totalPoint) {
		this.memberId = memberId;
		this.totalPoint = totalPoint;
	}

	function ItemPoint(memberId, itemId, point, pointId) {
		this.memberId = memberId;
		this.itemId = itemId;
		this.point = point;
		this.pointId = pointId;
	}
</script>
</html>