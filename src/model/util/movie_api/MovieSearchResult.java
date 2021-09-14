package model.util.movie_api;

import java.util.List;

import model.films.Movie;

public class MovieSearchResult {

	private int totalResults;
	private List<Movie> Search;

	public int getTotalResult() {
		return totalResults;
	}

	public void setTotalResult(int totalResult) {
		this.totalResults = totalResult;
	}

	public List<Movie> getResults() {
		return Search;
	}

	public void setResults(List<Movie> search) {
		Search = search;
	}

	public MovieSearchResult(int totalResult, List<Movie> search) {
		super();
		this.totalResults = totalResult;
		Search = search;
	}

}
