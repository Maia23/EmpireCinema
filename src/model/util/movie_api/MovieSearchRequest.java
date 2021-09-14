package model.util.movie_api;

import com.google.gson.Gson;

public class MovieSearchRequest extends Request {

	private static String SEARCHURL = "https://www.omdbapi.com/?apikey=67d07d29&s=";
	private String year = null;
	private int lastPage = 1;
	private int currentPage = 1;
	private MovieSearchResult movieSearchResult;

	public MovieSearchRequest(String title) {
		this.url = SEARCHURL + replaceSpace(title);
	}

	public String replaceSpace(String title) {
		title = title.replace(" ", "%20");
		return title;
	}

	public MovieSearchRequest(String title, String year) {
		this(title);
		addYear(year);
	}

	public void addYear(String year) {
		this.year = year;
		if (this.year != null) {
			this.url = this.url + "&y=" + this.year;
		}
	}

	public void changeId(String title) {
		title.replace(" ", "%20");
		url = SEARCHURL + title;
		year = null;
		currentPage = 1;
		lastPage = 1;
	}

	public void changeIdAndYear(String title, String year) {
		changeId(title);
		addYear(year);
	}

	public MovieSearchResult search() {
		queryResult = getJsonStringResponseFromUrl(url + "&page=" + currentPage);
		if (queryResult != null) {
			Gson g = new Gson();

			movieSearchResult = g.fromJson(queryResult, MovieSearchResult.class);

			lastPage = (int) Math.ceil(movieSearchResult.getTotalResult() / 10);
			currentPage = 1;

			return movieSearchResult;
		}
		return null;
	}

	public boolean hasNext() {
		if (currentPage < lastPage) {
			return true;
		}
		return false;
	}

	public boolean hasPrevious() {
		if (currentPage > 1) {
			return true;
		}
		return false;
	}

	public MovieSearchResult getNextPageResult() {
		if (hasNext()) {
			currentPage++;
			search();
			return movieSearchResult;
		}
		return null;
	}

	public MovieSearchResult getPreviousPageResult() {
		if (hasPrevious()) {
			currentPage--;
			search();
			return movieSearchResult;
		}
		return null;
	}

}
