package modules;

/**
* This class is a simple logger that displays log messages to standard out.
* It enables debug messages to be easily enable or disables as needed using config.java.
*/
public class Logger {

    /**
     * @param message this message is logged if debugging is enabled
     */
    public static void debug(String message) {
		if (config.isDebugEnabled() ) {
			System.out.println("Debug: " + message);
		}
	}

    /**
     * @param message this message is logged always
     */
    public static void error(String message) {
		System.out.println("Error: " + message);
	}

    /**
     * @param message this message is logged always
     * @param e the stack trace of this exception is logged
     */
    public static void error(String message, Exception e) {
                error(message);
		System.out.println("---Caused by: " + e);
		e.printStackTrace();
	}
	
}
