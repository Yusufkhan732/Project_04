package in.co.rays.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class JDBCDataSource {

	private JDBCDataSource() {

	}

	private static JDBCDataSource dataSource;

	private ComboPooledDataSource cpds = null;

	public static JDBCDataSource getInstance() {
		if (dataSource == null) {

			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

			dataSource = new JDBCDataSource();
			dataSource.cpds = new ComboPooledDataSource();
			try {
				dataSource.cpds.setDriverClass(rb.getString("driver"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			dataSource.cpds.setJdbcUrl(rb.getString("url"));
			dataSource.cpds.setUser(rb.getString("username"));
			dataSource.cpds.setPassword(rb.getString("password"));
			dataSource.cpds.setInitialPoolSize(new Integer((String) rb.getString("initialpoolsize")));
			dataSource.cpds.setAcquireIncrement(new Integer((String) rb.getString("acquireincrement")));
			dataSource.cpds.setMaxPoolSize(new Integer((String) rb.getString("maxpoolsize")));
			dataSource.cpds.setMinPoolSize(new Integer((String) rb.getString("minpoolsize")));

		}
		return dataSource;

	}

	public static Connection getConnection() throws SQLException {
		return getInstance().cpds.getConnection();
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {

			}
		}
	}

	public static void trnrollBack(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (Exception e) {

			}
		}
	}
}
