package dataModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SiteFormat extends Entity {
	// Real attributes
	private int id;
	private String url;
	private String url_page_id;

	// References attributes
	private Seller seller;
	private SiteFormat_NodeObject siteFormat_nodeObject;

	public SiteFormat(Connection pConn, int pintEntityID, boolean pblnIsLoadRecursive) {
		super(pConn, pintEntityID, pblnIsLoadRecursive);
	}

	public SiteFormat(Connection pConn, int id, int seller_id, int siteFormat_nodeObject_id, String uRL, String url_page_id) {
		this.id = id;
		this.url = uRL;
		this.setUrl_page_id(url_page_id);

		loadReferences(pConn, seller_id, siteFormat_nodeObject_id);
	}

	public SiteFormat(int id, Seller seller, SiteFormat_NodeObject siteFormat_nodeObject, String uRL, String url_page_id) {
		this.id = id;
		this.setSeller(seller);
		this.siteFormat_nodeObject = siteFormat_nodeObject;
		this.url = uRL;
		this.setUrl_page_id(url_page_id);
	}

	public String getURL() {
		return url;
	}

	public void setURL(String uRL) {
		url = uRL;
	}

	public String getUrl_page_id() {
		return url_page_id;
	}

	public void setUrl_page_id(String url_page_id) {
		this.url_page_id = url_page_id;
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

	public int getSiteFormat_nodeObject_id() {
		if (siteFormat_nodeObject != null) {
			return siteFormat_nodeObject.getId();
		}
		return 0;
	}

	public void setSiteFormat_nodeObject_id(int siteFormat_nodeObject_id) {
		// TODO implement
	}

	public SiteFormat_NodeObject getSiteFormat_nodeObject() {
		return siteFormat_nodeObject;
	}

	public void setSiteFormat_nodeObject(SiteFormat_NodeObject siteFormat_nodeObject) {
		this.siteFormat_nodeObject = siteFormat_nodeObject;
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
			rsNew = stmtNew.executeQuery("SELECT * FROM SiteFormat WHERE id = " + pintEntityID);

			if (rsNew.first()) {
				this.id = rsNew.getInt("id");
				this.url = rsNew.getString("url");
				this.url_page_id = rsNew.getString("url_page_id");
				if (pblnIsLoadRecursive) {
					loadReferences(pConn, rsNew.getInt("seller_id"), rsNew.getInt("siteFormat_nodeObject_id"));
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

	protected void loadReferences(Connection pConn, int seller_id, int siteFormat_nodeObject_id) {
		try {
			if (siteFormat_nodeObject_id >= 1000) {
				this.siteFormat_nodeObject = new SiteFormat_NodeObject(pConn, siteFormat_nodeObject_id, true);
			}
			if (seller_id >= 1000) {
				this.seller = new Seller(pConn, seller_id, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return "SiteFormat: " + id + " " + url + " " + url_page_id + " {" + super.toString() + "}";
	}
}
