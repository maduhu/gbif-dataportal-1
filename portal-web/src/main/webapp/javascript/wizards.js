var mousePos;
var rootSelected=false;
//actual lat long values
var rootX;
var rootY;
var boxWidthInPx = 1;
var boxHeightInPx = 1;
var mapImgName ="";
var filterId;
var boundingBoxImg;

var onSelectStartVar;
var mozUserSelectVar;
var onMouseDownVar;
var browserType;
var ie =0;
var ff =1;
var other =2;
var selectedBoundingBox = null;

var currentLatitude;
var currentLongitude;

//these are the values that are the displayed latitude and longitude when the user clicks
var selectedRootX;
var selectedRootY;
var selectedPointX;
var selectedPointY;

/********* Bounding box wizard ********/

/**
 * Disables selection (highlighting) of elements on the page.
 */
function disableSelection(){
	if (typeof document.body.onselectstart!="undefined"){ //IE route
		browserType =ie;
		onSelectStartVar = document.body.onselectstart;
		 document.body.onselectstart=function(){return false;}
	} else if (typeof document.body.style.MozUserSelect!="undefined"){ //Firefox route
		browserType =ff;
		mozUserSelectVar = document.body.style.MozUserSelect;
		 document.body.style.MozUserSelect="none";
	} else { //All other route (ie: Opera)
		browserType =other;
		onMouseDownVar=document.body.onmousedown;
		 document.body.onmousedown=function(){return false;}
	}
//	document.body.style.cursor = "default";
}

/**
 * Enables selection of elements on the page.
 */
function enableSelection(){
	if (browserType==ie){ //IE route
		 document.body.onselectstart=onSelectStartVar;
	} else if (browserType==ff) {//Firefox route
		 document.body.style.MozUserSelect=mozUserSelectVar;
	} else { //All other route (ie: Opera)
		 document.body.onmousedown=onMouseDownVar;
	}
}

/**
 * Creates an overlay div for an image.
 */
function setOverlayDiv(mapImgId, overlayDivId){
	document.getElementById(overlayDivId).style.top=(findPos(document.getElementById(mapImgId)).y-30)+"px";
	document.getElementById(overlayDivId).style.left=(findPos(document.getElementById(mapImgId)).x -30)+"px";
	document.getElementById(overlayDivId).style.width=(document.getElementById(mapImgId).offsetWidth+60)+"px";
	document.getElementById(overlayDivId).style.height=(document.getElementById(mapImgId).offsetHeight+60)+"px";
	document.getElementById(overlayDivId).style.visibility = "visible";
}

/**
 * Sets the bounding box for to the current selection.
 */
function setBoundingBox(filterIndex){

	rootSelected=false;
	//-4 is to account for the 2px border on the div
	var mapHeight = document.getElementById(mapImgName).height;
	var mapWidth= document.getElementById(mapImgName).width;
	boxHeightInDegrees = Math.round((boxHeightInPx+4)/(mapHeight/180));
	boxWidthInDegrees = Math.round((boxWidthInPx+4)/(mapWidth/360));		
	
	//set the coordinates for the selector
	setCoordinatesForMapSelector();
	
	//construct the bounding box string		
	var wc = Math.abs(westCoordinate)+(westCoordinate>0?'E':'W');
	var ec = Math.abs(eastCoordinate)+(eastCoordinate>0?'E':'W');
	var nc = Math.abs(northCoordinate)+(northCoordinate>0?'N':'S');
	var sc = Math.abs(southCoordinate)+(southCoordinate>0?'N':'S');
	
	var wcd = Math.abs(westCoordinate)+"&deg; "+(westCoordinate>0?'E':'W');
	var ecd = Math.abs(eastCoordinate)+"&deg; "+(eastCoordinate>0?'E':'W');
	var ncd = Math.abs(northCoordinate)+"&deg; "+(northCoordinate>0?'N':'S');
	var scd = Math.abs(southCoordinate)+"&deg; "+(southCoordinate>0?'N':'S');	
	
	//get the link
	selectedBoundingBox = wc+","+sc+","+ec+","+nc;
	var displayBoundingBox = wcd+", "+scd+", "+ecd+", "+ncd;
	setWizardValues(filterIndex, selectedBoundingBox, displayBoundingBox);
}

