package ClientPkg;

import java.io.IOException;

import GlobalPkg.ChatMessage;

public class SocketClientSingleton {

	   private static SocketClientSingleton instance = null;
	   private static NioClient client_instance = null;

	   private SocketClientSingleton() {
	      // Exists only to defeat instantiation.
	   }
	   public static SocketClientSingleton getInstance() {
	      if(instance == null) 
	      {
	         instance = new SocketClientSingleton();
	         client_instance = new NioClient(null);
	      } // if
	      
	      return instance;
	   }
	   
	   public boolean IsConnected()
	   {
		   return client_instance.m_bIsConnected;
	   }
	   
	   public  void startConnection(String strUsername, String strAddress) throws IOException, InterruptedException, ClassNotFoundException
	   {
		   
		   if( IsConnected() == true )
			   return;
		   
		   client_instance.setUsername( strUsername );
		   client_instance.setAddress( strAddress );
		   client_instance.ConnectToServer();
	   }
	   
	   public void SendMessage(ChatMessage oMessage)
	   {
		   try {
			client_instance.SendMessage(oMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
	   public void SendFile(ChatMessage oMessage )
	   {
		   try {
				client_instance.SendFile(oMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	   public void SendBroadcast( ChatMessage msg )
	   {
		   try {
			   client_instance.SendBroadcast( msg );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   public void SubscribeForChanges(IPendingChanges object)
	   {
		   client_instance.SubscribeForChanges(object);
	   }
	   
	   public String getUsername()
	   {
		   return client_instance.getUsername();
	   }
	   
	   public void CheckForChanges()
	   {
			client_instance.GetChanges();

	   }
	   
	   public void StopConnection()
	   {
		   client_instance.StopConnection();
	   }
	}