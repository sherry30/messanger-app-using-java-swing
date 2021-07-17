package application;
	
import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		int port =5678;
		if(args.length!=0)
	   	port = Integer.parseInt(args[0]);
   		try {
   			Thread t = new ConnectionServer(port);
   			t.start();
  		}catch(Exception e) {
	  		e.printStackTrace();
  		}
		launch(args);
	}
}
