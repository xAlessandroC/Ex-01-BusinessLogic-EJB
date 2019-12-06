<%@ page session ="true"%>
<%@ page import="java.util.*" %>
<%@ page import="it.distributedsystems.model.dao.*" %>
<%@ page import="it.distributedsystems.model.ejb.*" %>

<jsp:useBean id="cartFactory" class="it.distributedsystems.model.ejb.CartFactory" scope="application"/>
<%! Cart cart=null;%>
<%
	if(request.getSession().getAttribute("cart")==null)
		request.getSession().setAttribute("cart",cartFactory.getCart());
	cart= (Cart) request.getSession().getAttribute("cart");
%>

<%!
	String userNote="";
	String adminNote="";
	String printTableRow(Product product, String url) {
		StringBuffer html = new StringBuffer();
		html
				.append("<tr>")
				.append("<td>")
				.append(product.getProductNumber())
				.append("</td>")
				
				.append("<td>")
				.append(product.getName())
				.append("</td>")

				.append("<td>")
				.append( (product.getProducer() == null) ? "n.d." : product.getProducer().getName() )
				.append("</td>")
				
				.append("<td>")
				.append(product.getPrice())
				.append("</td>")
				
				.append("<td>")
				.append(product.getQuantity())
				.append("</td>")
				
				.append("<td><form>")
				.append("<input type=\"hidden\" name=\"code\" value=\"")
				.append(product.getProductNumber())
				.append("\"/>")
				.append("<button type=\"submit\" name=\"operation\" value=\"insertCart\">insert</input>")
				.append("<button type=\"submit\" name=\"operation\" value=\"removeCart\">remove</input>")
				.append("</form></td>");
		
		html
				.append("</tr>");

		return html.toString();
	}

	String printTableRows(List products, String url) {
		StringBuffer html = new StringBuffer();
		Iterator iterator = products.iterator();
		while ( iterator.hasNext() ) {
			html.append( printTableRow( (Product) iterator.next(), url ) );
		}
		return html.toString();
	}
	
	String printCart(){
		Map<Product,Integer> items = cart.getItems();
		// System.out.println("##JSP:"+items+","+items.size());
		Iterator iterator = items.keySet().iterator();
		StringBuffer html = new StringBuffer();
		while ( iterator.hasNext() ) {
			Product product = (Product)iterator.next();
			
			html.append("<li>")
			.append(product.getName())
			.append(",")
			.append(product.getProductNumber())
			.append("----->")
			.append(items.get(product))
			.append("</li>");
			
		}
		System.out.println("##JSP:"+html.toString());
		return html.toString();
	}%>

<html>

	<head>
		<title>HOMEPAGE DISTRIBUTED SYSTEM EJB</title>
	
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="Mon, 01 Jan 1996 23:59:59 GMT"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta name="Author" content="you">

		<link rel="StyleSheet" href="<%=request.getContextPath()%>/styles/default.css" type="text/css" media="all" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/utility.js"></script>
	</head>
	
	<body>

	<%
		// can't use builtin object 'application' while in a declaration!
		// must be in a scriptlet or expression!
		DAOFactory daoFactory = DAOFactory.getDAOFactory( application.getInitParameter("dao") );
		CustomerDAO customerDAO = daoFactory.getCustomerDAO();
		PurchaseDAO purchaseDAO = daoFactory.getPurchaseDAO();
		ProductDAO productDAO = daoFactory.getProductDAO();
		ProducerDAO producerDAO = daoFactory.getProducerDAO();
		PurchaseProductDAO ppDAO = daoFactory.getPurchaseProductDAO();

		System.out.println( "###DEBUG###" );
		System.out.println( daoFactory );
		System.out.println( customerDAO );
		System.out.println( purchaseDAO );
		System.out.println( productDAO );
		System.out.println( producerDAO );
		System.out.println( ppDAO );
		System.out.println( cart );
		
		String operation = request.getParameter("operation");
		
		if ( operation != null && operation.equals("insertCart") ) {
			try{
				int number = Integer.parseInt(request.getParameter("code"));
				Product product = productDAO.findProductByNumber(number);
				cart.addItem(product);
				userNote="Inserito in carrello!";
			}catch(Exception e){
				userNote="Errore inserimento product nel cart";
			}
		}
		else if ( operation != null && operation.equals("removeCart") ) {
			try{
				int number = Integer.parseInt(request.getParameter("code"));
				Product product = productDAO.findProductByNumber(number);
				cart.removeItem(product);
				userNote="Rimosso da carrello!";
			}catch(Exception e){
				userNote="Errore rimozione product dal cart";
			}
		}
		else if ( operation != null && operation.equals("buy") ) {
			try{
				Purchase p = new Purchase();
				Map<Product,Integer> items = cart.getItems();
				
				Customer customer;
				if(request.getSession().getAttribute("identity")==null){
					try{
						customer=customerDAO.findCustomerByName("Anonymous");
					}catch(Exception e){
						customer=new Customer("Anonymous");
					}						
				}else{
					customer=(Customer)request.getSession().getAttribute("identity");
				}
				p.setCustomer(customer);
				
				for (Product pr : items.keySet()){
					PurchaseProduct pp = new PurchaseProduct();
					int qnt = cart.getItems().get(pr);
					pr.setQuantity(pr.getQuantity() - qnt);
					pp.setProduct(pr);
					pp.setPurchase(p);
					pp.setQuantity(qnt);
					
					ppDAO.insertPurchaseProduct(pp);
				}
				
				/*System.out.println("##DEBUG LETTURA PURCHASE");
				Purchase pur = purchaseDAO.findPurchaseById(4);
				System.out.println("SET:"+pur.getPurchaseProducts());
				for(PurchaseProduct temp : pur.getPurchaseProducts()){
					System.out.println(temp.getProduct());
					System.out.println("NAME: "+temp.getProduct().getName());
					System.out.println("QUANTITY: "+temp.getQuantity());
				}*/
				
				cart.clear();
				userNote="Ordine effettuato con successo, carrello vuoto!";
			}catch(Exception e){
				userNote="Errore nel completamento dell'ordine!";
				System.out.println(e);
			}
		}
		//Da aggiungere la possibilità di fare un ordine in sessione e di finalizzarla per creare un purchase.
	%>


	<h1>Welcome <%if(request.getSession().getAttribute("identity")==null){ %>Anonymous
		<%}else{ %><%=(String)(request.getSession().getAttribute("identity")) %><%} %></h1>
	
	<div id="pagechoice">
		<input type="radio" id="administrationButton" name="page" value="administrationPage" onclick="check()"/>
		<label for="administrationButton">Administration Page</label>
		<input type="radio" id="userButton" name="page" value="userPage" checked onclick="check()"/>
		<label for='userButton'>User Page</label>
	</div>

	<div id="userHTML">
		<div style="height:70%;">
			<div id="store">
				<p>Products currently in the database:</p>
				<table>
					<tr><th>ProductNumber</th><th>Name</th><th>Publisher</th><th>Price</th><th>Available</th><th>Operation</th></tr>
					<%= printTableRows( productDAO.getAllProducts(), request.getContextPath() ) %>
				</table>
			</div>
			
			<div id="cart">
				<h3>CART</h3>
				<ul><%=printCart() %></ul>
				<form>
					<input type="submit" name="operation" value="buy"/>
				</form>
			</div>
		</div>
		
		<div id="userNotes">
			<p><%=userNote %></p>
		</div>
	</div>
	
	</body>

</html>