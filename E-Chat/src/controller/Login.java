package controller;

import logic.Client;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

public class Login {
	@FXML private Button login_button;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	public void handleLoginButtonClick(ActionEvent event) throws IOException {
		// Connect to server
		// Authorize
		Client c = ClientController.getInstance();
		// TODO: Add length checks
		String user = username.getText();
		String pass = password.getText();
		if (c.AuthenticateLogin(user, pass)) {
			// If auth successful, show friends list screen
			FriendList controller = new FriendList(username.getText());
			// Get Stage
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Friend_List.fxml"));
			loader.setController(controller);
			// Create new Scene
			Scene scene = new Scene (loader.load(), 500, 600);
			
			//sending parameter username
			//FriendList controller = loader.getController();			

			//  stage.show();
			// Update new Scene
			stage.setScene(scene);
		}
	}
	
	public void handleSignupButtonClick(ActionEvent event) throws IOException {
		// Get Stage
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		// Create new Scene
		Scene scene = new Scene (FXMLLoader.load(getClass().getResource("/ui/Signup_page.fxml")), 500, 700);
		// Update new Scene
		stage.setScene(scene);
	}
}
