package ServerPkg;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ServerWindow {

	private JFrame frame;
	private JLabel lblCurrentConnectedUsers;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void SetConnectedUsers(int value)
	{
		lblCurrentConnectedUsers.setText(""+value);
	}
	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
		NioServer.StartServer(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelConnectedUsers = new JPanel();
		frame.getContentPane().add(panelConnectedUsers, BorderLayout.NORTH);
		
		JLabel lblConnectedUsers = new JLabel("Connected users:");
		panelConnectedUsers.add(lblConnectedUsers);
		
		lblCurrentConnectedUsers = new JLabel("0");
		panelConnectedUsers.add(lblCurrentConnectedUsers);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Settings");
		menuBar.add(mntmNewMenuItem_1);
		
		
	}

}
