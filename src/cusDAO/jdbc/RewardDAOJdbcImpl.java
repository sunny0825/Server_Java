package cusDAO.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cusDAO.RewardDAO;

public class RewardDAOJdbcImpl implements RewardDAO {

	private Connection dbConn = null;

	public RewardDAOJdbcImpl(Connection connection) {
		this.dbConn = connection;
	}

	@Override
	public RewQueryResult getPoints(int cus_id) {
		RewQueryResult qResult = null;
		ResultSet result = null;
		
		String queryStr = "select cus_id, points, version from reward where cus_id=?;";
		PreparedStatement queryStmt;
		try {
			queryStmt = dbConn.prepareStatement(queryStr);
			queryStmt.setInt(1, cus_id);
			
			result = queryStmt.executeQuery();
			
			while(result.next()){
				int points = result.getInt("points");
				int version = result.getInt("version");
				
				qResult = new RewQueryResult(cus_id, points, version);
				break;
			}
			
			queryStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return qResult;
	}

	@Override
	public int updatePoint(RewQueryResult rs) {
		int rows = 0;
		int version = rs.getVersion();

		String queryStr = "UPDATE reward set points=?, version=? where cus_id=? and version=?";
		PreparedStatement queryStmt;
		try {
			queryStmt = dbConn.prepareStatement(queryStr);
			queryStmt.setInt(1, rs.getPoints());
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

}
