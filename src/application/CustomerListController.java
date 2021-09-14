package application;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.users.MemberType;
import model.users.User;
import model.util.Tools;
import view.View;

public class CustomerListController extends MenuController {

	@FXML
    private AnchorPane customerListPage;

    @FXML
    private ScrollPane scrollPaneClients;

    @FXML
    private Button refreshBtn;
    
    @FXML
    private Button detailsBtn;
    
    private TableView<User> customerList;
    
    /**
     * Method giving the details of the last purchases made by the client.
     * It generates a popup windows that presents the information to the user
     * @param event
     */
    @FXML
    void showLastTickets(ActionEvent event) {
    	
    	if (customerList.getSelectionModel().getSelectedItem() != null) {
    		User user = customerList.getSelectionModel().getSelectedItem();
    		TreeMap<String, HashMap<String, String>> customerTickets = dbManager.getTicketList(user.getUserID());
    		
    		StringBuilder res = new StringBuilder();
    		
    		if (customerTickets == null) {
    			View.generateInformationWindow("No tickets", "The user has not yet bought tickets", "");
			} else {
				for (Entry<String, HashMap<String, String>> entry : customerTickets.entrySet()) {
	    			res.append("\n -> (ID : " + entry.getKey() +") Ticket for movie : " + entry.getValue().get("movie.title") + " at a value of " + entry.getValue().get("ticket.price") + "£");
				}
	    		
	    		View.generateInformationWindow("Tickets Bought by " + user.getUsername() , "This are the last tickets bought by this user : ", res.toString());
			}
    	}
    }

    /**
     * Loads the client data to the TableView
     * @param event
     */
    @FXML
    void loadData(ActionEvent event) {
    	
    	TreeMap<String, HashMap<String, String>> customerListFromDB = dbManager.getCustomerList();
    	
    	VBox root = new VBox();
    	
    	customerList = new TableView<User>();
    	
    	// Create column ID
        TableColumn<User, String> idColumn
                = new TableColumn<User, String>("ID");
   
        // Create column username.
        TableColumn<User, String> usernameColumn
                = new TableColumn<User, String>("USERNAME");
   
        // Create column birthday.
        TableColumn<User, String> birthdayColumn
                = new TableColumn<User, String>("BIRTHDAY");
   
        // Create column memberType.
        TableColumn<User, String> accountColumn
                = new TableColumn<User, String>("ACCOUNT TYPE");
        
        customerList.getColumns().addAll(idColumn, usernameColumn, birthdayColumn, accountColumn);
        customerList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        customerList.prefHeightProperty().bind(scrollPaneClients.heightProperty());
        customerList.prefWidthProperty().bind(scrollPaneClients.widthProperty());
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    	usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    	birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
    	accountColumn.setCellValueFactory(new PropertyValueFactory<>("accountTypeString"));
    	
    	root.getChildren().add(customerList);
    	
    	idColumn.setSortType(TableColumn.SortType.ASCENDING);
    	
    	ObservableList<User> list = generateUserList(customerListFromDB);
    	customerList.setItems(list);
    	
    	scrollPaneClients.setFitToHeight(true);
    	scrollPaneClients.setFitToWidth(true);
    	scrollPaneClients.setContent(root);
    
    }
    

    /**
     * Method that constructs the list of information ready to be presented and introduced in the TableView
     * @param customerListFromDB
     * @return
     */
	private static ObservableList<User> generateUserList(TreeMap<String, HashMap<String, String>> customerListFromDB) {
		
		 ObservableList<User> list = FXCollections.observableArrayList();
		
		for (Entry<String, HashMap<String, String>> entry : customerListFromDB.entrySet()) {
			
			list.add(new User(Integer.parseInt(entry.getKey()), entry.getValue().get("member.username"), LocalDate.parse(entry.getValue().get("member.birthdayDate")), Tools.getMemberTypeFromString(entry.getValue().get("memberType.label"))));
			
		}
		
		return list;
	}
	
}
