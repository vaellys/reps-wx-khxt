<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>评分详情</title>
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
					<div style="margin-bottom: 5px;margin-top: 10px;"><span style="margin-left: 30px; margin-right: 700px;font-size:14px;font-weight:700">考核人：${khrPersonName }</span></div>
					<table class="gridtable" id="gridTable">
						<caption style="padding: 8px;font-weight: bold;">${khxtAppraiseSheet.season }${khxtAppraiseSheet.name }</caption>
						<tbody>
							<tr>
								<th>序号</th>
								<th>被考核人</th>
								<c:forEach var="item" items="${items}" varStatus="status">
									<c:set var="count" value="${count+ item.point}" />
									<th>${item.name}（<fmt:formatNumber
											value="${item.point}" pattern="#.#" />）
									</th>
								</c:forEach>
								<th>本月得分（<fmt:formatNumber value="${count}"
										pattern="#00.#" />）
								</th>
							</tr>
							<c:forEach var="member" items="${performanceMembers}"
								varStatus="status">
								<tr>
									<td>${status.count }<br>
									<td><reps:dialog cssClass="" id="detail" iframe="true"
											width="600" height="200"
											url="${ctx }/reps/khxt/member/workdetail.mvc?sheetId=${khxtAppraiseSheet.id }&personId=${member.bkhrPerson.id }"
											value="${member.bkhrPerson.name }"></reps:dialog><br>
									</td>
									<c:forEach var="item" items="${items}" varStatus="s">
										<td>
											<c:forEach var="p" items="${member.performancePoints}" varStatus="s">
												<c:if test="${item.id == p.itemId}"><fmt:formatNumber
											value="${p.point}" pattern="#.#" /></c:if>
											</c:forEach>
											
										</td>
									</c:forEach>
									<td><fmt:formatNumber value="${member.totalPoints}" pattern="#.#" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

			</br>
			</br>
			<reps:formbar>
					<reps:button cssClass="btn_back" type="button" onClick="back()"
					messageCode="manage.action.return" />
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
		window.location.href = "${ctx}/reps/khxt/khrprocess/list.mvc";
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