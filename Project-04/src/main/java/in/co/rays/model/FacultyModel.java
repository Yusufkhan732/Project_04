package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import in.co.rays.util.JDBCDataSource;

public class FacultyModel {

	public Integer nextPk() throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_faculty");
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			
			pk = rs.getInt(1);
		}
		return pk+1;
	}
	
}
