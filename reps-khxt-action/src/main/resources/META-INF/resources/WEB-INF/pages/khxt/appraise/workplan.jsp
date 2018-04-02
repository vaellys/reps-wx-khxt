<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<% 
String path = request.getContextPath().equals("/") ? "" : request.getContextPath();
String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
String basePath = request.getScheme() + "://" + request.getServerName() + port + path;
%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>上报工作计划</title>
		<script type="text/javascript" src="<%=basePath%>/library/base/jquery-1.11.2.min.js"></script>
		
		<style type="text/css">
table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 68%;
	margin-left: 0;
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
		<script type="text/javascript"> 
			$(function() { 
				 	
					$(".addOneRow").click(function() { 
						var tempTr = $("#create").clone(true); 
						$("tr:last").after(tempTr); 
						$("tr:last > td > #planning").val("");//新添加行name为空 
						$("tr:last > td > #execution").val("");//新添加行address为空 
					}); 
					$(".delOneRow").click(function() { 
					if ($("tr").length ==3) { 
						alert("至少保留一行!"); 
					} else{
						$("tr:last").remove();
					}
					
				}); 
					$("#cancel").click(function() { 
						window.location.href= "list.mvc";					
				}); 
					
					$("#submit1").click(function(){
						$("form").submit();
						window.location.href= "list.mvc";
					});
					
					$("#submit2").click(function(){
						$("#appear").val("appear");
						$("form").submit();
						window.location.href= "list.mvc";
					});
					
			}); 
			
			
		</script> 
		
	</head>
	<body>
		<div style="widht:1100px ;margin-left:200px;">
		  <form action="addWorkPlan.mvc" method="post" id="myFrom">
				<table border="1" cellspacing="1" width="1100" height="100" align="center"  class="gridtable">
					<input type="hidden" id="appear" name="appear"/>
					<input type="hidden" id="workId" name="workId" value="${workId }"/>
					<tr >    
						<th align="center" colspan="2">${sheet.name}日常考核量化评分细则 </th>
					</tr>
					<tr>    
						<th  align="center">本月工作计划</th> 
						<th  align="center">本月工作纪实</th>
						
					</tr>
					<c:forEach  var="listWork"   items="${listWork}">
						<tr id="create">
							<td align="center"><input type="text" id="planning" name="planning" value="${listWork.planning}" style="width:300px;height:25px" /></td>
	            		
							<td align="center"><input type="text" id="execution" name="execution" value="${listWork.execution }" style="width:300px;height:25px"/></td>
						</tr>
		             </c:forEach>
					
				</table>
					<div style="float:left;margin-left:200px;">
						<input type="hidden" name="sheetId" value="${sheet.id}">
						<input type="button" id="submit1" value="保存不上报" align="center">
						<input type="button" id ="submit2"value="上报" >
						<input type="button" id="cancel" value="取消">
					</div>
						
					<div style="margin-left:700px;">
						<input type="button" class="addOneRow" value="添加一行" align="center"/>
						<input type="button" class="delOneRow" value="删除一行" align="center"/>
					</div>
			</form>
		</div>
	</body>
</html>
