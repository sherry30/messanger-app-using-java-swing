package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.*;
import javafx.stage.Stage;
import logic.Client;
import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendList implements Initializable{
	private String username;
	@FXML private GridPane grid;
	public FriendList(String use) {
		username = use;
	}
    
	public void initialize(URL location, ResourceBundle resource){
		grid.setHgap(30);
	    grid.setVgap(10);
	    Client c = ClientController.getInstance();
	    String[] strArray = c.getFriends(username);
	    int n=0;
	    for(int i=0;i<strArray.length && strArray.length>=3;i+=3) {
	    	VBox inner = new VBox();
			inner.setMinWidth(600);
			inner.setMinHeight(80);
			//inner.setSpacing(10);
			inner.setBackground(new Background(new BackgroundFill(Color.web("#0099ff"),CornerRadii.EMPTY,null)));
			HBox hb = new HBox();
			hb.setMinWidth(500);
			hb.setMinHeight(80);
			hb.setSpacing(125);
			Text txt = new Text(strArray[i]+" "+strArray[i+1]);
			txt.setFont(new Font(20));
			txt.setFill(Color.WHITE);
			String temp = "Online";
			Color cl = Color.GREEN;
			if(strArray[i+2].equals("0")) {
				temp = "Offline";
				cl = Color.RED;
			}
			Button b = new Button("Chat");
			b.setStyle("-fx-background-color: #FFFFFF");
			b.setTextFill(Color.valueOf("0099FF"));
			b.setOnAction(e -> {
				try {
					chatHandle(e, txt);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			Text txt2 = new Text(temp);
			txt2.setFont(new Font(20));
			txt2.setFill(cl);
			hb.getChildren().addAll(txt,txt2,b);
			inner.getChildren().add(hb);					
			grid.add(inner, 0, n);
			n++;
	    }
	}
	
	private void chatHandle(ActionEvent event, Text txt) throws IOException {
		Chat controller = new Chat(txt.getText(), username);
		// Get Stage
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Chat.fxml"));
		loader.setController(controller);
		// Create new Scene
		Scene scene = new Scene (loader.load(), 400, 600);
		
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
	
	public void handleAddFreindButtonClick(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Add_Friend.fxml"));
		Parent root = loader.load();
		AddFriend controller = loader.getController();
		controller.initData(username);
	
		Stage stage = new Stage();
	
		stage.setScene(new Scene(root, 400, 400));
	
		stage.show();
	}
	
	public void handleFriendRequestsButtonClick(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Friend_Request_List.fxml"));
		FriendRequestList controller = new FriendRequestList(username);
		loader.setController(controller);
		Parent root = loader.load();
	
		Stage stage = new Stage();
	
		stage.setScene(new Scene(root, 400, 400));
	
		stage.show();
	}
}
