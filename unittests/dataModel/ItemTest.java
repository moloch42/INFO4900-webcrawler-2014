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

public class ItemTest {

	@Test(singleThreaded = true)
	public void testAttributeName() throws SQLException {

		Connection conn = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
														config.getDATABASE_USER(),
														config.getDATABASE_PASSWORD());

		Seller seller = new Seller("Test");
		seller.save(conn);
		
		Item a = new Item(seller);
		a.save(conn);
		
		
		AttributeName name = new AttributeName("attname", "String");
		name.save(conn);
		
		ItemAttribute att = new ItemAttribute(a, name, "value");
		att.save(conn);
		

		Item b = new Item(conn, a.getId(), true);

		Assert.assertEquals(b.getId(), a.getId());
		Assert.assertEquals(b.getSeller_id(), a.getSeller_id());
		Assert.assertEquals(b.getSeller().getId(), a.getSeller().getId());
		Assert.assertEquals(b.getState(), a.getState());
		Assert.assertEquals(b.isActive_flag(), a.isActive_flag());
		
		Assert.assertEquals(b.getItemAttributes().size(), 1);

		
		List<Item> items = Item.loadItemsFromDB(conn);
		Assert.assertEquals(items.size(), 1);
		
		int id = 0;
		try (Statement key = conn.createStatement();
				ResultSet rs = key.executeQuery("SELECT attribute_id from item_attribute where attribute_value = '" + att.getValue() + "'")) {
			
			  if (rs.first()) {
				  id = rs.getInt("attribute_id");
			  }
			  else {
				  Assert.fail();
			  }
			
		}


		try (Statement delete = conn.createStatement()) {
			
			delete.executeUpdate("DELETE from item_attribute where attribute_id = " + id);
			delete.executeUpdate("DELETE from item where item_id = " + a.getId());
			delete.executeUpdate("DELETE from seller where seller_id = " + seller.getId());
			delete.executeUpdate("DELETE from attribute_name where id = " + name.getId());

		}
		conn.close();

	}
}
