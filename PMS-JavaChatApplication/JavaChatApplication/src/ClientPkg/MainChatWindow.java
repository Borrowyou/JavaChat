package ClientPkg;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import GlobalPkg.ChatChanges;
import GlobalPkg.ChatMessage;
import GlobalPkg.UserInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainChatWindow
{
	private AnchorPane		 anchorPaneMain;
	
	private Stage			 stagePrimary;

	public MainChatWindow()
	{
	}

	public void Init()
	{
		FXMLLoader loader = new FXMLLoader();
        URL u = MainChatWindow.class.getResource("MainChatWindow.fxml");
        loader.setLocation( MainChatWindow.class.getResource("MainChatWindow.fxml") );
        
        // Load the user interface from fxml
        try
        {
			anchorPaneMain = (AnchorPane) loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Scene scene = new Scene(anchorPaneMain);
        stagePrimary = new Stage();
        stagePrimary.setScene(scene);
		stagePrimary.show();
	}
	public void showWindow()
	{
		stagePrimary.show();
	}
	

	
}
