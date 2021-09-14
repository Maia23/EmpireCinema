package application;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.util.DataChecker;
import model.util.db.ECDBManager;
import view.ScreenController;
import view.ScreenController.Pages;
import view.View;

public class SignUpController {

	private static ScreenController screenController = ScreenController.getInstance();
	private static ECDBManager dbManager = ECDBManager.getInstance();

	@FXML
	private ProgressBar pwdStrengthBar;

	@FXML
	private Button logInRedirectBtn;

	@FXML
	private PasswordField signUpPwdInput;

	@FXML
	private Button createAccountBtn;

	@FXML
	private DatePicker signUpBirthdayInput;

	@FXML
	private TextField signUpUsernameInput;

	/**
	 * Method to create a new account and upload the data to the DB
	 *  IT DOES NOT CHECK IF DATA IS VALID ( @testData )
	 * @param event
	 */
	@FXML
	void createNewAccount(ActionEvent event) {
		// All fields have been validated, uploading to DB
		dbManager.signUp(signUpUsernameInput.getText(), signUpPwdInput.getText(), signUpBirthdayInput.getValue());

		View.generateInformationWindow("Account Created Succesfully", "Your account has been created",
				"You can now log into your account, enjoy!");
		
		signUpUsernameInput.setText("");
		signUpBirthdayInput.getEditor().clear();
		signUpPwdInput.setText("");
		pwdStrengthBar.setProgress(0);
		
		screenController.activateScreen(Pages.menu);
		// accountCreatedMsg.setVisible(true);
	}

	/**
	 * Method testing the data given by user
	 * @param event
	 */
	@FXML
	void testData(Event event) {
		if (DataChecker.checkPwdStrength(signUpPwdInput.getText()) >= 6
				&& DataChecker.checkUsername(signUpUsernameInput.getText())
				&& !dbManager.usernameExist(signUpUsernameInput.getText())
				&& (signUpBirthdayInput != null && DataChecker.checkBirthdayDate(signUpBirthdayInput.getValue()))) {
			createAccountBtn.setDisable(false);
		} else {
			createAccountBtn.setDisable(true);
		}
	}

	
	/**
	 * This method tests a passwords strength according to certain principals, length, the presence or absent of special characters, ...
	 * @param event
	 */
	@FXML
	void testPwdStrength(KeyEvent event) {
		int strengthLevel = DataChecker.checkPwdStrength(signUpPwdInput.getText());
		pwdStrengthBar.setProgress((float) strengthLevel / 10);
		switch (strengthLevel) {
		case 2:
			pwdStrengthBar.setStyle("-fx-accent: red;");
			break;
		case 6:
			pwdStrengthBar.setStyle("-fx-accent: orange;");
			break;
		case 10:
			pwdStrengthBar.setStyle("-fx-accent: green;");
			break;
		}

		testData(null);
	}

	@FXML
	void showLogInPage(ActionEvent event) {
		screenController.activateScreen(Pages.menu);
	}

}