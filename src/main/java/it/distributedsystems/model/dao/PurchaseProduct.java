package it.distributedsystems.model.dao;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class PurchaseProduct {

	@EmbeddedId
	private PurchaseProductPK id;
	@ManyToOne(
            cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.EAGER	//LazyInitializaztionException
    )
	@MapsId("purchaseId")
	private Purchase purchase;
	@ManyToOne(
            cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.EAGER //LazyInitializaztionException
    )
	@MapsId("productId")
	private Product product;
	
	private int quantity;
	
	public PurchaseProduct() { this.id = new PurchaseProductPK(); }
	
	public PurchaseProduct(int purchaseId, int productId) {
		this.id = new PurchaseProductPK(purchaseId,productId);
	}
	
	public PurchaseProductPK getId() {
		return id;
	}
	public void setId(PurchaseProductPK id) {
		this.id = id;
	}
	
	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
