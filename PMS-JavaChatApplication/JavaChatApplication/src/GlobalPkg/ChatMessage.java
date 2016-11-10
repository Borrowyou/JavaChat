package GlobalPkg;

public class ChatMessage {
	public String sender;
	public String receiver;
	public String messageText;
	public String messageTimestamp;
	public boolean bIsFile;
	public String strFileName;
	public boolean bIsBroadCast;
	
	public String toString()
	{
		return "Message: "+messageText;
	}
}
