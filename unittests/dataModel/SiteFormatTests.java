package dataModel;

import modules.config;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import dataModel.exceptions.SiteFormatException;

/**This class contains unit tests*/
public class SiteFormatTests {


	/** Tests that a SiteFormat template can be loaded properly. Requires a database connection.
	 * @throws SiteFormatException if there is a template parsing error
	 * @throws SQLException if a database error occurred
	 */
	@Test(singleThreaded = true)
	public void constructorTest() throws SiteFormatException, SQLException {
		
		File f = new File("ExcelTemplates/PinkbikeBuySellAllMountain.xlsx");
		
		Assert.assertEquals(f.exists(), true);
		
		Connection conn = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
				config.getDATABASE_USER(),
				config.getDATABASE_PASSWORD());
		
	    Map<String, Seller> sellers = new HashMap<String, Seller>();

	    Map<String, AttributeName> attributes = new HashMap<String, AttributeName>();
		
		SiteFormat s = new SiteFormat(conn, sellers, attributes, f);
		
		
		
		Assert.assertEquals(s.getItemRootPattern().getElement_attribute_name(), "class");
		Assert.assertEquals(s.getItemRootPattern().getElement_attribute_value(), "bsitem");
		Assert.assertEquals(s.getItemRootPattern().getElement_name(), "div");
		
		Assert.assertEquals(s.getQueryStringPagingSufix(), "&page=");
		
		Assert.assertEquals(s.getSeller().getName(), "Pinkbike");
		
		Assert.assertEquals(s.getURL(), "http://www.pinkbike.com/buysell/list/?region=3&page=1&category=2");
		
		Assert.assertEquals(s.getAttributes().size(), 2);
		
		Assert.assertEquals(s.getAttributes().get(0).getAttributeName().getName(), "bikeModel");

		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().size(), 2);
		
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(0).getElement_name(), "div");
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(0).getElement_attribute_name(), "style");
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(0).getElement_attribute_value(), "margin-bottom:5px;");
		
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(1).getElement_name(), "a");
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(1).getElement_attribute_name(), "style");
		Assert.assertEquals(s.getAttributes().get(0).getAttributePattern().get(1).getElement_attribute_value(), "font-size: 18px;font-weight:bold;color:#000000");
		
		
		Assert.assertEquals(s.getAttributes().get(1).getAttributeName().getName(), "price");
		
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().size(), 3);
		
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(0).getElement_name(), "table");
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(0).getElement_attribute_name(), "border");
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(0).getElement_attribute_value(), "0");
		
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(1).getElement_name(), "td");
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(1).getElement_attribute_name(), "colspan");
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(1).getElement_attribute_value(), "2");
		
		Assert.assertEquals(s.getAttributes().get(1).getAttributePattern().get(2).getElement_name(), "b");
		
		
		try ( Statement delete = conn.createStatement() ) {
			
			for (AttributeName a: attributes.values()) {
				delete.executeUpdate("DELETE from attribute_name where id =" + a.getId() );
			}

			for (Seller se: sellers.values()) {
				delete.executeUpdate("DELETE from seller where seller_id = " + se.getId() );
			}
			
		}
		conn.close();
		
	}

}
