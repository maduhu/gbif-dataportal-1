<%@ include file="/common/taglibs.jsp"%>
<table id="repatTable" class="repat">
  <tbody>
  <tr class="topLabels">
    <td style="padding:0;">
      <img src="${pageContext.request.contextPath}/images/hostCountryLegend.gif"/>
    </td>
    <c:forEach items="${hosts}" var="host" varStatus="hostIndex">
    	<c:if test="${host!='TW' && host!='UK' && host!='CN' && host!='HK' && host!='IL'}">   
        <td id="host-${host}" class="hostLabel"
          <c:choose>
            <c:when test="${host!='XX'}">title="<spring:message code="country.abbr.${host}" text="${host}"/>"</c:when>
            <c:otherwise>title="<spring:message code="repat.networks"/>"</c:otherwise>
          </c:choose>><string:trim>
          <c:choose>
            <c:when test="${host!='XX'}">
               <c:if test="${showFlags}"><img class="flag" src="${pageContext.request.contextPath}/images/flags/<string:lowerCase>${host}</string:lowerCase>.gif"/></c:if>
               <p><string:trim>
               <c:choose>
                 <c:when test="${showIso}">${host}</c:when>
                 <c:otherwise><spring:message code="country.abbr.${host}" text="${host}"/></c:otherwise>  
               </c:choose>
               </string:trim></p>
            </c:when>
            <c:otherwise>
              <spring:message code="repat.abbr.intl.networks.line.separated"/>
            </c:otherwise>
          </c:choose>  
        </string:trim></td>
        </c:if>
    </c:forEach>   
    <c:if test="${!countrySelected && !hostSelected}"><td>&nbsp;</td></c:if>  
  </tr>
  <c:forEach items="${countries}" var="country" varStatus="countryIndex">
    <c:if test="${country.isoCountryCode!='TW' && country.isoCountryCode!='UK' && country.isoCountryCode!=null && (!countrySelected || param['country']==country.isoCountryCode)}">
    <tr class="rowEntry">
      <td id="country-${country.isoCountryCode}" class="leftLabel" title="<spring:message code="country.abbr.${country.isoCountryCode}" text="${country.isoCountryCode}"/>">
       <c:if test="${showFlags && country.isoCountryCode!='XX'}"><img src="${pageContext.request.contextPath}/images/flags/<string:lowerCase>${country.isoCountryCode}</string:lowerCase>.gif"/></c:if>
       <string:trim>      
       <c:choose>
         <c:when test="${showIso}">
           <c:choose>
             <c:when test="${country.isoCountryCode=='XX'}">
              <spring:message code="country.abbr.${country.isoCountryCode}" text="${country.isoCountryCode}"/>
             </c:when>
             <c:otherwise>
                 ${country.isoCountryCode}
             </c:otherwise>  
           </c:choose>          
         </c:when>
         <c:otherwise>
           <spring:message code="country.abbr.${country.isoCountryCode}" text="${country.isoCountryCode}"/>
         </c:otherwise>  
       </c:choose>
      </string:trim></td>
       <c:set var="tagArray" value="${stats[country.isoCountryCode]}"/>
       <c:forEach items="${tagArray}" var="stat" varStatus="statIndex">
       	<c:if test="${hosts[statIndex.index]!='TW' && hosts[statIndex.index]!='UK' && hosts[statIndex.index]!='CN' && hosts[statIndex.index]!='HK' && hosts[statIndex.index]!='IL'}">
         <td id="${stat.fromEntityName}-${stat.toEntityName}"><string:trim>
            <c:choose>
                <c:when test="${selectedView=='concise'}">
                  <c:if test="${stat.count>0}">X</c:if>
                </c:when>
                <c:when test="${selectedView=='percent'}">
                   <c:if test="${not empty stat.count && stat.count>0 && country.occurrenceCount>0}">
                    <c:choose>
                    <c:when test="${(stat.count/country.occurrenceCount)*100>=0.1}"> 
                      <span
                        <c:choose>
                         <c:when test="${(stat.count/country.occurrenceCount)>0.75}">class="percent75"</c:when>                        
                         <c:when test="${(stat.count/country.occurrenceCount)>0.50}">class="percent50"</c:when>
                         <c:when test="${(stat.count/country.occurrenceCount)>0.25}">class="percent25"</c:when>
                         <c:when test="${(stat.count/country.occurrenceCount)>0.01}">class="percent1"</c:when>
                         <c:when test="${(stat.count/country.occurrenceCount)<0.01}">class="percent01"</c:when>
                        </c:choose> 
                      >
                      <fmt:formatNumber type="percent" minFractionDigits="1">${stat.count/country.occurrenceCount}</fmt:formatNumber>
                      </span>
                    </c:when>
                    <c:otherwise>
                    <span class="percent01">&lt;0.1%</span>
                    </c:otherwise>
                    </c:choose> 
                  </c:if>
                </c:when>               
                <c:otherwise>
                  <fmt:formatNumber pattern="###,###">${stat.count}</fmt:formatNumber>
                </c:otherwise>
            </c:choose>      
          </string:trim></td></c:if></c:forEach>   
      <c:if test="${!countrySelected && !hostSelected }"> 
        <td class="rightLabel"><string:trim>
         <c:choose>
           <c:when test="${showIso}">
             <c:choose>
               <c:when test="${country.isoCountryCode=='XX'}">
                <spring:message code="country.abbr.${country.isoCountryCode}" text="${country.isoCountryCode}"/>
               </c:when>
               <c:otherwise>
                   ${country.isoCountryCode}
               </c:otherwise>  
             </c:choose> 
           </c:when>
           <c:otherwise>
             <spring:message code="country.abbr.${country.isoCountryCode}" text="${country.isoCountryCode}"/>
           </c:otherwise>  
         </c:choose>
         </string:trim>
         <c:if test="${showFlags && country.isoCountryCode!='XX'}"><img class="flag" src="${pageContext.request.contextPath}/images/flags/<string:lowerCase>${country.isoCountryCode}</string:lowerCase>.gif"/></c:if>         
        </td>
      </c:if>        
    </tr></c:if></c:forEach>
  <c:if test="${!countrySelected && !hostSelected}">
  <tr class="bottomLabels">
    <td>&nbsp;</td>
    <c:forEach items="${hosts}" var="host"><c:if test="${host!='TW' && host!='CN' && host!='UK' && host!='HK' && host!='IL'}"><td id="host-${host}" title="<spring:message code="country.abbr.${host}" text="${host}"/>"><string:trim>
          <c:choose>
            <c:when test="${host!='XX'}">
              <p><string:trim>
               <c:choose>
                 <c:when test="${showIso}">
                  ${host}
                 </c:when>
                 <c:otherwise>
                    <spring:message code="country.abbr.${host}" text="${host}"/>
                 </c:otherwise>  
               </c:choose>
               </string:trim></p>
               <c:if test="${showFlags}"><img src="${pageContext.request.contextPath}/images/flags/<string:lowerCase>${host}</string:lowerCase>.gif"/></c:if>
            </c:when>
            <c:otherwise>
              <spring:message code="repat.abbr.intl.networks.line.separated"/>
            </c:otherwise>
          </c:choose>  
        </string:trim></td></c:if></c:forEach>  
    <td>&nbsp;</td>  
  </tr>
  </c:if>
  </tbody>  
</table>