package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.films.Movie;
import model.util.movie_api.MovieSearchResult;
import view.View;

public class MovieSearchResultController extends Controller implements Initializable {

	@FXML
	private GridPane grid;

	private MovieSearchResult movieSearchResult;

	public MovieSearchResultController(MovieSearchResult movieSearchResult) {
		super();
		this.movieSearchResult = movieSearchResult;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateGridResultMovies();
	}

	public void updateGridResultMovies() {
		ArrayList<VBox> movies = initializeResultMovies();

		int row = 0;
		int col = 0;
		grid.setHgap(15);
		grid.setVgap(25);

		for (VBox vbox : movies) {
			grid.add(vbox, col, row, 1, 1);
			if (++col == 3) {
				col = 0;
				row++;
			}
		}

		grid.setPadding(new Insets(20));
	}

	public ArrayList<VBox> initializeResultMovies() {
		List<Movie> movies = movieSearchResult.getResults();
		ArrayList<VBox> results = new ArrayList<VBox>();

		for (Movie movie : movies) {
			VBox vbox = new VBox(10);
			vbox.setAlignment(Pos.CENTER);

			Image image = new Image(movie.getPoster());
			ImageView poster = new ImageView(image);
			poster.setFitWidth(180);
			poster.setFitHeight(270);
			vbox.setFillWidth(true);
			vbox.setStyle("-fx-cursor: hand");
			vbox.setOnMouseClicked(e -> {
				if (View.generateConfirmationWindow("Save movie in catalag",
						"Do you want to save the movie in the catalog?",
						"This action will store the movie in the database.")) {
					if (dbManager.addMovieToCatalog(movie)) {
						Stage stage = (Stage) grid.getScene().getWindow();
						stage.close();
						View.generateInformationWindow("Movie added!", "The movie has been added to the catalog.",
								"You can now find the movie when you reload the catalog.");
					}
				}
			});

			vbox.getChildren().addAll(poster, new Text(movie.getTitle()));
			results.add(vbox);
		}

		return results;
	}

}
