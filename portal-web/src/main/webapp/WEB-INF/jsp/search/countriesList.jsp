<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${searchResults}" var="country" varStatus="status">
	<tr valign="top">
	    <td class="tdColumn1">
	        <p class="column1"><spring:message code="country"/></p>
	    </td>
	    <td class="tdColumn2">
	        <p class="column2">
	        	<span class="speciesName">
	        		<a href="${pageContext.request.contextPath}/countries/<string:encodeUrl>${country.isoCountryCode}</string:encodeUrl>/"><string:trim>
						<gbif:highlight keyword="${searchString}" cssClass="match">
							<string:capitalize><spring:message code="country.${country.isoCountryCode}"/></string:capitalize>
						</gbif:highlight>
					</string:trim></a>
					</span>
					<c:if test="${fn:toUpperCase(searchString)!=fn:toUpperCase(country.name) && fn:toUpperCase(country.name)!=fn:toUpperCase(country.interpretedFrom)}">
					<span class="interpretedFrom">
					(<spring:message code="interpreted.from" text="interpreted from"/>"<gbif:highlight keyword="${searchString}" cssClass="match"><gbif:capitalize>${country.interpretedFrom}</gbif:capitalize></gbif:highlight>")		
					</span>
					</c:if>
					</p>
	    </td>
	    <td class="tdColumn3">
	        <p class="column3">
	        	<c:if test="${not empty country.region}">
							<spring:message code="region.${country.region}"/>
						</c:if>
					</p>
	    </td>
	</tr>
</c:forEach>