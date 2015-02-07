package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import modules.Logger;

//TODO update this javadoc
/**
* @author
*/
public class ItemAttribute extends Entity {
    // Real attributes
    private String value;

    // Referenced attributes
    private AttributeName attributeName;
    private Item parentItem;

    /** this constructor loads an ItemAttribute from the DB based on a given id
     * @param pConn The database connection to be used to load the ItemAttribute
     * @param pintNodeObject_attribute_id the database id of the ItemAttribute to be loaded
     * @param pblnIsLoadRecursive true to load related entities
     */
    public ItemAttribute(Connection pConn, int pintNodeObject_attribute_id, boolean pblnIsLoadRecursive) {
        super(pConn, pintNodeObject_attribute_id, pblnIsLoadRecursive);
    }

//    /** 
//     * @param pConn
//     * @param nodeObject_id
//     * @param code_nodeObject_attribute_id
//     * @param value
//     */
//    public ItemAttribute(Connection pConn, int nodeObject_id, int code_nodeObject_attribute_id, String value) {
//        this.value = value;
//
//        loadReferences(pConn, nodeObject_id, code_nodeObject_attribute_id);
//    }

    /** This constructor creates a new ItemAttribute. make sure to run the save() method if you want
     * to persist it to the database
     * @param parent The item that this Attribute is related to
     * @param attributeName The name of this attribute
     * @param value The value of this attribute
     */
    public ItemAttribute(Item parent, AttributeName attributeName, String value) {
        this.parentItem = parent;
        this.attributeName = attributeName;
        this.value = value;
        setState(State.added);
        Logger.debug("Created new ItemAttribute of type: " + attributeName.getName());
    }

    /**
     * @return the id of the parent item
     */
    public int getItem_id() {
        if (parentItem != null) {
            return parentItem.getId();
        }
        return 0;
    }

    //	public void setAttributeNameID(int id) {
    //		
    //	}

    /**
     * @return the id of the attribute name
     */
    public int getAttributeNameID() {
        if (this.attributeName != null) {
            return this.attributeName.getId();
        }
        return 0;
    }

    //	public void setCode_nodeObject_attribute_id(int code_nodeObject_attribute_id) {
    //		//
    //	}

    /**
     * @return the AttributeName object
     */
    public AttributeName getAttributeName() {
        return attributeName;
    }

//    /**
//     * @param attributeName
//     */
//    public void setAttributeName(AttributeName attributeName) {
//        this.attributeName = attributeName;
//    }

    /**
     * @return The value of this attribute
     */
    public String getValue() {
        return value;
    }

//    /**
//     * @param value
//     */
//    public void setValue(String value) {
//        this.value = value;
//    }

    /**
     * @return the parent Item of this ItemAttribute
     */
    public Item getParentItem() {
        return parentItem;
    }

//    /**
//     * @param parent
//     */
//    public void setParentItem(Item parent) {
//        this.parentItem = parent;
//    }

    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
//        Statement newStatement = null;
        try (Statement newStatement = pConn.createStatement();
        	ResultSet newResult = newStatement.executeQuery("SELECT * FROM item_attribute WHERE attribute_id = " + pintEntityID);
        ) {
//            newStatement = pConn.createStatement();
//            ResultSet newResult =
//                newStatement.executeQuery("SELECT * FROM Item_Attribute WHERE attribute_id = " + pintEntityID);

            if (newResult.first()) {
                this.value = newResult.getString("attribute_value");

                if (pblnIsLoadRecursive) {
                    loadReferences(pConn, newResult.getInt("item_id"),
                                   newResult.getInt("attribute_name_fk"));
                }
            }
        } catch (SQLException e) {
            Logger.error("An error occured while loading an ItemAttribute with id=" + pintEntityID, e);
        }
    }

    /** Load entities related to this ItemAttribute
     * @param pConn The database connection to use
     * @param pintItem_id The id of the item to load
     * @param pintAttributeName_id The id AttributeName to load
     */
    protected void loadReferences(Connection pConn, int pintItem_id, int pintAttributeName_id) {
            this.attributeName = new AttributeName(pConn, pintAttributeName_id, false);
            this.parentItem = new Item(pConn, pintItem_id, false);
    }

    @Override
    public int saveReferences(Connection pConn) {
        return this.attributeName.save(pConn);
    }

    @Override
    protected int update(Connection pConn) {
        return super.executeUpdate(pConn,
                                   String.format("UPDATE item_attribute SET value = %s WHERE item_id = %d AND attribute_id = %d",
                                                 this.value, this.parentItem.getId(), this.attributeName.getId()));
    }

    @Override
    protected int insert(Connection conn) {
        int intResult = 0;

        Logger.debug("Saving Attribute: item_id=" + parentItem.getId()
        		+ " attribute_id=" + attributeName.getId()
        		+ " attribute_value=" + value
        		+ " attribute_name_fk=" + this.attributeName.getId());

        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO item_attribute(item_id, attribute_value, attribute_name_fk) VALUES(?, ?, ?)")) {
        	statement.setInt(1, parentItem.getId());
        	statement.setString(2, value);
        	statement.setInt(3, attributeName.getId());
            statement.execute();
            
            setState(State.unchanged);
            intResult++;
        } catch (SQLException e) {
        	Logger.error("An error occured while saving an ItemAttribute: " + this.toString(), e);
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
    	String rv = "ItemAttribute: {\n";
    	rv += "name: " + attributeName.toString() + "\n";
    	rv += "value: " + value + "\n";
    	rv += "}";
        return rv;
    }
}
