package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.stage.Stage;
import logic.Client;
import javafx.geometry.Pos;
//import javafx.scene.layout.GridPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.*;
import javafx.scene.control.Button;


public class FriendRequestList implements Initializable{
	
	private String username; 
	@FXML private GridPane grid;
	public FriendRequestList(String use) {
		username = use;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resource) {
		//grid.getChildren().clear();
		grid.setHgap(30);
	    grid.setVgap(30);
		Client c = ClientController.getInstance();
		String[] strArray = c.getRequests(username);		
		int n=Integer.parseInt(strArray[0]);
		for(int i=0;i<n;i++) {
			VBox inner = new VBox();
			inner.setMinWidth(400);
			inner.setMinHeight(20);
			inner.setSpacing(10);
			inner.setBackground(new Background(new BackgroundFill(Color.web("#121212"),CornerRadii.EMPTY,null)));
			Text txt = new Text(strArray[i+1]);
			txt.setFont(new Font(20));
			txt.setFill(Color.WHITE);
			
			//HBox of buttons 
			HBox hbut = new HBox();
			hbut.setMinWidth(400);
			hbut.setMinHeight(20);
			hbut.setSpacing(20);
			hbut.setBackground(new Background(new BackgroundFill(Color.web("#121212"),CornerRadii.EMPTY,null)));
			
			//reject buttin
			Button reject = new Button("REJECT");
			reject.setStyle("-fx-font-size: 40pt;");
		    reject.setStyle("-fx-background-color: RED;");
		    //reject.setStyle("fx-stroke-width: 2;");
		    reject.setMinSize(100, 30);
		    
		    //accept button
		    Button accept = new Button("ACCEPT");
		    accept.setStyle("-fx-font-size: 40pt;");
		    accept.setStyle("-fx-background-color: GREEN;");
		    //accept.setStyle("fx-stroke-width: 2;");
		    accept.setMinSize(100, 30);
		    
		    final int temp = i+1;
		    accept.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                c.acceptRequest(username,strArray[temp]);
	            }
	        });
		    
		    hbut.getChildren().addAll(reject,accept);
			inner.getChildren().addAll(txt,hbut);					
			grid.add(inner, 0, i);
		}
		
	}
	
	public void setData(String user) {
		username = user;
		//System.out.println("username: "+username);
		
		
		
	}
	
	
}
