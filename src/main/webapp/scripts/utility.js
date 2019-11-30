function appendElement(text, div){
	var node = document.createElement("P");                 
	var textnode = document.createTextNode(text);        
	node.appendChild(textnode);                             
	document.getElementById(div).appendChild(node);
}

function modifyCart(op){
	var f = document.getElementById("cartForm");
	var hiddenInput = document.getElementById("operation");
	if(op==="insert")
		hiddenInput.value="insertCart";
	if(op==="remove")
		hiddenInput.value="removeCart";
	f.submit();
}