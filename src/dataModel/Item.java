package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//TODO update this javadoc
/**
* @author
*/
public class Item extends Entity {
    // Real attributes
    private int id;
    private boolean active_flag;

    // Referenced attributes
    private Seller seller;
    private List<ItemAttribute> itemAttributes = new Vector<ItemAttribute>();

    /** This constructor is used to load a given Item from the database.
     * @param pConn The database connection to be used to load this Item
     * @param pintEntityID The database id of the item to be loaded
     * @param pblnIsLoadRecursive true of referenced entities should be loaded
     */
    public Item(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
        super(pConn, pintEntityID, pblnIsLoadRecursive);
    }

//    /**
//     * @param pConn The database connection to be used to load this Item
//     * @param id
//     * @param seller_id
//     * @param active_flag
//     */
//    public Item(Connection pConn, int id, int seller_id, boolean active_flag) {
//        this.id = id;
//        this.active_flag = active_flag;
//
//        loadReferences(pConn, seller_id);
//    }

    /** This constructor is used to create an Item with a given database ID.
     * @param id The database id to be used
     * @param seller The seller related to this item
     * @param active_flag The active flag of this Item.
     */
    public Item(int id, Seller seller, boolean active_flag) {
        this.id = id;
        this.seller = seller;
        this.active_flag = active_flag;
    }

    /** This constructor is used to create a new Item. make sure to save it with the save() method
     * if you want to persist it to the database.
     * @param seller The seller related to this item.
     */
    public Item(Seller seller) {
        this.seller = seller;
        this.active_flag = true;
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

//    /**
//     * @param seller_id
//     */
//    public void setSeller_id(int seller_id) {
//        // not implemented and not planned
//    }

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

//    /**
//     * @param active_flag
//     */
//    public void setActive_flag(boolean active_flag) {
//        this.active_flag = active_flag;
//    }

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
        Statement stmtNew = null;
        ResultSet rsNew = null;
        try {
            stmtNew = pConn.createStatement();
            rsNew = stmtNew.executeQuery("SELECT * FROM Item WHERE item_id = " + pintEntityID);

            if (rsNew.first()) {
                this.id = rsNew.getInt("id");
                this.active_flag = rsNew.getBoolean("active_flag");

                loadReferences(pConn, rsNew.getInt("seller_id"));
            }
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

    /**
     * @param pConn The connection to be used when loading entities related to this Item
     * @param seller_id The id of the seller related to this Item
     */
    protected void loadReferences(Connection pConn, int seller_id) {
        Statement stmtNew = null;
        ResultSet rsNew = null;
        try {
            stmtNew = pConn.createStatement();
            rsNew = stmtNew.executeQuery("SELECT * FROM Item_Attribute WHERE item_id = " + this.id);
            while (rsNew.next()) {
                this.itemAttributes.add(new ItemAttribute(this,
                                                          new AttributeName(pConn,
                                                                            rsNew.getInt("code_nodeObject_attribute_id"),
                                                                            true), rsNew.getString("value")));
            }
            // rsNew.close();
            rsNew = stmtNew.executeQuery("SELECT * FROM Seller WHERE seller_id = " + seller_id);
            if (rsNew.first()) {
                seller = new Seller(rsNew.getInt("id"), rsNew.getString("name"));
            }
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
        int intResult = 0;
        try {
            Iterator<ItemAttribute> itNodeObject_attributes = itemAttributes.iterator();
            while (itNodeObject_attributes.hasNext()) {
                intResult += itNodeObject_attributes.next().save(pConn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intResult;
    }

    @Override
    protected int update(Connection pConn) {
        return super.executeUpdate(pConn,
                                   String.format("UPDATE Item SET seller_id = %d, active_flag = %b WHERE id = %d",
                                                 this.seller.getId(), this.active_flag, this.id));
    }

    @Override
    protected int insert(Connection pConn) {
        int intResult = 0;
        try {
            int intGenKey =
                super.executeInsert(pConn,
                                    String.format("INSERT INTO Item(seller_id, active_flag) VALUES(%d, %b)",
                                                  this.getSeller_id(), this.active_flag));
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
}