function setCoordinatesForMapSelector(){
	
	if(selectedPointX==undefined)
		selectedPointX=selectedRootX;
	if(selectedPointY==undefined)
		selectedPointY=selectedRootY;
	
	northCoordinate = selectedRootY> selectedPointY ? selectedRootY:selectedPointY;
	southCoordinate = selectedRootY< selectedPointY ? selectedRootY:selectedPointY;
	eastCoordinate = selectedRootX> selectedPointX ? selectedRootX:selectedPointX;
	westCoordinate = selectedRootX< selectedPointX ? selectedRootX:selectedPointX;
	
	document.getElementById("northCoordinate").innerHTML=northCoordinate	
	document.getElementById("southCoordinate").innerHTML=southCoordinate;
	document.getElementById("westCoordinate").innerHTML=westCoordinate;
	document.getElementById("eastCoordinate").innerHTML=eastCoordinate;			
}

function checkForMouseUp(ev){

if(rootSelected){

}

}


/**
 * Sets the point to pivot around.
 */
function setSelectorRoot(mapImgElementName, theFilterId){

	selectedRootX = currentLongitude;
	selectedRootY = currentLatitude;

	filterId = theFilterId;
	mapImgName=mapImgElementName;
	//get coordinates
	var mapLeft = findPos(document.getElementById(mapImgName)).x;
	var mapWidth = document.getElementById(mapImgName).width;
	var mapTop = findPos(document.getElementById(mapImgName)).y;
	var mapHeight = document.getElementById(mapImgName).height;
	
	var mapBottom = mapTop + mapHeight -3;
	var mapRight = mapLeft + mapWidth -3;
	
	//alert(mapLeft+":"+mapTop+","+mapRight+":"+mapBottom);
	
	var x=Math.floor(mousePos.x);
	var y=Math.floor(mousePos.y);		
	
	document.getElementById("mapSelector").style.left = x+"px";
	document.getElementById("mapSelector").style.top = y+"px";
	document.getElementById("mapSelector").style.right = x+"px";
	document.getElementById("mapSelector").style.bottom = y+"px";
	document.getElementById("mapSelector").style.width="0px";
	document.getElementById("mapSelector").style.height="0px";
	
	//alert(x+":"+y);
	
	//sanity checks
	if(x<mapLeft){
		x = mapLeft;
		document.getElementById("mapSelector").style.left = x+"px";	
	}
	if(y<mapTop){
		y = mapTop;
		document.getElementById("mapSelector").style.top = y+"px";
	}

	if(x>mapRight){
		x = mapRight;
		document.getElementById("mapSelector").style.left = x+"px";	
	}
	if(y>mapBottom){
		y = mapBottom;
		document.getElementById("mapSelector").style.top = y+"px";
	}	

		
	//reset	
	boxWidthInPx = 0;
	boxHeightInPx = 0;
	//minX and minY are the relative positions from on the map img	
	minX = x-mapLeft;
	minY = y-mapTop;
	//rootX and rootY are the actual position on the page
	rootX = x;
	rootY = y;
	//set root
	rootSelected=true;
	
	//add event mouse up event handler
	
}

/**
 * Sets the bounding box if root selected.
 */
function setSelectorArea(){
	if(rootSelected){
		rootSelected=false;
		setBoundingBox(filterId);
		return;
	}
}


