package model.films;

public class MovieDetail extends Movie {

	private int movieID = -1;
	private String Released = "";
	private String Runtime = "";
	private String Genre = "";
	private String Plot = "";
	private String Language = "";

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public String getReleased() {
		return Released;
	}

	public void setReleased(String released) {
		Released = released;
	}

	public String getRuntime() {
		return Runtime;
	}

	public void setRuntime(String runtime) {
		Runtime = runtime;
	}

	public String getGenre() {
		return Genre;
	}

	public void setGenre(String genre) {
		Genre = genre;
	}

	public String getPlot() {
		return Plot;
	}

	public void setPlot(String plot) {
		Plot = plot;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public MovieDetail(String imdbID, String title, String year, String poster) {
		super(imdbID, title, year, poster);
		// TODO Auto-generated constructor stub
	}
}
