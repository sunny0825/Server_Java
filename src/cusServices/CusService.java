package cusServices;

import cusDAO.CustomerDAO;
import cusDAO.RewardDAO;
import cusDAO.jdbc.CusQueryResult;
import cusDAO.jdbc.CustomerDAOJdbcImpl;
import cusDAO.jdbc.DBConnection;
import cusDAO.jdbc.RewQueryResult;
import cusDAO.jdbc.RewardDAOJdbcImpl;
import cusData.CusResult;

public class CusService {
	private DBConnection dbConn = null;
	private CustomerDAO cusDao = null;
	private RewardDAO rewDao = null;

	public CusService() {
		dbConn = new DBConnection();

		cusDao = new CustomerDAOJdbcImpl(dbConn.getConnInstance());
		rewDao = new RewardDAOJdbcImpl(dbConn.getConnInstance());
	}

	
	/**
	 * Purchase method, update balance & points
	 * @param phone - customer's phone number
	 * @param amount - the amount of purchase
	 * @return
	 */
	public synchronized CusResult doPurchase(String phone, float amount) {
		int points = 0;
		int r1 = 0, r2 = 0;
		CusQueryResult cusRes = null;
		CusResult re = new CusResult();

		points = ((int) amount - 20) / 10; // calculate reward points

		for (int i = 0; i < 2; i++) { // Attempt to try 2 times
			cusRes = cusDao.getBalanceByPhone(phone);

			if (cusRes == null) {
				re.setDone(false);
				re.setMsg("Can't find customer with this phone num");
				return re;
			}

			cusRes.adjustBalance(amount);
			RewQueryResult rewRes = rewDao.getPoints(cusRes.getCus_id());
			rewRes.adjustPoints(points);

			r1 = cusDao.updateBalance(cusRes);
			r2 = rewDao.updatePoint(rewRes);

			if (r1 == 1 && r2 == 1) {
				dbConn.doCommit();
				break; //
			} else {
				dbConn.doRollback();
			}
		}

		// Judge the update result
		if (r1 == 1 && r2 == 1) { // successful
			re.setDone(true);
			re.setMsg("Successful");
			re.setNewBalance(cusRes.getBalance());
		} else { // fail
			re.setDone(false);
			re.setMsg("The operation was not performed -- please try again later");
		}

		return re;
	}

	
	/**
	 * Payment method, update balance & points
	 * @param phone - customer's phone number
	 * @param amount - the amount of payment
	 * @return
	 */
	public synchronized CusResult doPayment(String phone, float amount) {
		int r1 = 0;
		CusResult re = new CusResult();
		CusQueryResult cusRes = null;

		for (int i = 0; i < 2; i++) { // Attempt to try 2 times
			cusRes = cusDao.getBalanceByPhone(phone);

			if (cusRes == null) {
				re.setDone(false);
				re.setMsg("Can't find customer with this phone num");
				return re;
			}

			cusRes.adjustBalance(-amount); // 
			r1 = cusDao.updateBalance(cusRes);

			if (r1 == 1) {
				dbConn.doCommit();
				break;
			} else {
				dbConn.doRollback();
			}
		}

		if (r1 == 1) {
			re.setDone(true);
			re.setMsg("Successful");
			re.setNewBalance(cusRes.getBalance());
		} else {
			re.setDone(false);
			re.setMsg("The operation was not performed -- please try again later");
		}

		return re;
	}

}