function changeSelectedArea(){
	if(!rootSelected)
		return;
				
	//record the second selected point
	selectedPointX = currentLongitude;
	selectedPointY = currentLatitude;			
				
	var x=Math.round(mousePos.x);
	var y=Math.round(mousePos.y);

	//check boundarys -4 is to account for the 2px border on the div
	var mapImg = document.getElementById(mapImgName);
	var imgPos = findPos(mapImg);
	var mapLeft =imgPos.x;
	var mapTop= imgPos.y;	
	var mapRight = imgPos.x +mapImg.offsetWidth-4;
	var mapBottom = imgPos.y+mapImg.offsetHeight-4;
	
	// -4 for the div border (2px either side)
	boxWidthInPx = Math.abs(x - rootX);
	boxHeightInPx = Math.abs(y - rootY);	
	
	var mapSelector = document.getElementById("mapSelector");
	
	if(x>rootX){
		//swap left and right
		mapSelector.style.left = rootX+"px";
		mapSelector.style.right = mousePos.x+"px" ;
		minX = rootX-mapLeft;
	} else {
		mapSelector.style.right = rootX+"px" ;
		mapSelector.style.left = mousePos.x+"px";
		minX = rootX - mapLeft -  boxWidthInPx;
	}

	if(y<rootY){
		//swap top and bottom
		mapSelector.style.bottom = rootY+"px";
		mapSelector.style.top = mousePos.y+"px" ;
		minY = rootY-mapTop-boxHeightInPx;
	} else {
		mapSelector.style.top = rootY+"px" ;
		mapSelector.style.bottom = mousePos.y+"px";
		minY = rootY-mapTop;
	}

	if(x>rootX){
		if((boxWidthInPx + rootX)>mapRight)
			boxWidthInPx = mapRight - rootX;
	} else {
		if((rootX - boxWidthInPx)<=mapLeft){
			boxWidthInPx = rootX - mapLeft;
			mapSelector.style.left = mapLeft+"px";
		}
	}

	if(y>rootY){	
		if((boxHeightInPx+rootY)>mapBottom){
			boxHeightInPx = mapBottom-rootY;
		}	
	} else {
		if(Math.abs(boxHeightInPx-rootY)<mapTop){
			boxHeightInPx = rootY - mapTop;
			mapSelector.style.top = mapTop+"px";			
		}		
	}	
		
	boxWidthInPx = Math.abs(boxWidthInPx);
	boxHeightInPx = Math.abs(boxHeightInPx);

	//set selector width/height
	mapSelector.style.width=boxWidthInPx+"px";
	mapSelector.style.height=boxHeightInPx+"px";
	
	//-4 is to account for the 2px border on the div
	var mapHeight = document.getElementById(mapImgName).height;
	var mapWidth= document.getElementById(mapImgName).width;
	boxHeightInDegrees = Math.round((boxHeightInPx+4)/(mapHeight/180));
	boxWidthInDegrees = Math.round((boxWidthInPx+4)/(mapWidth/360));		
	
	//set the coordinates for the selector
	setCoordinatesForMapSelector();
}

function trackMouse(ev){
	if (capture) {
		ev = ev || window.event;
		mousePos = mouseCoords(ev);
		setLatLongTable(mousePos);
		if(rootSelected){
			changeSelectedArea();
		}
	}
}

/**
 * Sets the displayed latitude and longitude for the mouse cursor.
 */
function setLatLongTable(mousePos){
	
	var mapHeight = document.getElementById('boundingBoxMap').height;
	var mapWidth= document.getElementById('boundingBoxMap').width;
	var mapImg = document.getElementById('boundingBoxMap');
	var mapPos = findPos(mapImg);
	var mapLeft = mapPos.x;
	var mapTop = mapPos.y;

	minX = mousePos.x-mapLeft;
	minY = mousePos.y-mapTop;

	//set the currently selected lat/long
	currentLatitude = Math.round(90-(minY/ (mapHeight/180) ));
	currentLongitude = Math.round(-180+(minX/(mapWidth/360)));

	//sanity checks - latitude
	if(currentLatitude>90){
		currentLatitude=90;
	} else if(currentLatitude<-90){
			currentLatitude=-90;
	}

	//sanity checks - longitude
	if(currentLongitude>180){
		currentLongitude=180;
	} else if(currentLongitude<-180){
		currentLongitude=-180;
	}
	
	//set the display values
	document.getElementById("latitude").innerHTML=currentLatitude;
	document.getElementById("longitude").innerHTML=currentLongitude;
}

function mouseDown(ev){
	if (!e) var e = window.event;
	e.cancelBubble = true;
	if (e.stopPropagation) e.stopPropagation();	
}

function mouseClick(ev){
	if (!e) var e = window.event;
	e.cancelBubble = true;
	if (e.stopPropagation) e.stopPropagation();	
}

function mouseUp(ev){
	setSelectorArea();	
}


/****** Classification Wizard ******/

var classificationFilterCallback = {
	
	pre:function(conceptId, taxon, rankAndTaxon, rank, expand){ 	
		document.getElementById("progress").style.visibility="visible";
		if(conceptId!=null)
			setWizardValues(0, conceptId, rankAndTaxon);
		else
			resetWizardValues(0);
	},	
	post:function(conceptId, taxon, rankAndTaxon, rank, expand){
		document.getElementById("progress").style.visibility="hidden";
	}
}

/****** Occurrence Date Wizard ******/
/**
 * Tests for a valid year.
 */
function hasValidateYear(containerDiv){
	var startYear = containerDiv.getElementsByTagName("INPUT")[0].value;	
	return isValidateYear(startYear);
}

/**
 * Tests for a valid year.
 */
function isValidateYear(yearValue){
	yearValue = yearValue.replace(/^\s+|\s+$/g, '') ;
	for(var i=0; i<yearValue.length; i++){
		var character = parseInt(yearValue.charAt(i));
		if(character == null || isNaN(character) ||  character == 'NaN')
			return false;
	}
	var parsedInt = parseInt(yearValue);
	return (parsedInt !=null && !isNaN(parsedInt) && parsedInt!=0 && parsedInt>0);
}


