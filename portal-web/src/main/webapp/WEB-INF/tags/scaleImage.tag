<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="imageUrl" required="true" rtexprvalue="true" %>
<%@ attribute name="maxWidth" required="true" rtexprvalue="true" type="java.lang.Float" %>
<%@ attribute name="maxHeight" required="true" rtexprvalue="true" type="java.lang.Float" %>
<%@ attribute name="addLink" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="imgClass" required="false" rtexprvalue="true" %>
<%@ attribute name="linkClass" required="false" rtexprvalue="true"%>
<c:choose>
	<c:when test="${fn:endsWith(imageUrl, 'mpg') || fn:endsWith(imageUrl, 'mpeg') }">
		<embed src="${imageUrl}" autostart="false" controller="true" controls="console" />
	</c:when>
	<c:otherwise>
		<%
			int[] imageDimensions = null;
			try{
				imageDimensions = org.gbif.portal.util.image.ImageUtils.findImageDimensions(imageUrl);
				float imgWidth = (float) imageDimensions[0];
				float imgHeight = (float) imageDimensions[1];
				
				float scale = 1;
				
				if(imgHeight>maxHeight){
					scale = maxHeight/imgHeight;
					imgHeight=maxHeight;
					imgWidth=imgWidth*scale;
				}
				
				if(imgWidth>maxWidth){				
					scale = maxWidth/imgWidth;
					imgWidth=maxWidth;
					imgHeight=imgHeight*scale;					
				}
				
				request.setAttribute("renderImage", true);				
				request.setAttribute("imgWidth", (int) imgWidth);
				request.setAttribute("imgHeight", (int) imgHeight);					
			} catch (Exception e){
				//probably due to a problem accessing the supplied url			
			}	
		%>
		<c:if test="${renderImage!=null && renderImage==true}">
			<c:if test="${addLink==null || addLink==true}"><a href="${imageRecord.url}" ></c:if>
				<img src="${imageUrl}"
					width="${imgWidth}"
					height="${imgHeight}"
					<c:if test="${not empty imgClass}">class="${imgClass}"</c:if>
				/>
			<c:if test="${addLink==null || addLink==true}"></a></c:if>
		</c:if>	
	</c:otherwise>
</c:choose>