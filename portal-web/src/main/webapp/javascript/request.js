/** Functions to assist requesting data from providers */

function requestDataFromProvider(ajaxUrl){
		
	var ajaxCallback = {
		success:function(o){ 
			document.location = o.responseText;
		},
		failure:function(o){
			document.location = document.location;
		}
	}
	
	YAHOO.util.Connect.asyncRequest('GET',
		ajaxUrl, 
		ajaxCallback, 
		null); 	
}