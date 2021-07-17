package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Client;

public class Chat {
	@FXML private Text user;
	@FXML private TextField msg;
	@FXML private ListView chatListView;
	ObservableList<String> chatMessages = FXCollections.observableArrayList();
	private String username;
	private String name;
	
	public Chat(String text, String user) {
		name = text;
		username = user;
	}
	
	public void initialize() {
		user.setText(name);
		// Request chat log from server
		loadChat();
	}
	
	private void loadChat() {
		boolean messageFound = true;
		Client c = ClientController.getInstance();
		String strArray = c.getChatLog(username, name);
		List<String> messageList = Arrays.asList(strArray.split(","));
		System.out.println(strArray);
		for (int i = 0; i < messageList.size(); i+=6) {
			if(messageList.size()<=i+5) {
				messageFound = false;
				return;
				
			}
			if (username.equals(messageList.get(i+1))) {
				//System.out.println("Working");
				chatMessages.add("me: " + messageList.get(i+5));
			}
			else {
				//System.out.println("Working");
				chatMessages.add(name + ": " + messageList.get(i+5));
			}
			System.out.println(messageList.get(i+3));
		}
		if(messageFound)
			chatListView.setItems(chatMessages);
	}
	
	public void handleFriendListClick(ActionEvent event) throws IOException {
		FriendList controller = new FriendList(username);
		// Get Stage
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Friend_List.fxml"));
		loader.setController(controller);
		// Create new Scene
		Scene scene = new Scene (loader.load(), 500, 600);
		
		stage.setScene(scene);
	}
	
	public void handleLogoutButtonClick(ActionEvent event) throws IOException {
		Client c = ClientController.getInstance();
		if (c.Logout(username)) {
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			// Create new Scene
			Scene scene = new Scene (FXMLLoader.load(getClass().getResource("/ui/Login_page.fxml")), 400, 400);
			// Update new Scene
			stage.setScene(scene);
		}
	}
	
	public void handleSendClick(ActionEvent event) throws IOException{
		Client c = ClientController.getInstance();
		c.sendMessage(username, name, msg.getText());
		chatMessages.add(msg.getText());
		msg.setText("");
		
	}
}
