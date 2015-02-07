package dataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import modules.config;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AttributeNameTest {

	
	
	@Test(singleThreaded = true)
	public void testAttributeName() throws SQLException {
		
		
		Connection conn = DriverManager.getConnection(	config.getDATABASE_CONNECTION_STRING(),
				config.getDATABASE_USER(),
				config.getDATABASE_PASSWORD());
		
		
		AttributeName a = new AttributeName("Test", "String");
		
		a.save(conn);
		
		AttributeName b = new AttributeName(conn, a.getId(), false);
		
		Assert.assertEquals(b.getData_type(), a.getData_type());
		Assert.assertEquals(b.getId(), a.getId());
		Assert.assertEquals(b.getName(), a.getName());
		Assert.assertEquals(b.getState(), a.getState());
		
		
		List<AttributeName> atts = AttributeName.loadAttributeNamesFromDB(conn);
		
		Assert.assertEquals(atts.size(), 1);
		
		try ( Statement delete = conn.createStatement() ) {
			delete.executeUpdate("DELETE from attribute_name where id = " + b.getId());
		}
		
	}

}
