package controller;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.*;
import javafx.stage.Stage;
import logic.Client;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;

public class AddFriend {
	private String from;
	@FXML private GridPane grid;
	@FXML private TextField username;
	public void initData(String fr) {
		from = fr;
	}
	
	@FXML
	public void handleSearchButtonClick(ActionEvent event) throws IOException{
		grid.getChildren().clear();
		/*HBox hb = new HBox();
		hb.setSpacing(100.0);
		hb.setAlignment(Pos.TOP_LEFT);
		Text txt = new Text();
		txt.setText("Found here");
		hb.getChildren().add(txt);
		HBox hb2 = new HBox();
		hb2.getChildren().add(txt);
		hb2.setSpacing(200.0);
		hb2.setAlignment(Pos.TOP_LEFT);*/
		Client c = ClientController.getInstance();
		if(c.searchFriend(username.getText())) {
			
			grid.setHgap(20);
		    grid.setVgap(20);
		    
		    Button btn = new Button(username.getText());
		    btn.setStyle("-fx-font-size: 40pt;");
		    btn.setStyle("-fx-background-color: #0099ff;");
		    btn.setMinSize(400, 100);
		    btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                c.addFriend(username.getText(),from);
	            }
	        });
		    
		    //adding functionality for button
		    grid.add(btn, 0, 0);
		}
		else {
			Text txt = new Text("User Not Found");
			txt.setFont(new Font(20));
			grid.add(txt, 0, 0);
		}
		
	    
	    /*Text txt = new Text();
		txt.setText("Found here");
		txt.setFont(new Font(20));
		
		Text txt2 = new Text();
		txt2.setText("Found here");
		txt2.setFont(new Font(20));
		grid.add(txt,0,0);
		grid.add(txt2,0,1);*/
		//hboxx.getChildren().addAll(hb,hb2);
		
	}
}
