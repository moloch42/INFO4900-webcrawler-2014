package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SiteFormat_NodeObject extends Entity {
	// Real attributes
	private int id;
	private String element_name;
	private String element_attribute_name;
	private String element_attribute_value;
	private String element_content_attribute_name;

	// Reference attributes
	private SiteFormat_NodeObject parent_siteFormat_nodeObject;
	private Vector<SiteFormat_NodeObject> child_siteFormat_nodeObjects;
	private Code_NodeObject_Attribute code_nodeObject_attribute;

	public SiteFormat_NodeObject(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		super(pConn, pintEntityID, pblnIsLoadRecursive);
	}

	public SiteFormat_NodeObject(Connection pConn, int id, SiteFormat_NodeObject parent_siteFormat_nodeObject, int code_nodeObject_attribute_id, String element_name, String element_attribute_name,
			String element_attribute_value, String element_content_attribute_name) {
		this.id = id;
		this.element_name = element_name;
		this.element_attribute_name = element_attribute_name;
		this.element_attribute_value = element_attribute_value;
		this.element_content_attribute_name = element_content_attribute_name;

		this.parent_siteFormat_nodeObject = parent_siteFormat_nodeObject;

		loadReferences(pConn, code_nodeObject_attribute_id);
	}

	public SiteFormat_NodeObject(int id, SiteFormat_NodeObject parent_siteFormat_nodeObject, Code_NodeObject_Attribute code_nodeObject_attribute, String element_name, String element_attribute_name,
			String element_attribute_value, String element_content_attribute_name) {
		this.id = id;
		this.parent_siteFormat_nodeObject = parent_siteFormat_nodeObject;
		this.code_nodeObject_attribute = code_nodeObject_attribute;
		this.element_name = element_name;
		this.element_attribute_name = element_attribute_name;
		this.element_attribute_value = element_attribute_value;
		this.element_content_attribute_name = element_content_attribute_name;
	}

	public int getParent_siteFormat_nodeObject_id() {
		if (this.parent_siteFormat_nodeObject != null) {
			return this.parent_siteFormat_nodeObject.getId();
		}
		return 0;
	}

	public void setParent_siteFormat_nodeObject_id(int parent_siteFormat_nodeObject_id) {
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

	public String getElement_name() {
		return element_name;
	}

	public void setElement_name(String element_name) {
		this.element_name = element_name;
	}

	public String getElement_attribute_name() {
		return element_attribute_name;
	}

	public void setElement_attribute_name(String element_attribute_name) {
		this.element_attribute_name = element_attribute_name;
	}

	public String getElement_attribute_value() {
		return element_attribute_value;
	}

	public void setElement_attribute_value(String element_attribute_value) {
		this.element_attribute_value = element_attribute_value;
	}

	public String getElement_content_attribute_name() {
		return element_content_attribute_name;
	}

	public void setElement_content_attribute_name(String element_content_attribute_name) {
		this.element_content_attribute_name = element_content_attribute_name;
	}

	public Vector<SiteFormat_NodeObject> getChild_siteFormat_nodeObjects() {
		return child_siteFormat_nodeObjects;
	}

	public void setChild_siteFormat_nodeObjects(Vector<SiteFormat_NodeObject> child_siteFormat_nodeObjects) {
		this.child_siteFormat_nodeObjects = child_siteFormat_nodeObjects;
	}

	public Code_NodeObject_Attribute getCode_nodeObject_attribute() {
		return code_nodeObject_attribute;
	}

	public void setCode_nodeObject_attribute(Code_NodeObject_Attribute code_nodeObject_attribute) {
		this.code_nodeObject_attribute = code_nodeObject_attribute;
	}

	public int getId() {
		return id;
	}

	@Override
	public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		Statement stmtNew = null;
		ResultSet rsNew = null;
		try {
			stmtNew = pConn.createStatement();
			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat_NodeObject WHERE id = " + pintEntityID);

			if (rsNew.first()) {
				this.id = rsNew.getInt("id");
				this.element_name = rsNew.getString("element_name");
				this.element_attribute_name = rsNew.getString("element_attribute_name");
				this.element_attribute_value = rsNew.getString("element_attribute_value");
				this.element_content_attribute_name = rsNew.getString("element_content_attribute_name");

				if (pblnIsLoadRecursive) {
					loadReferences(pConn, rsNew.getInt("code_nodeObject_attribute_id"));
				}
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

	protected void loadReferences(Connection pConn, int code_nodeObject_attribute_id) {
		Statement stmtNew = null;
		ResultSet rsNew = null;
		try {
			if (code_nodeObject_attribute_id >= 1000) {
				this.code_nodeObject_attribute = new Code_NodeObject_Attribute(pConn, code_nodeObject_attribute_id, true);
			}

			stmtNew = pConn.createStatement();
			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat_NodeObject WHERE parent_siteFormat_nodeObject_id = " + this.id);
			child_siteFormat_nodeObjects = new Vector<SiteFormat_NodeObject>();
			while (rsNew.next()) {
				child_siteFormat_nodeObjects.add(new SiteFormat_NodeObject(pConn, rsNew.getInt("id"), this, rsNew.getInt("code_nodeObject_attribute_id"), rsNew.getString("element_name"), rsNew
						.getString("element_attribute_name"), rsNew.getString("element_attribute_value"), rsNew.getString("element_content_attribute_name")));
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int update(Connection pConn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int insert(Connection pConn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int delete(Connection pConn) {
		// TODO Auto-generated method stub
		return 0;
	}
}