function validateYearFields(){
	var startDateDiv = document.getElementById("startDate");
	var validYear = hasValidateYear(startDateDiv);
	if(!validYear){
		document.getElementById("dateWarnings").style.visibility="visible";
		return false;
	}
	var endDateDiv = document.getElementById("endDate");		
	validYear = hasValidateYear(endDateDiv);
	if(!validYear){
		document.getElementById("dateWarnings").style.visibility="visible";
		return false;
	}
	
	if(document.getElementById("dateWarnings")!=null){
	 document.getElementById("dateWarnings").style.visibility="hidden";
	}
	return true;
}

/**
 * Date selector wizard 
 */
function dateChanged(){
	
	//validate year fields
	var startDateDiv = document.getElementById("startDate");
	var endDateDiv = document.getElementById("endDate");		
	if(!validateYearFields()){
		return;
	}

	var singleDate = document.getElementById("singleDate");
	var displayDate, dateValue;
	
	if(singleDate.checked){
		dateValue = getSelectorDate(startDateDiv);
		displayDate = getSelectorDisplayDate(startDateDiv);
	} else {
		dateValue = getSelectorDate(startDateDiv)+"-"+getSelectorDate(endDateDiv);
		displayDate = "between " +getSelectorDisplayDate(startDateDiv) +" and "+ getSelectorDisplayDate(endDateDiv);
	}
	setWizardValues(0, dateValue, displayDate);
}

/**
 * Retrieves the date to display in 12 June 2005 format
 */
function getSelectorDisplayDate(containerDiv){
	var startDay = containerDiv.getElementsByTagName("SELECT")[0].value;
	var startMonthSelect = containerDiv.getElementsByTagName("SELECT")[1];
	var startYear = containerDiv.getElementsByTagName("INPUT")[0].value;
	var startMonthText = startMonthSelect.options[startMonthSelect.selectedIndex].innerHTML;
	var startDisplayDate=startDay+" "+startMonthText+" "+startYear;	
	return startDisplayDate;
}

/**
 * Retrieve the selected date in ddmmyyyy format
 */
function getSelectorDate(containerDiv){	
	var startDay = containerDiv.getElementsByTagName("SELECT")[0].value;
	var startMonth = containerDiv.getElementsByTagName("SELECT")[1].value;
	var startYear = containerDiv.getElementsByTagName("INPUT")[0].value;
	var startDate=startDay+startMonth+startYear;
	return startDate;
}

/**
 * Enable/Disable a date selector control.
 */
function toggleDateSelector(containerDivName, disabled){
	var containerDiv = document.getElementById(containerDivName);
	var selects = containerDiv.getElementsByTagName("SELECT");
	var inputs = containerDiv.getElementsByTagName("INPUT");
	if(disabled){
		selects[0].disabled=true;
		selects[1].disabled=true;
		inputs[0].disabled=true;
	} else {
		selects[0].disabled=false;
		selects[1].disabled=false;
		inputs[0].disabled=false;
	}
}

/**
 * Switches single date on/off
 */
function toggleSingleDate(setCheckedTo){
	document.getElementById('singleDate').checked=setCheckedTo;
}

/**
 * Sets the date select
 */
function setDateSelector(dateSelectPrefixId, day, month, year){

  var daySelect = document.getElementById(dateSelectPrefixId+"_day");
  var monthSelect = document.getElementById(dateSelectPrefixId+"_month");
  var yearInput = document.getElementById(dateSelectPrefixId+"_year");
  monthSelect.value = month;
  yearInput.value = year;
  validateDayListFromMonth(daySelect, monthSelect, yearInput);
	daySelect.value = day;
}

/**
 * TODO not i18n-y. But not sure if it makes sense to set the filter value for wizards.
 * Maybe confusing too user.
 */
function setRestrictions(){
	var georef = document.getElementById("georeferencedCheckBox").checked;
	var geoIssues = document.getElementById("geospatialIssuesCheckBox").checked;
	var taxonIssues = document.getElementById("taxonomyIssuesCheckBox").checked;		
	var value = georef+","+geoIssues+","+taxonIssues;
	var displayValue = "";
	if(georef)
		displayValue=displayValue+"georeferenced only";
	if(geoIssues){
		if(displayValue.length>0)
			displayValue=displayValue+", ";
		displayValue=displayValue+"no geospatial issues";
	}
	if(taxonIssues){
		if(displayValue.length>0)
			displayValue=displayValue+", ";
		displayValue=displayValue+"no taxonomy issues";
	}
	if(displayValue.length>0)
		setWizardValues(0, value, displayValue);
}

