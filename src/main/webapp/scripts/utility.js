function appendElement(text, div){
	var node = document.createElement("P");                 
	var textnode = document.createTextNode(text);        
	node.appendChild(textnode);                             
	document.getElementById(div).appendChild(node);
}

function check(){
	userRadio = document.getElementById("userButton");
	administrationRadio = document.getElementById("administrationButton");
	
	if(userRadio.checked){
		//document.getElementById("userHTML").hidden=false;
		//document.getElementById("administrationHTML").hidden=true;
		window.location.replace("./user.jsp");
	}
	if(administrationRadio.checked){
		//document.getElementById("userHTML").hidden=true;
		//document.getElementById("administrationHTML").hidden=false;
		window.location.replace("./index.jsp");
	}
		
}