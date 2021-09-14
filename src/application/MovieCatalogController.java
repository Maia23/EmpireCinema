package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.films.MovieDetail;

public class MovieCatalogController extends Controller implements Initializable {

	@FXML
	private AnchorPane movieCatalogPage;

	@FXML
	private ScrollPane scrollPaneMovies;

	@FXML
	private GridPane gridMovies;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateGridMovies();
	}

	public void updateGridMovies() {
		ArrayList<VBox> movies = initializeMovies();

		int row = 0;
		int col = 0;

		for (VBox vbox : movies) {
			gridMovies.add(vbox, col, row, 1, 1);
			if (++col == 3) {
				col = 0;
				row++;
			}
		}

		gridMovies.setPadding(new Insets(20));
	}

	public ArrayList<VBox> initializeMovies() {
		ArrayList<MovieDetail> movies = dbManager.getMovies();
		ArrayList<VBox> results = new ArrayList<VBox>();

		for (MovieDetail movie : movies) {
			VBox vbox = new VBox(10);
			vbox.setAlignment(Pos.CENTER);

			Image image = new Image(movie.getPoster());
			ImageView poster = new ImageView(image);
			poster.setFitWidth(180);
			poster.setFitHeight(270);
			vbox.setFillWidth(true);
			vbox.setStyle("-fx-cursor: hand");
			vbox.setOnMouseClicked(e -> {
				FXMLLoader loaderMovieCatalog = new FXMLLoader(
						getClass().getResource("/view/rsrc/fxFiles/MovieSessions.fxml"));
				loaderMovieCatalog.setController(new MovieSessionsController(movie));
				AnchorPane movieCatalogRoot = null;
				try {
					movieCatalogRoot = loaderMovieCatalog.load();
				} catch (IOException e1) {
					System.out.println("Movie sessions was not found!");
					e1.printStackTrace();
				}

				Scene secondScene = new Scene(movieCatalogRoot, 800, 750);

				// New window (Stage)
				Stage newWindow = new Stage();
				newWindow.setTitle("Reserve this movie.");
				newWindow.setScene(secondScene);

				newWindow.show();
			});

			vbox.getChildren().addAll(poster, new Text(movie.getTitle()));
			results.add(vbox);
		}

		return results;
	}

}
