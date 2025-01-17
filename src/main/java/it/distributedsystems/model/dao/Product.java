package it.distributedsystems.model.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import it.distributedsystems.model.dao.Purchase;

@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = 7879128649212648629L;

    protected int id;
    protected int productNumber;
    protected String name;
    protected int price;
    protected int quantity;
    protected Set<PurchaseProduct> ass_purchase;
    protected Producer producer;

    public Product() {}

    public Product(String name) { this.name = name; }

    public Product(String name, int price) { this.name = name; this.price = price; }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(unique = true)
    public int getProductNumber() { return productNumber; }

    public void setProductNumber(int productNumber) { this.productNumber = productNumber; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public int getQuantity()  {
        return this.quantity;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException{
    	if(quantity<0)
    		throw new IllegalArgumentException("quantity must be non negative!");
        this.quantity = quantity;
    }

    @OneToMany(
            cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch=FetchType.LAZY,
            mappedBy = "product"
    )
    public Set<PurchaseProduct> getPurchase() {
        return this.ass_purchase;
    }

    public void setPurchase(Set<PurchaseProduct> purchase) {
        this.ass_purchase = purchase;
    }

    @ManyToOne(
            cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
  
    @Override
    public int hashCode() {
    	return this.productNumber;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == this) {
    		return true; 
    	} 
    	if (obj == null || obj.getClass() != this.getClass()) {
    		return false; 
    	}
    	
    	Product p = (Product) obj;
    	
    	return p.getProductNumber() == this.getProductNumber();
    }
}
