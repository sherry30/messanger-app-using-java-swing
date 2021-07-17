package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.Client;
import javafx.scene.text.Text;

public class Signup {
	@FXML private Button signUp_button;
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private PasswordField conf_password;
	@FXML private TextField email;
	@FXML private TextField FirstName;
	@FXML private TextField LastName;
	@FXML private Text passNotMatchingError;
	
	public void handleSignUpButtonClick(ActionEvent event) throws IOException {
		// TODO: Add length checks
		String user = username.getText();
		String pass = password.getText();
		String conf_pass = conf_password.getText();
		String em = email.getText();
		String fName = FirstName.getText();
		String lName = LastName.getText();
		
		if(!pass.equals(conf_pass)) {
			System.out.println("Password not matching");
			passNotMatchingErrorF("passwords don't match");
			return;
		}
		else {
			Client c = ClientController.getInstance();
			// If auth successful, show friends list screen
			if(c.SignUp(user, pass,em,fName,lName)){
				// Get Stage
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				// Create new Scene
				Scene scene = new Scene (FXMLLoader.load(getClass().getResource("/ui/Login_page.fxml")), 400, 400);
				// Update new Scene
				stage.setScene(scene);				
			}

		}
	}
	
	public void handleLoginButtonClick(ActionEvent event) throws IOException {
		// Get Stage
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		// Create new Scene
		Scene scene = new Scene (FXMLLoader.load(getClass().getResource("/ui/Login_page.fxml")), 400, 400);
		// Update new Scene
		stage.setScene(scene);
	}
	
	//when confirm pass doesnt match
	private void passNotMatchingErrorF(String error) {
		passNotMatchingError.setText(error);
	}
}
