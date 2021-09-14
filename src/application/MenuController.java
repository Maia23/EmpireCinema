package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import view.MenuViewManager;
import view.MenuViewManager.MenuPages;
import view.ScreenController.Pages;
import view.View;

/**
 * Class that delegates wich screen should be active and when
 * 
 * @author Maia
 *
 */
public class MenuController extends Controller {

	private static MenuViewManager menuViewManager = MenuViewManager.getInstance();

	@FXML
	private Text usernameMemberMenu;

	/* #########STAFF########### */
	@FXML
	private Text accountName;

	@FXML
	private Button manageCatalogBtn;

	@FXML
	private Button manageDiscountsBtn;

	@FXML
	private Button customerListBtn;

	@FXML
	private Button statsBtn;

	@FXML
	private Button logoutBtn;

	@FXML
	private Button cartButton;

	@FXML
	private Button settingsBtn;

	private BorderPane mainView;

	@FXML
	private Button movieCatalogBtn;

	@FXML
	void displayMovieCatalog(ActionEvent event) throws IOException {
		// MOVIE CATALOG
		FXMLLoader loaderMovieCatalog = new FXMLLoader(
				getClass().getResource("/view/rsrc/fxFiles/menuScenes/movieCatalog.fxml"));
		AnchorPane movieCatalogRoot = loaderMovieCatalog.load();
		menuViewManager.getMainView().setCenter(movieCatalogRoot);
	}

	@FXML
	void displayUserSettings(ActionEvent event) {
		menuViewManager.getMainView().setCenter(menuViewManager.getRoot(MenuPages.userSettings));
	}

	@FXML
	void logOut(ActionEvent event) {
		if (View.generateConfirmationWindow("LogOut", "Are you sure that you want to disconnect?",
				"You will be returned to our menu.")) {
			currentUser = null;
			screenController.activateScreen(Pages.menu);
		}

	}

	@FXML
	void showManageCatalogPage(ActionEvent event) {
		menuViewManager.getMainView().setCenter(menuViewManager.getRoot(MenuPages.catalogManager));
	}

	@FXML
	void displayCartSummary(ActionEvent event) throws IOException {
		FXMLLoader cartSummaryMainMenu = new FXMLLoader(
				getClass().getResource("/view/rsrc/fxFiles/menuScenes/CartSummary.fxml"));
		AnchorPane cartSummaryMainMenuRoot = cartSummaryMainMenu.load();
		menuViewManager.getMainView().setCenter(cartSummaryMainMenuRoot);
	}

	@FXML
	void showCustomerListPage(ActionEvent event) {
		menuViewManager.getMainView().setCenter(menuViewManager.getRoot(MenuPages.customerList));
	}

	@FXML
	void showManageDiscountPage(ActionEvent event) {

	}

	@FXML
	void showStatsPage(ActionEvent event) {

	}

}
