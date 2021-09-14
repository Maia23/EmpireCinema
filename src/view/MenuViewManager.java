package view;

import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * Class controlling the secondary scene of our application when menu is
 * displayed
 * 
 * @author Maia
 *
 */
public class MenuViewManager {

	private static MenuViewManager instance = null;

	public static MenuViewManager getInstance() {
		if (instance == null)
			instance = new MenuViewManager();

		return instance;
	}

	private MenuViewManager() {
	}

	private HashMap<MenuPages, Node> roots = new HashMap<MenuPages, Node>();
	private BorderPane activeMainView = null;

	public void addScreen(MenuPages name, Node root) {
		if (roots.get(name) != null) {
			roots.remove(name);
		}
		roots.put(name, root);
	}

	public Node getRoot(MenuPages pageName) {
		return roots.get(pageName);
	}

	public void setMainView(Node mainView) {
		this.activeMainView = (BorderPane) mainView;
	}

	public BorderPane getMainView() {
		return activeMainView;
	}

	public enum MenuPages {
		movieCatalog, userSettings, customerList, catalogManager
	}
}
