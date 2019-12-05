package it.distributedsystems.model.ejb;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.ConnectionFactory;
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
	
	@Resource(name="java:/jms/queue/loggingQueue")
	private Queue logQueue;
	@Resource(name="java:/ConnectionFactory")
	private QueueConnectionFactory factory;

	@AroundInvoke
	public Object log(InvocationContext ctx) {
	
        try {
	        QueueConnection connection = this.factory.createQueueConnection();
	        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	        QueueSender sender = session.createSender(this.logQueue);
	        
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
	        connection.close();
	        
	        return ctx.proceed();
        }catch(Exception e) {
        	System.err.println(e);
        	return null;
        }
	}
}
