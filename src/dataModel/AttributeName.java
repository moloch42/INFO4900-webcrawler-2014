package dataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import dataModel.Entity.State;
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
        //DONE load attribute names from the DB

        List<AttributeName> attributes = new LinkedList<AttributeName>();


        //Execute SQL to get the attribute names back
        try (Statement getAllAttributes = conn.createStatement();
        	 ResultSet newResult = getAllAttributes.executeQuery("SELECT * FROM attribute_name");
        	) {
            
            //If there is at least one result
            if (newResult.first()) {
                do {
                    //create a new attribute name and add it to the list
                    AttributeName a =
                        new AttributeName(newResult.getInt("id"), newResult.getString("name"),
                                          newResult.getString("data_type"));
                    attributes.add(a);

                    //keep going as long as there are more results
                } while (newResult.next());

            }

        } catch (SQLException e) {
        	 Logger.error("An error occured while loading all of the AttributeNames", e);
        }

        Logger.debug("Done loading AttributeNames from the DB");

        return attributes;
    }

    /**
     * @return The name of the attribute represented by this AttributeName
     */
    public String getName() {
        return name;
    }

    /**
     * @return the data type of this AttributeName
     */
    public String getData_type() {
        return dataType;
    }

    /**
     * @return The database id of this AttributeName
     */
    public int getId() {
        return id;
    }

    @Override
    public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {

        /*
    	try (Statement stmtNew = pConn.createStatement();
        	 ResultSet rsNew = stmtNew.executeQuery("SELECT * FROM attribute_name WHERE id = " + pintEntityID)
        ){
        */
    	try (PreparedStatement statement = pConn.prepareStatement("SELECT * FROM attribute_name WHERE id = ?")) {
    		statement.setInt(1,pintEntityID);
    		statement.execute();
    		ResultSet rsNew = statement.getResultSet();
        	
            if (rsNew.first()) {
            	Logger.debug("Loading AttributeName with id=" + pintEntityID);
                this.id = rsNew.getInt("id");
                this.name = rsNew.getString("name");
                this.dataType = rsNew.getString("data_type");
            } else {
            	Logger.debug("Failed to Load AttributeName with id=" + pintEntityID);
            }
        } catch (SQLException e) {
           Logger.error("An error occured while loading the AttributeName with id="+pintEntityID, e);
        }
    }

    /** Method not implemented.
     * @param pConn
     */
    protected void loadReferences(Connection pConn) {
    	//not implemented and not planned
    }

    @Override
    protected int saveReferences(Connection pConn) {
        //No references to save
        return 0;
    }

    @Override
    protected int update(Connection pConn) {
        //return super.executeUpdate(pConn,
        //                           String.format("UPDATE attribute_name SET name = %s, data_type = %s WHERE id = %d",
        //                                         this.name, this.dataType, this.id));
    	
    	try (PreparedStatement statement = pConn.prepareStatement("UPDATE attribute_name SET name = ?, data_type = ? WHERE id = ?")) {
        	statement.setString(1, this.name);
        	statement.setString(2, this.dataType);
        	statement.setInt(3, this.id);
            
        	return statement.executeUpdate();

        } catch (SQLException e) {
        	Logger.error("An error occured while executing the SQL Update for AttributeName id: " + this.id, e);
        }
    	
        return 0;
    }

    @Override
    protected int insert(Connection pConn) {
        int intResult = 0;
        
        Logger.debug("--------Saving new Attribute Name '" + name + "' to the DataBase");
        
        try (PreparedStatement statement = pConn.prepareStatement("INSERT INTO attribute_name(name, data_type) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
        	statement.setString(1, this.name);
        	statement.setString(2, this.dataType);
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
        	Logger.error("An error occured while saving an ItemAttribute: " + this.toString(), e);
        }
        return intResult;
        
        /* Old code block
        try {
        	Logger.debug("--------Saving new Attribute Name '" + name + "' to the DataBase");
        	int intGenKey =
                super.executeInsert(pConn,
                                    String.format("INSERT INTO attribute_name(name, data_type) VALUES('%s', '%s')",
                                                  this.name, this.dataType));
            this.id = intGenKey;
            setState(State.unchanged);
            intResult++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return intResult;
        */
    }

    @Override
    protected int delete(Connection pConn) {
        // not implemented and not planned
        return 0;
    }

    @Override
    public String toString() {
        String rv = "AttributeName: {\n";
        rv += "id: " + id + "\n";
        rv += "name: " + name + "\n";
        rv += "dataType: " + dataType + "\n";
        rv += "}\n";
        return rv;
    }
}
