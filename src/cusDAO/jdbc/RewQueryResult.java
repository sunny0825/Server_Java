package cusDAO.jdbc;

public class RewQueryResult {
	private int cus_id;
	private int points;
	private int version;
	
	public RewQueryResult(int cus_id, int points, int version) {
		this.cus_id = cus_id;
		this.points = points;
		this.version = version;
	}

	public int getCus_id() {
		return cus_id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public void adjustPoints(int amount) {
		this.points += amount;
	}

	public int getVersion() {
		return version;
	}

	
	

}
