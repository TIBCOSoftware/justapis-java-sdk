package com.anypresence.gw;

public class BaseLogger implements ILogger {

	public void log(String message) {
		log(message, null);
	}
	
	public void log(String message, Exception ex) {
		System.out.println(message);
		if (ex != null) {
			ex.printStackTrace();
		} 
	}
	
}
