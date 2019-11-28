function appendElement(text, div){
	var node = document.createElement("P");                 
	var textnode = document.createTextNode(text);        
	node.appendChild(textnode);                             
	document.getElementById(div).appendChild(node);
}