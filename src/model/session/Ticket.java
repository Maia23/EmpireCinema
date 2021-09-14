package model.session;

public class Ticket {

	private float price;
	private int userID;
	private Session session;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Ticket(float price, int userID, Session session) {
		super();
		this.price = price;
		this.userID = userID;
		this.session = session;
	}

}
