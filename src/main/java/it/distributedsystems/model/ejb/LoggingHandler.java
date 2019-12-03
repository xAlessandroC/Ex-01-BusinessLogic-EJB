package it.distributedsystems.model.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(
		activationConfig={
				@ActivationConfigProperty(
						propertyName="destinationType",
						propertyValue="javax.jms.Queue"),
				@ActivationConfigProperty(
						propertyName="destinationLookup",
						propertyValue="java:/jms/queue/loggingQueue")
		})
public class LoggingHandler implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("########\n[LOGGING]:"+((TextMessage)message).getText()+"\n########");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
