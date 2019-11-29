function appendElement(text, div){
	var node = document.createElement("P");                 
	var textnode = document.createTextNode(text);        
	node.appendChild(textnode);                             
	document.getElementById(div).appendChild(node);
}

function insertInCart(productNumber){
	var f = document.createElement("form");
	f.action="/demo/?operation=insertCart&code="+productNumber;
	document.body.appendChild(f);
	f.submit();
}