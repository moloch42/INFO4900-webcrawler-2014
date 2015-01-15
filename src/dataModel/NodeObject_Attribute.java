package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modules.Logger;

public class NodeObject_Attribute extends Entity {
	// Real attributes
	private String value;

	// Referenced attributes
	private Code_NodeObject_Attribute code_nodeObject_attribute;
	private NodeObject nodeObject;

	public NodeObject_Attribute(Connection pConn, int pintNodeObject_attribute_id, boolean pblnIsLoadRecursive) {
		super(pConn, pintNodeObject_attribute_id, pblnIsLoadRecursive);
	}

	public NodeObject_Attribute(Connection pConn, int nodeObject_id, int code_nodeObject_attribute_id, String value) {
		this.value = value;

		loadReferences(pConn, nodeObject_id, code_nodeObject_attribute_id);
	}

	public NodeObject_Attribute(NodeObject nodeObject, Code_NodeObject_Attribute code_nodeObject_attribute, String value) {
		this.nodeObject = nodeObject;
		this.code_nodeObject_attribute = code_nodeObject_attribute;
		this.value = value;
	}

	public int getNodeObject_id() {
		if (nodeObject != null) {
			return nodeObject.getId();
		}
		return 0;
	}

	public void setNodeObject_id(int nodeObject_id) {
		// TODO implement
	}

	public int getCode_nodeObject_attribute_id() {
		if (this.code_nodeObject_attribute != null) {
			return this.code_nodeObject_attribute.getId();
		}
		return 0;
	}

	public void setCode_nodeObject_attribute_id(int code_nodeObject_attribute_id) {
		// TODO implement
	}

	public Code_NodeObject_Attribute getCode_nodeObject_attribute() {
		return code_nodeObject_attribute;
	}

	public void setCode_nodeObject_attribute(Code_NodeObject_Attribute code_nodeObject_attribute) {
		this.code_nodeObject_attribute = code_nodeObject_attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NodeObject getNodeObject() {
		return nodeObject;
	}

	public void setNodeObject(NodeObject nodeObject) {
		this.nodeObject = nodeObject;
	}

	@Override
	public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		Statement newStatement = null;
		try {
			newStatement = pConn.createStatement();
			ResultSet newResult = newStatement.executeQuery("SELECT * FROM NodeObject_Attribute WHERE id = " + pintEntityID);

			if (newResult.first()) {
				this.value = newResult.getString("value");

				if (pblnIsLoadRecursive) {
					loadReferences(pConn, newResult.getInt("nodeObject_id"), newResult.getInt("code_nodeObject_attribute_id"));
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

	protected void loadReferences(Connection pConn, int pintNodeObject_id, int pintCode_nodeObject_attribute_id) {
		if (pintNodeObject_id >= 1000) {
			this.code_nodeObject_attribute = new Code_NodeObject_Attribute(pConn, pintNodeObject_id, false);
		}
		if (pintNodeObject_id >= 1000) {
			this.nodeObject = new NodeObject(pConn, pintNodeObject_id, false);
		}
	}

	@Override
	public int saveReferences(Connection pConn) {
		return this.code_nodeObject_attribute.save(pConn);
	}

	@Override
	protected int update(Connection pConn) {
		return super.executeUpdate(pConn, String.format("UPDATE NodeObject_Attribute SET value = %s WHERE nodeObject_id = %d AND code_nodeObject_attribute_id = %d", this.value,
				this.nodeObject.getId(), this.code_nodeObject_attribute.getId()));
	}

	@Override
	protected int insert(Connection pConn) {
		int intResult = 0;
		
		Logger.debug("Saving Attribute: node_id=" + nodeObject.getId() + " attribute_code=" + code_nodeObject_attribute.getId() + " value=" + value);
		
		try {
			super.executeInsert(
					pConn,
					String.format("INSERT INTO NodeObject_Attribute(nodeObject_id, code_nodeObject_attribute_id, value) VALUES(%d, %d, '%s')", this.nodeObject.getId(),
							this.code_nodeObject_attribute.getId(), this.value));
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
		return "NodeObject_Attribute: " + value + " {" + super.toString() + "}";
	}
}
