package cusDAO;

import cusDAO.jdbc.CusQueryResult;

public interface CustomerDAO {
	
	CusQueryResult getBalanceByPhone(String phoneNum);
	int updateBalance(CusQueryResult rs);

}
