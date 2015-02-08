package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import modules.Logger;

/**
* This class represents a single seller as defined in an Excel template.
* It contains a list of all items associated with the seller.
*/
public class Seller extends Entity {

    private int id;
    private String name;
    private List<Item> items;

    /** This constructor loads a Seller from the database based on a given id
     * @param pConn The database connection to use
     * @param pintEntityID The id of the Seller to load
     * @param pblnIsLoadRecursive true to load related Entities
     */
    public Seller(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
        
    	this.items = new LinkedList<Item>();
        this.load(pConn, pintEntityID, pblnIsLoadRecursive);
        setState(State.unchanged);
    }

    /** This constructor creates a new seller with a given database id. 
     * @param id the database id to use
     * @param name the name of the seller
     */
    public Seller(int id, String name) {
    	this.items = new LinkedList<Item>();
        this.id = id;
        this.name = name;
        setState(State.unchanged);
    }

    /** This constructor creates a new Seller. make sure to run the save() method if you want to
     * persist this seller to the database
     * @param name The name of the seller
     */
    public Seller(String name) {
    	this.items = new LinkedList<Item>();
        this.name = name;
        setState(State.added);
    }

    /** This method loads all existing sellers from the database
     * @param conn The database connection to use
     * @return A list of sellers loaded from the database
     */
    public static List<Seller> loadSellersFromDB(Connection conn) {

        List<Seller> sellers = new LinkedList<Seller>();
        
        Logger.debug("Selecting sellers from the DB");
        try (Statement statement = conn.createStatement();
        	ResultSet result = statement.executeQuery("SELECT * FROM seller");
        ){
        	
            Logger.debug("Select complete");
            
            //if there is at least one result
            if (result.first()) {
            	do {
            		//create a new result with the current result
            		Seller newSeller = new Seller(result.getInt("seller_id"), result.getString("name"));
            		sellers.add(newSeller);
            	
            	//loop as long as there are more results
            	} while (result.next());
            }
            
        } catch (SQLException e) {
        	Logger.error("An error occured loading sellers from the database", e);
        }

        Logger.debug("Done loading Sellers from the DB");
        return sellers;
    }

    /**
     * @return The name of this seller
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The new name for this seller
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The items associated with this seller
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items The new items to be associated with this seller
     */
    public void setItems(Vector<Item> items) {
        this.items = items;
    }

    /**
     * @return the database id of this seller
     */
    public int getId() {
        return id;
    }

    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
    	
    	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM seller WHERE seller_id = ?")) {
    		statement.setInt(1,pintEntityID);
    		statement.execute();
    		ResultSet newResult = statement.getResultSet();

            if (newResult.first()) {
                this.id = newResult.getInt("seller_id");
                this.name = newResult.getString("name");

                if (pblnIsLoadRecursive) {
                    loadReferences(pConn);
                }
            }
        } catch (SQLException e) {
            Logger.error("An error occured while loading a Seller with id="+pintEntityID, e);
        }
    }

    /** This method loads entities related to this seller
     * @param pConn The database connection to use
     */
    protected void loadReferences(Connection pConn) {

    	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM item WHERE seller_id = ?")) {
    		statement.setInt(1,this.id);
    		statement.execute();
    		ResultSet rsNew = statement.getResultSet();
        	
            while (rsNew.next()) {
                this.items.add(new Item(rsNew.getInt("seller_id"), this, rsNew.getBoolean("active_flag")));
            }

        } catch (SQLException e) {
        	Logger.error("An error occured while loading a Seller with id="+this.id, e);
        }
    }

    @Override
    public int saveReferences(Connection pConn) {
        // not implemented and not planned
        return 0;
    }

    @Override
    protected int update(Connection pConn) {
        // not implemented and not planned
        return 0;
    }

    @Override
    protected int insert(Connection pConn) {

    	int results = 0;

    	Logger.debug("----Saving Seller: '" + name + "' to the database" );
    	
        try ( PreparedStatement statement = pConn.prepareStatement("INSERT INTO seller(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS); ) {
        	
            statement.setString(1, name);
            statement.execute();
            
            results = statement.getUpdateCount();
            try (ResultSet count = statement.getGeneratedKeys()) {
            	if ( count.first() ) {
            		this.id = count.getInt(1);
            	}
            }
            setState(State.unchanged);

        } catch (SQLException e) {
        	Logger.error("An error occured while inserting the seller '" + this.name + "' into the database", e);
        }

        return results;
 
    }

    @Override
    protected int delete(Connection pConn) {
        // not implemented and not planned
        return 0;
    }

    @Override
    public String toString() {
        String rv = "Seller: {\n";
        rv += "id: " +id + "\n";
        rv += "name: " +name + "\n";
        rv += "}";

        return rv;
    }
}
