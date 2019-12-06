<%@ page session ="true"%>
<%@ page import="java.util.*" %>
<%@ page import="it.distributedsystems.model.dao.*" %>
<%@ page import="it.distributedsystems.model.ejb.*" %>

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
	
%>

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

		System.out.println( "###DEBUG###" );
		System.out.println( daoFactory );
		System.out.println( customerDAO );
		System.out.println( purchaseDAO );
		System.out.println( productDAO );
		System.out.println( producerDAO );
		
		String operation = request.getParameter("operation");
		if ( operation != null && operation.equals("insertCustomer") ) {
			
			Customer customer = new Customer();
			customer.setName( request.getParameter("name") );
			try{
				int id = customerDAO.insertCustomer( customer );
				adminNote="Inserimento customer riuscito!";
			}catch(Exception e){
				adminNote="Errore inserimento customer";
			}
		}
		else if ( operation != null && operation.equals("insertProducer") ) {
			Producer producer = new Producer();
			producer.setName( request.getParameter("name") );
			try{
				int id = producerDAO.insertProducer( producer );
				adminNote="Inserimento producer riuscito!";
			}catch(Exception e){
				adminNote="Errore inserimento producer";
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
				adminNote="Inserimento product riuscito!";
			}catch(Exception e){
				adminNote="Errore inserimento product";
			}
		}
	%>


	<h1>Administrator</h1>
	
	<div id="pagechoice">
		<input type="radio" id="administrationButton" name="page" value="administrationPage" checked onclick="check()"/>
		<label for="administrationButton">Administration Page</label>
		<input type="radio" id="userButton" name="page" value="userPage"  onclick="check()"/>
		<label for='userButton'>User Page</label>
	</div>

	<div id="administrationHTML">
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
		
		<div id="adminNotes">
			<p><%=adminNote %></p>
		</div>
	</div>
	
	</body>

</html>