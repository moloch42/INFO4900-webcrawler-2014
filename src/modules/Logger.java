package modules;

import webcrawler.config;


public class Logger {

	
	
	public static void debug(String message) {
		if ( config.DEBUG_FLAG ) {
			System.out.println("Debug: " + message);
		}
	}
	
	public static void error(String message) {
		System.out.println("Error: " + message);
	}
	
	public static void error(String message, Exception e) {
		System.out.println("Error: " + message);
		System.out.println("---Caused by: " + e);
		e.printStackTrace();
	}
	
}
