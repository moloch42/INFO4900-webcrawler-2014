package dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import modules.Logger;

/**
* This class represents a single item that has been parsed by the crawler from
* an HTML document. it contains information about the Seller this item is related to as
* well as a list of the attributes that were parsed for the item based on the Excel template.
*/
public class Item extends Entity {

    private int id;
    private boolean active_flag;
    private Seller seller;
    private List<ItemAttribute> itemAttributes;

    /** This constructor is used to load a given Item from the database.
     * @param pConn The database connection to be used to load this Item
     * @param pintEntityID The database id of the item to be loaded
     * @param pblnIsLoadRecursive true of referenced entities should be loaded
     */
    public Item(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
    	this.itemAttributes = new LinkedList<ItemAttribute>();
    	this.load(pConn, pintEntityID, pblnIsLoadRecursive);
        setState(State.unchanged);
    }

    /** This constructor is used to create an Item with a given database ID.
     * @param id The database id to be used
     * @param seller The seller related to this item
     * @param active_flag The active flag of this Item.
     */
    public Item(int id, Seller seller, boolean active_flag) {
        this.id = id;
        this.seller = seller;
        this.active_flag = active_flag;
        this.itemAttributes = new LinkedList<ItemAttribute>();
        setState(State.unchanged);
    }

    /** This constructor is used to create a new Item. make sure to save it with the save() method
     * if you want to persist it to the database.
     * @param seller The seller related to this item.
     */
    public Item(Seller seller) {
        this.seller = seller;
        this.active_flag = true;
        this.itemAttributes = new LinkedList<ItemAttribute>();
        setState(State.added);
    }

    /**
     * @return The id of the seller related to this item.
     */
    public int getSeller_id() {
        if (seller != null) {
            return seller.getId();
        }
        return 0;
    }

    /**
     * @return the Seller associated with this item
     */
    public Seller getSeller() {
        return seller;
    }

    /**
     * @param seller the Seller to be associated with this item
     */
    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    /**
     * @return true if this item is active
     */
    public boolean isActive_flag() {
        return active_flag;
    }

    /**
     * @return The attributes that this Item has
     */
    public List<ItemAttribute> getItemAttributes() {
        return itemAttributes;
    }

