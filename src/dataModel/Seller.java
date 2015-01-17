package dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import modules.Logger;


//TODO update this javadoc
/**
* @author
*/
public class Seller extends Entity {
    // Real attributes
    private int id;
    private String name;

    // Reference attributes
    private Vector<Item> items = new Vector<Item>();

    /** This constructor loads a Seller from the database based on a given id
     * @param pConn The database connection to use
     * @param pintEntityID The id of the Seller to load
     * @param pblnIsLoadRecursive true to load related Entities
     */
    public Seller(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
        super(pConn, pintEntityID, pblnIsLoadRecursive);
    }

//    /**
//     * @param pConn
//     * @param id
//     * @param name
//     */
//    public Seller(Connection pConn, int id, String name) {
//        this.id = id;
//        this.name = name;
//
//        loadReferences(pConn);
//    }

    /** This constructor creates a new seller with a given database id. 
     * @param id the database id to use
     * @param name the name of the seller
     */
    public Seller(int id, String name) {
        this.id = id;
        this.name = name;
        setState(State.unchanged);
    }

    /** This constructor creates a new Seller. make sure to run the save() method if you want to
     * persist this seller to the database
     * @param name The name of the seller
     */
    public Seller(String name) {
        this.name = name;
        setState(State.added);
    }

    /** This method loads all existing sellers from the database
     * @param conn The database connection to use
     * @return A list of sellers loaded from the database
     */
    public static List<Seller> loadSellersFromDB(Connection conn) {

        //TODO load all existing sellers from the DB
        List<Seller> sellers = new LinkedList<Seller>();
        
        Statement statement = null;
        try {
        	Logger.debug("Selecting sellers from the DB");
        	statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Seller");

            Logger.debug("Select complete");
            
            //if there is at least one result
            if (result.first()) {
            	do {
            		//create a new result with the current result
            		Seller newSeller = new Seller(result.getInt("id"), result.getString("name"));
            		sellers.add(newSeller);
            	
            	//loop as long as there are more results
            	} while (result.next());
            }
            
        } catch (SQLException e) {
        	Logger.error("An error occured loading sellers from the database", e);
        } finally {
            try {
                if (statement != null) {
                	statement.close();
                }
            } catch (SQLException e) {
            	Logger.error("An error occured closing a database statement while loading sellers from the databse", e);
            }
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
    public Vector<Item> getItems() {
        return items;
    }

    /**
     * @param items The new items to be associated with this seller
     */
    public void setItems(Vector<Item> items) {
        this.items = items;
    }

    //	public Vector<SiteFormat> getSiteFormats() {
    //		return siteFormats;
    //	}
    //
    //	public void setSiteFormats(Vector<SiteFormat> siteFormats) {
    //		this.siteFormats = siteFormats;
    //	}

    /**
     * @return the database id of this seller
     */
    public int getId() {
        return id;
    }

    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
        Statement stmtNew = null;
        try {
            stmtNew = pConn.createStatement();
            ResultSet newResult = stmtNew.executeQuery("SELECT * FROM Seller WHERE seller_id = " + pintEntityID);

            if (newResult.first()) {
                this.id = newResult.getInt("id");
                this.name = newResult.getString("name");

                if (pblnIsLoadRecursive) {
                    loadReferences(pConn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmtNew != null) {
                    stmtNew.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** This method loads entities related to this seller
     * @param pConn The databsa connection to use
     */
    protected void loadReferences(Connection pConn) {
        Statement stmtNew = null;
        ResultSet rsNew = null;
        try {
            stmtNew = pConn.createStatement();

            rsNew = stmtNew.executeQuery("SELECT * FROM Item WHERE seller_id = " + this.id);
            while (rsNew.next()) {
                this.items.add(new Item(rsNew.getInt("id"), this, rsNew.getBoolean("active_flag")));
            }

            //			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat WHERE seller_id = " + this.id);
            //			while (rsNew.next()) {
            //				this.siteFormats.add(new SiteFormat(rsNew.getInt("id"), this, new AttributePattern(pConn, rsNew.getInt("siteFormat_nodeObject_id"), true), rsNew.getString("url"), rsNew
            //						.getString("url_page_id")));
            //			}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsNew != null) {
                    rsNew.close();
                }
                if (stmtNew != null) {
                    stmtNew.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        // TODO Implement This
        int intResult = 0;
 
        try {
            int intGenKey =
                super.executeInsert(pConn,
                                    String.format("INSERT INTO Seller(name) VALUES(%s)", this.getName()));
            this.id = intGenKey;
            intResult++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intResult;
        
    }

    @Override
    protected int delete(Connection pConn) {
        // not implemented and not planned
        return 0;
    }

    @Override
    public String toString() {
        String rv = "Code_NodeObject_Attribute:" + id + " " + name + " " + " {" + super.toString() + "}\n";
        //		for (SiteFormat sf: siteFormats) {
        //			rv += sf.toString();
        //			rv += "\n";
        //		}


        return rv;
    }
}
