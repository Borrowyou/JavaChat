package ClientPkg;

import GlobalPkg.ChatChanges;

public interface IPendingChanges {
	public void ProcessPendingChanges( ChatChanges oChanges );
}
