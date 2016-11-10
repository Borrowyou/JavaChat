package ClientPkg;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import GlobalPkg.ChatChanges;
import GlobalPkg.ChatMessage;
import GlobalPkg.UserInfo;

public class ClientWindow extends JFrame implements IPendingChanges{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7694416322164182922L;
	private JFrame ClientChannelFrame;
	private JTextField txtMessageField;
	private Set<UserInfo> oUserInfoSet = null;
	private JList<UserInfo> lstUsers;
	private List<MessageWindow> listWindows = new ArrayList<MessageWindow>();
	
	/**
	 * Launch the application.
	 */
	public static void ShowWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.ClientChannelFrame.setVisible(true);
					window.subscribeForChanges();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.ClientChannelFrame.setVisible(true);
					window.subscribeForChanges();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	protected void subscribeForChanges() {
		SocketClientSingleton.getInstance().SubscribeForChanges(this);
	}

	/**
	 * Create the application.
	 */
	public ClientWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ClientChannelFrame = new JFrame();
		ClientChannelFrame.setBounds(100, 100, 450, 300);
		ClientChannelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[] {250, 35, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		ClientChannelFrame.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		ClientChannelFrame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {350, 100, 2};
		gbl_panel.rowHeights = new int[]{139, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(5, 3, 0, 3);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		JTextArea txtMessageArea = new JTextArea();
		txtMessageArea.setEditable(false);
		scrollPane.setViewportView(txtMessageArea);
		txtMessageArea.setRows(10);
		txtMessageArea.setLineWrap(true);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(5, 0, 0, 3);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);
		
		lstUsers = new JList<UserInfo>();
		lstUsers.setModel( new DefaultListModel<UserInfo>());
		scrollPane_1.setViewportView(lstUsers);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		ClientChannelFrame.getContentPane().add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {350, 100};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		txtMessageField = new JTextField();
		GridBagConstraints gbc_txtMessageField = new GridBagConstraints();
		gbc_txtMessageField.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessageField.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessageField.gridx = 0;
		gbc_txtMessageField.gridy = 0;
		panel_1.add(txtMessageField, gbc_txtMessageField);
		txtMessageField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 3);
		gbc_btnSend.fill = GridBagConstraints.BOTH;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 0;
		panel_1.add(btnSend, gbc_btnSend);
		
		oUserInfoSet = new HashSet<UserInfo>();
		
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem jMenuItemSendMessage = new JMenuItem("Send Message");
		jMenuItemSendMessage.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				UserInfo oUser = lstUsers.getSelectedValue();
				boolean bHasWindow = false;
				for(MessageWindow oWindow : listWindows )
				{
					if( oWindow.GetUsername().equals( oUser.strUsername ) == true )
					{
						bHasWindow = true;
						oWindow.ShowDialog();
						break;
					} // if
					
					
				}
				if( bHasWindow == false )
				{
					MessageWindow window = MessageWindow.ShowWindow(oUser.strUsername);
					window.SetUsername(oUser.strUsername);
					listWindows.add(window);
				}
			}
		});
		
		JMenuItem jMenuItemSendFile = new JMenuItem("Send File");
		jMenuItemSendFile.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(ClientChannelFrame);
				if (result != JFileChooser.APPROVE_OPTION) {
				    return;
				} // if
				File selectedFile = fileChooser.getSelectedFile();
				
				ChatMessage oMessage= new ChatMessage();
				UserInfo oUser = lstUsers.getSelectedValue();
				
				oMessage.sender = SocketClientSingleton.getInstance().getUsername();
				oMessage.receiver = oUser.strUsername;
				oMessage.bIsFile = true;
				oMessage.strFileName = selectedFile.getName();
				
				try {
					oMessage.messageText = new String(Files.readAllBytes(selectedFile.toPath()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SocketClientSingleton.getInstance().SendFile(oMessage);
			}
		});
		
		JMenuItem jMenuItemViewHistory = new JMenuItem("View History");
		jMenuItemViewHistory.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("skicka view history!");
			}
		});
		
		popupMenu.add(jMenuItemSendMessage);
		popupMenu.add(jMenuItemSendFile);
		popupMenu.add(new JPopupMenu.Separator());
		popupMenu.add(jMenuItemViewHistory);
	      
		lstUsers.addMouseListener(new MouseAdapter() 
		{
	         public void mouseClicked(MouseEvent me) 
	         {
	            if (SwingUtilities.isRightMouseButton(me)    // if right mouse button clicked
	                  && !lstUsers.isSelectionEmpty()            // and list selection is not empty
	                  && lstUsers.locationToIndex(me.getPoint()) // and clicked point is
	                     == lstUsers.getSelectedIndex()) 
	            {       //   inside selected item bounds
	            	UserInfo oUser = lstUsers.getSelectedValue();
	            	if( oUser.bIsOurUser == false )
	            		popupMenu.show(lstUsers, me.getX(), me.getY());
	            }
	         }
	      });
		
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
	
	public void FillUsers(List<UserInfo> oUsersList)
	{
		FillUserLists(oUsersList);
		
		// deleting disconnected users
		ListModel<UserInfo> oModel = lstUsers.getModel();
		DefaultListModel<UserInfo> oDefaultListModel = (DefaultListModel<UserInfo>)lstUsers.getModel();
		for( int i=0; i<oModel.getSize(); i++ )
		{
			UserInfo user = oModel.getElementAt(i);
			if( oUserInfoSet.contains( user ) == false )
				oDefaultListModel.removeElement(user);
		} // for
		
		for( UserInfo user : oUserInfoSet )
		{
			if( oDefaultListModel.contains(user) == false )
				oDefaultListModel.addElement(user);
		}
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
			else
				ProcessSingleMessage(oMessage);
		}
	}
	private void ProcessFileMessage(ChatMessage oMessage) 
	{
		int dialogResult = JOptionPane.showConfirmDialog (ClientChannelFrame, "Would you like to receive a file with name '"+oMessage.strFileName+"' from "+oMessage.sender+"?","Warning",JOptionPane.YES_NO_OPTION);
		if(dialogResult == JOptionPane.NO_OPTION)
			return;
		
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

}
