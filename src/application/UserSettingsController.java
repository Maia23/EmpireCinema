package application;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.util.DataChecker;


public class UserSettingsController extends Controller {
	
	@FXML
    private AnchorPane userSettingsPage;

    @FXML
    private TextField newUsernameInput;

    @FXML
    private PasswordField newPasswordInput;

    @FXML
    private DatePicker newBirthdayInput;

    @FXML
    private Button applyChangesBtn;
    
    @FXML
    private Text invalidInputText;
    
    @FXML
    private Text validInputText;
    
    /**
     * Method to change User settings, it tests the inputs to make sure they are valid
     * @param event
     */
    @FXML
    void applyNewChanges(ActionEvent event) {
    	
    	String username = !newUsernameInput.getText().isBlank() ? newUsernameInput.getText() : "";
    	String password = !newPasswordInput.getText().isBlank() ? newPasswordInput.getText() : "";
    	LocalDate date = newBirthdayInput.getValue() == null ? null : newBirthdayInput.getValue();
    	
    	if (testInputs(username, password, date)) {
    		invalidInputText.setVisible(false);
    		if (dbManager.updateUserSettings(currentUser.getUserID(), username, password, date)) {
    			validInputText.setVisible(true);
			} else {
				invalidInputText.setText("There was an error while updating your information, try again later!");
				invalidInputText.setVisible(true);
			}
    		
    	} else {
			invalidInputText.setVisible(true);
		}
    }

	private boolean testInputs(String username, String password, LocalDate date) {
		
		boolean result = true;
		
		if (username != "" && !DataChecker.checkUsername(username)) {
			newUsernameInput.setText("");
			newUsernameInput.setStyle("-fx-text-box-border: #822222; -fx-focus-color: #B22222");
			result = false;
		}
		
		if (password != "" && DataChecker.checkPwdStrength(password) < 6) {
			newPasswordInput.setText("");
			newPasswordInput.setStyle("-fx-text-box-border: #822222; -fx-focus-color: #B22222");
			result = false;
		}
		
		if (date != null && !DataChecker.checkBirthdayDate(date)) {
			result = false;
		}
		
		return result;
	}

}
