package dataModel;

import java.io.File;

import java.io.IOException;

import java.sql.Connection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import modules.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.htmlcleaner.TagNode;


//TODO update this javadoc
/**
* @author
*/
public class SiteFormat {
	// Real attributes
//	private int id;
	private String siteUrl;
	private String queryStringPagingSufix;

    // References attributes
	private Seller seller;
	private AttributePattern itemRootPattern;

    /**
     * @associates <{dataModel.SiteFormatAttribute}>
     */
    private List<SiteFormatAttribute> attributes;


    //	public SiteFormat(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
//		super(pConn, pintEntityID, pblnIsLoadRecursive);
//	}

    /** This constructor loads a SiteFormat from a given excel spreadsheet file. If the SiteFormat
     * contains a new seller or new Attribute names these will be saved to the database.
     * @param con The database connection to be used
     * @param existingSellers A Map relating seller names to Seller objects for all existing sellers
     * @param existingAttributeNames  A Map relating attribute names to AttributeName objects for all existing AttributeName
     * @param excelSheet the file containing the SiteFormat data
     */
        public SiteFormat(Connection con, Map<String, Seller> existingSellers, Map<String, AttributeName> existingAttributeNames, File excelSheet/*or java excel object*/) {
            //TODO implement this
            //parse seller from excel
            //if seller exists, use the existing one
            //if not, create a new one and save it
            //parse the url and the paging sufix
            //parse the root pattern
            //loop through the attributes
            //  if the Attribute name already exists use it
            //  if not, create a new one and save it
            //  parse the attribute patterns
            //create a new SiteFormatAttribute and add it to the list
        }


//	public SiteFormat(Connection pConn, int id, int seller_id, int siteFormat_nodeObject_id, String uRL, String url_page_id) {
//		//this.id = id;
//		this.siteUrl = uRL;
//		this.setQueryStringPagingSufix(url_page_id);
//
//		//loadReferences(pConn, seller_id, siteFormat_nodeObject_id);
//	}
//
//	public SiteFormat(int id, Seller seller, AttributePattern siteFormat_nodeObject, String uRL, String url_page_id) {
//		//this.id = id;
//		this.setSeller(seller);
//		this.itemRootPattern = siteFormat_nodeObject;
//		this.siteUrl = uRL;
//		this.setQueryStringPagingSufix(url_page_id);
//	}

        /**
         * @return the URL of this SiteFormat
         */
        public String getURL() {
            return siteUrl;
        }

//        /**
//         * @param uRL
//         */
//        public void setURL(String uRL) {
//            siteUrl = uRL;
//        }

        /**
         * @return The Query string parameter to be used for paging with is SiteFormat's URL
         */
        public String getQueryStringPagingSufix() {
            return queryStringPagingSufix;
        }

//        /**
//         * @param url_page_id
//         */
//        public void setQueryStringPagingSufix(String url_page_id) {
//            this.queryStringPagingSufix = url_page_id;
//        }

        /**
         * @return The seller associated with this SiteFormat
         */
        public Seller getSeller() {
            return seller;
        }

//        /**
//         * @param seller
//         */
//        public void setSeller(Seller seller) {
//            this.seller = seller;
//        }


        /**
         * @return the AttributePattern that identifies top level item tags on this site
         */
        public AttributePattern getItemRootPattern() {
            return itemRootPattern;
        }

//        /**
//         * @param siteFormat_nodeObject
//         */
//        public void setItemRootPattern(AttributePattern siteFormat_nodeObject) {
//            this.itemRootPattern = siteFormat_nodeObject;
//        }

        /** This method parseds Item objects out of a given HTML document.
         * @param cleanHTML The HTML document to extract items from
         * @return A list of all items found within the HTML
         */
        public List<Item> parseItems(TagNode cleanHTML) {
            //TODO implement this method
            //get all of the items using the itemRootPatern
            //for each item create a new item
            //  parse each attribute for the item
            //  add the attribute to the item
            //  add the item to the list
            return new LinkedList<Item>();
        }

//	public int getId() {
//		return id;
//	}

//	@Override
//	public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
//		Statement stmtNew = null;
//		ResultSet rsNew = null;
//		try {
//			stmtNew = pConn.createStatement();
//			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat WHERE id = " + pintEntityID);
//
//			if (rsNew.first()) {
//				this.id = rsNew.getInt("id");
//				this.siteUrl = rsNew.getString("url");
//				this.queryStringPagingSufix = rsNew.getString("url_page_id");
//				if (pblnIsLoadRecursive) {
//					loadReferences(pConn, rsNew.getInt("seller_id"), rsNew.getInt("siteFormat_nodeObject_id"));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rsNew != null) {
//					rsNew.close();
//				}
//				if (stmtNew != null) {
//					stmtNew.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	protected void loadReferences(Connection pConn, int seller_id, int siteFormat_nodeObject_id) {
//		try {
//			if (siteFormat_nodeObject_id >= 1000) {
//				this.itemRootPattern = new AttributePattern(pConn, siteFormat_nodeObject_id, true);
//			}
//			if (seller_id >= 1000) {
//				this.seller = new Seller(pConn, seller_id, false);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	
	@Override
	public String toString() {
//		return "SiteFormat: " + id + " " + siteUrl + " " + queryStringPagingSufix + " {" + super.toString() + "}";
	    return "SiteFormat: " + siteUrl + " " + queryStringPagingSufix + " {" + super.toString() + "}";
	}
}
