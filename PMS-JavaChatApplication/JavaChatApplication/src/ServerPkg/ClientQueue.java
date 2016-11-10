package ServerPkg;

import java.util.concurrent.ConcurrentLinkedQueue;

import GlobalPkg.ChatMessage;

public class ClientQueue implements Comparable<ClientQueue>{
	public String name;
	public ConcurrentLinkedQueue<ChatMessage> messageQueue;
	
	ClientQueue()
	{
		name = "";
		messageQueue = new ConcurrentLinkedQueue<ChatMessage>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientQueue other = (ClientQueue) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(ClientQueue o) {
		return this.name.compareTo(o.name);
	}
	
}
