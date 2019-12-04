package it.distributedsystems.model.ejb;

import java.util.Hashtable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import it.distributedsystems.model.dao.Customer;
import it.distributedsystems.model.dao.Producer;
import it.distributedsystems.model.dao.Product;
import it.distributedsystems.model.dao.PurchaseProduct;

public class LoggingInterceptor {

	@AroundInvoke
	public Object log(InvocationContext ctx) {
		
   	 	Hashtable props = new Hashtable();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.remote.client.InitialContextFactory");
        Queue logQueue= null;
        QueueConnectionFactory factory = null;
        try {
	        InitialContext context = new InitialContext(props);
	        logQueue = (Queue) context.lookup("java:/jms/queue/loggingQueue");
	        factory = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");
	        
	        // System.out.println("###DEBUG JMS");
	        // System.out.println(logQueue);
	        // System.out.println(factory);
	        
	        QueueConnection connection = factory.createQueueConnection();
	        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	        QueueSender sender = session.createSender(logQueue);
	        
	        connection.start();
	        TextMessage message = session.createTextMessage();
	        
	        //ipotizzando che l'intercettazione venga fatta su tutti i metodi insert<Nome>()
	        String typeOp = ctx.getMethod().getName().substring(6);
	        Object target = ctx.getParameters()[0];
	        String text = "Inserimento "+typeOp+" nel DB \n"+typeOp.toUpperCase()+" DATA: \n";
	        switch(typeOp){
	        case "Customer":{
	        	text+="NAME: "+((Customer)target).getName();
	        	break;
	        }
	        case "Product":{
	        	text+="NAME: "+((Product)target).getName()+"\nPRODUCT NUMBER: "+((Product)target).getProductNumber();
	        	break;
	        }
	        case "Producer":{
	        	text+="NAME: "+((Producer)target).getName();
	        	break;
	        }
	        case "Purchase":{
	        	text+="NAME: "+((Customer)target).getName();
	        	break;
	        }
	        case "PurchaseProduct":{
	        	text+="PRODUCT NAME: "+((PurchaseProduct)target).getProduct().getName();
	        	text+="\nQUANTITY: "+((PurchaseProduct)target).getQuantity();
	        	break;
	        }
	        }
	        
	        message.setText(text);
	        sender.send(message);
	        return ctx.proceed();
        }catch(Exception e) {
        	System.err.println(e);
        	return null;
        }
	}
}
