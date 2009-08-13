<%@ attribute name="inputId" required="true" %>
<%@ attribute name="selectedVar" required="true" %>
<%@ attribute name="receivedKeyPressVar" required="true" %>
<%@ include file="/common/taglibs.jsp"%>
<script>
	var ${selectedVar} = false;

	<gbiftag:ieCheck/>
	<c:choose>	
	<c:when test="${isMSIE}">
		document.getElementById('${inputId}').attachEvent("onclick", reset${inputId});		
	</c:when>
	<c:otherwise>
		document.getElementById('${inputId}').addEventListener('click', reset${inputId}, false);
	</c:otherwise>
	</c:choose>	
	
	function reset${inputId}(ev){
		if(!${selectedVar}){
			${selectedVar}=true;
			document.getElementById('${inputId}').value="";
			document.getElementById('${inputId}').style.color="#000000";
		}
	}

	var ${receivedKeyPressVar} = false;
	
	<c:choose>	
	<c:when test="${isMSIE}">
		document.getElementById('statesinput').attachEvent("onkeypress", check${inputId});		
	</c:when>
	<c:otherwise>
		document.getElementById('${inputId}').addEventListener('keypress', check${inputId}, false);		
	</c:otherwise>
	</c:choose>	
		
	function check${inputId}(ev){
		${receivedKeyPressVar} = true;
	}
</script>	