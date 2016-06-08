package hooks;

import net.server.ServerThread;

public interface ServerCreatedHook {
	public void serverCreated(ServerThread server);
}
