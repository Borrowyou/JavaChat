package ClientPkg;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController
{
	
	@FXML
	private TextField	txtFieldName;
	@FXML
	private TextField 	txtFieldPassword;
	@FXML
	private Button	  	btnLogin;
	@FXML
	private Button	 	btnExit;
	
	@FXML
	public void initialize()
	{
	}

	public LoginController()
	{
	}
	
	@FXML
	private void OnBtnLoginClick( ActionEvent event ) 
	{
		sendLogin();
	}
	
	private void sendLogin()
	{
		
		try {
			SocketClientSingleton.getInstance().startConnection(txtFieldName.getText(), "127.0.0.1");
		} catch (InterruptedException | ConnectException e2) {
			MessageBox.Show("Cannot connect to server", "Error");
			return;
		} catch (IOException e1) {
			MessageBox.Show("Cannot connect to server", "Error");
			return;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		MainChatWindow mainChat = new MainChatWindow();
		mainChat.Init();
	    Stage stage = (Stage) btnLogin.getScene().getWindow();

	    stage.close();
		
	}
}
