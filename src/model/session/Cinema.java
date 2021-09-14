package model.session;

import java.util.ArrayList;

import model.films.Room;

public class Cinema {

	ArrayList<Room> rooms = new ArrayList<Room>();

	public Cinema() {

	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public void removeRoom(Room room) {
		rooms.remove(room);
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
}
