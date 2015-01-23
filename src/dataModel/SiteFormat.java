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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.htmlcleaner.TagNode;

import dataModel.exceptions.SiteFormatException;


//TODO update this javadoc
/**
* @author
*/
public class SiteFormat {

	private String siteUrl;
	private String queryStringPagingSuffix;
	private Seller seller;
	private AttributePattern itemRootPattern;

    /**
     * @associates <{dataModel.SiteFormatAttribute}>
     */
    private List<SiteFormatAttribute> attributes;

    
	/** This constructor loads a SiteFormat from a given excel spreadsheet file. If the SiteFormat
     * contains a new seller or new Attribute names these will be saved to the database.
     * @param con The database connection to be used
     * @param existingSellers A Map relating seller names to Seller objects for all existing sellers
     * @param existingAttributeNames  A Map relating attribute names to AttributeName objects for all existing AttributeName
     * @param excelSheet the file containing the SiteFormat data
	 * @throws SiteFormatException if the given file does not parse correctly
     */
        public SiteFormat(Connection con, Map<String, Seller> existingSellers, Map<String, AttributeName> existingAttributeNames, File excelSheet) throws SiteFormatException {
        	
        	attributes = new LinkedList<SiteFormatAttribute>();
        	
            //Load the Excel file into a Workbook Object and select the first sheet
            try (Workbook book = WorkbookFactory.create(excelSheet);){

            	Sheet workSheet = book.getSheetAt(0);
	            DataFormatter formatter = new DataFormatter();
	            
	            //initialize the values we need during parsing
	            seller = null;
	            siteUrl = null;
	            queryStringPagingSuffix = null;
	            
	            itemRootPattern = null;
	            boolean rootPattern = false;
	                
	            String elementName = null;
	            String elementAttributeName = null;
	            String elementAttributeValue = null;
	            
	            String elementContentAttributeName = null;
	            
	            List<AttributePattern> patterns = new LinkedList<AttributePattern>();
	            AttributeName currentAttribute = null;
	            
	            
	            //loop through the rows of the sheet
	            for (Iterator<Row> rows = workSheet.rowIterator(); rows.hasNext();) {
	                Row r = rows.next();
	                
	                //get the values of the first 2 cells of this row
	                Cell l = r.getCell(0);
	                String label = "";
	                if (l != null) {
	                	label = formatter.formatCellValue(l).toLowerCase();
	                }
	                
	                Cell c = r.getCell(1);
	                String value = null;
	                if (c != null) {
	                	if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
	                		value = formatter.formatCellValue(c);
	                	}
	                }
	                Logger.debug(("Label=" + label + ": Value=" + value));
	                
	                
	                //take action based on the row label
	                switch (label) {
	                	case "":
	                		//this row is empty, skip it.
	                		break;
	                		
	                    case "seller":
	                    	//if the seller is already set then it is an error to have a second one
	                        if (seller != null) {
	                            throw new SiteFormatException("Duplicate Seller Name on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                        }
	                        
	                        //set the seller from the map of existing sellers if it is there, otherwise create and save a new one
	                        if (existingSellers.containsKey(value)) {
	                            seller = existingSellers.get(value);
	                        }
	                        else {
	                            seller = new Seller(value);
	                            seller.save(con);
	                        }
	                        break;
	                        
	                    case "searchurl":
	                    	//if the site URL is already set then it is an error to have a second one
	                        if (siteUrl != null) {
	                        	 throw new SiteFormatException("Duplicate Site URL on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                        }
	                        siteUrl = value;
	                        break;
	                        
	                    case "searchurl_pageparameter":
	                    	//if the paging suffix is already set then it is an error to have a second one
	                        if (queryStringPagingSuffix != null) {
	                        	 throw new SiteFormatException("Duplicate Site URL Paging Parameter on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                        }
	                        queryStringPagingSuffix = value;
	                        break;
	                        
	                    case "product_tags":
	                    	//if we are already parsing a root pattern, or if the root pattern is already parsed, then it is an error to try and start a new one
	                    	if (rootPattern || itemRootPattern != null) {
	                    		throw new SiteFormatException("Duplicate product_tags element on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	rootPattern = true;
	                        break;
	                        
	                    case "item_attribute_name":
	                    	
	                    	// when we encounter the start of a new attribute pattern, check to see if we were parsing an old attribute pattern
	                    	if (elementName != null || elementAttributeName != null || elementAttributeValue != null) {
	                    		
	                    		//if we were, then finish it off
	                    		AttributePattern newPattern = new AttributePattern(elementName, elementAttributeName, elementAttributeValue);
	                    		
	                    		//and store it in the right place
	                    		if (rootPattern) {
	                    			itemRootPattern = newPattern;
	                    			rootPattern = false;
	                    		} else {
	                    			patterns.add(newPattern);
	                    		}
	                    		
	                    		//then start a new pattern
	                    		elementName = null;
	                            elementAttributeName = null;
	                            elementAttributeValue = null;
	                    		
	                    	}
	                    	
	                    	//the value of an attribute_name element can not be null
	                    	if (value == null) {
	                    		throw new SiteFormatException("attribute_name with no value on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	
	                    	
	                    	// if we are not working on an existing Attribute, then get an AttributeName to start on
	                    	if (currentAttribute == null) {
	                    		currentAttribute = getAttributeNameFromMap(value, existingAttributeNames, con);
	                    	}
	                    	
	                    	//if we are working on an existing attribute and this attribute is new, then finish the old attribute off
	                    	// and start a new one
	                    	else if (!value.equalsIgnoreCase(currentAttribute.getName())) {
	                    		
	                    		//last attribute finished
	                    		SiteFormatAttribute newSiteFormatAttribute = new SiteFormatAttribute(currentAttribute, patterns, elementContentAttributeName);
	                    		attributes.add(newSiteFormatAttribute);
	                    
	                            elementContentAttributeName = null;
	                            patterns = new LinkedList<AttributePattern>();
	                            currentAttribute = getAttributeNameFromMap(value, existingAttributeNames, con);
	                    	}
	                        
	                        break;
	                        
	                    case "html_tag":
	                    	if (elementName != null) {
	                    		throw new SiteFormatException("Duplicate element_name '"+ value +"' on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	if (!rootPattern && currentAttribute == null) {
	                    		throw new SiteFormatException("element_name '"+ value +"' without a preceeding attribute_name or product_tags on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	elementName = value;
	                        break;
	                        
	                    case "html_tag_attribute_name":
	                    	if (elementAttributeName != null) {
	                    		throw new SiteFormatException("Duplicate element_attribute_name '"+ value +"' on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	if (!rootPattern && currentAttribute == null) {
	                    		throw new SiteFormatException("element_attribute_name '"+ value +"' without a preceeding attribute_name or product_tags on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	elementAttributeName = value;
	                        break;
	                        
	                    case "html_tag_attribute_value":
	                    	if (elementAttributeValue != null) {
	                    		throw new SiteFormatException("Duplicate element_attribute_value '"+ value +"' on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	if (!rootPattern && currentAttribute == null) {
	                    		throw new SiteFormatException("element_attribute_value '"+ value +"' without a preceeding attribute_name or product_tags on line " + r.getRowNum() + " of excel sheet"  + excelSheet.getName());
	                    	}
	                    	elementAttributeValue = value;
	                        break;
	                        
	                    case "content_location":
	                    	if (elementContentAttributeName != null) {
	                    		throw new SiteFormatException("Duplicate element_content_attribute_name '"+ value +"' on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	if (!rootPattern && currentAttribute == null) {
	                    		throw new SiteFormatException("element_content_attribute_name '"+ value +"' without a preceeding attribute_name or product_tags on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	if (rootPattern) {
	                    		throw new SiteFormatException("a product_tags element can not have an element_content_attribute_name: found on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                    	}
	                    	elementContentAttributeName = value;
	                        break;
	                        
	                    default:
	                    	 throw new SiteFormatException("Invalid Label '" + label +"' on line " + r.getRowNum() + " of excel sheet " + excelSheet.getName());
	                }//end switch statement
	     
	            }//end for loop
	            
	            
	            //finish the last entry
	            if (elementName != null || elementAttributeName != null || elementAttributeValue != null) {
	        		
	        		AttributePattern newPattern = new AttributePattern(elementName, elementAttributeName, elementAttributeValue);
	        		
	        		//and store it in the right place
	        		if (rootPattern) {
	        			itemRootPattern = newPattern;
	        			rootPattern = false;
	        		} else {
	        			patterns.add(newPattern);
	        		}
	        	}
	            SiteFormatAttribute newSiteFormatAttribute = new SiteFormatAttribute(currentAttribute, patterns, elementContentAttributeName);
	    		attributes.add(newSiteFormatAttribute);
	            
            } catch (InvalidFormatException e) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' is not recognised as a valid Excel sheet", e);
            } catch (IOException e) {
            	throw new SiteFormatException("An IOException occured while loading the Excel sheet: " + excelSheet.getName(), e);
            }//end main try statement
            
            
            
            //validate this object. make sure that the parts that need to exist actually exist.
            
            if (siteUrl == null) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' does not contain a SearchURL");
            }
            
            if (queryStringPagingSuffix == null) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' does not contain a SearchURL_PageParameter");
            }
            
            if (seller == null) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' does not contain a Seller");
            }
            
            if (itemRootPattern == null) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' does not contain a Product_Tags Element");
            }
            
            if (attributes.size() == 0) {
            	throw new SiteFormatException("The File '" + excelSheet.getName() + "' does not contain any Attribute_Names");
            }

        }//end SiteFormat constructor

        
        private static AttributeName getAttributeNameFromMap(String value, Map<String, AttributeName> existingAttributeNames, Connection con) {
        	AttributeName currentAttribute;
        	
        	if (existingAttributeNames.containsKey(value)) {
      			 currentAttribute = existingAttributeNames.get(value);
      			 Logger.debug("----Found existing Attribute name '" + value +"'");
            }
            else {
              	 //TODO remove hard coded "String"
              	 currentAttribute = new AttributeName(value, "String");
              	 existingAttributeNames.put(value, currentAttribute);
              	 Logger.debug("----Found new Attribute name '" + value +"'. saving it to the database.");
              	 currentAttribute.save(con);
            }
        	return currentAttribute;
        }


        /**
         * @return the URL of this SiteFormat
         */
        public String getURL() {
            return siteUrl;
        }


        /**
         * @return The Query string parameter to be used for paging with is SiteFormat's URL
         */
        public String getQueryStringPagingSufix() {
            return queryStringPagingSuffix;
        }


        /**
         * @return The seller associated with this SiteFormat
         */
        public Seller getSeller() {
            return seller;
        }

        /**
         * @return the AttributePattern that identifies top level item tags on this site
         */
        public AttributePattern getItemRootPattern() {
            return itemRootPattern;
        }
        
        /**
         * @return the list of SiteFirmatAttributes in this SiteFormat
         */
        public List<SiteFormatAttribute> getAttributes() {
    		return attributes;
    	}

        /** This method parses Item objects out of a given HTML document.
         * @param cleanHTML The HTML document to extract items from
         * @return A list of all items found within the HTML
         */
        public List<Item> parseItems(TagNode cleanHTML) {

        	List<Item> parsedItems = new LinkedList<Item>();
            List<TagNode> rootItems = cleanHTML.getElementListByName(itemRootPattern.getElement_name(), true);

            for (TagNode t: rootItems) {
            	
            	//skip this node if either the attribute name of the attribute value don't match
            	if (  itemRootPattern.getElement_attribute_name() != null && !t.hasAttribute(itemRootPattern.getElement_attribute_name())) {
            		continue;
            	}
            	if (  itemRootPattern.getElement_attribute_value() != null && !t.getAttributeByName(itemRootPattern.getElement_attribute_name()).equals(itemRootPattern.getElement_attribute_value()) ) {
            		continue;
            	}

            	//create a new Item
	            Item item = new Item(seller);

	            for (SiteFormatAttribute a : attributes) {
	            	//parse each attribute for the item
	                List<ItemAttribute> atts = a.parseItemAttribute(item, t);
	                
	                //add the attribute to the item
	                for (ItemAttribute att : atts) {
	                    item.addAttribute(att);
	                }
	            }
	
	            //add the item to the list
	            parsedItems.add(item);
            	
            }
            return parsedItems;
        }

	@Override
	public String toString() {
	    return "SiteFormat: " + siteUrl + " " + queryStringPagingSuffix + " {" + "}";
	}
}
