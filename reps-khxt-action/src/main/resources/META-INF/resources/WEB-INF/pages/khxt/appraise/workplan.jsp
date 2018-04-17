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
		font-size: 18px;
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
	.button {
	    background-color: #4CAF50; /* Green */
	    border: none;
	    color: white;
	    padding: 15px 32px;
	    text-align: center;
	    text-decoration: none;
	    display: inline-block;
	    font-size: 16px;
	    margin: 4px 2px;
	    cursor: pointer;
		} 
	.button1 {
	    background-color: #4CAF50; /* Green */
	    border: none;
	    color: black;
	    padding: 1px 15px;
	    text-align: center;
	    text-decoration: none;
	    display: inline-block;
	    font-size: 16px;
	    margin: 4px 2px;
	    cursor: pointer;
		}
	.button3 {background-color: #e7e7e7;} /* Red */ 
	.button4 {background-color: #e7e7e7; color: black; border-radius: 8px;}
	
	#msg{
	    height: 4rem;
	    text-align: center;
	    position: fixed;
	    top: 50%;
	    margin-top: -1rem;
	    line-height: 2rem;
	    width: 100%;
	}
	#msg span{
	    color: #030303;
	    background: rgba(0,0,0,0.9);
	    background-color: yellow;
	    height: 2rem;
	    display: inline-block;
	    padding: 0 3rem;
	    border-radius: 5px;
	}
	th div {
	width:40px;
	}
	</style>
<script type="text/javascript"> 
			$(function() { 
					$("#addOneRow").click(function() { 
						var tempTr = $("#create").clone(true); 
						$("tr:last").after(tempTr); 
						$("tr:last > td > #planning").val("");//新添加行name为空 
						$("tr:last > td > #execution").val("");//新添加行address为空 
						
						 var len = $('tbody tr').length-2;
							$("tr:last #index").text(len)
					}); 
					$("#delOneRow").click(function() { 
						if ($("tr").length ==3) { 
							
						} else{
							$("tr:last").remove();
						}
					}); 
					
					 $("#cancel").click(function() { 
						window.location.href= "bkhrlist.mvc";					
					}); 
					
					 $("#save").click(function(){
						 $.ajax({
			                type: "POST",//方法类型
			                dataType: "json",//预期服务器返回的数据类型
			                url: "addWorkPlan.mvc" ,//url
			                data: $("form").serialize() ,
			                success: function (message) {
			                	alert(message.message);
			                	window.location.href= "bkhrlist.mvc"; 	
			                }
			            });
				}); 
					
					$("#saveappear").click(function(){
						$("#appear").val("appear");
						$.ajax({
			                type: "POST",//方法类型
			                dataType: "json",//预期服务器返回的数据类型
			                url: "addWorkPlan.mvc" ,//url
			                data: $("form").serialize() ,
			                success: function (message) {
			                	alert(message.message);
			                	window.location.href= "bkhrlist.mvc";	
			                }
			            });
						
					});
			});
			
			function alert(e){
			    $("body").append("<div id='msg'><span>"+e+"</span></div>");
			    clearmsg();
			}
			function clearmsg(){
			    var t = setTimeout(function(){
			        $("#msg").remove();
			    },2500)
			};
			
	</script> 
		
</head>
	<body>
		<div style="widht:1100px ;margin-left:200px;">
		  <form action="addWorkPlan.mvc" method="post" if="myFrom">
				<table border="1" cellspacing="1" width="1100" height="100" align="center"  class="gridtable">
						<input type="hidden" id="appear" name="appear"/>
						<input type="hidden" id="workId" name="workId" value="${workId }"/>
					<tr >    
						<th align="center" colspan="4">${sheet.name}日常考核量化评分细则 </th>
					</tr>
					<tr>   
						<th align="center" ><div>序号</div></th>   
						<th  align="center">本月工作计划</th> 
						<th  align="center">本月工作纪实</th>
						<th>
							<input type="button" id="addOneRow" value="加一行" align="center" class="button1 button3"/>
							<input type="button" id="delOneRow" value="删一行" align="center" class="button1 button3"/>
						</th>
						
					</tr>
					<c:forEach  var="listWork"   items="${listWork}">
						<c:if test="${not empty  listWork }">
							<tr id="create">
								<td  id="index" align="center"></td>
								<td align="center" ><input type="text" id="planning" name="planning" value="${listWork.planning}" style="width:300px;height:25px" /></td>
								<td align="center" ><input type="text" id="execution" name="execution" value="${listWork.execution }" style="width:300px;height:25px"/></td>
								<td  align="center">
									<input type="hidden"  style="width:20px;height:25px"/>
								</td>
							</tr>
						</c:if>
		             </c:forEach>
				</table>
					<div style="float:left;margin-left:150px;">
						<input type="hidden" name="sheetId" value="${sheet.id}"></br>
						<input type="button" id="save" value="保存不上报" align="center" class="button button4">
						<input type="button" id ="saveappear"value="上报" class="button button4">
						<input type="button" id="cancel" value="取消" class="button button4">
					</div>
			</form>
		</div>
		<script type="text/javascript"> 
				$(function() { 
					 <c:forEach  var="work"  items="${work}">
						<c:if test="${not empty  work }">
							var td ='<tr id="create">'
								td+='<td  id="index" align="center"></td>'
								td+='<td align="center"><input type="text" id="planning" name="planning" style="width:300px;height:25px" /></td>'
		            			td+='<td align="center"><input type="text" id="execution" name="execution" style="width:300px;height:25px"/></td>'
								td+='<td  align="center">'
								td+='<input type="hidden"  style="width:20px;height:25px"/>'
								td+='</td>'
		            			td+='</tr>'
							$("tr:last").after(td); 							
						</c:if>
	            	</c:forEach> 
	            	
	            	 var len = $('tbody tr').length;
			          var b = 0
			          for(var i =1	;i<len;i++,b++){
			              $('tbody tr:eq('+i+') #index').text(b);
			          }
				});
		</script> 
	</body>
</html>
