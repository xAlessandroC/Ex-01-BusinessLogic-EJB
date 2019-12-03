package it.distributedsystems.model.ejb;

//import it.distributedsystems.model.logging.OperationLogger;
import it.distributedsystems.model.dao.Customer;
import it.distributedsystems.model.dao.CustomerDAO;

import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Hashtable;
import java.util.List;

@Stateless
@Local(CustomerDAO.class)
//@Remote(CustomerDAO.class) //-> TODO: serve nella versione clustering???
public class EJB3CustomerDAO implements CustomerDAO {

    @PersistenceContext(unitName = "distributed-systems-demo")
    EntityManager em;

    @Override
//    @Interceptors(OperationLogger.class)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int insertCustomer(Customer customer) {
    	
    	//messaggio alla coda di logging
    	 Hashtable props = new Hashtable();
         props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.remote.client.InitialContextFactory");
         Queue logQueue= null;
         QueueConnectionFactory factory = null;
         try {
         InitialContext context = new InitialContext(props);
         logQueue = (Queue) context.lookup("java:/jms/queue/loggingQueue");
         factory = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");
         
         System.out.println("###DEBUG JMS");
         System.out.println(logQueue);
         System.out.println(factory);
         
         QueueConnection connection = factory.createQueueConnection();
         QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
         QueueSender sender = session.createSender(logQueue);
         
         connection.start();
         TextMessage message = session.createTextMessage();
         message.setText("Inserimento customer nel DB --- CUSTOMER DATA: "+customer.getName());
         sender.send(message);
         }catch(Exception e) {
         	System.err.println(e);
         }
    	
    	
        em.persist(customer);
        return customer.getId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int removeCustomerByName(String name) {

        Customer customer;
        if(name != null && !name.equals("")) {
            customer = (Customer) em.createQuery("FROM Customer c WHERE c.name = :customerName").setParameter("customerName", name).getSingleResult();
            em.remove(customer);
            return customer.getId();
        } else
            return 0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int removeCustomerById(int id) {
        Customer customer = em.find(Customer.class, id);
        if (customer!=null){
            em.remove(customer);
            return id;
        }
        else
            return 0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Customer findCustomerByName(String name) {
        if(name != null && !name.equals("")) {
            return (Customer) em.createQuery("FROM Customer c where c.name = :customerName").
                    setParameter("customerName", name).getSingleResult();
        } else
            return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Customer findCustomerById(int id) {
        return em.find(Customer.class, id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Customer> getAllCustomers() {
        return em.createQuery("FROM Customer").getResultList();
    }
}
