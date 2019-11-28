package it.distributedsystems.model.ejb;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;

import it.distributedsystems.model.dao.*;
import org.apache.log4j.Logger;
import org.jboss.system.server.ServerInfo;

public class EJB3DaoFactory extends DAOFactory {
    private static Logger logger = Logger.getLogger("DAOFactory");
    
    /*@EJB(name="ejb:distributed-systems-demo/distributed-systems-demo.war/EJB3CustomerDAO!it.distributedsystems.model.dao.CustomerDAO")
    private CustomerDAO customer;
    @EJB(name="ejb:distributed-systems-demo/distributed-systems-demo.war/EJB3PurchaseDAO!it.distributedsystems.model.dao.PurchaseDAO")
    private PurchaseDAO purchase;
    @EJB(name="ejb:distributed-systems-demo/distributed-systems-demo.war/EJB3ProductDAO!it.distributedsystems.model.dao.ProductDAO")
    private ProductDAO product;
    @EJB(name="java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3ProducerDAO!it.distributedsystems.model.dao.ProducerDAO")
    private ProducerDAO producer;*/

    public EJB3DaoFactory() {


    }

    private static InitialContext getInitialContext() throws Exception {
        Hashtable props = getInitialContextProperties();
        return new InitialContext(props);
    }

    private static Hashtable getInitialContextProperties() {
        Hashtable props = new Hashtable();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.remote.client.InitialContextFactory");
        
        /*
        props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        props.put("java.naming.provider.url", "127.0.0.1:1099"); //(new ServerInfo()).getHostAddress()  --- 127.0.0.1 --
        */
        return props;
    }

    public CustomerDAO getCustomerDAO() {
        try {
            InitialContext context = getInitialContext();
            CustomerDAO result = (CustomerDAO)context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3CustomerDAO!it.distributedsystems.model.dao.CustomerDAO");
        	return result;
        	//return customer;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3CustomerDAO", var3);
            return null;
        }
    }

    public PurchaseDAO getPurchaseDAO() {
        try {
            InitialContext context = getInitialContext();
            PurchaseDAO result = (PurchaseDAO)context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3PurchaseDAO!it.distributedsystems.model.dao.PurchaseDAO");
        	return result;
        	//return purchase;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3PurchaseDAO", var3);
            return null;
        }
    }

    public ProductDAO getProductDAO() {
        try {
            InitialContext context = getInitialContext();
            ProductDAO result = (ProductDAO)context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3ProductDAO!it.distributedsystems.model.dao.ProductDAO");
        	return result;
        	//return product;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3ProductDAO", var3);
            return null;
        }
    }

    public ProducerDAO getProducerDAO() {
        try {
            InitialContext context = getInitialContext();
            ProducerDAO result = (ProducerDAO)context.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3ProducerDAO!it.distributedsystems.model.dao.ProducerDAO");
            return result;
        	//return producer;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3ProducerDAO", var3);
            return null;
        }
    }
}
