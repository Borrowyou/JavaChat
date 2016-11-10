package ServerPkg;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import GlobalPkg.*;

public class NioServer {
	private Selector selector;
    private Map<SocketChannel, ClientQueue> dataMapper;
    private InetSocketAddress listenAddress;
    private ServerWindow mainWindow;
    
    /*
    public static void main(String[] args) throws Exception {
    	Runnable server = new Runnable() {
			@Override
			public void run() {
				 try {
					new NioServerExample("localhost", 8090, null).startServer();
				} catch (IOException e) {
					e.printStackTrace();
				} catch( ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		};
		
       new Thread(server).start();
       //new Thread(client, "client-A").start();
       //new Thread(client, "client-B").start();
    }
    */
    public static void StartServer( ServerWindow mainWindow )
    {
    	Runnable server = new Runnable() {
			@Override
			public void run() {
				 try {
					new NioServer("localhost", 8090, mainWindow).startServer();
				} catch (IOException e) {
					e.printStackTrace();
				} catch( ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		};
		
       new Thread(server).start();
    }

    public NioServer(String address, int port, ServerWindow mainWindow) throws IOException {
    	listenAddress = new InetSocketAddress(address, port);
        dataMapper = new HashMap<SocketChannel, ClientQueue>();
        this.mainWindow = mainWindow;
    }

    // create server channel	
    private void startServer() throws IOException, ClassNotFoundException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // retrieve server socket and bind to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started...");

        while (true) {
            // wait for events
            this.selector.select();

            //work on selected keys
            Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                // this is necessary to prevent the same key from coming up 
                // again the next time around.
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                }
                else if (key.isReadable()) {
                    this.read(key);
                }
            }
        }
    }

    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);
        // register channel with selector for further IO
        dataMapper.put(channel, new ClientQueue() );
        channel.register(this.selector, SelectionKey.OP_READ);
        mainWindow.SetConnectedUsers( dataMapper.size());
    }
    
    private void sendBroadcastMessage(ChatMessage msg, SocketChannel fromChannel )
    {
    	for( SocketChannel sc : dataMapper.keySet() )
    	{
    	//	if( sc.equals(fromChannel) == true )
    		//	continue;

    		sendMessage(msg, sc );
    	}
    }
    
    private void sendMessage(ChatMessage msg, SocketChannel toChannel )
    {
    	GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        
        String strMsgToSend = gson.toJson(msg);

		 byte [] message = strMsgToSend.getBytes();
		 ByteBuffer buffer = ByteBuffer.wrap(message);
		 try {
			toChannel.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 buffer.clear();
    }
    //read from the socket channel
    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            mainWindow.SetConnectedUsers( dataMapper.size());
            return;
        }

        //buffer.getInt(0);
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String strMessage = new String(data,1,numRead-1);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String strMessage2 = new String(data,0,1);
        ChatOperations enChatOperations = ChatOperations.values()[Integer.parseInt(strMessage2)-1];
        switch( enChatOperations )
        {
        case ChatOperationsRefresh:
        {
        	GetChanges(channel);
        	break;
        }
        	
        case ChatOperationsSendMessageToAll:
        {
        	ChatMessage oMsg = gson.fromJson(strMessage, ChatMessage.class);
          
            System.out.println("Broadcast: " + oMsg.toString());
            sendBroadcastMessage(oMsg, channel);
        	break;
        } // case ChatOperationsSendMessageToAll
        	
        
        case ChatOperationsSendMessageToUser:
        {
        	ChatMessage oMsg = gson.fromJson(strMessage, ChatMessage.class);
        	sendMessageToUser(oMsg);
        	break;
        }
        	
        case ChatOperationsSendFileToUser:
        {
        	ChatMessage oMsg = gson.fromJson(strMessage, ChatMessage.class);
        	sendMessageToUser(oMsg);
        	break;
        } // case ChatOperationsSendFileToUser
        	
        case ChatOperationsInitializeName:
        	InitializeClientName(channel, strMessage);
        	break;
        } // switch
        
    }

	private void sendMessageToUser(ChatMessage oMsg) 
	{
		for(ClientQueue queue : dataMapper.values() )
		{
			if( queue.name.equals( oMsg.receiver ) == false )
				continue;
			
			queue.messageQueue.add( oMsg );
		} // for
	}

	private void GetChanges(SocketChannel channel) throws IOException
	{
		ChatChanges oChatChanges = new ChatChanges();
		
		for( SocketChannel sc : dataMapper.keySet() )
    	{
			ClientQueue queue = dataMapper.get(sc);
			
			UserInfo oUserInfo = new UserInfo();
    		oUserInfo.strUsername =  queue.name;
    		
    		if( sc.equals(channel) == true )
    			oUserInfo.bIsOurUser = true;
    		
    		oChatChanges.oUsers.add( oUserInfo );
    		
    		if( sc.equals(channel) != true )
    			continue;
    		
    		while( queue.messageQueue.peek() != null )
    		{
    			oChatChanges.oMessages.add (queue.messageQueue.poll() );
    		}
    	} // for
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        
        String strMsgToSend = gson.toJson(oChatChanges);

		byte [] message = strMsgToSend.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);

		channel.write(buffer);

		buffer.clear();
	}

	private void InitializeClientName(SocketChannel channel, String strMessage) 
	{
		ClientQueue clQueue = dataMapper.get( channel );
		clQueue.name = strMessage;
	}
}