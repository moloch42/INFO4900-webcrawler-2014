package dataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import modules.config;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ItemAttributeTest {


	@Test(singleThreaded = true)
	public void testItemAttribute() throws SQLException {
		
		
		Connection conn = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
				config.getDATABASE_USER(),
				config.getDATABASE_PASSWORD());
		
		Seller seller = new Seller("sellername");
		seller.save(conn);
		
		Item item = new Item(seller);
		item.save(conn);
		
		AttributeName name = new AttributeName("attname", "String");
		name.save(conn);
		
		ItemAttribute a = new ItemAttribute(item, name, "value");
		a.save(conn);
		
		int id = 0;
		try (Statement key = conn.createStatement();
				ResultSet rs = key.executeQuery("SELECT attribute_id from item_attribute where attribute_value = '" + a.getValue() + "'")) {
			
			  if (rs.first()) {
				  id = rs.getInt("attribute_id");
			  }
			  else {
				  Assert.fail();
			  }
			
		}
		
		ItemAttribute b = new ItemAttribute(conn, id, true);

		Assert.assertEquals(b.getValue(), a.getValue());
		Assert.assertEquals(b.getItem_id(), a.getItem_id());
		Assert.assertEquals(b.getAttributeNameID(), a.getAttributeNameID());
		Assert.assertEquals(b.getState(), a.getState());
		
		
		try ( Statement delete = conn.createStatement() ) {
			
			delete.executeUpdate("DELETE from item_attribute where attribute_id = " + id);
			delete.executeUpdate("DELETE from item where item_id = " + item.getId());
			delete.executeUpdate("DELETE from attribute_name where id = " + name.getId());
			delete.executeUpdate("DELETE from seller where seller_id = " + seller.getId());
			
		}
		
		conn.close();
		
	}
}
