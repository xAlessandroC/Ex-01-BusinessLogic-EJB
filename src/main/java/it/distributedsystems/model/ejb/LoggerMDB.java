package it.distributedsystems.model.ejb;

import java.time.LocalDateTime;

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
public class LoggerMDB implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			LocalDateTime now = LocalDateTime.now();
			String logMessage = "###"+now+": "+((TextMessage)message).getText();
			
			//sia stdout che file
			System.out.println(logMessage);
			FileLogger.writeMessage(logMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
