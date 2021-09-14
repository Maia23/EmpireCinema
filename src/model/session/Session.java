package model.session;

import java.time.LocalDateTime;

import model.films.MovieDetail;
import model.films.Room;

public class Session {

	private MovieDetail movie;
	private Room room;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int ticketPrice;
	private int availableSeats;
	private int id;

	public MovieDetail getMovie() {
		return movie;
	}

	public void setMovie(MovieDetail movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Session(int id, MovieDetail movie, Room room, LocalDateTime startTime, LocalDateTime endTime,
			int ticketPrice, int availableSeats) {
		super();
		this.movie = movie;
		this.room = room;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ticketPrice = ticketPrice;
		this.availableSeats = availableSeats;
		this.id = id;
	}

	public String toString() {
		return "Start: " + this.startTime.toString() + "\nEnd: " + this.endTime.toString() + "\nPrice: "
				+ this.ticketPrice + "€";

	}

}
