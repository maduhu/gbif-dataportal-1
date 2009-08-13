<%@ include file="/common/taglibs.jsp"%>
<a href="http://cumuseum.colorado.edu/"><img style="float:right;" src="${pageContext.request.contextPath}/images/uni_colorado.jpg"/></a>
<a href="http://www.cria.org.br/"><img style="float:right; right:200px; padding-right:30px;" src="${pageContext.request.contextPath}/images/logo_cria.gif"/></a>
<h4>
<c:choose>
  <c:when test="${empty param['img']}">Create Niche Model</c:when>
  <c:otherwise>Your Generated Niche Model</c:otherwise>
</c:choose>
</h4>
<p>
This tools provides an integration between <a href="http://openmodeller.sourceforge.net">openModeller</a>
and the occurrence point data available within the portal.<br/>
Its main purpose is to demonstrate the integration of GBIF-mediated occurrence data with other applications like modelling tools.<br/> 
Please be aware that the significance of the modelling result varies greatly with the selection of meaningful parameters.
<br/>
</p>
<p>
This integration takes the occurrence points provided by your search, submits these points
and the layers selected to openModeller.<br/>
openModeller then generates a probability distribution using the 
<b><a href="http://openmodeller.sourceforge.net/index.php?option=com_content&task=view&id=61&Itemid=4">Envelope Score Algorithm</a></b>.
</p>
<!-- 
<p>
In this model, any point can be classified as:<br/><br/>
<b>Suitable</b>: if all associated environmental values fall within the calculated envelopes;<br/>
<b>Marginal</b>: if one or more associated environmental value falls outside the calculated envelope, but still within the upper and lower limits.<br/>
<b>Unsuitable</b>: if one or more associated environmental value falls outside the upper and lower limits.<br/>
Bioclim's categorical output is mapped to probabilities of 1.0, 0.5 and 0.0 respectively.<br/>
</p>
 -->

<c:if test="${not empty param['img']}">
<div id="generatedImage" style="margin-bottom:10px;">
<table>
<tr>
	<td>
		<a href="${pageContext.request.contextPath}/download/${param['img']}" style="border:none;">
		<img class="generatedModel" src="${pageContext.request.contextPath}/download/${param['img']}"/>
		</a>
	</td>
	<td style="padding-left:10px; padding-right:5px;vertical-align: top; text-align: center; font-size: 10px;">
	  <span style="color: gray;">Probability Scale</span>
	  <table style="border: 1px #CCCCCC solid; padding:5px;margin-top:5px;">
	   <tr>
		   <td><img src="${pageContext.request.contextPath}/images/nicheModellingScale.gif"/></td>
		   <td style="vertical-align: top;">
			   <table style="height:100%; color: gray;">
			    <tr><td style="vertical-align: top; height:60px;">High probability</td></tr>
			    <tr><td style="vertical-align: bottom; height:60px;padding-bottom:5px;">Low probability</td></tr>
			   </table>
			 </td>
		  </tr> 
    </table>
	</td>
</tr>
</table>
</div>
</c:if>

