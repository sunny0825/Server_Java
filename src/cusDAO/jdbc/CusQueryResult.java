package cusDAO.jdbc;

public class CusQueryResult {
	private int version;
	private int cus_id;
	private float balance;
	
	public CusQueryResult(int ver, int cus_id, float balance){
		this.version = ver;
		this.cus_id = cus_id;
		this.balance = balance;
	}

	public int getVersion() {
		return version;
	}

	public int getCus_id() {
		return cus_id;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	public void adjustBalance(float amount){
		this.balance += amount;
	}

}
