package it.distributedsystems.model.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;

import it.distributedsystems.model.dao.Product;

@Stateful
@Local(Cart.class)
public class EJB3Cart implements Cart{

	private Map<Product,Integer> items;
	
	/*public static Cart getCart() {
        Hashtable props = new Hashtable();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.remote.client.InitialContextFactory");
        Cart cart=null;
        try {
        InitialContext context = new InitialContext(props);
        Object obj = context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/Cart!it.distributedsystems.model.ejb.Cart");
        cart = (Cart) obj;
        }catch(Exception e) {
        	System.err.println(e);
        }
        return cart;
	}*/
	
	public EJB3Cart() {
		this.items=new HashMap<>();
	}
	
	public Map<Product,Integer> getItems(){
		return this.items;
	}
	
	public void addItem(Product product) {
		if(this.items.get(product)!=null) {
			this.items.put(product,this.items.get(product)+1);
		}else{
			this.items.put(product,1);
		}
		show();
	}
	
	public void removeItem(Product product) {
		Integer qnt = this.items.get(product);
		if(qnt != null) {
			if(qnt == 1)
				this.items.remove(product);
			else
				this.items.put(product,this.items.get(product)-1);
		}
		show();
	}
	
	private void show() {
		Map<Product,Integer> items = this.getItems();
		Iterator iterator = items.keySet().iterator();
		System.out.println("##CONTENT");
		while ( iterator.hasNext() ) {
			Product product = (Product)iterator.next();
			System.out.print(product.getName()+",");
			System.out.print(product.getProductNumber()+",");
			System.out.println(this.items.get(product));
		}
	}

	public void clear() {
		this.items.clear();
	}
	
}
