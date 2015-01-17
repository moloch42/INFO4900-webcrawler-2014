package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        Statement newStatement = null;
        try {
            newStatement = pConn.createStatement();
            ResultSet newResult =
                newStatement.executeQuery("SELECT * FROM Item_Attribute WHERE attribute_id = " + pintEntityID);

            if (newResult.first()) {
                this.value = newResult.getString("value");

                if (pblnIsLoadRecursive) {
                    loadReferences(pConn, newResult.getInt("nodeObject_id"),
                                   newResult.getInt("code_nodeObject_attribute_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (newStatement != null) {
                    newStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** Load entities related to this ItemAttribute
     * @param pConn The database connection to use
     * @param pintItem_id The id of the item to load
     * @param pintAttributeName_id The id AttributeName to load
     */
    protected void loadReferences(Connection pConn, int pintItem_id, int pintAttributeName_id) {
        if (pintAttributeName_id >= 1000) {
            this.attributeName = new AttributeName(pConn, pintAttributeName_id, false);
        }
        if (pintItem_id >= 1000) {
            this.parentItem = new Item(pConn, pintItem_id, false);
        }
    }

    @Override
    public int saveReferences(Connection pConn) {
        return this.attributeName.save(pConn);
    }

    @Override
    protected int update(Connection pConn) {
        return super.executeUpdate(pConn,
                                   String.format("UPDATE Item_Attribute SET value = %s WHERE item_id = %d AND attribute_id = %d",
                                                 this.value, this.parentItem.getId(), this.attributeName.getId()));
    }

    @Override
    protected int insert(Connection pConn) {
        int intResult = 0;

        Logger.debug("Saving Attribute: node_id=" + parentItem.getId() + " attribute_code=" + attributeName.getId() +
                     " value=" + value);

        try {
            super.executeInsert(pConn,
                                String.format("INSERT INTO Item_Attribute(item_id, attribute_id, value) VALUES(%d, %d, '%s')",
                                              this.parentItem.getId(), this.attributeName.getId(), this.value));
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
        return "NodeObject_Attribute: " + value + " {" + super.toString() + "}";
    }
}
