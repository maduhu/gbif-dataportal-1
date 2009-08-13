<%@ include file="/common/taglibs.jsp"%>
<div class="adminConsoleContainer">
	<form action="createImmediateTrigger.htm" method="post">
		<h1><spring:message code="admin.index.schedule.create.new.immediate"/></h1>
		<p>
			<label for="selectDataResource"><spring:message code="admin.index.schedule.select.resource"/></label>
			<select id="selectDataResource" name="dataResourceId">
				<c:forEach items="${dataResources}" var="dataResource">
					<option value="${dataResource.key}">${dataResource.name}</option>
				</c:forEach>
			</select>
		</p>
		<p class="center">
			<input type="submit" class="submit" value="<spring:message code="create"/>"/>
		</p>
	</form>		
</div>
<div class="adminConsoleContainer">
	<form action="createTrigger.htm" method="post">
		<h1><spring:message code="admin.index.schedule.create.new"/></h1>
		<p>
			<label for="selectDataResource"><spring:message code="admin.index.schedule.select.resource"/></label>
			<select id="selectDataResource" name="dataResourceId">
				<c:forEach items="${dataResources}" var="dataResource">
					<option value="${dataResource.key}">${dataResource.name}</option>
				</c:forEach>
			</select>
		</p>
		
		<p>
			<label for="selectFrequency"><spring:message code="admin.index.schedule.select.frequency"/></label>
			<select name="frequency" id="selectFrequency">
				<c:forEach items="${periodList}" var="period">
					<option value="${period}"><spring:message code="${period}"/></option>
				</c:forEach>
			</select>
		</p>
		
		<p>
			<label for="selectTime"><spring:message code="admin.index.schedule.select.start.time"/></label>
			<select name="time" id="selectTime">
				<c:forEach items="${timeList}" var="time">
					<option value="${time}">${time}</option>
				</c:forEach>
			</select>
		</p>
		
		<p>
			<label for="startDate"><spring:message code="admin.index.schedule.select.start.date"/></label>
			<div id="startDate" class="calendar"></div>
			<input type="hidden" id="dateToStart" name="dateToStart"/>
			<script>
				var cal = new YAHOO.widget.Calendar("cal","startDate"); 
				cal.render(); 
				// sets the date as a dd/MM/yyyy (regardless of locale!)
				function setDate(){
					date = cal.getSelectedDates()[0];
					var to2Digits = function(target) {
						targetString = new String(target);
						if (targetString.length == "1") {
							targetString = "0" + target;
						}
						return targetString;
					}
					
					day = to2Digits(date.getDate());
					month = to2Digits((date.getMonth()+1));
					year = date.getFullYear();
					dateString = day + "/" + month + "/" + year;
					document.getElementById("dateToStart").value = dateString;
				}
			</script>
		</p>
		<br/>		
		<p class="center">
			<input type="submit" onClick='javascript:setDate()' class="submit" value="<spring:message code="create"/>"/>
		</p>
	</form>		
</div>
<div class="adminConsoleContainer">
	<h1><spring:message code="admin.index.schedule.table.caption"/></h1>
	
	<display:table name="scheduleDetailList" uid="scheduleItem" class="simpleAdminTable">
		<spring:message code="admin.index.schedule.table.title.data.resource.name" var="scheduleHeaderI18N" scope="page"/>
		<display:column title="${scheduleHeaderI18N}" property="jobName" headerClass="left" class="left"/>

		<spring:message code="admin.index.schedule.table.title.data.description" var="descriptionHeaderI18N" scope="page"/>
		<display:column title="${descriptionHeaderI18N}" property="description"/>

		<spring:message code="admin.index.schedule.table.title.create.date" var="createDateHeaderI18N" scope="page"/>
		<display:column title="${createDateHeaderI18N}" property="createDate" decorator="org.gbif.portal.web.ui.I18nDateTimeDecorator" class="center"/>

		<spring:message code="admin.index.schedule.table.title.last.start" var="lastStartHeaderI18N" scope="page"/>
		<display:column title="${lastStartHeaderI18N}" property="lastStartDate" decorator="org.gbif.portal.web.ui.I18nDateTimeDecorator" class="center"/>
			
		<spring:message code="admin.index.schedule.table.title.next.start" var="nextStartHeaderI18N" scope="page"/>
		<c:choose>
		    <c:when test="${scheduleItem.nextIndexInPast}">
				<display:column title="${nextStartHeaderI18N}" property="nextStartDate" decorator="org.gbif.portal.web.ui.I18nDateTimeDecorator" class="warning"/>        
		    </c:when>
		    <c:otherwise>
				<display:column title="${nextStartHeaderI18N}" property="nextStartDate" decorator="org.gbif.portal.web.ui.I18nDateTimeDecorator" class="center"/>        
		    </c:otherwise>
		</c:choose>						
		
		<spring:message code="admin.index.schedule.table.title.delete" var="deleteHeaderI18N" scope="page"/>
		<display:column title="${deleteHeaderI18N}" class="rightLink" headerClass="right">        
			<a href="deleteTrigger.htm?triggerName=${scheduleItem.triggerName}"><spring:message code="delete"/></a>
		</display:column>

	</display:table>
</div>