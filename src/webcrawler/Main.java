package webcrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import dataModel.NodeObject;

import modules.Logger;

public class Main {
	public static void main(String[] args) throws InstantiationException, ClassNotFoundException, SQLException, IllegalAccessException {
		Logger.debug("Starting Crawl");
//		if (args.length > 0) {
//			if (args[0].equalsIgnoreCase("-debug")) {
//				config.DEBUG_FLAG = true;
//			}
//		}
		config.DEBUG_FLAG = true;

		//opening connection to the Database
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Logger.debug("Opening DB Connection");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/webcrawler", "root", "Insert Password Here");
		
		
		//performing the crawl
		Logger.debug("getting node objects");
		Vector<NodeObject> no_vecNew = modules.Crawler.getNodeObjects(con, 1000);

		//saving the results
		Logger.debug("Saving node objects");
		Iterator<NodeObject> itNew = no_vecNew.iterator();
		while (itNew.hasNext()) {
			NodeObject tmp = itNew.next();
			Logger.debug("Saving node object {" + tmp.toString() +"}");
			tmp.save(con);
		}
		
		Logger.debug("Crawl Done");
	}
}
