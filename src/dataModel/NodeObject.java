package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

public class NodeObject extends Entity {
	// Real attributes
	private int id;
	private boolean active_flag;

	// Referenced attributes
	private Seller seller;
	private Vector<NodeObject_Attribute> nodeObject_attributes = new Vector<NodeObject_Attribute>();

	public NodeObject(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		super(pConn, pintEntityID, pblnIsLoadRecursive);
	}

	public NodeObject(Connection pConn, int id, int seller_id, boolean active_flag) {
		this.id = id;
		this.active_flag = active_flag;

		loadReferences(pConn, seller_id);
	}

	public NodeObject(int id, Seller seller, boolean active_flag) {
		this.id = id;
		this.seller = seller;
		this.active_flag = active_flag;
	}

	public NodeObject() {
	}

	public int getSeller_id() {
		if (seller != null) {
			return seller.getId();
		}
		return 0;
	}

	public void setSeller_id(int seller_id) {
		// TODO implement
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public boolean isActive_flag() {
		return active_flag;
	}

	public void setActive_flag(boolean active_flag) {
		this.active_flag = active_flag;
	}

	public Vector<NodeObject_Attribute> getNodeObject_attributes() {
		return nodeObject_attributes;
	}

	public void setNodeObject_attributes(Vector<NodeObject_Attribute> nodeObject_attributes) {
		this.nodeObject_attributes = nodeObject_attributes;
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
			rsNew = stmtNew.executeQuery("SELECT * FROM NodeObject WHERE id = " + pintEntityID);

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

	protected void loadReferences(Connection pConn, int seller_id) {
		Statement stmtNew = null;
		ResultSet rsNew = null;
		try {
			stmtNew = pConn.createStatement();
			rsNew = stmtNew.executeQuery("SELECT * FROM NodeObject_Attribute WHERE nodeObject_id = " + this.id);
			while (rsNew.next()) {
				this.nodeObject_attributes.add(new NodeObject_Attribute(this, new Code_NodeObject_Attribute(pConn, rsNew.getInt("code_nodeObject_attribute_id"), true), rsNew.getString("value")));
			}
			// rsNew.close();
			rsNew = stmtNew.executeQuery("SELECT * FROM Seller WHERE id = " + seller_id);
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
			Iterator<NodeObject_Attribute> itNodeObject_attributes = nodeObject_attributes.iterator();
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
		return super.executeUpdate(pConn, String.format("UPDATE NodeObject SET seller_id = %d, active_flag = %b WHERE id = %d", this.seller.getId(), this.active_flag, this.id));
	}

	@Override
	protected int insert(Connection pConn) {
		int intResult = 0;
		try {
			int intGenKey = super.executeInsert(pConn, String.format("INSERT INTO NodeObject(seller_id, active_flag) VALUES(%d, %b)", this.getSeller_id(), this.active_flag));
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
}
