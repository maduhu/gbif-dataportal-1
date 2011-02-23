<%@ include file="/common/taglibs.jsp"%>

<%// Resource networks %>
<c:forEach items="${resourceNetworks}" var="resourceNetwork" varStatus="status">
        <tr valign="top">
            <td class="tdColumn1" >
                <p class="column1"><spring:message code="dataset.network"/></p>
            </td>
            <td class="tdColumn2" >
                <p class="column2">
                	<span class="speciesName">
				 		<a href="${pageContext.request.contextPath}/datasets/network/${resourceNetwork.key}/"><string:trim>
							<gbif:highlight keyword="${searchString}" cssClass="match" matchAnyPart="true">
								<string:truncateNicely lower="100" upper="120">
										<gbiftag:resourceNetworkPrint resourceNetwork="${resourceNetwork}"/>
								</string:truncateNicely>		
							</gbif:highlight>
						</string:trim></a>
					</span>
				</p>
            </td>
            <td class="tdColumn3" >
                <p class="column3">
					Resource network<c:if test="${resourceNetwork.dataResourceCount!=null}"> with ${resourceNetwork.dataResourceCount} resources</c:if>
				</p>
            </td>
        </tr>
</c:forEach>

<%// Data Providers %>
<c:forEach items="${dataProviders}" var="dataProvider" varStatus="status">
        <tr valign="top">
            <td class="tdColumn1" >
                <p class="column1"><spring:message code="dataset.provider"/></p>
            </td>
            <td class="tdColumn2" >
                <p class="column2">
                	<span class="speciesName">
						<a href="${pageContext.request.contextPath}/datasets/provider/${dataProvider.key}/"><string:trim>
							<gbif:highlight keyword="${searchString}" cssClass="match" matchAnyPart="true">
								<string:truncateNicely lower="80" upper="100">
										${dataProvider.name}
								</string:truncateNicely>		
							</gbif:highlight>
						</string:trim></a>
					</span>
				</p>
            </td>
            <td class="tdColumn3" >
                <p class="column3">
					Data publisher<c:if test="${dataProvider.dataResourceCount!=null}"> with ${dataProvider.dataResourceCount} resources</c:if>
				</p>
            </td>
        </tr>
</c:forEach>

<%// Data Resources %>
<c:forEach items="${dataResources}" var="dataResource" varStatus="status">
        <tr valign="top">
            <td class="tdColumn1" >
                <p class="column1"><spring:message code="dataset.resource"/></p>
            </td>
            <td class="tdColumn2" >
                <p class="column2">
                	<span class="speciesName">
						<a href="${pageContext.request.contextPath}/datasets/resource/${dataResource.key}/"><string:trim>
							<gbif:highlight keyword="${searchString}" cssClass="match" matchAnyPart="true">
								<string:truncateNicely lower="100" upper="120">
										${dataResource.name}
								</string:truncateNicely>		
							</gbif:highlight>
						</string:trim></a>
					</span>
				</p>
            </td>
            <td class="tdColumn3" >
                <p class="column3">
					Dataset from ${dataResource.dataProviderName}
				</p>
            </td>
        </tr>
</c:forEach>