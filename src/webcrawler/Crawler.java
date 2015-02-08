package webcrawler;

import dataModel.AttributeName;
import dataModel.Item;
import dataModel.Seller;
import dataModel.SiteFormat;
import dataModel.exceptions.SiteFormatException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import modules.Logger;
import modules.config;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
* This class implements the core logic of the crawler.
* It loads templates from Excel, crawls the given web sites,
* and saves the results.
*/
public class Crawler {


    /**
     * @associates <{dataModel.Seller}>
     */
    private Map<String, Seller> sellers;

    /**
     * @associates <{dataModel.AttributeName}>
     */
    private Map<String, AttributeName> attributes;

    private List<SiteFormat> sitesToCrawl;

    private List<Item> parsedItems;


    /** Create a new Crawler object.
     * @param conn The database connection that this crawler will use
     * @param excelSheets a list of excel files containing SiteFormat information
     */
    public Crawler(Connection conn, List<File> excelSheets) {
    	
    	sellers = new HashMap<String, Seller>();
    	attributes = new HashMap<String, AttributeName>();
    	sitesToCrawl = new LinkedList<SiteFormat>();
    	parsedItems = new LinkedList<Item>();
    	
    	Logger.debug("Loading existing Sellers from the Database");
    	//Load all existing Sellers from the database and store them in the map keyed by their name
    	List<Seller> existingSellers = Seller.loadSellersFromDB(conn);
    	for (Seller s: existingSellers) {
    		Logger.debug("----Found Existing Seller: " + s.getName());
    		sellers.put(s.getName(), s);
    	}

    	Logger.debug("Loading existing AttributeNames from the Database");
    	//Load all existing AttributeNames from the database and store them in the map keyed by their name
    	List<AttributeName> existingAttributes = AttributeName.loadAttributeNamesFromDB(conn);
    	for (AttributeName a: existingAttributes) {
    		Logger.debug("----Found Existing AttributeName: " + a.getName());
    		attributes.put(a.getName(), a);
    	}
    	
    	Logger.debug("Loading SiteFormats from the Excel Sheets");
    	//load SiteFormats from the directory.
    	for (File f: excelSheets) {
    		//Create new SiteFormats and add them to the list of sites to crawl
    		try {
    			SiteFormat newSite = new SiteFormat(conn, sellers, attributes, f);
    			Logger.debug("----Loaded SiteFormat for: " + newSite.getSeller().getName() + " at " + newSite.getURL());
    			sitesToCrawl.add(newSite);
    		} catch (SiteFormatException e) {
    			Logger.error("Failed to Load SiteFormat from file: " + f.getName(), e);
    		}
    	}
 
    }
    
    /** This method executes the web crawl. It retrieves the HTML for each siteFormat
     * and parses the items from the HTML
     * */
    public void crawl() {
    	HtmlCleaner cleaner = new HtmlCleaner();
    	
    	//go through each of the Sites
    	for (SiteFormat format: sitesToCrawl) {
    		
    		//go through all of the pages up to the maximum number of pages
    		for (int intPageCount = 1; intPageCount <= config.MAX_CRAWL_PAGES; intPageCount++) {
    			String myURL = format.getURL() + format.getQueryStringPagingSufix() + intPageCount;
    			Logger.debug("getting and cleaning web document from '" + myURL + "'");
    			
    			try {
    				//Retrieve the HTML and parse it into a TagNode
    				TagNode cleanHTML = cleaner.clean(new URL(myURL));
    				
    				//Parse the items out of the TagNode
    				List<Item> items = format.parseItems(cleanHTML);
    				parsedItems.addAll(items);
    				
    			} catch (MalformedURLException e) {
    				Logger.error("A malformed URL was encounterd while crawling: " + myURL, e);
    			} catch (IOException e) {
    				Logger.error("An Unknown IOException occured while crawling: " + myURL, e);
    			}
    		}
    	}
    }
    
    /** This method saves all items contained in this crawler to the database.
     * @param conn The database connection to be used.
     */
    public void save(Connection conn) {
    	for (Item i: parsedItems) {
    		Logger.debug("----Saving Item: " + i.toString());
    		i.save(conn);
    	}
    }
    
    /** This method lists all items that currently exist in the database.
     * @param conn
     */
    public void list(Connection conn) {
    	
    	List<Item> items = Item.loadItemsFromDB(conn);
    	
    	for (Item i: items) {
    		Logger.debug(i.toString());
    		Logger.debug("");
    		Logger.debug("");
    	}	
    }

}
