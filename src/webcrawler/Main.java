package webcrawler;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import modules.Logger;
import modules.config;


//TODO update this javadoc
/**
* @author
*/
public class Main {
    /**
     * @param args Any command line arguments to be passed to this program
     * @throws SQLException if a database error occurs while creating or closing the database connection
     */
    public static void main(String[] args) throws SQLException {
        Logger.debug("Starting Crawl");
        //		if (args.length > 0) {
        //			if (args[0].equalsIgnoreCase("-debug")) {
        //				config.DEBUG_FLAG = true;
        //			}
        //		}
        config.DEBUG_FLAG = true;

        //opening connection to the Database
        /*Class.forName("com.mysql.jdbc.Driver").newInstance();*/
        Logger.debug("Opening DB Connection");
        
        //TODO update the SQL script and all SQL statements in all java files
        try (Connection con = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
        												config.getDATABASE_USER(),
        												config.getDATABASE_PASSWORD()) ) {

	        //performing the crawl
	        Logger.debug("Loading SiteFormats");
	        List<File> excelFiles = new ArrayList<File>();
	        //TODO populate excelFiles
	        
	        File f = new File(config.getSITEFORMAT_DIRECTORY());
	        Logger.debug(f.getAbsolutePath());
	        
	        String[] list = f.list();
	        for (int i = 0; i < list.length; i++) {
	        	 Logger.debug("Found a siteFormat to use: " + list[i]);
	        	 excelFiles.add( new File(f, list[i]) );
	        }
	
	        Logger.debug("Creating the Crawler");
	        Crawler crawler = new Crawler(con, excelFiles);
	
	        //crawling the sites
	        Logger.debug("Executing the Crawl");
	        crawler.crawl();
	
	        //saving the results
	        Logger.debug("Saving results");
	        crawler.save(con);
	        
	        crawler.list(con);
	
	        Logger.debug("Crawl Completed Successfully");
	        
        } finally {
        	Logger.debug("Exiting");
        }
    }
}
