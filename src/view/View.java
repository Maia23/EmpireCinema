package view;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.MenuViewManager.MenuPages;
import view.ScreenController.Pages;

public class View extends Application {

	private static Stage primaryStage;

	private static ScreenController screenController = ScreenController.getInstance();
	private static MenuViewManager menuViewManager = MenuViewManager.getInstance();

	/**
	 * Initiates graphic interface and loads all scenes from fxml files
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			View.primaryStage = primaryStage;
			screenController.setPrimaryStage(primaryStage);

			/* ####################### MAIN PAGES ############################# */

			// MenuScene
			FXMLLoader loaderMenu = new FXMLLoader(getClass().getResource("/view/rsrc/fxFiles/LogIn.fxml"));
			AnchorPane menuRoot = loaderMenu.load();
			Scene menuScene = new Scene(menuRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

			// SignUp Scene
			FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/view/rsrc/fxFiles/SignUp.fxml"));
			AnchorPane signUpRoot = loaderSignUp.load();
			Scene signUpScene = new Scene(signUpRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

			// MemberMainMenu Scene
			FXMLLoader loaderMemberMainMenu = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/mainMenus/memberMainMenu.fxml"));
			AnchorPane memberMainMenuRoot = loaderMemberMainMenu.load();
			Scene memberMenu = new Scene(memberMainMenuRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

			// StaffMainMenu Scene
			FXMLLoader loaderStaffMainMenu = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/mainMenus/staffMainMenu.fxml"));
			AnchorPane staffMainMenuRoot = loaderStaffMainMenu.load();
			Scene staffMenu = new Scene(staffMainMenuRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

			// Guest Scene
			FXMLLoader loaderGuestMainMenu = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/mainMenus/guestMainMenu.fxml"));
			AnchorPane guestMainMenuRoot = loaderGuestMainMenu.load();
			Scene guestMenu = new Scene(guestMainMenuRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

			screenController.addScreen(Pages.menu, menuScene);
			screenController.addScreen(Pages.signUp, signUpScene);
			screenController.addScreen(Pages.memberDashboard, memberMenu);
			screenController.addScreen(Pages.staffDashboard, staffMenu);
			screenController.addScreen(Pages.guestDashboard, guestMenu);

			/* ####################### MEMBER MENU PAGES ############################# */

			// Load menu pages

			// USER SETTINGS
			FXMLLoader loaderUserSettings = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/menuScenes/member/userSettings.fxml"));
			AnchorPane userSettingsRoot = loaderUserSettings.load();

			menuViewManager.addScreen(MenuPages.userSettings, userSettingsRoot);

			/*
			 * // MenuScene FXMLLoader loaderMenu = new
			 * FXMLLoader(getClass().getResource("/view/test/memberMainMenu.fxml"));
			 * AnchorPane menuRoot = loaderMenu.load(); Scene menuScene = new
			 * Scene(menuRoot, SCREEN_WIDTH, SCREEN_HEIGHT);
			 * //menuScene.getStylesheets().add(CSS_FILE_PATH);
			 */

			/* ####################### STAFF MENU PAGES ############################# */

			// CustomerList
			FXMLLoader loaderCustomerList = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/menuScenes/staff/customerListPage.fxml"));
			AnchorPane customerListRoot = loaderCustomerList.load();

			menuViewManager.addScreen(MenuPages.customerList, customerListRoot);

			// ManageCatalog
			FXMLLoader loaderCatalogManagerList = new FXMLLoader(
					getClass().getResource("/view/rsrc/fxFiles/menuScenes/staff/ManageMovieCatalog.fxml"));
			AnchorPane catalogManagerRoot = loaderCatalogManagerList.load();

			menuViewManager.addScreen(MenuPages.catalogManager, catalogManagerRoot);

			primaryStage.setTitle("Empire Cinemas");
			primaryStage.setResizable(false);
			primaryStage.setScene(menuScene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a graphical confirmation window
	 * 
	 * @param Title    to window
	 * @param Window's header
	 * @param text     to be displayed
	 * @return true if user clicked on confirm, false otherwise
	 */
	public static boolean generateConfirmationWindow(String titleWindow, String header, String text) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(primaryStage);
		alert.setTitle(titleWindow);
		alert.setHeaderText(header);
		alert.setContentText(text);

		ButtonType confirmBtn = new ButtonType("Confirm");
		ButtonType cancelBtn = new ButtonType("Cancel");

		alert.getButtonTypes().setAll(confirmBtn, cancelBtn);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmBtn) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Generates graphical option window
	 * 
	 * @param titleWindow
	 * @param header
	 * @param text        to be displayed
	 * @param options     for user (unlimited)
	 * @return string from chosen option
	 */
	public static String generateOptionWindow(String titleWindow, String header, String text, String... options) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(titleWindow);
		alert.setHeaderText(header);
		alert.setContentText(text);

		ButtonType cancelBtn = new ButtonType("Cancel");
		alert.getButtonTypes().setAll(cancelBtn);
		for (int i = 0; i < options.length; i++) {
			alert.getButtonTypes().add(alert.getButtonTypes().size() - 1, new ButtonType(options[i]));
		}

		Optional<ButtonType> result = alert.showAndWait();
		return result.toString();
	}

	/**
	 * Generates a graphical informational window
	 * 
	 * @param titleWindow
	 * @param header
	 * @param text        to be displayed
	 */
	public static void generateInformationWindow(String titleWindow, String header, String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(primaryStage);
		alert.setTitle(titleWindow);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public void launch() {
		launch("");
	}

	private final static int SCREEN_WIDTH = 1126;
	private final static int SCREEN_HEIGHT = 803;

	private final static String CSS_FILE_PATH = "View/rsrc/fxFiles/application.css";

}
