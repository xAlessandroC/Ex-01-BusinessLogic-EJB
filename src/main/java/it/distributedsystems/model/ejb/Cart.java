package it.distributedsystems.model.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;

import it.distributedsystems.model.dao.Product;

@Stateful
@Local(Cart.class)
public class Cart {

	private List<Product> products;
	private Map<Product,Integer> items;
	
	public static Cart getCart() {
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
	}
	
	public Cart() {
		this.items=new HashMap<>();
		
		//this.products = new ArrayList<>();
	}
	
	public void addItem(Product product) {
		if(this.items.get(product)!=null) {
			this.items.put(product,this.items.get(product)+1);
		}else{
			this.items.put(product,1);
		}
		
		//this.products.add(product);
	}
	
	public void removeItem(Product product) {
		Integer qnt = this.items.get(product);
		if(qnt != null) {
			if(qnt == 1)
				this.items.remove(product);
			else
				this.items.put(product,this.items.get(product)-1);
		}
		
		
		/*int index=-1;
		for(int i=0;i<this.products.size();i++) {
			if(this.products.get(i).getProductNumber() == product.getProductNumber()) {
				index=i;
				break;
			}
		}
		if(index>=0)
			this.products.remove(index);*/
	}
	
	public Map<Product,Integer> getAllItems(){
		return this.items;
		
		//return this.products;
	}
	
	//public boolean confirmOrder()
}
