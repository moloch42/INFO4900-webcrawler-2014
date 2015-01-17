package webcrawler;

import dataModel.AttributeName;
import dataModel.Item;
import dataModel.Seller;
import dataModel.SiteFormat;

import java.io.File;

import java.net.URL;

import java.sql.Connection;

import java.util.List;
import java.util.Map;

import modules.Logger;
import modules.config;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;


//TODO update this javadoc
/**
* @author
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

//    /**
//     * @associates <{dataModel.SiteFormat}>
//     */
    private List<SiteFormat> sitesToCrawl;

//    /**
//     * @associates <{dataModel.Item}>
//     */
    private List<Item> parsedItems;


    /** Create a new Crawler object.
     * @param conn The database connection that this crawler will use
     * @param excelSheets a list of excel files containing SiteFormat information
     */
    public Crawler(Connection conn, List<File> excelSheets) {
    	
    	Logger.debug("Creating a new Crawler Object");
    	
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
    		SiteFormat newSite = new SiteFormat(conn, sellers, attributes, f);
    		Logger.debug("----Loaded SiteFormat for: " + newSite.getSeller().getName() + " at " + newSite.getURL());
    		sitesToCrawl.add(newSite);
    	}
 
    }
    
    /** This method executes the web crawl. It retrieves the HTML for each siteFormat
     * and parses the items from the HTML
     * */
    public void crawl() {
        //TODO for each site to crawl:
        //get The HTML
        //parse the Items

    }
    
    /** This method saves all items contained in this crawler to the database.
     * @param conn The database connection to be used.
     */
    public void save(Connection conn) {
        //TODO save the results to the DB    
    }

//    public static Vector<Item> getNodeObjects(Connection pConn, int pintSiteFormatID) {
//		Vector<Item> no_vecNew = new Vector<Item>();
//		try {
//			SiteFormat sf = new SiteFormat(pConn, pintSiteFormatID, true);
//			Logger.debug("SiteFormat {" + sf.toString() + "} Created");
//			AttributePattern sfpMain = sf.getItemRootPattern();
//			Logger.debug("SiteFormat_NodeObject {" + sfpMain.toString() + "} Created");
//
//			HtmlCleaner hc = new HtmlCleaner();
//			TagNode tnWebDoc;
//			TagNode[] tn_arrNodeObject;
//			for (int intPageCount = 1; intPageCount <= 2; intPageCount++) {
//				String myUrl = sf.getURL() + sf.getQueryStringPagingSufix() + intPageCount;
//				Logger.debug("getting and cleaning web document from '" + myUrl + "'");
//				tnWebDoc = hc.clean(new URL(myUrl));
//				Logger.debug(tagNodeToString(tnWebDoc, 0));
//				Logger.debug("Searching for " + sfpMain.getElement_attribute_name() + "==" + sfpMain.getElement_attribute_value());
//				tn_arrNodeObject = tnWebDoc.getElementsByAttValue(sfpMain.getElement_attribute_name(), sfpMain.getElement_attribute_value(), true, true);
//
//				for (int intNodeObjectCount = 0; intNodeObjectCount < tn_arrNodeObject.length; intNodeObjectCount++) {
//					no_vecNew.add(getNodeObject(tn_arrNodeObject[intNodeObjectCount], sfpMain, sf.getSeller()));
//				}
//			}
//		} catch (Exception e) {
//			Logger.error("An error occured getting node objects", e);
//		}
//		return no_vecNew;
//	}
//
//	private static Item getNodeObject(TagNode ptnNodeObject, AttributePattern psfpNodeObject, Seller pSeller) {
//		Item noNew = null;
//		try {
//			noNew = new Item();
//			noNew.setActive_flag(true);
//			noNew.setSeller(pSeller);
//
//			noNew.getNodeObject_attributes().addAll(getrNodeObject_Attributes(noNew, ptnNodeObject, psfpNodeObject));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return noNew;
//	}
//
//	private static Vector<ItemAttribute> getrNodeObject_Attributes(Item pnoOwner, TagNode ptnAttribute, AttributePattern psfpNodeObject) {
//		Vector<ItemAttribute> pa_vecNew = new Vector<ItemAttribute>();
//		try {
//			String strAttributeValue, strContent;
//			TagNode[] tn_arrElements;
//			AttributePattern sfpAttribute;
//			ItemAttribute paNew;
//			boolean blnFound;
//
//			Iterator<AttributePattern> itChild_siteFormat_nodeObjects = psfpNodeObject.getChild_siteFormat_nodeObjects().iterator();
//			while (itChild_siteFormat_nodeObjects.hasNext()) {
//				sfpAttribute = itChild_siteFormat_nodeObjects.next();
//				blnFound = false;
//
//				tn_arrElements = ptnAttribute.getElementsByName(sfpAttribute.getElement_name(), true);
//				for (int i = 0; i < tn_arrElements.length; i++) {
//					if (sfpAttribute.getElement_attribute_name() != null) {
//						strAttributeValue = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_attribute_name());
//						if (strAttributeValue != null) {
//							if (strAttributeValue.equals(sfpAttribute.getElement_attribute_value())) {
//								if (sfpAttribute.getCode_nodeObject_attribute_id() >= 1000) {
//									if (sfpAttribute.getElement_content_attribute_name() != null) {
//										strContent = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_content_attribute_name());
//									} else {
//										strContent = tn_arrElements[i].getText().toString();
//									}
//									paNew = new ItemAttribute(pnoOwner, sfpAttribute.getAttributeName(), ContentFilter.cleanInput(strContent));
//									pa_vecNew.add(paNew);
//									blnFound = true;
//								}
//								pa_vecNew.addAll(getrNodeObject_Attributes(pnoOwner, tn_arrElements[i], sfpAttribute));
//								if (blnFound) {
//									break;
//								}
//							}
//						}
//					} else {
//						if (sfpAttribute.getCode_nodeObject_attribute_id() >= 1000) {
//							if (sfpAttribute.getElement_content_attribute_name() != null) {
//								strContent = tn_arrElements[i].getAttributeByName(sfpAttribute.getElement_content_attribute_name());
//							} else {
//								strContent = tn_arrElements[i].getText().toString();
//							}
//							paNew = new ItemAttribute(pnoOwner, sfpAttribute.getAttributeName(), ContentFilter.cleanInput(strContent));
//							pa_vecNew.add(paNew);
//							blnFound = true;
//						}
//						pa_vecNew.addAll(getrNodeObject_Attributes(pnoOwner, tn_arrElements[i], sfpAttribute));
//						if (blnFound) {
//							break;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return pa_vecNew;
//	}
//	
//	
//	public static String tagNodeToString(TagNode node, int indent) {
//		
//		StringBuffer rv = new StringBuffer();
//		
//		for (int i=0; i < indent; i++) {
//			rv.append("\t");
//		}
//		rv.append(node.toString() + ": ");
//		Map<String, String> attributes = node.getAttributes();
//		for (String key: attributes.keySet()) {
//			rv.append(key + "==" + attributes.get(key) + " ");
//		}
//		
//		
//		rv.append("\n");
//		
//		for (TagNode t: node.getChildTags()) {
//			rv.append(tagNodeToString(t, indent+1));
//		}
//
//		return rv.toString();
//	}
}
