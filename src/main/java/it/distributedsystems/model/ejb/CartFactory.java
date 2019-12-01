package it.distributedsystems.model.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

public class CartFactory {

	public CartFactory() {
		
	}
	
	public Cart getCart() {
        Hashtable props = new Hashtable();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.remote.client.InitialContextFactory");
        Cart cart=null;
        try {
        InitialContext context = new InitialContext(props);
        Object obj = context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3Cart!it.distributedsystems.model.ejb.Cart");
        cart = (Cart) obj;
        }catch(Exception e) {
        	System.err.println(e);
        }
        return cart;
	}
}
