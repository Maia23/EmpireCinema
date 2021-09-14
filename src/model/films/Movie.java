package model.films;

public class Movie {

	protected String imdbID = "";
	protected String Title = "";
	protected String Year = "";
	protected String Poster = "";

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getPoster() {
		return Poster;
	}

	public void setPoster(String poster) {
		Poster = poster;
	}

	public Movie(String imdbID, String title, String year, String poster) {
		super();
		this.imdbID = imdbID;
		Title = title;
		Year = year;
		Poster = poster;
	}

}
