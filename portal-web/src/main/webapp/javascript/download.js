/** Functions to assist download */

function pollforFile(ajaxUrl){
	
	var ajaxCallback = {
		success:function(o){ 
			//if not empty redirect to page			
			if(o.responseText.length>0){
				document.location = o.responseText;
			} else {
				setTimeout("pollforFile('"+ajaxUrl+"')",10000);
			}
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