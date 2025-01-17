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

<%!//Cart cart = Cart.getCart();
	String note="";
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
		if ( operation != null && operation.equals("insertCustomer") ) {
			//per ipotesi se aggiungo un customer vuol dire che sono io
			Customer customer = new Customer();
			customer.setName( request.getParameter("name") );
			try{
				int id = customerDAO.insertCustomer( customer );
				request.getSession().setAttribute("identity",customer);
				note="Inserimento customer riuscito!";
			}catch(Exception e){
				note="Errore inserimento customer";
			}
		}
		else if ( operation != null && operation.equals("insertProducer") ) {
			Producer producer = new Producer();
			producer.setName( request.getParameter("name") );
			try{
				int id = producerDAO.insertProducer( producer );
				note="Inserimento producer riuscito!";
			}catch(Exception e){
				note="Errore inserimento producer";
			}
		}
		else if ( operation != null && operation.equals("insertProduct") ) {
			Product product = new Product();
			product.setName( request.getParameter("name") );
			product.setProductNumber(Integer.parseInt(request.getParameter("number")));
			product.setPrice(Integer.parseInt(request.getParameter("price")));
			product.setQuantity(Integer.parseInt(request.getParameter("quantity")));

			Producer producer = producerDAO.findProducerByName(request.getParameter("producer"));
			product.setProducer(producer);
			try{
				int id = productDAO.insertProduct(product);
				note="Inserimento product riuscito!";
			}catch(Exception e){
				note="Errore inserimento product";
			}
		}
		else if ( operation != null && operation.equals("insertCart") ) {
			try{
				int number = Integer.parseInt(request.getParameter("code"));
				Product product = productDAO.findProductByNumber(number);
				cart.addItem(product);
				note="Inserito in carrello!";
			}catch(Exception e){
				note="Errore inserimento product nel cart";
			}
		}
		else if ( operation != null && operation.equals("removeCart") ) {
			try{
				int number = Integer.parseInt(request.getParameter("code"));
				Product product = productDAO.findProductByNumber(number);
				cart.removeItem(product);
				note="Rimosso da carrello!";
			}catch(Exception e){
				note="Errore rimozione product dal cart";
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
				note="Ordine effettuato con successo, carrello vuoto!";
			}catch(Exception e){
				note="Errore nel completamento dell'ordine!";
				System.out.println(e);
			}
		}
		//Da aggiungere la possibilità di fare un ordine in sessione e di finalizzarla per creare un purchase.
	%>


	<h1>Customer Manager</h1>

	<div>
		<p>Add Customer:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			<input type="hidden" name="operation" value="insertCustomer"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>

	<div>
		<p>Add Producer:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			<input type="hidden" name="operation" value="insertProducer"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>

	<%
		List producers = producerDAO.getAllProducers();
		if ( producers.size() > 0 ) {
	%>
	<div>
		<p>Add Product:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			Product Number: <input type="text" name="number"/><br/>
			Price: <input type="text" name="price"/><br/>
			Quantity: <input type="text" name="quantity"/><br/>
			Producers: <select name="producer">
			<%
				Iterator iterator = producers.iterator();
				while ( iterator.hasNext() ) {
					Producer producer = (Producer) iterator.next();
			%>
			<option value="<%= producer.getName() %>"><%= producer.getName()%></option>
			<%
				}// end while
			%>

			<input type="hidden" name="operation" value="insertProduct"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>
	<%
	}// end if
	else {
	%>
	<div>
		<p>At least one Producer must be present to add a new Product.</p>
	</div>
	<%
		} // end else
	%>
	<div>
		<p>Products currently in the database:</p>
		<table>
			<tr><th>ProductNumber</th><th>Name</th><th>Publisher</th><th>Price</th><th>Available</th><th>Operation</th></tr>
			<%= printTableRows( productDAO.getAllProducts(), request.getContextPath() ) %>
		</table>
	</div>

	<div>
		<a href="<%= request.getContextPath() %>">Ricarica lo stato iniziale di questa pagina</a>
	</div>
	
	<form>
		<input type="submit" name="operation" value="buy"/>
	</form>
	
	<div>
		<h3>CART</h3>
		<ul><%=printCart() %></ul>
	</div>

	<div id="notes">
		<p><%=note %></p>
	</div>
	</body>

</html>