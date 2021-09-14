package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.util.movie_api.MovieSearchRequest;
import model.util.movie_api.MovieSearchResult;
import view.View;

public class ManageCatalogController extends MovieCatalogController {

	@FXML
	private AnchorPane manageCatalogPage;

	@FXML
	private ChoiceBox<String> movieChoice;

	@FXML
	private Button deleteBtn;

	@FXML
	private TextField searchInput;

	@FXML
	private Button searchBtn;

	@FXML
	void deleteFilm(ActionEvent event) {
		if (movieChoice.getSelectionModel().getSelectedItem() != null) {
			if (View.generateConfirmationWindow("Deleting a movie", "You are about to delete a movie from the Catalog",
					"Please, do not get yelled at by your boss...")) {
				dbManager.deleteMovieFromDB(movieChoice.getSelectionModel().getSelectedItem());
				View.generateInformationWindow("The video has been removed!",
						"Please keep in mind that it was YOUR decision", "Don't try to blame it on us!");
				updateGridMovies();
			}
		}
	}

	@FXML
	void search(ActionEvent event) {
		MovieSearchRequest movieSearchRequest = new MovieSearchRequest(searchInput.getText());
		MovieSearchResult result = movieSearchRequest.search();

		FXMLLoader loadSearchResult = new FXMLLoader(
				getClass().getResource("/view/rsrc/fxFiles/MovieSearchResult.fxml"));
		loadSearchResult.setController(new MovieSearchResultController(result));
		AnchorPane movieSearchResultRoot = null;
		try {
			movieSearchResultRoot = loadSearchResult.load();
		} catch (IOException e1) {
			System.out.println("Movie search was not found!");
			e1.printStackTrace();
		}

		Scene secondScene = new Scene(movieSearchResultRoot);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("Movie Search Result");
		newWindow.setScene(secondScene);

		newWindow.show();

	}

	/**
	 * Loads the choiceBox for film deletion and loads it with data
	 * 
	 * @param event
	 */
	@FXML
	void updateMovies(MouseEvent event) {

		if (movieChoice.getItems().size() == 0) {
			ArrayList<String> movieList = dbManager.retrieveAllMoviesTitles();

			for (String title : movieList) {
				movieChoice.getItems().add(title);
			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
