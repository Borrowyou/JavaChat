package ClientPkg;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GlobalPkg.ChatMessage;

public class MessageWindow {

	private JFrame frame;
	private JTextField textField;
	private String strUsername;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static MessageWindow ShowWindow(String strUsername) {
		MessageWindow window = new MessageWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
					window.SetUsername(strUsername);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return window;
	}

	/**
	 * Create the application.
	 */
	
	public String GetUsername()
	{
		return strUsername;
	}
	
	public MessageWindow() {
		initialize();
	}
	public void SetUsername(String strUsername)
	{
		this.strUsername = strUsername;
		frame.setTitle("Correspondence with user '"+strUsername+"'");
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[] {245, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		frame.setTitle("Corespondence with user '"+strUsername+"'");
		
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {370, 80, 0};
		gbl_panel.rowHeights = new int[]{29, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		textField.addKeyListener( new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					SendText();
				}
				else
					super.keyPressed(e);
			}
			
		}
		);
		
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SendText();
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.BOTH;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 0;
		panel.add(btnSend, gbc_btnSend);
	}
	
	/**
	 * 
	 */
	private void SendText()
	{
		if( textField.getText().isEmpty() == true )
			return;
		
		String strMessage = textField.getText();
		String strTimeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		String strNewText = "["+strTimeStamp+"] "+SocketClientSingleton.getInstance().getUsername() + " > " + textField.getText() + "\r\n";
		textArea.setText( textArea.getText() + strNewText);
		textField.setText("");
		
		ChatMessage oMessage = new ChatMessage();
		oMessage.messageText = strMessage;
		oMessage.receiver = strUsername;
		oMessage.sender = SocketClientSingleton.getInstance().getUsername();
		oMessage.messageTimestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		
		SocketClientSingleton.getInstance().SendMessage(oMessage);
	}
	
	public void ReceivedMessage(ChatMessage oMessage)
	{
		String strNewText = "["+oMessage.messageTimestamp +"] "+ strUsername + " > " + oMessage.messageText + "\r\n";
		textArea.setText( textArea.getText() + strNewText);
	}

	public void ShowDialog() {
		frame.setVisible(true);
		
	}

}
