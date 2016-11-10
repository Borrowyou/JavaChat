package ClientPkg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;


import GlobalPkg.ChatChanges;
import GlobalPkg.ChatMessage;
import GlobalPkg.UserInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class MainChatWindowController  implements IPendingChanges
{
	@FXML
	private ListView<UserInfo> listViewUsersList;
	@FXML
	private TextArea		 textAreaAllChat;
	@FXML
	private TextField		 textFieldUserMessage;
	@FXML
	private Button			 buttonSendMessage;
	
	private Set<UserInfo> oUserInfoSet = null;
	private List<MessageWindow> listWindows = new ArrayList<MessageWindow>();
	
	public MainChatWindowController()
	{
		oUserInfoSet = new HashSet<UserInfo>();

	}
	@Override
	public void ProcessPendingChanges(ChatChanges oChanges) {
		FillUsers(oChanges.oUsers);
		ProcessMessages(oChanges.oMessages);
	}
	
	private void ProcessMessages(List<ChatMessage> oMessages) 
	{
		for( ChatMessage oMessage : oMessages )
		{
			if( oMessage.bIsFile )
				ProcessFileMessage(oMessage);
			else if( oMessage.bIsBroadCast )
				ProcessBroadCastMessage( oMessage );
			else
				ProcessSingleMessage(oMessage);
		} 
	}
	
	private void ProcessBroadCastMessage(ChatMessage oMessage)
	{
		String strMessage = textAreaAllChat.getText() + oMessage.toString();
		textAreaAllChat.setText( strMessage );
	}
	
	private void ProcessFileMessage(ChatMessage oMessage) 
	{
		//int dialogResult = JOptionPane.showConfirmDialog (ClientChannelFrame, "Would you like to receive a file with name '"+oMessage.strFileName+"' from "+oMessage.sender+"?","Warning",JOptionPane.YES_NO_OPTION);
		//if(dialogResult == JOptionPane.NO_OPTION)
		//	return;
		
		JFileChooser saveFile = new JFileChooser();
		saveFile.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
	    if( saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
	    {
	        String strPath = saveFile.getSelectedFile().getPath();
	        String strFullFileName = strPath +"//"+ oMessage.strFileName;
	        File file = new File(strFullFileName);
	        
	        try {
	        	Files.createDirectories(file.toPath().getParent());
				Files.write(file.toPath(), oMessage.messageText.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } // if
		
	}
	
	private void ProcessSingleMessage(ChatMessage oMessage) 
	{
		
		boolean bHasWindow = false;
		MessageWindow windowToSend = null;
		for(MessageWindow oWindow : listWindows )
		{
			if( oWindow.GetUsername().equals( oMessage.sender ) == true )
			{
				windowToSend = oWindow;
				windowToSend.ShowDialog();
				bHasWindow = true;
				break;
			} // if
			
			
		}
		if( bHasWindow == false )
		{
			MessageWindow window = MessageWindow.ShowWindow(oMessage.sender);
			window.SetUsername(oMessage.sender);
			windowToSend = window;
			listWindows.add(window);
		}
		
		windowToSend.ReceivedMessage( oMessage );
	}
	
	public void FillUserLists( List<UserInfo> oUsersList )
	{
		// adding new users
		synchronized(oUserInfoSet)
		{
			for( UserInfo user : oUsersList )
			{
				if( oUserInfoSet.contains( user ) == true )
					continue;

					oUserInfoSet.add( user );
			} // for
			
			// remove disconnected users
			for( UserInfo user : oUserInfoSet )
			{
				if( oUsersList.contains(user) == false )
				{
					
					oUserInfoSet.remove(user);
				}
			}
		}
	}
	private void FillUsers(List<UserInfo> oUsersList)
	{
		FillUserLists(oUsersList);
		
		ObservableList< UserInfo > listUserInfo = listViewUsersList.getItems();
		ObservableList< UserInfo > listViewItems = listViewUsersList.getItems();
		for(int i = 0; i < listUserInfo.size(); i ++ )
		{
			UserInfo current = listUserInfo.get( i );
			if( oUserInfoSet.contains( current ) )
				listViewItems.remove( i );
		}
		
		for( UserInfo user : oUserInfoSet )
		{
			if( listViewItems.contains(user) == false )
				listViewItems.add(user);
		}
		
		listViewUsersList.setItems(listViewItems);

	}
	
	public void subscribeForChanges()
	{
		SocketClientSingleton.getInstance().SubscribeForChanges(this);
	}
	
	
	// Initial initialization of the controls from the fxml
	@FXML
	public void initialize()
	{
		listViewUsersList.setCellFactory(new Callback<ListView<UserInfo>, ListCell<UserInfo>>()
				{
					@Override
					public ListCell<UserInfo> call(ListView<UserInfo> arg0) {
						return new initListCell();
					}	
				});
		subscribeForChanges();
	}
	
	// Callback which sets the text in the list
    static class initListCell extends ListCell<UserInfo> {
        @Override
        public void updateItem(UserInfo user, boolean empty) {
            super.updateItem(user, empty);
            if( empty )
            	return;
            setText(user.toString());
        }
    }
    
	// Event handler on button click
	@FXML
	private void onBtnClickSendMessage( ActionEvent actionEvent )
	{				
		if( textFieldUserMessage.getText().isEmpty() == true )
			return;
		
		String strMessage = textFieldUserMessage.getText();
		String strTimeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		String strNewText = "["+strTimeStamp+"] "+SocketClientSingleton.getInstance().getUsername() + " > " + textFieldUserMessage.getText() + "\r\n";
		//textAreaAllChat.setText( textFieldUserMessage.getText() + strNewText);
		textFieldUserMessage.setText("");
		
		ChatMessage oMessage = new ChatMessage();
		oMessage.messageText = strMessage;
		oMessage.sender = SocketClientSingleton.getInstance().getUsername();
		oMessage.messageTimestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		oMessage.bIsBroadCast = true;
		SocketClientSingleton.getInstance().SendBroadcast(oMessage);
	}
	

}
