package modules;

/**
* This class contains configuration options related to the operation of the crawler
*/
public class config {
	
	/**Determines if debug logging should be printed*/
	public static boolean DEBUG_FLAG = false;
	
	/**Specifies how many pages the crawler should traverse for a given site.
	 * Defaulted to a low value so that test runs do not spam sites with requests.*/
    public static int MAX_CRAWL_PAGES = 2;
    
  	/**The username to be used accessing the database*/
    public static String DATABASE_USER = "root";

  	/**The password to be used accessing the database*/
    public static String DATABASE_PASSWORD = "insert password here";
    
  	/**The database connection string*/
    public static String DATABASE_CONNECTION_STRING = "jdbc:mysql://localhost/webcrawler";
    
  	/**The directory to use when looking for the excel templates*/
    public static String SITEFORMAT_DIRECTORY = "ExcelTemplates";
    

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
	
	/**
	 * @return the directory where the SiteFormat Excel Sheets are located
	 */
	public static String getSITEFORMAT_DIRECTORY() {
		return SITEFORMAT_DIRECTORY;
	}

}

