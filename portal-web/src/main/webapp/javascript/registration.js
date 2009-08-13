/**
 * Takes options from select element and appends to another
 */
function addSelectedOption(sourceSelectId,destinationSelectId){
	
	var sourceSelect = document.getElementById(sourceSelectId);
	var destinationSelect = document.getElementById(destinationSelectId);
	var selectedOption = sourceSelect.options[sourceSelect.selectedIndex];
	
	if(destinationSelect.options.length>0)	{
		var notFound=true;
		for (var i=0; i<destinationSelect.options.length && notFound;i++){
			if(destinationSelect.options[i].innerHTML>selectedOption.innerHTML){
				destinationSelect.insertBefore(selectedOption, destinationSelect.options[i]);		
				notFound=false;
			}
		}
		if(notFound){
			destinationSelect.appendChild(selectedOption);		
		}
	} else {
		destinationSelect.appendChild(selectedOption);
	}
}

/**
 * Sets the value of a multi select select element into a hidden element
 * in a comma separated format.
 */
function resetHidden(selectElementId, hiddenElementId){
	
	var selectElement = document.getElementById(selectElementId);
	var hiddenElement = document.getElementById(hiddenElementId);
	
	var value = "";
	
	for (var i=0; i<selectElement.options.length; i++){
		if(i>0)	{
			value = value +",";
		}
		value = value+selectElement.options[i].value;	
	}
	hiddenElement.value = value;
}

/**
 * 
 */
function setDateFromSelectElements(dateSelectId, hiddenElementId){
	var day = document.getElementById(dateSelectId+'_day');
	var month = document.getElementById(dateSelectId+'_month');
	var year = document.getElementById(dateSelectId+'_year');		
	document.getElementById(hiddenElementId).value=day+month+year;
}