package ClientPkg;

import java.awt.EventQueue;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ConnectException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import GlobalPkg.ValidateIPV4;

public class ClientConnectionSettings {

	private JFrame frmConnectToChat;
	private JTextField txtUsername;
	private JFormattedTextField formattedTextField;
	/**
	 * Launch the application.
	 */
	
	private boolean ValidateFields()
	{
		if( ValidateIPV4.isValidIPV4(formattedTextField.getText()) == false )
		{
			formattedTextField.setFocusable(true);
			MessageBox.Show("Invalid server adress.", "Error");
			return false;
		} // if
		
		
		if( txtUsername.getText().isEmpty() == true )
		{
			txtUsername.setFocusable(true);
			MessageBox.Show("Invalid username.", "Error");
			return false;
		} // if
		
		return true;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientConnectionSettings window = new ClientConnectionSettings();
					window.frmConnectToChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientConnectionSettings() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConnectToChat = new JFrame();
		frmConnectToChat.setType(Type.UTILITY);
		frmConnectToChat.setTitle("Connect to chat\n");
		frmConnectToChat.setResizable(false);
		frmConnectToChat.setBounds(100, 100, 301, 100);
		frmConnectToChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblServer = new JLabel("Server:");
		
		formattedTextField = new JFormattedTextField();
		formattedTextField.setText("127.0.0.1");
		formattedTextField.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblPort = new JLabel("Port:");
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField();
		
		JLabel lblUsername = new JLabel("Username:");
		
		txtUsername = new JTextField();
		txtUsername.setText("Username");
		txtUsername.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		
	btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if( !ValidateFields() )
					return;
				
				try {
					SocketClientSingleton.getInstance().startConnection(txtUsername.getText(), formattedTextField.getText());
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
				
	            ClientWindow.ShowWindow();
	            frmConnectToChat.setVisible(false);
	            
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frmConnectToChat.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblServer)
						.addComponent(lblUsername))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addComponent(formattedTextField, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtUsername, 0, 0, Short.MAX_VALUE)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(14)
							.addComponent(lblPort)
							.addGap(5)
							.addComponent(formattedTextField_1, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnConnect)))
					.addGap(85))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblServer)
							.addComponent(formattedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(5)
							.addComponent(lblPort))
						.addComponent(formattedTextField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
					.addGap(47))
		);
		frmConnectToChat.getContentPane().setLayout(groupLayout);
	}
}
