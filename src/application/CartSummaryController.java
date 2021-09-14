package application;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.films.MovieDetail;
import model.session.Session;
import model.session.Ticket;
import view.View;

public class CartSummaryController extends Controller implements Initializable {

	@FXML
	private Label total;

	@FXML
	private AnchorPane movieCatalogPage;

	@FXML
	private VBox sessions;

	@FXML
	private Button confirmButton;

	@FXML
	private Label discount;

	private float totalPrice = 0;

	@FXML
	void confirmOrder(ActionEvent event) {
		if (sessions.getChildren().size() > 0) {
			float discountRate = 1;
			if (currentUser.getAccountType() != null) {
				discountRate -= dbManager.getDiscount(currentUser);
			}
			for (Session session : currentUser.getCart()) {
				float discountedPrice = session.getTicketPrice() * discountRate;
				Ticket ticket = new Ticket(discountedPrice, currentUser.getUserID(), session);
				dbManager.buyTickets(ticket);
			}
			currentUser.emptyCart();
			this.sessions.getChildren().clear();
			this.total.setText("0€");
			this.discount.setText("0€");
			refreshCart();
			View.generateInformationWindow("Order confirmed !", "Your order has been confirmed.", "Enjoy the film!");
		} else {
			View.generateInformationWindow("Cart empty !", "Your cart is empty.", "Add a movie session to your cart!");
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshCart();
	}

	public void refreshCart() {
		for (Session session : currentUser.getCart()) {
			HBox hbox = new HBox();
			hbox.setPrefHeight(60);
			MovieDetail movie = session.getMovie();
			Image poster = new Image(movie.getPoster());
			ImageView view = new ImageView(poster);
			view.setFitHeight(70);
			view.setFitWidth(42);
			Label title = new Label("Title: " + movie.getTitle());
			title.setPrefHeight(15);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			Label startTime = new Label("Start time: " + session.getStartTime().format(formatter));
			startTime.setPrefHeight(15);
			Label price = new Label("Price: " + session.getTicketPrice() + "€");
			price.setPrefHeight(15);
			VBox info = new VBox(title, startTime, price);
			info.setSpacing(10);
			info.setPadding(new Insets(5, 80, 5, 15));
			Button delete = new Button("Delete");
			delete.setPrefHeight(30);
			delete.setPrefWidth(70);
			delete.setAlignment(Pos.CENTER);
			delete.setPadding(new Insets(10, 0, 5, 0));
			delete.setOnAction(e -> {
				currentUser.removeMovieFromCart(session);
				this.sessions.getChildren().remove(hbox);
				calculateTotalPrice();
			});
			hbox.setAlignment(Pos.CENTER);
			hbox.getChildren().addAll(view, info, delete);
			this.sessions.getChildren().add(hbox);
			addToTotalPrice(session.getTicketPrice());
			updateDiscount();
		}
	}

	public void calculateTotalPrice() {
		this.totalPrice = 0;
		for (Session session : currentUser.getCart()) {
			this.totalPrice += session.getTicketPrice();
		}
		this.total.setText(Float.toString(this.totalPrice) + "€");
		updateDiscount();
	}

	public void updateDiscount() {
		if (currentUser.getAccountType() != null) {
			float discount = dbManager.getDiscount(currentUser) * this.totalPrice;
			this.discount.setText(Float.toString(discount) + "€");
			substractFromTotalPrice(discount);
		}
	}

	public void addToTotalPrice(int price) {
		this.totalPrice += price;
		this.total.setText(Float.toString(this.totalPrice) + "€");
	}

	public void substractFromTotalPrice(float discount) {
		this.totalPrice -= discount;
		this.total.setText(Float.toString(this.totalPrice) + "€");
	}

}