    /**
     * @param itemAttributes The attributes that this Item will have
     */
    public void setItemAttributes(List<ItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    /** Add a new Attribute to this Item by name and value
     * @param name The name of the attribute to be added
     * @param value The value of the attribute to be added.
     */
    public void addAttribute(AttributeName name, String value) {
        this.itemAttributes.add(new ItemAttribute(this, name, value));
    }

    /** Add a new Attribute to this Item
     * @param att The attribute to be added
     */
    public void addAttribute(ItemAttribute att) {
        this.itemAttributes.add(att);
    }

    /**
     * @return The database id of this Item
     */
    public int getId() {
        return id;
    }

    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
    	
    	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM item WHERE item_id = ?")) {
    		statement.setInt(1,pintEntityID);
    		statement.execute();
    		ResultSet rsNew = statement.getResultSet();

            if (rsNew.first()) {
            	Logger.debug("Loading Item from DB with id=" + pintEntityID);
                this.id = rsNew.getInt("item_id");
                this.active_flag = rsNew.getBoolean("active_flag");
                
                if (pblnIsLoadRecursive) {
                	loadReferences(pConn, rsNew.getInt("seller_id"));
                }
            } else {
            	Logger.debug("Failed to load Item from DB with id=" + pintEntityID);
            }
        } catch (SQLException e) {
        	Logger.error("An error occured while loading item with id=" + pintEntityID, e);
        }
    }

    /**
     * @param pConn The connection to be used when loading entities related to this Item
     * @param seller_id The id of the seller related to this Item
     */
    protected void loadReferences(Connection pConn, int seller_id) {

        try(Statement stmtNew= pConn.createStatement()) {

        	Logger.debug("Loading ItemAttributes from DB with id=" + this.id);

        	//load all ItemAttributes related to this item
        	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM item_attribute WHERE item_id = ?")) {
        		statement.setInt(1,this.id);
        		statement.execute();
        		ResultSet attributes = statement.getResultSet();
	            while (attributes.next()) {
	            	Logger.debug("----Attribute Found: " + attributes.getString("attribute_value"));
	            	this.itemAttributes.add(
	                		new ItemAttribute(this,
	                							new AttributeName(pConn, attributes.getInt("attribute_name_fk"), false),
	                							attributes.getString("attribute_value")));
	            }
            }

        	//load the Seller related to this item
        	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM seller WHERE seller_id = ?")) {
        		statement.setInt(1, seller_id);
        		statement.execute();
        		
        		ResultSet sellers = statement.getResultSet();
	            if (sellers.first()) {
	                seller = new Seller(sellers.getInt("seller_id"), sellers.getString("name"));
	            }
        	}
        } catch (SQLException e) {
            Logger.error("An error occured while loading references of item with id=" + id, e);
        }
    }

    @Override
    public int saveReferences(Connection pConn) {
        int intResult = 0;
        try {
            Iterator<ItemAttribute> atts = itemAttributes.iterator();
            while (atts.hasNext()) {
                intResult += atts.next().save(pConn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intResult;
    }

    @Override
    protected int update(Connection pConn) {
        
    	try (PreparedStatement statement = pConn.prepareStatement("UPDATE item SET seller_id = ?, active_flag = ? WHERE id = ?")) {
        	statement.setInt(1, this.seller.getId());
        	statement.setBoolean(2, this.active_flag);
        	statement.setInt(3, this.id);
            
        	return statement.executeUpdate();

        } catch (SQLException e) {
        	Logger.error("An error occured while executing the SQL Update for item id: " + this.id, e);
        }
    	
        return 0;
    }

    @Override
    protected int insert(Connection pConn) {
        int intResult = 0;
        
        try (PreparedStatement statement = pConn.prepareStatement("INSERT INTO item(seller_id, active_flag) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
        	statement.setInt(1, this.getSeller_id());
        	statement.setBoolean(2, this.active_flag);
            statement.execute();
            
            // Retrieve the primary keys of any inserted rows
            ResultSet insertedKeys;
            insertedKeys = statement.getGeneratedKeys();
            
            // If there is a successfully inserted row, use the first row's primary key as this object's ID
            if (insertedKeys.first()) {
            	this.id = insertedKeys.getInt(1);
            }
                        
            setState(State.unchanged);
            intResult++;
        } catch (SQLException e) {
        	 e.printStackTrace();
        }
        return intResult;
    }

    @Override
    protected int delete(Connection pConn) {
        // not implemented and not planned
        return 0;
    }
    
    
    
    /** This method loads all existing sellers from the database
     * @param conn The database connection to use
     * @return A list of sellers loaded from the database
     */
    public static List<Item> loadItemsFromDB(Connection conn) {

        List<Item> items = new LinkedList<Item>();
        
        Logger.debug("Selecting items from the DB");
        try (Statement statement = conn.createStatement();
        	ResultSet result = statement.executeQuery("SELECT item_id FROM item");
        ){
        	
            Logger.debug("Select complete");
            
            //if there is at least one result
            if (result.first()) {
            	do {
            		//create a new result with the current result

            		Item newItem = new Item(conn, result.getInt("item_id"), true);
            		items.add(newItem);
            	
            	//loop as long as there are more results
            	} while (result.next());
            }
            
        } catch (SQLException e) {
        	Logger.error("An error occured loading items from the database", e);
        }

        Logger.debug("Done loading items from the DB");
        return items;
    }
    
    @Override
    public String toString() {
    	String rv = "Item: {\n";
    	rv += "id: " + id + "\n";
    	rv += "active_flag: " + active_flag + "\n";
    	rv += seller.toString() + "\n";
    	for (ItemAttribute a: itemAttributes) {
    		rv += a.toString() + "\n";
    	}
    	rv +="}";
    	
    	return rv;
    }
    
}
