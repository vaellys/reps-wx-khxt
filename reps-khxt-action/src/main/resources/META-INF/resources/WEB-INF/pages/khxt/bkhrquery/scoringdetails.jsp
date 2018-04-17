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
	<title>被考核人查询列表</title>
	<script type="text/javascript" src="<%=basePath%>/library/base/jquery-1.11.2.min.js"></script>
	
	
	<style type="text/css">
		table  
        {  
            font-family:宋体;
			font-size: 18px;
			color: #333333;
			border-width: 1px;
			border-color: #666666;
			border-collapse: collapse;
			width: 68%;
			margin-left: 0;
			
        }  
        table td, table th  
        {  
            border: 1px solid #cad9ea; 
            color: #666666;  
            height: 30px;  
        }  
        table thead th  
        {  
            background-color: #CCE8EB;  
            width: 100px;  
        }  
        table tr:nth-child(odd)  
        {  
            background: #fff;  
        }  
        table tr:nth-child(even)  
        {  
            background: #F5FAFA;  
        }
        
        .button {
		  display: inline-block;
		  padding: 15px 25px;
		  font-size: 14px;
		  cursor: pointer;
		  text-align: center;   
		  text-decoration: none;
		  outline: none;
		  color: #fff;
		  background-color:#87CEFF;
		  border: none;
		  border-radius: 25px;
		  box-shadow: 0 9px #999;
		}
		.button:hover {background-color: #3e8e41}
		.button:active {
		  background-color: #3e8e41;
		  box-shadow: 0 5px #666;
		  transform: translateY(4px);
		} 
		.size1{
			font-size:12px;
		} 
		.size2{
			font-size:20px;
		} 
	</style>
	
</head>
<body>
<div>
	<table  class="gridtable" style="float:left;margin-left:100px;">
		</br>
		<tr >
			<th align="left" style="border:0px;" colspan="2" class="size1">被考核人：${members.bkhrPerson.name}</th>
			<th align="center" style="border:0px;" class="size2">${sheet.name}</th>
		</tr>
		<tr>
			<th align="center" style="width:40px;">序号</th>
			
			<th align="center">考核人</th>
			 <c:forEach items="${list}"  var="members" end="0">
				<c:forEach items="${members.performancePoints}"  var="points">
					<th align="center">${points.khxtItem.name }(${points.khxtItem.point})</th>
					<td id="monthscore"  style="display:none">${points.khxtItem.point}</td>
				</c:forEach>
			</c:forEach>
			
			<th align="center" id="center">本月得分</th>
		</tr>
			<c:forEach items="${list}"  var="members">
				<tr>
					<td id="index" align="center"></td>
					<td align="center">${members.khrPerson.name}</td>
					<c:choose>
					    <c:when test="${not empty  members.performancePoints}">
							<c:forEach items="${members.performancePoints}"  var="points">
								<td align="center">${points.point}</td>
							</c:forEach>
					    </c:when>
					    <c:otherwise>
							
					    </c:otherwise>
					</c:choose>
					<td align="center" id="minute">${members.totalPoints}</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2" align="center" id="total">合计</td>
				<td id="totalPoints" align="center"></td>
			</tr>
		</table>
		<div style="margin-top:200px;margin-left:700px;"><button class="button">返回</button></div>
</div>		
</body>
	<script type="text/javascript">
			$(function(){
				/* 添加序号 */
				 var len = $('tbody tr').length;
		          var b = 0
		          for(var i =1	;i<len;i++,b++){
		              $('tbody tr:eq('+i+') #index').text(b);
		          }
		          
				var length =$('tr:eq(1) th').length;
				$("tr:first th:eq(1)").prop("colspan",length);
				
				$("button").click(function(){
					window.location.href= "bkhrquery.mvc";
				})
				
				var sum = 0
				$('tbody #monthscore').each(function(){
					sum+=+$(this).text();
						
				})
				
				$("#center").text("本月得分("+sum+")")
				var b = length-3;
				for (var i = 0; i < b; i++) {
					$("#total").after("<td></td>")
				}
			})
	</script>
</html>
