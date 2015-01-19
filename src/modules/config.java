package modules;

//TODO update this javadoc
/**
* @author David Tickner
*/
public class config {
	
	//TODO update this javadoc
	/***/
	public static boolean DEBUG_FLAG = false;
	
	//TODO update this javadoc
	/***/
    public static int MAX_CRAWL_PAGES = 2;
    
    //TODO update this javadoc
  	/***/
    public static String DATABASE_USER = "root";

    //TODO update this javadoc
  	/***/
    public static String DATABASE_PASSWORD = "insert password here";
    
    
    //TODO update this javadoc
  	/***/
    public static String DATABASE_CONNECTION_STRING = "jdbc:mysql://localhost/webcrawler";
    
	/**
	 * @return true is debug output is enabled
	 */
	public static boolean isDebugEnabled() {
		return DEBUG_FLAG;
	}

	/**
	 * @return the maximum number of pages that the crawler will visit at any one site.
	 */
	public static int getMAX_CRAWL_PAGES() {
		return MAX_CRAWL_PAGES;
	}

	/**
	 * @return the user name to be used to access the database.
	 */
	public static String getDATABASE_USER() {
		return DATABASE_USER;
	}

	/**
	 * @return the password to be used to access the database.
	 */
	public static String getDATABASE_PASSWORD() {
		return DATABASE_PASSWORD;
	}

	/**
	 * @return the connection string to be used to access the database.
	 */
	public static String getDATABASE_CONNECTION_STRING() {
		return DATABASE_CONNECTION_STRING;
	}
}

