package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.films.MovieDetail;
import model.session.Session;

public class MovieSessionsController extends Controller implements Initializable {

	private final MovieDetail movie;

	@FXML
	private Button addToCartButton;

	@FXML
	private VBox sessions;

	@FXML
	private Label releaseDate;

	@FXML
	private Label plot;

	@FXML
	private Label genre;

	@FXML
	private Label title;

	@FXML
	private ImageView poster;

	ToggleGroup radioGroup;

	Session selectedSession = null;

	@FXML
	void addToCart(ActionEvent event) {
		if (selectedSession != null) {
			currentUser.addMovieToCart(selectedSession);
		}
		Stage stage = (Stage) addToCartButton.getScene().getWindow();
		stage.close();
	}

	public MovieSessionsController(MovieDetail movie) {
		this.movie = movie;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		title.setText(this.movie.getTitle());
		releaseDate.setText(this.movie.getReleased());
		plot.setText(this.movie.getPlot());
		plot.setWrapText(true);
		genre.setText(this.movie.getGenre());
		poster.setImage(new Image(this.movie.getPoster()));
		setSessions();
	}

	public void setSessions() {
		sessions.setSpacing(10);

		ArrayList<Session> sessionList = dbManager.getSessionsByMovie(movie);
		if (sessionList != null) {
			radioGroup = new ToggleGroup();
			for (Session session : sessionList) {
				RadioButton radioButton = new RadioButton(session.toString());
				radioButton.setOnAction(e -> {
					this.selectedSession = session;
				});
				radioButton.setToggleGroup(radioGroup);
				sessions.getChildren().add(radioButton);
			}
		}

	}

}
