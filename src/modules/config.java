package modules;

//TODO update this javadoc
/**
* @author
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
	 * @return
	 */
	public static boolean isDebugEnabled() {
		return DEBUG_FLAG;
	}

	/**
	 * @return
	 */
	public static int getMAX_CRAWL_PAGES() {
		return MAX_CRAWL_PAGES;
	}

	/**
	 * @return
	 */
	public static String getDATABASE_USER() {
		return DATABASE_USER;
	}

	/**
	 * @return
	 */
	public static String getDATABASE_PASSWORD() {
		return DATABASE_PASSWORD;
	}

	/**
	 * @return
	 */
	public static String getDATABASE_CONNECTION_STRING() {
		return DATABASE_CONNECTION_STRING;
	}
}

