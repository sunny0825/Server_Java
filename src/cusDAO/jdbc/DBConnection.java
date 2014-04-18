package cusDAO.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.PreDestroy;

public class DBConnection {

	private Connection dbConn = null;
	private String userName = "root";
	private String password = "1234";

	public DBConnection() {
		String dbSourceUrl = "jdbc:mysql://localhost/CS532_DB";

		try {
			dbConn = DriverManager.getConnection(dbSourceUrl, userName,
					password);
			
			dbConn.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@PreDestroy
	private void doClear() {
		if (dbConn != null) {
			try {
				dbConn.close();
			} catch (SQLException e) {
				// TO DO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public Connection getConnInstance() {
		return dbConn;
	}
	
	public void doCommit(){
		try {
			dbConn.commit();
		} catch (SQLException e) {
			// TO DO Auto-generated catch block
			System.out.println("Unable to commit changes"); 
			e.printStackTrace();
		}
	}
	
	public void doRollback(){
		try {
			dbConn.rollback();
		} catch (SQLException e) {
			// TO DO Auto-generated catch block
			System.out.println("Unable to rollback changes"); 
			e.printStackTrace();
		}
	}

}
