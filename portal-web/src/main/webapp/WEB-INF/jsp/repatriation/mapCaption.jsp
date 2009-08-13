<%@ include file="/common/taglibs.jsp"%>
<div id="mapCaption">

	<div id="statistics">
		<tiles:insert page="statistics.jsp"/>
		<tiles:insert page="statisticsActions.jsp"/>
		<ul class="genericList">
			<li><a href="javascript:toggleView('breakdown', 'statistics');">Show breakdown by kingdom</a></li>
		</ul>	
	</div><!-- statistics -->
	
	<div id="breakdown" style="display:none;">
		<tiles:insert page="breakdown.jsp"/>
		<ul class="genericList">		
			<li><a href="javascript:toggleView('statistics', 'breakdown');">Back to statistics</a></li>
		</ul>	
	</div><!-- breakdown -->
	
	<p style="margin-top:20px;">
		Note: Map only shows georeferenced records that are deemed to have 
		valid coordinates and hence is not necessarily a full representation of the data available.
	</p>
	
</div><!-- mapCaption -->	