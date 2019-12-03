package it.distributedsystems.model.ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.distributedsystems.model.dao.PurchaseProduct;
import it.distributedsystems.model.dao.PurchaseProductDAO;

@Stateless
@Local(PurchaseProductDAO.class)
public class EJB3PurchaseProductDAO implements PurchaseProductDAO {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public void insert(PurchaseProduct pp) {
		
		if(pp.getProduct()!=null && pp.getProduct().getId()>0)
			pp.setProduct(em.merge(pp.getProduct()));
		
		if(pp.getPurchase()!=null && pp.getPurchase().getId()>0)
			pp.setPurchase(em.merge(pp.getPurchase()));
		
		if(pp.getPurchase().getCustomer()!=null && pp.getPurchase().getCustomer().getId()>0)
			pp.getPurchase().setCustomer(em.merge(pp.getPurchase().getCustomer()));
		
		em.persist(pp);
	}

}
