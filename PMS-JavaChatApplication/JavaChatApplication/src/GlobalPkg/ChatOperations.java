package GlobalPkg;

public enum ChatOperations
{
	ChatOperationsRefresh 				(1)
,	ChatOperationsSendMessageToAll		(2)
,	ChatOperationsSendMessageToUser		(3)
,	ChatOperationsSendFileToUser		(4)
,	ChatOperationsInitializeName		(5)
;
	
	 private final int m_ChatOperations;

	 ChatOperations(int chatOperation) 
	 {
		 this.m_ChatOperations = chatOperation;
	 }
	    
	 ChatOperations(byte[] arrChatOperation) 
	 {
		 this.m_ChatOperations = Integer.parseInt(new String(arrChatOperation));
	 }
	 
    public int getChatOperation() 
    {
        return this.m_ChatOperations;
    }
	    
   public byte[] getByteArrayChatOperation()
   {
	   return Integer.toString(this.m_ChatOperations).getBytes();
   }
	 
}
