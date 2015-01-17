package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;

import modules.Logger;


//TODO update this javadoc
/**
 * @author
 */
public class AttributeName extends Entity {

    private int id;
    private String name;
    private String dataType;

    /** This constructor loads a given AttributeName from the database based on a given id.
     *  related items can be loaded recursively.
     * @param pConn The database connection that will be used to retrieve the AttributeName
     * @param AttributeName_id the id of the AttributeName to load
     * @param pblnIsLoadRecursive true if related attributes should be loaded recursively
     */
    public AttributeName(Connection pConn, int AttributeName_id, boolean pblnIsLoadRecursive) {
        super(pConn, AttributeName_id, pblnIsLoadRecursive);
    }

//    /**
//     * @param pConn
//     * @param id
//     * @param name
//     * @param data_type
//     */
//    public AttributeName(Connection pConn, int id, String name, String data_type) {
//        this.id = id;
//        this.name = name;
//        this.dataType = data_type;
//    }

    /** This constructor is used to create a new AttributeName object with a given id.
     * @param id The database id of this AttributeName
     * @param name the name of this AttributeName
     * @param data_type the datatype of this AttributeName
     */
    public AttributeName(int id, String name, String data_type) {
        this.id = id;
        this.name = name;
        this.dataType = data_type;
        setState(State.unchanged);
    }

    /** This constructor is used to create a new AttributeName with no database id.
     * Use this constructor when a brand new AttributeName is needed. Make sure to run the save()
     * method on it after you create it if you need to persist it to the database
     * @param name the name of this AttributeName
     * @param data_type the datatype of this AttributeName
     */
    public AttributeName(String name, String data_type) {
        this.name = name;
        this.dataType = data_type;
        setState(State.added);
    }

    /** This method loads all existing AttributeNames from the database.
     * @param conn The database connection that will be used to retrieve the AttributeNames
     * @return A list of all AttributeNames in the database
     */
    public static List<AttributeName> loadAttributeNamesFromDB(Connection conn) {

        Logger.debug("Loading AttributeNames from the DB");
        //TODO load attribute names from the DB

        List<AttributeName> attributes = new LinkedList<AttributeName>();

        Logger.debug("Done loading AttributeNames from the DB");

        return attributes;
    }

    /**
     * @return The name of the attriubute represented by this AttributeName
     */
    public String getName() {
        return name;
    }

//    /**
//     * @param name The new name for this attribu
//     */
//    public void setName(String name) {
//        this.name = name;
//    }

    /**
     * @return the data type of this AttributeName
     */
    public String getData_type() {
        return dataType;
    }

//    /**
//     * @param data_type
//     */
//    public void setData_type(String data_type) {
//        this.dataType = data_type;
//    }

    /**
     * @return The database id of this AttributeName
     */
    public int getId() {
        return id;
    }


    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
        Statement stmtNew = null;
        try {
            stmtNew = pConn.createStatement();
            ResultSet rsNew =
                stmtNew.executeQuery("SELECT * FROM Attribute_Name WHERE id = " + pintEntityID);

            if (rsNew.first()) {
                this.id = rsNew.getInt("id");
                this.name = rsNew.getString("name");
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

    /** Method not implemented.
     * @param pConn
     */
    protected void loadReferences(Connection pConn) {
    }

    @Override
    protected int saveReferences(Connection pConn) {
        //No references to save
        return 0;
    }

    @Override
    protected int update(Connection pConn) {
        return super.executeUpdate(pConn,
                                   String.format("UPDATE Attribute_Name SET name = %s, data_type = %s WHERE id = %d",
                                                 this.name, this.dataType, this.id));
    }

    @Override
    protected int insert(Connection pConn) {
        int intResult = 0;
        try {
            int intGenKey =
                super.executeInsert(pConn,
                                    String.format("INSERT INTO Attribute_Name(name, data_type) VALUES(%1, %2)",
                                                  this.name, this.dataType));
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
        String rv = "Code_NodeObject_Attribute:" + id + " " + name + " " + dataType + " {" + super.toString() + "}";
        return rv;
    }
}
