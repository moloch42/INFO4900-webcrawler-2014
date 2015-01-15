package modules;


//TODO update this javadoc
/**
* @author
*/
public class Logger {


    /**
     * @param message this message is logged if debugging is enabled
     */
    public static void debug(String message) {
		if (config.DEBUG_FLAG ) {
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
