package view;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class allowing to change graphical Scenes
 * 
 * @author Maia
 *
 */
public class ScreenController {

	private static ScreenController instance = null;

	public static ScreenController getInstance() {
		if (instance == null)
			instance = new ScreenController();

		return instance;
	}

	private Stage primaryStage = null;
	private Pages activeScreen = null;
	private HashMap<Pages, Scene> screens = new HashMap<Pages, Scene>();

	private ScreenController() {

	}

	public void addScreen(Pages name, Scene screen) {
		if (screens.get(name) != null) {
			screens.remove(name);
		}
		screens.put(name, screen);
	}

	public Scene getScreen(Pages name) {
		return screens.get(name);
	}

	public Pages getActiveScreenName() {
		return activeScreen;
	}

	public void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}

	public void activateScreen(Pages name) {
		if (!screens.containsKey(name)) {
			throw new IllegalArgumentException("Screen is not referenced!");
		}

		primaryStage.setScene(screens.get(name));
		activeScreen = name;
		if (name.toString().contains("Dashboard")) {
			MenuViewManager.getInstance().setMainView(screens.get(name).lookup("#mainView"));
		}
	}

	public enum Pages {
		menu, signUp, memberDashboard, staffDashboard, guestDashboard, cartSummary
	}

	private final static String MENU_SCREEN_NAME = "menu";
	private final static String SIGNUP_SCREEN_NAME = "signUp";

}
