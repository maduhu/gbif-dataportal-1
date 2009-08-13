function openTaxonomyTreeAtConcept(url, containerDivId, postCallback){
	var callback = {
		success:function(o){
			document.getElementById(containerDivId).innerHTML=o.responseText;
			postCallback.post();
		},	
		failure: function(o){
			postCallback.post();
		}
	}	
	//make the ajax request for the wizard
	YAHOO.util.Connect.asyncRequest('GET',
		url, 
		callback, 
		null); 
}

