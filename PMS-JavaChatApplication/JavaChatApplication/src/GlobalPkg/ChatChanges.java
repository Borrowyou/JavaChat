package GlobalPkg;

import java.util.ArrayList;
import java.util.List;

public class ChatChanges {
	public List<UserInfo> oUsers;
	public List<ChatMessage> oMessages;
	
	public ChatChanges()
	{
		oUsers = new ArrayList<UserInfo>();
		oMessages = new ArrayList<ChatMessage>();
	}
}
