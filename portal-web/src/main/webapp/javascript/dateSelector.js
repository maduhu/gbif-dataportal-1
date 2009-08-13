var month30 = new Array (4, 6, 9, 11);
var february = 2;	

function validateDayListFromMonthSelector(dataSelectorDiv){

  var daySelect = dataSelectorDiv.getElementsByTagName("SELECT")[0];
  var monthSelect = dataSelectorDiv.getElementsByTagName("SELECT")[1];
  var yearInput = dataSelectorDiv.getElementsByTagName("INPUT")[0];
  validateDayListFromMonth(daySelect, monthSelect, yearInput); 
}

/**
 * Validates the date list for the selected month
 */
function validateDayListFromMonth(daySelect, monthSelect, yearInput){

	var month = monthSelect.value;
	var currentDayCount = daySelect.getElementsByTagName("OPTION").length;
		
	if(month==february){
	
		if(isLeapYear(yearInput.value)){
			if(currentDayCount!=29){
				if(currentDayCount>29){
					removeDayOptions(daySelect, currentDayCount-29);
				} else {
					addDayOptions(daySelect, currentDayCount+1,29-currentDayCount);
				}
			}
		} else {
			//check if year is set and if it is a leap year
			if(currentDayCount!=28){
				removeDayOptions(daySelect, currentDayCount-28);				
			}
		}
		
	} else if (isInArray(month30, month) && currentDayCount!=30){
		
		if(currentDayCount>30){
			removeDayOptions(daySelect, currentDayCount-30);
		} else {
			addDayOptions(daySelect, currentDayCount+1,30-currentDayCount);
		}
	} else if (currentDayCount!=31){
		//add day options
			addDayOptions(daySelect, currentDayCount+1,31-currentDayCount);
	}
}

/**
 * Add Day options
 */
function addDayOptions(daySelect, startIndex, dayCount){
	for(var i=startIndex; i< (dayCount+startIndex); i++){
		daySelect.appendChild(new Option(i, i));
	}
}

/**
 * Add Month options
 */
function removeDayOptions(daySelect, numberToRemove){
	var options = daySelect.getElementsByTagName("OPTION");	
	var lastEntry = options.length-1;
	for(var i=0; i<numberToRemove; i++){
		daySelect.removeChild(options[lastEntry]);
		lastEntry = lastEntry-1;
	}
}

/**
 * Returns true if the supplied testValue is in the array.
 */
function isInArray (testArray, testValue){
	for(var i=0; i<testArray.length; i++){
		if(testArray[i]==testValue)	
			return true;
	}
	return false;
}

/**
 * Returns true if the supplied testValue is in the array.
 */
function isLeapYear(year){
	if (
		year % 4 == 0 
		|| ((year % 100 != 0) && (year % 400 == 0))
		)
		return true	
	return false;
}