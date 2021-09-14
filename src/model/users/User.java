package model.users;

import java.time.LocalDate;
import java.util.ArrayList;

import model.session.Session;

public class User {

	private int userID = -1; // Do we really need an ID for guests????
	private String username = null;
	private LocalDate birthday = null;
	private MemberType accountType = null;
	private ArrayList<Session> cart = new ArrayList<Session>();

	public User() {

	}

	public User(int userID, String username, LocalDate birthday, MemberType memberType) {
		this.userID = userID;
		this.username = username;
		this.birthday = birthday;
		this.accountType = memberType;
	}

	public void setCart(ArrayList<Session> cart) {
		this.cart = cart;
	}

	public void addMovieToCart(Session movie) {
		this.cart.add(movie);
	}

	public void removeMovieFromCart(Session movie) {
		this.cart.remove(movie);
	}

	public void emptyCart() {
		this.cart.clear();
	}

	public ArrayList<Session> getCart() {
		return this.cart;
	}

	public int getUserID() {
		return userID;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public String getUsername() {
		return username;
	}

	public MemberType getAccountType() {
		return accountType;
	}

	public String getAccountTypeString() {
		return accountType.toString();
	}

}
