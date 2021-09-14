package model.util.movie_api;

import com.google.gson.Gson;

import model.films.MovieDetail;

public class MovieRequest extends Request {

	private static String QUERYURL = "https://www.omdbapi.com/?apikey=67d07d29&i=";

	public MovieRequest() {
	}

	public MovieRequest(String id) {
		this.url = QUERYURL + id;
	}

	public MovieRequest(String id, String year) {
		this.url = QUERYURL + id;
	}

	public void changeId(String id) {
		url = QUERYURL + id;
	}

	public MovieDetail execute() {
		queryResult = getJsonStringResponseFromUrl(url);
		if (queryResult != null) {
			Gson g = new Gson();

			MovieDetail movie = g.fromJson(queryResult, MovieDetail.class);

			return movie;
		}
		return null;
	}

}
