package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Seller extends Entity {
	// Real attributes
	private int id;
	private String name;

	// Reference attributes
	private Vector<NodeObject> nodeObjects = new Vector<NodeObject>();
	private Vector<SiteFormat> siteFormats = new Vector<SiteFormat>();

	public Seller(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		super(pConn, pintEntityID, pblnIsLoadRecursive);
	}

	public Seller(Connection pConn, int id, String name) {
		this.id = id;
		this.name = name;

		loadReferences(pConn);
	}

	public Seller(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<NodeObject> getNodeObjects() {
		return nodeObjects;
	}

	public void setNodeObjects(Vector<NodeObject> nodeObjects) {
		this.nodeObjects = nodeObjects;
	}

	public Vector<SiteFormat> getSiteFormats() {
		return siteFormats;
	}

	public void setSiteFormats(Vector<SiteFormat> siteFormats) {
		this.siteFormats = siteFormats;
	}

	public int getId() {
		return id;
	}

	@Override
	public void load(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		Statement stmtNew = null;
		try {
			stmtNew = pConn.createStatement();
			ResultSet newResult = stmtNew.executeQuery("SELECT * FROM Seller WHERE id = " + pintEntityID);

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

	protected void loadReferences(Connection pConn) {
		Statement stmtNew = null;
		ResultSet rsNew = null;
		try {
			stmtNew = pConn.createStatement();

			rsNew = stmtNew.executeQuery("SELECT * FROM NodeObject WHERE seller_id = " + this.id);
			while (rsNew.next()) {
				this.nodeObjects.add(new NodeObject(rsNew.getInt("id"), this, rsNew.getBoolean("active_flag")));
			}

			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat WHERE seller_id = " + this.id);
			while (rsNew.next()) {
				this.siteFormats.add(new SiteFormat(rsNew.getInt("id"), this, new SiteFormat_NodeObject(pConn, rsNew.getInt("siteFormat_nodeObject_id"), true), rsNew.getString("url"), rsNew
						.getString("url_page_id")));
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
	
	@Override
	public String toString() {
		String rv = "Code_NodeObject_Attribute:" + id + " " + name + " " + " {" + super.toString() + "}\n";
		for (SiteFormat sf: siteFormats) {
			rv += sf.toString();
			rv += "\n";
		}
		
		
		return rv;
	}
}