/********* Scientific name filter *********/

function findConcepts(ajaxUrl, inputNameId, containerToPopulate){
	var conceptName = document.getElementById(inputNameId).value;
	populateWithAjax(ajaxUrl+conceptName, containerToPopulate);
}

/********* Year range filter *********/

/**
 * Year selector wizard 
 */
function yearRangeChanged(){
	
	//validate year fields
	var startYear = document.getElementById("startYear").value;
	var endYear = document.getElementById("endYear").value;		
	if(!isValidateYear(startYear) || !isValidateYear(endYear)){
		return;
	}

	var	dateValue = startYear+"-"+endYear;
	var	displayDate = "between " +startYear +" and "+ endYear;
	setWizardValues(0, dateValue, displayDate);
}


/** Utilty methods */

/**
 * Populate a dropdown using the supplied ajax url and dropdown id.
 */
function populateWithAjax(ajaxUrl, containerToPopulateId){
	var ajaxCallback = {
		success:function(o){ 
			if(navigator.appName=="Microsoft Internet Explorer"){
				select_innerHTML(document.getElementById(containerToPopulateId),o.responseText);
			} else {
				document.getElementById(containerToPopulateId).innerHTML=o.responseText;
			}},
		failure: function(o){}
	};
	YAHOO.util.Connect.asyncRequest('GET',
		ajaxUrl, 
		ajaxCallback, 
		null); 	
}	

/**
 * Sets the wizard values from the supplied dropdown name.
 */
function setWizardValuesFromDropdown(dropdownName){
	var dropdown = document.getElementById(dropdownName);
	setWizardValues(0, dropdown.value, dropdown.options[dropdown.selectedIndex].innerHTML);
}
 
 
 
 
 /**
  * Fill a select with options using DOM rather than innerHTML (only for IE).
  */
 function select_innerHTML(objeto, innerHTML) {
         /******
          * select_innerHTML - corrige o bug do InnerHTML em selects no IE
          * Veja o problema em: http://support.microsoft.com/default.aspx?scid=kb;en-us;276228
          * Versão: 2.1 - 04/09/2007
          * Autor: Micox - Náiron José C. Guimarães - micoxjcg@yahoo.com.br
          * @objeto(tipo HTMLobject): o select a ser alterado
          * @innerHTML(tipo string): o novo valor do innerHTML
          *******/ 
         objeto.innerHTML = ""
         var selTemp = document.createElement("micoxselect")
         var opt;
         selTemp.id = "micoxselect1"
         document.body.appendChild(selTemp)
         selTemp = document.getElementById("micoxselect1")
         selTemp.style.display = "none"
         if (innerHTML.toLowerCase().indexOf("<option") < 0) {//se não é option eu converto
                 innerHTML = "<option>" + innerHTML + "</option>"
         }
         innerHTML = innerHTML.toLowerCase().replace(/<option/g, "<span").replace(
                         /<\/option/g, "</span")
         selTemp.innerHTML = innerHTML

         for ( var i = 0; i < selTemp.childNodes.length; i++) {
                 var spantemp = selTemp.childNodes[i];

                 if (spantemp.tagName) {
                         opt = document.createElement("OPTION")

                         if (document.all) { //IE
                                 objeto.add(opt)
                         } else {
                                 objeto.appendChild(opt)
                         }

                         //getting attributes
                         for ( var j = 0; j < spantemp.attributes.length; j++) {
                                 var attrName = spantemp.attributes[j].nodeName;
                                 var attrVal = spantemp.attributes[j].nodeValue;
                                 if (attrVal) {
                                         try {
                                                 opt.setAttribute(attrName, attrVal);
                                                 opt.setAttributeNode(spantemp.attributes[j]
                                                                 .cloneNode(true));
                                         } catch (e) {
                                         }
                                 }
                         }
                         //getting styles
                         if (spantemp.style) {
                                 for ( var y in spantemp.style) {
                                         try {
                                                 opt.style[y] = spantemp.style[y];
                                         } catch (e) {
                                         }
                                 }
                         }
                         //value and text
                         opt.value = spantemp.getAttribute("value")
                         opt.text = spantemp.innerHTML
                         //IE
                         opt.selected = spantemp.getAttribute('selected');
                         opt.className = spantemp.className;
                 }
         }
         document.body.removeChild(selTemp)
         selTemp = null
 } 