<div id="downloadFields">
<form name="mashupForm" action="${pageContext.request.contextPath}/occurrences/createModel.htm" method="GET">
<!-- 
  <p style="float:right; margin-right:20px; border: 1px gray solid; padding :5px; width:250px;">
		<b>Template:</b> Specifies the cell size, projection and reference 
		system of the distribution map.
		<br/>    
  </p>
 -->  
  <h5>Please select environment to model within</h5>
  <script type="text/javascript">
    function toggleLayers(showLayers, hideLayers){
      document.getElementById(showLayers).className='modellingLayers';
      document.getElementById(hideLayers).className='hidden';
      
      var hiddenInputs = document.getElementById(hideLayers);
      var inputs = hiddenInputs.getElementsByTagName("INPUT");
      for(var i=0; i<inputs.length; i++){
        inputs[i].disabled=true;
      }
      
      var activeInputs = document.getElementById(showLayers);
      var inputs = activeInputs.getElementsByTagName("INPUT");
      for(var i=0; i<inputs.length; i++){
        inputs[i].disabled=false;
      }     
      
      document.getElementById(showLayers+"Selector").className = "selectedLayerSet";
      document.getElementById(hideLayers+"Selector").className = "nonSelectedLayerSet";
      document.getElementById('selectedLayers').value = showLayers;
    }
  </script>
  
  <ul class="genericList">
    <li id="landLayersSelector" class="${empty param['selectedLayers'] || param['selectedLayers']=='landLayers' ? 'selectedLayerSet' : 'nonSelectedLayerSet'}"><a href="javascript:toggleLayers('landLayers','marineLayers');">Land</a></li>
    <li id="marineLayersSelector" class="${param['selectedLayers']=='marineLayers' ? 'selectedLayerSet' : 'nonSelectedLayerSet'}"><a href="javascript:toggleLayers('marineLayers','landLayers');">Ocean</a></li>
    <input id="selectedLayers" type="hidden" name="selectedLayers" value="${param['selectedLayers']}"/>
  </ul>
  
  <fieldset>
	    <table id="landLayers" class="${empty param['selectedLayers'] || param['selectedLayers']=='landLayers' ? 'modellingLayers' : 'hidden'}">
        <thead>
          <th valign="top">
            Land Layers - provided by <a href="http://www.worldclim.org/">Worldclim</a>
          </th>
          <th>  
            <img style="height:40px;" src="${pageContext.request.contextPath}/images/mvz_logo.jpg"/>
          </th>
        </thead>
        <tbody> 	    
	      <tr>
	       <td colspan="2"><gbiftag:selectAll fieldsetId="landLayers"/></td>
	      </tr> 
	      <tr>
		      <td>
            <table>		      
				      <c:forEach items="${landLayers}" var="layer" varStatus="layerStatus">
				        <c:if test="${layerStatus.index mod 12==0 && layerStatus.index!=0}">
					          </table>
				  	      </td>
				  	      <td>
				  	         <table>     
				        </c:if>
                <tr>
	                <td><input type="checkbox" name="layer" value="${layer.id}" <c:if test="${not empty param['selectedLayers'] && param['selectedLayers']!='landLayers'}">disabled="true"</c:if> <gbif:listContains list="${selectedLayers}" object="${layer.id}">checked="true"</gbif:listContains><c:if test="${empty selectedLayers || param['selectedLayers']=='marineLayers'}">checked="true"</c:if>/></td>
	                <td><spring:message code="${layer.id}" text="${layer.id}"/></td>
                </tr>
				      </c:forEach>
			      </table>
		      </td>
	      </tr>
	      </tbody> 
	     </table>
      <table id="marineLayers" class="${param['selectedLayers']=='marineLayers' ? 'modellingLayers' : 'hidden'}">
        <thead>
	        <th valign="top">
	          Ocean Layers - provided by <a href="http://www.incofish.org/">IncoFish Project</a>
	        </th>
	        <th>
	          <img src="${pageContext.request.contextPath}/images/incofish_logo.gif"/>
	        </th>
        </thead>
        <tbody>       
	        <tr>
	         <td colspan="2"><gbiftag:selectAll fieldsetId="marineLayers"/></td>
	        </tr> 
	        <tr>
	          <td>
	            <table>         
	              <c:forEach items="${marineLayers}" var="layer" varStatus="layerStatus">
	                <c:if test="${layerStatus.index mod 12==0 && layerStatus.index!=0}">
	                    </table>
	                  </td>
	                  <td>
	                     <table>     
	                </c:if>
	                <tr>
	                  <td><input type="checkbox" name="layer" value="${layer.id}" <c:if test="${param['selectedLayers']!='marineLayers'}">disabled="true"</c:if> <gbif:listContains list="${selectedLayers}" object="${layer.id}">checked="true"</gbif:listContains><c:if test="${empty selectedLayers || param['selectedLayers']=='landLayers' || empty param['selectedLayers']}">checked="true"</c:if>/></td>
	                  <td><spring:message code="${layer.id}" text="${layer.id}"/></td>
	                </tr>
	              </c:forEach>
	            </table>
	          </td>
	        </tr>
        <tbody>
       </table>	     
    </fieldset>     
    <input type="hidden" name="mask" value="prec_1"/>
    <input type="hidden" name="template" value="biod_5_15"/>
    <input type="hidden" name="criteria" value="<gbif:criteria criteria="${criteria}" urlEncode="true"/>"/>
    <input type="submit" name="model" value="Create Model">	
 </form>
</div>