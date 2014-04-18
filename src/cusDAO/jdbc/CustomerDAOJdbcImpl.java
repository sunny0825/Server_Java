package cusDAO.jdbc;

import java.sql.*;

import cusDAO.CustomerDAO;

public class CustomerDAOJdbcImpl implements CustomerDAO {

	private Connection dbConn = null;

	public CustomerDAOJdbcImpl(Connection connection) {
		this.dbConn = connection;
	}

	@Override
	public int updateBalance(CusQueryResult rs) {
		int rows = 0;
		int version = rs.getVersion();
		
		String queryStr = "UPDATE customer set balance=?,version=? where cus_id=? and version=?";
		PreparedStatement queryStmt;
		try {
			queryStmt = dbConn.prepareStatement(queryStr);
			queryStmt.setFloat(1, rs.getBalance());
			queryStmt.setInt(2, version+1);
			queryStmt.setInt(3, rs.getCus_id());
			queryStmt.setInt(4, version);
			
			rows = queryStmt.executeUpdate();
			queryStmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rows;
	}

	
	@Override
	public CusQueryResult getBalanceByPhone(String phoneNum) {
		CusQueryResult qResult = null;
		ResultSet result = null;
		
		String queryStr = "select cus_id, balance, version from customer where phone_num=?;";
		PreparedStatement queryStmt;
		try {
			queryStmt = dbConn.prepareStatement(queryStr);
			queryStmt.setString(1, phoneNum);
			
			result = queryStmt.executeQuery();
			
			while(result.next()){
				float balance = result.getFloat("balance");
				int cus_id = result.getInt("cus_id");
				int version = result.getInt("version");
				
				qResult = new CusQueryResult(version, cus_id, balance);
				break;
			}
			
			queryStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return qResult;
	}

}
