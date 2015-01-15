package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Code_NodeObject_Attribute extends Entity {
	// Real attributes
	private int id;
	private String name;
	private String dataType;

	public Code_NodeObject_Attribute(Connection pConn, int pCode_nodeObject_attribute_id, boolean pblnIsLoadRecursive) {
		super(pConn, pCode_nodeObject_attribute_id, pblnIsLoadRecursive);
	}

	public Code_NodeObject_Attribute(Connection pConn, int id, String name, String data_type) {
		this.id = id;
		this.name = name;
		this.dataType = data_type;
	}

	public Code_NodeObject_Attribute(int id, String name, String data_type) {
		this.id = id;
		this.name = name;
		this.dataType = data_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData_type() {
		return dataType;
	}

	public void setData_type(String data_type) {
		this.dataType = data_type;
	}

	public int getId() {
		return id;
	}

	@Override
	public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		Statement stmtNew = null;
		try {
			stmtNew = pConn.createStatement();
			ResultSet rsNew = stmtNew.executeQuery("SELECT * FROM Code_NodeObject_Attribute WHERE id = " + pintEntityID);

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

	protected void loadReferences(Connection pConn) {
	}

	@Override
	protected int saveReferences(Connection pConn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int update(Connection pConn) {
		return super.executeUpdate(pConn, String.format("UPDATE Code_NodeObject_Attribute SET name = %s, data_type = %s WHERE id = %d", this.name, this.dataType, this.id));
	}

	@Override
	protected int insert(Connection pConn) {
		int intResult = 0;
		try {
			int intGenKey = super.executeInsert(pConn, String.format("INSERT INTO Code_NodeObject_Attribute(name, data_type) VALUES(%1, %2)", this.name, this.dataType));
			this.id = intGenKey;
			intResult++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intResult;
	}

	@Override
	protected int delete(Connection pConn) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String toString() {
		String rv = "Code_NodeObject_Attribute:" + id + " " + name + " " + dataType + " {" + super.toString() + "}";
		return rv;
	}
}
