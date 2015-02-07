package dataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import modules.config;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SellerTest {

	
	@Test(singleThreaded = true)
	public void testAttributeName() throws SQLException {
		
		
		Connection conn = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
				config.getDATABASE_USER(),
				config.getDATABASE_PASSWORD());
		
		
		Seller a = new Seller("Test");
		
		a.save(conn);
		
		Item item = new Item(a);
		item.save(conn);
		
		Seller b = new Seller(conn, a.getId(), true);
		
		Assert.assertEquals(b.getId(), a.getId());
		Assert.assertEquals(b.getName(), a.getName());
		Assert.assertEquals(b.getState(), a.getState());
		Assert.assertEquals(b.getItems().size(), 1);
		
		
		List<Seller> atts = Seller.loadSellersFromDB(conn);
		
		Assert.assertEquals(atts.size(), 1);
		
		try ( Statement delete = conn.createStatement() ) {
			delete.executeUpdate("DELETE from item where item_id = " + item.getId());
			delete.executeUpdate("DELETE from seller where seller_id = " + b.getId());
			
		}
		
	}
	
}
