function swapContent(containerId1, containerId2){
	var containerId1Content = document.getElementById(containerId1).innerHTML;	
	var containerId2Content = document.getElementById(containerId2).innerHTML;
	document.getElementById(containerId1).innerHTML = containerId2Content;
	document.getElementById(containerId2).innerHTML = containerId1Content;
}