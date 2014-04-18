package cusDAO;

import cusDAO.jdbc.RewQueryResult;

public interface RewardDAO {

	RewQueryResult getPoints(int cus_id);
	int updatePoint(RewQueryResult rs);
}
