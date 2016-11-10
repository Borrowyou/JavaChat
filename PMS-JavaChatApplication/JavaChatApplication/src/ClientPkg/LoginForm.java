package ClientPkg;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginForm extends Application
{
    private Stage primaryStage;
    private AnchorPane rootLayout;
	
	public boolean DoLogin()
	{
		try
		{
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation( LoginForm.class.getResource("LoginForm.fxml") );

	        rootLayout = (AnchorPane) loader.load();
	        Scene scene = new Scene(rootLayout);
	        
	        primaryStage.setScene(scene);
	        primaryStage.show();
		}
		catch( IOException E )
		{
			E.printStackTrace();
		}
		return true;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("×àò");
        DoLogin();
    
	}
	@Override
	public void stop()
	{
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
