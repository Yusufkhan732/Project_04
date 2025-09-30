package in.co.rays.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Singleton utility class for managing JDBC connections using C3P0 connection
 * pool. Provides methods to get a database connection, close connection, and
 * rollback transaction. Configurations are loaded from a ResourceBundle.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public final class JDBCDataSource {

	/** Private constructor to prevent instantiation */
	private JDBCDataSource() {
	}

	/** Singleton instance of JDBCDataSource */
	private static JDBCDataSource dataSource;

	/** C3P0 connection pool instance */
	private ComboPooledDataSource cpds = null;

	/**
	 * Returns the singleton instance of JDBCDataSource. Initializes the C3P0
	 * connection pool if not already initialized.
	 * 
	 * @return JDBCDataSource singleton instance
	 */
	public static JDBCDataSource getInstance() {
		if (dataSource == null) {

			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

			dataSource = new JDBCDataSource();
<<<<<<< HEAD
			try {
			dataSource.cpds = new ComboPooledDataSource();
			
				dataSource.cpds.setDriverClass(rb.getString("driver"));
				
				String jdbcUrl = System.getenv("DATABASE_URL");
			 
				if (jdbcUrl==null) {
					System.out.println("database "+jdbcUrl);
					jdbcUrl= rb.getString("url");
				}
				
				System.out.println("database "+jdbcUrl);
			dataSource.cpds.setJdbcUrl(jdbcUrl);
=======
			dataSource.cpds = new ComboPooledDataSource();
			try {
				dataSource.cpds.setDriverClass(rb.getString("driver"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			dataSource.cpds.setJdbcUrl(rb.getString("url"));
>>>>>>> 44550202eb54eef528014c7b862ae884b334e858
			dataSource.cpds.setUser(rb.getString("username"));
			dataSource.cpds.setPassword(rb.getString("password"));
			dataSource.cpds.setInitialPoolSize(new Integer((String) rb.getString("initialpoolsize")));
			dataSource.cpds.setAcquireIncrement(new Integer((String) rb.getString("acquireincrement")));
			dataSource.cpds.setMaxPoolSize(new Integer((String) rb.getString("maxpoolsize")));
			dataSource.cpds.setMinPoolSize(new Integer((String) rb.getString("minpoolsize")));
<<<<<<< HEAD
          
			
			}catch (Exception e) {
				e.printStackTrace();
			}
=======

>>>>>>> 44550202eb54eef528014c7b862ae884b334e858
		}
		return dataSource;

	}

	/**
	 * Returns a database connection from the connection pool.
	 * 
	 * @return Connection object
	 * @throws SQLException if a database access error occurs
	 */
	public static Connection getConnection() throws SQLException {
		return getInstance().cpds.getConnection();
	}

	/**
	 * Closes the given database connection.
	 * 
	 * @param connection the Connection to close
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Rolls back the transaction on the given connection.
	 * 
	 * @param connection the Connection to rollback
	 */
	public static void trnrollBack(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (Exception e) {

			}
		}
	}
}
