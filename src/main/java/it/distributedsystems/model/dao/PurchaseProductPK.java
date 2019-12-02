package it.distributedsystems.model.dao;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class PurchaseProductPK implements Serializable{

	private static final long serialVersionUID = 1L;

	private int purchaseId;
	private int productId;
	
	public PurchaseProductPK() {  }
	
	public PurchaseProductPK(int purchaseId, int productId) {
		this.purchaseId=purchaseId;
		this.productId=productId;
	}
	
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public int hashCode() {
		//TODO: Cambiare
		return purchaseId + productId;
	}
	
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj==this)
			return true;
		if(!(obj instanceof PurchaseProductPK ))
			return false;
					
		PurchaseProductPK toCompare = (PurchaseProductPK) obj;
		
		return toCompare.getPurchaseId() == this.getPurchaseId()
				&& toCompare.getProductId() == this.getProductId();
	}
	
	
}
