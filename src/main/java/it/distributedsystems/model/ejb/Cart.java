package it.distributedsystems.model.ejb;

import java.util.Map;

import it.distributedsystems.model.dao.Product;

public interface Cart {
	
	public Map<Product,Integer> getItems();
	
	public void addItem(Product product);
	
	public void removeItem(Product product);

}
