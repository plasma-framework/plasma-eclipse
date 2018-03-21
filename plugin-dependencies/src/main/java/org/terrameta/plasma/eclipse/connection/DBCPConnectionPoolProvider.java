
package org.terrameta.plasma.eclipse.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudgraph.rdb.connect.PoolProvder;


/**
 */
public class DBCPConnectionPoolProvider extends DefaultDBCPConnectionPoolProvider implements PoolProvder {

	private static final Log log = LogFactory.getLog(DBCPConnectionPoolProvider.class);

	public DBCPConnectionPoolProvider() {
	    super();
	}


	@Override
	public Connection getConnection() throws SQLException {
		if (log.isDebugEnabled())
			try {
				printDriverStats();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		Connection con = DS.getConnection();
		
		return con;
	}
}
