package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.users.User;
import model.util.db.ECDBManager;
import view.ScreenController;
import view.ScreenController.Pages;
import view.View;

/**
 * The Controller class is the spinal cord of the program, it controls every
 * action it happens
 * 
 * @author Maia
 *
 */
public class Controller {

	public static void main(String[] args) {
		Application.launch(View.class, "");

	}

	protected static User currentUser = null;
	protected static ScreenController screenController = ScreenController.getInstance();
	protected static ECDBManager dbManager = ECDBManager.getInstance();

	@FXML
	private TextField usernameInput;

	@FXML
	private PasswordField pwdInput;

	@FXML
	private Button logInBtn;

	@FXML
	private Button signUpBtn;

	@FXML
	private Button maiaLK;

	@FXML
	private Button massLK;

	@FXML
	private Button guestBtn;

	@FXML
	private Text textWrongCredentials;

	@FXML
	private VBox accountCreatedMsg;

	@FXML
	private Button createAccountBtn;

	@FXML
	private Button logInRedirectBtn;

	@FXML
	private Text usernameMemberMenu;

	@FXML
	void guestLogIn(ActionEvent event) {
		currentUser = new User();
		screenController.activateScreen(Pages.guestDashboard);
	}

	@FXML
	void logIn(ActionEvent event) {
		if (usernameInput.getText().equals("") || pwdInput.getText().equals("")) {
			return;
		}

		if (dbManager.tryLogIn(usernameInput.getText(), pwdInput.getText())) {
			// Credentials are good, logging in user
			currentUser = new User(dbManager.getUserID(usernameInput.getText()), usernameInput.getText(),
					dbManager.getUserBirthday(usernameInput.getText()),
					dbManager.getUserMemberType(usernameInput.getText()));

			// Check user's rank
			if (dbManager.isStaff(usernameInput.getText())) {
				screenController.activateScreen(Pages.staffDashboard);
			} else {
				screenController.activateScreen(Pages.memberDashboard);
			}
		} else {
			pwdInput.setText("");
			textWrongCredentials.setVisible(true);
		}
	}

	@FXML
	void logInIfEnterKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			logIn(null);
		}
	}

	@FXML
	void showMaiaLk(ActionEvent event) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(MAIA_LINKEDIN));
			}
		} catch (IOException | URISyntaxException e) {
			System.out.println("Exception when loading Maia's LinkedIn ");
			e.printStackTrace();
		}
	}

	@FXML
	void showMassLk(ActionEvent event) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(SOURANG_LINKEDIN));
			}
		} catch (IOException | URISyntaxException e) {
			System.out.println("Exception when loading Mass's LinkedIn ");
			e.printStackTrace();
		}
	}

	@FXML
	void signUp(ActionEvent event) {
		screenController.activateScreen(Pages.signUp);
	}

	private final static long TRANSITION_TIME = 100;

	private final static String SOURANG_LINKEDIN = "https://www.linkedin.com/in/massourang-sourang-751208185/";
	private final static String MAIA_LINKEDIN = "https://www.linkedin.com/in/alexandre-maia-7b00b9175/";

}
