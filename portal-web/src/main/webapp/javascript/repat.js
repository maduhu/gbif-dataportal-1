/*
 Javascript for the Repatriation Table page
*/ 
var selectedRow = null;
var selectedCol = null;
var serverUrl="http://localhost:8080/portaldev/";
var captionUrl = "http://localhost:8080/portaldev/countries/repatriation/ajaxCaption?";
var mapServerUrl = "http://maps.gbif.org/mapserver/draw.pl?dtype=box&imgonly=1&mode=browse&refresh=Refresh&layer=countryborders&layer=countrylabel&path=";
var mapLayerUrl = "http://localhost:8080/portaldev/maplayers/homeCountry/";
var hostCountryUrl = "http://localhost:8080/portaldev/countries/hosted/";
var countryUrl = "http://localhost:8080/portaldev/countries/";
var overviewMapFileName = "overviewMap.png";
var debug = false;

function highlight(){
	var hoveredCell = this;
	if (this.parentNode) {
		// is FF
	} else { // is IE
		hoveredCell = event.srcElement;
	}
  toggle(hoveredCell, 'highlighted'); 
  if(!isSelectedCell(hoveredCell) && !(hoveredCell.cellIndex==0 && hoveredCell.parentNode.rowIndex==0)){
    hoveredCell.className='highlightedCell';
  }
}

function unhighlight(){
	var hoveredCell = this;
	if (this.parentNode) {
		// is FF
	} else { // is IE
		hoveredCell = event.srcElement;
	}
  toggle(hoveredCell, '');
}

function selectCell(){
	// test if IE - QUICK hack
	var selectedCell = this;
	if (this.parentNode) {
		// is FF
	} else { // is IE
		selectedCell = event.srcElement;
	}

  if(selectedCell.cellIndex==0 && selectedCell.parentNode.rowIndex==0)
    return; 
  
  //deselect old selected cell
  if(selectedRow!=null){ 
    var tbodyElement = selectedCell.parentNode.parentNode;
    var rows = tbodyElement.rows;
    var cells = rows[selectedRow].cells;
    cells[selectedCol].className='unhighlighted';
  }

  selectedCol = selectedCell.cellIndex;
  selectedRow = selectedCell.parentNode.rowIndex;
  //highlight the elements in the row and column
  toggle(selectedCell, 'highlighted');
  //highlight the selected cell 
  selectedCell.className = 'selectedCell';  
  document.getElementById('selectedRowDebug').innerHTML="column "+selectedCol+':'+" row "+selectedRow;
  
  //if already selected
  if(selectedCell.id.indexOf("host-")>=0){
    var isoCountryCode = selectedCell.id.substring(5,7);
    var img = null
    if(isoCountryCode=='XX'){
      img = hostCountryUrl+"0/"+overviewMapFileName;
    } else {
      img = hostCountryUrl+isoCountryCode+"/"+overviewMapFileName;
    }
    
    //retrieve caption
    var callback = {
      success:function(o){
        var caption = o.responseText;
        Lightbox.show(img, caption);
      },  
      failure: function(o){}
    } 
    //make the ajax request for the wizard
    YAHOO.util.Connect.asyncRequest('GET',
      captionUrl+"host="+isoCountryCode, 
      callback, 
      null); 

  } else if(selectedCell.id.indexOf("country-")>=0){
    var isoCountryCode = selectedCell.id.substring(8,10);
    var img = countryUrl+isoCountryCode+"/"+overviewMapFileName;
    //alert(img);
    //retrieve caption
    var callback = {
      success:function(o){
        var caption = o.responseText;
        Lightbox.show(img, caption);
      },  
      failure: function(o){}
    } 
    //make the ajax request for the wizard
    YAHOO.util.Connect.asyncRequest('GET',
      captionUrl+"country="+isoCountryCode, 
      callback, 
      null);  
  } else if( selectedCell.id.length==5) {
    
   var host = selectedCell.id.substring(0,2);
   var country = selectedCell.id.substring(3,5);
   var layerPath=host+"/"+country+".txt";
   var img = mapServerUrl+mapLayerUrl+layerPath;
   
    //retrieve caption
    var callback = {
      success:function(o){
        var caption = o.responseText;
        Lightbox.show(img, caption);
      },  
      failure: function(o){}
    } 
    //make the ajax request for the wizard
    YAHOO.util.Connect.asyncRequest('GET',
      captionUrl+"country="+country+"&host="+host, 
      callback, 
      null);     
  }   
}

function isSelectedCell(cell){
  return cell.cellIndex == selectedCol && cell.parentNode.rowIndex==selectedRow;
}

function toggle(hoveredCell, className){

  var colIdx = hoveredCell.cellIndex; 
  var rowElement = hoveredCell.parentNode;
  
  //up 2 levels, tr -> tbody -> table
  var tbodyElement = rowElement.parentNode;
  var tableElement = tbodyElement.parentNode;
  var rowIdx = rowElement.rowIndex; 
  
  //debug
  if(debug){
    document.getElementById('rowDebug').innerHTML="column "+colIdx+':'+" row "+rowIdx;
  }
  
  //highlight cells in row
  var cellsInRow = rowElement.cells;
  var rowElements = tbodyElement.rows;

  var colLimit = colIdx+1;
  var rowLimit = rowIdx;
  
  if(colIdx==0 && rowIdx==0){
    colLimit = 0;
    rowLimit = 0; 
  } else if(rowIdx==0){
    colLimit = 0;
    rowLimit = rowElements.length;
  } else if(colIdx==0){
    colLimit = cellsInRow.length;
    rowLimit = 0;
  }
  
  //highlight cells in row
  for(var i=0; i<colLimit; i++){
    if(!isSelectedCell(cellsInRow[i])){
      cellsInRow[i].className = className;
    }
  }
  
  //highlight cells in column
  for(var i=0; i<rowLimit; i++){  
    var cells = rowElements[i].cells;
    if(!isSelectedCell(cells[colIdx])){
      cells[colIdx].className = className;
    }
  }
}

function addListenersToTable(tableElementId, minCellIndex, maxCellIndex, minRowIndex, maxRowIndex){
  var table = document.getElementById(tableElementId);
  var cells = table.getElementsByTagName('TD');
  for(var i=0; i<cells.length; i++){
    if(cells[i].cellIndex>=minCellIndex 
        && cells[i].cellIndex<=maxCellIndex 
        && cells[i].parentNode.rowIndex>=minRowIndex 
        && cells[i].parentNode.rowIndex<=maxRowIndex){
        
		if (cells[i].addEventListener){
		      cells[i].addEventListener('click', selectCell, false);
		      cells[i].addEventListener('mouseover', highlight, false);   
		      cells[i].addEventListener('mouseout', unhighlight, false);
		  
		} else if (cells[i].attachEvent){
		      cells[i].attachEvent('onclick', selectCell);
		      cells[i].attachEvent('onmouseover', highlight);   
		      cells[i].attachEvent('onmouseout', unhighlight);
		}
      cells[i].className='';
    }        
  } 
}

function toggleView(showElement, hideElement){
	document.getElementById(hideElement).style.display = 'none';
	document.getElementById(showElement).style.display = 'block';
} 