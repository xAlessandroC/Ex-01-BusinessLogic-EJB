package it.distributedsystems.model.ejb;

import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger {
	
	private static PrintWriter writer;

	public synchronized static void writeMessage(String message) {
		try {
			if(writer==null) {
				writer = new PrintWriter("C:\\Users\\alexc\\Workspace_SD\\Ex-01-BusinessLogic-EJB\\log.txt");
			}
			
			writer.append(message+"\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			writer.close();
		}
	}
}
