package model.util.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import org.mindrot.jbcrypt.BCrypt;

import model.films.Movie;
import model.films.MovieDetail;
import model.films.Room;
import model.session.Session;
import model.session.Ticket;
import model.users.MemberType;
import model.users.User;
import model.util.Tools;
import model.util.movie_api.MovieRequest;

/**
 * EMPIRE CINEMA DB MANAGER contains every essential method exploiting the EC
 * database
 * 
 * @author Maia
 *
 */
public class ECDBManager extends DBManager {

	private static ECDBManager instance = null;

	private String url = "jdbc:mysql://localhost:3306/empire_cinema";
	private String user = "root";
	private String dbpwd = "Youllneverknow0@";

	public static ECDBManager getInstance() {
		if (instance == null) {
			instance = new ECDBManager();
		}
		return instance;
	}

	/**
	 * Tests the user's credentials
	 * 
	 * @param username
	 * @param password
	 * @return true if inputs are valid, false otherwise
	 */
	public boolean tryLogIn(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);

			// Test if username exists
			if (!usernameExist(username)) {
				return false;
			}

			String userPwd = select(conn, "SELECT pwd FROM member WHERE username='" + username + "';").firstKey();
			String userSalt = select(conn, "SELECT grainSalt FROM member WHERE username='" + username + "';")
					.firstKey();
			if (userPwd.equals(BCrypt.hashpw(password, userSalt))) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception ! Maybe turn it on ! ");
		}
	}

	/**
	 * Adds information of new accounts to the database
	 * 
	 * @param username
	 * @param password
	 * @param birthday
	 * @return true if insertion went well, false otherwise
	 */
	public boolean signUp(String username, String password, LocalDate birthday) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		Objects.requireNonNull(birthday);

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);

			// If all is good we can register the user
			// First calculate membership type
			LocalDate now = LocalDate.now();
			int age = Period.between(birthday, now).getYears();
			String salt = BCrypt.gensalt();

			String query = "INSERT INTO member VALUES (NULL, '" + Date.valueOf(birthday) + "', '" + username + "', '"
					+ salt + "', '" + BCrypt.hashpw(password, salt) + "', '" + getMembershipType(age) + "');";

			insertValues(conn, query);

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;

	}

	/**
	 * Updates the member table with the newly given informations
	 * 
	 * @param userID
	 * @param username
	 * @param password
	 * @param birthday
	 * @return true if insertion was completed, false otherwise
	 */
	public boolean updateUserSettings(int userID, String username, String password, LocalDate birthday) {

		boolean usernameUpdate = (username == "" ? false : true);
		boolean pwdUpdate = (password == "" ? false : true);
		boolean dateUpdate = (birthday == null ? false : true);

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);

			/*
			 * PreparedStatement ps = conn.prepareStatement(
			 * "UPDATE member SET birthdayDate = ?, username = ?, password = ? WHERE id = ?"
			 * );
			 */

			PreparedStatement ps = conn.prepareStatement("UPDATE member SET " + (dateUpdate ? "birthdayDate = ?" : "")
					+ (dateUpdate && usernameUpdate ? ", " : "") + (usernameUpdate ? "username = ?" : "")
					+ (pwdUpdate && (usernameUpdate || dateUpdate) ? ", " : "") + (pwdUpdate ? "pwd = ?" : "")
					+ " WHERE id = ?");

			int currentIndex = 1;

			if (dateUpdate) {
				ps.setDate(currentIndex++, Date.valueOf(birthday));
			}

			if (usernameUpdate) {
				ps.setString(currentIndex++, username);
			}

			if (pwdUpdate) {
				String salt = getUserSalt(userID);
				ps.setString(currentIndex++, BCrypt.hashpw(password, salt));
			}

			ps.setInt(currentIndex++, userID);
			ps.executeUpdate();
			conn.close();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Retrieves all the necessary raw data to identify a client
	 * 
	 * @return a treemap containing raw data
	 */
	public TreeMap<String, HashMap<String, String>> getCustomerList() {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT member.id, member.birthdayDate, member.username, memberType.label FROM member, memberType WHERE memberType.id=member.id_memberType ORDER BY member.id;");
			conn.close();

			if (results.size() > 0) {
				return results;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return null;
	}

	public ArrayList<MovieDetail> getMovies() {

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn, "SELECT id, imdbID, title FROM movie");
			conn.close();

			if (results.size() > 0) {
				ArrayList<MovieDetail> movies = new ArrayList<MovieDetail>();
				MovieRequest movieRequest = new MovieRequest();
				for (Entry<String, HashMap<String, String>> result : results.entrySet()) {
					HashMap<String, String> entry = result.getValue();
					movieRequest.changeId(entry.get("imdbID"));
					MovieDetail movie = movieRequest.execute();
					movie.setMovieID(Integer.parseInt(result.getKey()));
					movies.add(movie);
				}
				return movies;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return null;
	}

	/**
	 * Checks if a username exists in database
	 * 
	 * @param username
	 * @return true if its present, false otherwise
	 */
	public boolean usernameExist(String username) {

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT id FROM member WHERE username='" + username + "';");
			conn.close();

			if (results.size() > 0) {
				return true;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return false;
	}

	/**
	 * Allows to retrieve a user's ID base on its username
	 * 
	 * @param username
	 * @return the ID
	 */
	public int getUserID(String username) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT id FROM member WHERE username='" + username + "';");
			conn.close();

			if (results.size() > 0) {
				return Integer.parseInt(results.firstKey());

			} else {
				throw new RuntimeException("User not in DB!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception, maybe turn on wampserver?");
		}
	}

	/**
	 * Allows to retrieve the user's account Type
	 * 
	 * @param username
	 * @return MemberType telling if its staff, member, ...
	 */
	public MemberType getUserMemberType(String username) {

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT label FROM membertype, member WHERE member.username='" + username
							+ "' AND member.id_memberType=membertype.id;");
			conn.close();

			if (results.size() > 0) {
				// return Integer.parseInt(results.firstKey());

				switch (results.firstKey().toUpperCase()) {
				case "REGULAR":
					return MemberType.REGULAR;
				case "SENIOR":
					return MemberType.SENIOR;
				case "CHILDREN":
					return MemberType.CHILDREN;
				case "STAFF":
					return MemberType.STAFF;
				}
			} else {
				throw new RuntimeException("User not in DB!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception, maybe turn on wampserver?");
		}

		return MemberType.UNDEFINED;

	}

	public LocalDate getUserBirthday(String username) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT birthdayDate FROM member WHERE username='" + username + "';");
			conn.close();

			if (results.size() > 0) {
				return LocalDate.parse(results.firstKey());
			} else {
				throw new RuntimeException("User not in DB!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception, maybe turn on wampserver?");
		}
	}

	public String getUserSalt(int userID) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT grainSalt FROM member WHERE id='" + userID + "';");
			conn.close();

			if (results.size() > 0) {
				return results.firstKey();
			} else {
				throw new RuntimeException("User not in DB!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception, maybe turn on wampserver?");
		}
	}

	/**
	 * Checks if user is staff by consulting the Database
	 * 
	 * @param username
	 * @return true if user is staff, false otherwise
	 */
	public boolean isStaff(String username) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT id_memberType FROM member WHERE username='" + username + "';");
			conn.close();

			if (results.size() > 0) {

				if (Integer.parseInt(results.firstKey()) == getMemberTypeID(MemberType.STAFF)) {
					return true;
				} else {
					return false;
				}

			} else {
				throw new RuntimeException("User not in DB!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception, maybe turn on wampserver?");
		}

	}

	private int getMemberTypeID(MemberType label) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT id FROM membertype WHERE label='" + label.toString() + "';");
			conn.close();

			if (results.size() > 0) {
				return Integer.parseInt(results.firstKey());

			} else {
				throw new RuntimeException("Value is not in DB, add it!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}
	}

	private int getMembershipType(int age) {

		if (Tools.isBetween(age, 1, 15)) {
			return getMemberTypeID(MemberType.CHILDREN);
		} else if (Tools.isBetween(age, 16, 50)) {
			return getMemberTypeID(MemberType.REGULAR);
		} else {
			return getMemberTypeID(MemberType.SENIOR);
		}
	}

	public TreeMap<String, HashMap<String, String>> getTicketList(int userID) {

		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT ticket.id, ticket.price, movie.title FROM ticket, movie WHERE ticket.id_movie=movie.id AND ticket.id_member='"
							+ userID + "';");
			conn.close();

			if (results.size() > 0) {
				return results;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return null;
	}

	public Room getRoomById(String id) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT number, nbPlaces FROM room WHERE number = '" + id + "';");
			conn.close();

			if (results.size() > 0) {
				Room room = new Room(Integer.parseInt(results.firstKey()),
						Integer.parseInt(results.firstEntry().getValue().get("nbPlaces")));
				return room;
			} else {
				throw new RuntimeException("Value is not in DB, add it!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}
	}

	/*
	 * Get the discount of a User
	 */
	public float getDiscount(User currentUser) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT a.id, a.discount FROM membertype as a, member as b WHERE b.id = " + currentUser.getUserID()
							+ " AND b.id_memberType = a.id;");
			conn.close();
			if (results.size() > 0) {
				float discount = Float.parseFloat(results.firstEntry().getValue().get("a.discount"));
				return discount;
			} else {
				throw new RuntimeException("Value is not in DB, add it!");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}
	}

	/*
	 * Get all available sessions for a movie
	 */
	public ArrayList<Session> getSessionsByMovie(MovieDetail movie) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn,
					"SELECT id, start, end, ticketPrice, id_movie, number, available_seats FROM session where id_movie = "
							+ movie.getMovieID() + ";");
			conn.close();

			if (results.size() > 0) {
				ArrayList<Session> sessions = new ArrayList<Session>();
				for (Entry<String, HashMap<String, String>> result : results.entrySet()) {
					int key = Integer.parseInt(result.getKey());
					HashMap<String, String> value = result.getValue();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					Room room = getRoomById(value.get("number"));
					LocalDateTime startTime = LocalDateTime.parse(value.get("start"), formatter);
					LocalDateTime endTime = LocalDateTime.parse(value.get("end"), formatter);
					int ticketPrice = Integer.parseInt(value.get("ticketPrice"));
					int availableSeats = Integer.parseInt(value.get("available_seats"));
					Session session = new Session(key, movie, room, startTime, endTime, ticketPrice, availableSeats);
					sessions.add(session);
				}
				return sessions;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return null;
	}

	/**
	 * Allows us to retrive a full list of all the film's titles in the database
	 * 
	 * @return
	 */
	public ArrayList<String> retrieveAllMoviesTitles() {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			TreeMap<String, HashMap<String, String>> results = select(conn, "SELECT title FROM movie");
			conn.close();

			if (results.size() > 0) {
				ArrayList<String> movies = new ArrayList<String>();
				for (Entry<String, HashMap<String, String>> result : results.entrySet()) {
					movies.add(result.getKey());
				}
				return movies;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Connection to the database lifted an exception !");
		}

		return null;

	}

	/**
	 * Deletes a movie from the DB using its title
	 * 
	 * @param movieTitle
	 */
	public void deleteMovieFromDB(String movieTitle) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			// Check if username exists
			dropValues(conn, "DELETE FROM movie WHERE movie.title='" + movieTitle + "';");
			conn.close();

		} catch (Exception e) {
			throw new RuntimeException("Error while deleting!");
		}
	}

	public boolean buyTickets(Ticket ticket) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);
			String userId = null;
			if (ticket.getUserID() != -1) {
				userId = Integer.toString(ticket.getUserID());
			}

			String query = "INSERT INTO ticket VALUES (NULL, " + ticket.getPrice() + ", "
					+ ticket.getSession().getMovie().getMovieID() + ", " + userId + ", " + ticket.getSession().getId()
					+ ");";

			insertValues(conn, query);

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean addMovieToCatalog(Movie movie) {
		try {
			Connection conn = DriverManager.getConnection(url, user, dbpwd);

			String query = "INSERT INTO movie VALUES (NULL, '" + movie.getImdbID() + "', '" + movie.getTitle() + "');";

			insertValues(conn, query);

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

}
