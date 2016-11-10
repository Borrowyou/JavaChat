package ClientPkg;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import GlobalPkg.ChatChanges;
import GlobalPkg.ChatMessage;
import GlobalPkg.ChatOperations;

public class NioClient implements Runnable {
 
	private String m_strUsername = "";
	private InetSocketAddress hostAddress;
	SocketChannel client;
	Selector selector;
	public boolean m_bIsConnected = false;
	IPendingChanges object = null;
	private String m_strAddress = "";
	
	public NioClient( String strUsername )
	{
		m_strUsername = strUsername;
	}
	
	protected void finalize() throws Throwable 
	{
		try {
			client.close();
			m_bIsConnected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public void setUsername( String strUsername )
	{
		m_strUsername = strUsername;
	}
	
	public String getUsername()
	{
		return m_strUsername;
	}
	public void SendInitializeOperation() throws IOException
	{
		String strMessage = "5" + m_strUsername;
		byte [] message = strMessage.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		//client.register(selector, SelectionKey.OP_WRITE);
		client.write(buffer);

		buffer.clear();
	}
	
	public void SendBroadcast(ChatMessage msg ) throws IOException
	{
		String strMsg = Integer.toString(ChatOperations.ChatOperationsSendMessageToAll.getChatOperation());

		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        strMsg = strMsg+ gson.toJson(msg);
        
        
		byte [] message = strMsg.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		client.register(selector, SelectionKey.OP_WRITE);
		client.write(buffer);

		buffer.clear();
	}
	
    public void startClient()
            throws IOException, InterruptedException, ClassNotFoundException {
 
        hostAddress = new InetSocketAddress(m_strAddress, 8090);
        //client = SocketChannel.open(hostAddress);
        client = SocketChannel.open();
        client.configureBlocking(false);
        System.out.println("Client... started");
        client.connect(hostAddress);
        m_bIsConnected = true;
     // get a selector
        selector = Selector.open();

        // register the client socket with "connect operation" to the selector
        client.register(selector, SelectionKey.OP_CONNECT);
        
        // After successful connection sending initialize message to server with username
        
        //String threadName = Thread.currentThread().getName();
        while (true)
        {
            // wait for events
            this.selector.select(10000);

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
                else if ( key.isConnectable() )
                {
                	client.finishConnect();
                	SendInitializeOperation();
                } // else if
            }
        }
    }
    
    private void accept(SelectionKey key) throws IOException 
    {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);
        // register channel with selector for further IO
        channel.register(this.selector, SelectionKey.OP_READ);
        SendInitializeOperation();
    }
    
    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            //this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            //mainWindow.SetConnectedUsers( dataMapper.size());
            return;
        }

        //buffer.getInt(0);
        
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String strMessage = new String(data);
        
        GsonBuilder builder = new GsonBuilder();
        
        Gson gson = builder.create();
        JsonReader reader = new JsonReader(new StringReader(strMessage));
        reader.setLenient(true);
        
        ChatChanges oChanges = gson.fromJson( reader, ChatChanges.class);
        object.ProcessPendingChanges(oChanges);
        buffer.clear();
    }
    public void ConnectToServer() throws IOException, InterruptedException, ClassNotFoundException
    {
    	new Thread(this,"Test").start();
    }
    
    public static void main(String[] args) throws Exception {
		
		Runnable client = new Runnable() {
			@Override
			public void run() {
				 
				
			}
		};
       new Thread(client, "Marev").start();
       //new Thread(client, "client-B").start();
    }

	@Override
	public void run()
	{
		try
		{
			startClient();
		} catch (IOException | InterruptedException e) 
		{
			MessageBox.Show("Cannot connect to server", "Error");
		}
 catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void GetChanges()
	{
		if( m_bIsConnected == false )
			return;

		String strMessage = Integer.toString(ChatOperations.ChatOperationsRefresh.getChatOperation());
		byte [] message = strMessage.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);

		try
		{
		client.write(buffer);
		client.register(selector, SelectionKey.OP_READ);
		}
		catch( Exception E)
		{
			E.printStackTrace();
		}
	}

	private void CheckForChangesTimer()
	{

		while(true)
		{
				GetChanges();
		
			// 3 sec sleep
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void SubscribeForChanges(IPendingChanges object) 
	{
		this.object = object;
		
		Runnable timer = new Runnable() {
			@Override
			public void run() 
			{
				CheckForChangesTimer();
			}
		};
       new Thread(timer, "TimerThread").start();
	}
	
	public void SendMessage(ChatMessage oMessage) throws IOException
	{
		String strMsg = Integer.toString(ChatOperations.ChatOperationsSendMessageToUser.getChatOperation());

		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        strMsg = strMsg+ gson.toJson(oMessage);
        
        
		byte [] message = strMsg.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		client.register(selector, SelectionKey.OP_WRITE);
		client.write(buffer);

		buffer.clear();
	}

	public void setAddress(String strAddress) {
		m_strAddress = strAddress;
	}

	public void SendFile(ChatMessage oMessage) throws IOException 
	{
		String strMsg = Integer.toString(ChatOperations.ChatOperationsSendFileToUser.getChatOperation());

		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        strMsg = strMsg+ gson.toJson(oMessage);
        
        
		byte [] message = strMsg.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		
		client.register(selector, SelectionKey.OP_WRITE);
		client.write(buffer);

		buffer.clear();
		
	}
	
	public void StopConnection()
	{
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

