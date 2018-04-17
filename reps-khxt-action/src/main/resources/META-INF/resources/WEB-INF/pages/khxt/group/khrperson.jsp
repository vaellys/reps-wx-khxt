<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>考核人用户查询</title>
<reps:theme />
<style type="text/css">
.selecteUser {
	width: 500px;
	height: 30px;
	overflow: auto;
	white-space: pre-wrap;
	word-wrap: break-word;
	float: left;
	margin-left: 10px;
	text-align: left;
}

.submit_btn {
	float: left;
	margin-left: 20px;
}

.hk {
	margin-right: 10px;
	align-content: right;
}
</style>
</head>
<body>
	<reps:container layout="true">
		<reps:panel id="top" dock="top" method="post" action="khrlistlevel.mvc?levelId=${levelId}" formId="queryForm">
			<reps:formcontent parentLayout="true" style="width:80%;">
				<input name="roleId" type="hidden" value="${roleId}" />
				<input name="showName" type="hidden" value="${showName}" />
				<input name="hideName" type="hidden" value="${hideName}" />
				<input name="multiple" type="hidden" value="${multiple}" />
				<input name="hideNameValue" type="hidden" value="${hideNameValue}" />
				<input name="dialogId" type="hidden" value="${dialogId}" /> 
				<input name="hiddenlevelId" id="hiddenlevelId" type="hidden" value="${u.levelId}" /> 
				<reps:formfield label="姓名">
					<reps:input name="personName">${u.personName}</reps:input>
				</reps:formfield>
				
				<reps:formfield label="工作单位">
					<reps:input name="organize.name">${u.organize.name}</reps:input>
				</reps:formfield>
				
			</reps:formcontent>
			<reps:querybuttons style="margin-right:20px;">
				<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="mygrid" cssClass="search-form-a"></reps:ajaxgrid>
			</reps:querybuttons>
		</reps:panel>
		<reps:panel id="mybody" dock="center" border="true">
			<reps:grid id="mygrid" items="${list}" form="queryForm" var="u" pagination="${pager}">
				<reps:gridrow>
					<reps:gridfield title="姓名" width="15">${u.personName}</reps:gridfield>
					
					<reps:gridfield title="性别" width="10">
						<sys:dictionary src="sex">${u.personSex}</sys:dictionary>
					</reps:gridfield>
					<reps:gridfield title="工作单位" width="20">${u.organize.name}</reps:gridfield>
			
					<reps:gridfield title="选择" width="10" align="center">
					<c:if test="${'00' == u.personId}">
						<input type="checkbox" disabled="disabled"/>
					</c:if>
					<c:if test="${'00' != u.personId}">
						<c:choose>
							<c:when test="${fn:contains(hideNameValue,u.personId)}">
								<input type="checkbox" checked="checked" name="user" value="${u.personId}" nameAttr="${u.personName}" data-options="{'id':'${u.personId}','name':'${u.personName}'}" />
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="user" value="${u.personId}" nameAttr="${u.personName}" data-options="{'id':'${u.personId}','name':'${u.personName}'}" />
							</c:otherwise>
						</c:choose>
					</c:if>
					</reps:gridfield>
				</reps:gridrow>
			</reps:grid>
			<reps:formbar>
				<div class="dialogButtons" style="height:100px;margin-top: 10px">
					<div id="selecteUser" class="selecteUser"></div>
					<div class="submit_btn">
						<ul>
							<div class="small_btn_save">
								<input type="button" onclick="okCallback()" value="确定">
							</div>
							<div class="small_btn_cancel hk">
								<input type="button" onclick="cancelCallback()" value="取消">
							</div>
						</ul>
					</div>
				</div>
			</reps:formbar>
		</reps:panel>
	</reps:container>
	<script type="text/javascript">
	var chooseUserBack = function(name,id){
		$("input[name='"+$("input[name=showName]").val()+"']",window.parent.document).val(name);
		$("input[name='"+$("input[name=hideName]").val()+"']",window.parent.document).val(id);
		cancelCallback();
	};
	
	$(function(){
		$(".gridScroller").css("height","185px");
		var data = '${hideNameValue}';
		if(data && "false" === "${filterSelected}"){
			var dataJson = data.split(",");
			for(var i in dataJson){
				var dataOptions = $("input[value='"+dataJson[i]+"'][name='user']").data("options");
				var dataObj = eval("("+dataOptions+")");
				if(0 != i){
					if((dataJson.length - 1) == i){
						$("#selecteUser").append("<span id='show"+dataObj["id"]+"'nameAttr='"+dataObj["name"]+"'idAttr='"+dataObj["id"]+"' >"+dataObj["name"]+"</span>");
					}else{
						$("#selecteUser").append("<span id='show"+dataObj["id"]+"'nameAttr='"+dataObj["name"]+"'idAttr='"+dataObj["id"]+"' >"+dataObj["name"]+", </span>");
					}
					
				}else{
					if(dataJson.length == 1){
						$("#selecteUser").append("<span id='show"+dataObj["id"]+"'nameAttr='"+dataObj["name"]+"'idAttr='"+dataObj["id"]+"' >"+dataObj["name"]+"</span>");
					}else{
						$("#selecteUser").append("<span id='show"+dataObj["id"]+"'nameAttr='"+dataObj["name"]+"'idAttr='"+dataObj["id"]+"' >"+dataObj["name"]+", </span>");
					}
				}
			}
		}
		$(".gridTbody").on("click","input[name=user]",function(){
			var $this = $(this);
			var id = $this.val();
			var len = $("input[name=user]:checked").length;
			if($this.is(":checked")){
				var nameAttr = $this.attr("nameAttr");
				if(len>1){
					$("span[id^=show]:last").append(", ");
					$("#selecteUser").append("<span id='show"+id+"'nameAttr='"+nameAttr+"'idAttr='"+id+"' >"+nameAttr+"</span>");
				}else{
					$("#selecteUser").append("<span id='show"+id+"'nameAttr='"+nameAttr+"'idAttr='"+id+"' >"+nameAttr+"</span>");
				}
			}else{
				var length = $("span[id^=show]").size();
				if((length - 1) == $("#show"+id).index()){
					$lastSecond = $("span[id^=show]").eq(length - 2);
					$lastSecond.html($lastSecond.attr("nameAttr"));
				}
				$("#show"+id).remove();
				
			};
			
		});
		
		var orgIds = $("input[name=hideNameValue]").val();
		var orgIdArray = orgIds.split(",");
		$("input[name=user]").each(function(j,value){
			var $this = $(this);
			$.each(orgIdArray,function(i,v){
				if($this.val() == v){
					$this.prop("checked",true);
					return false;
				}
			});
		});
		$(".gridTbody").delegate("input[name=user]","click",function(){
			var $this = $(this);
			//判断是选中还是不选中，判断是不是已经在hideNameValue中
			var flag = false;
			if($this.prop("checked")){
				$.each(orgIdArray,function(i,v){
					if($this.val() == v){
						flag = true;
						return false;
					}
				});
				if(!flag){
					var personname;
					if("" == orgIds || null == orgIds){
						orgIds += $this.val();
						
					}else{
						orgIds += (","+$this.val());
					}
					$("input[name=hideNameValue]").val(orgIds);
					flag = false;
				}
			}else{
				var index;
				orgIds = $("input[name=hideNameValue]").val();
				orgIdArray = orgIds.split(","); 
				$.each(orgIdArray,function(i,v){
					if($this.val() == v){
						flag = true;
						index = i;
						return false;
					}
				});
				if(flag){
					orgIdArray.splice(index,1);
					orgIds = orgIdArray.join(",");
					$("input[name=hideNameValue]").val(orgIds);
					flag = false;
				}
			}
			
		});
	});
	
	var okCallback=function(){
		var hideNameValue = $('input[name=hideNameValue]').val();
		
		
		if(hideNameValue.length == 0){
			alert('请选择人员！');
			return false;
		}  
		var hideName = $('input[name=hideName]').val();
		
		var str = new Array();
		
		var name;
		
		$("input:checked").each(function(){
			
			str.push($(this).attr("nameattr"));
			
			name = str.join(";");
		})

		window.parent.$('input[name='+hideName+']').val(hideNameValue+"="+name);

		var call = "${callBack}";
		if(call){
			var callBack = null;
			if(call.indexOf("(") > 0){
				callBack = "window.parent.${callBack}";
			}else{
				callBack = "window.parent.${callBack}()";
			}
			eval("("+callBack+")");
		}
		var hideIdMulCallback = function(){
		};
		cancelCallback();
	};
	
	//取消关闭窗口
	var cancelCallback=function(){
		//关闭弹出框
		$.pdialog.pdialogClose();
	};
	
	$(function () {
 		<c:forEach  items="${list}" var="u">
 			<c:if test="${!empty u}">  
				var a ="${u.levelId}";
				$("#hiddenlevelId").val(a);
         	</c:if>
     </c:forEach>
 	});
	
</script>
</body>
</html>
