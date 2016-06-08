package hooks;

import net.client.ClientThread;

public interface ClientCreatedHook {
	public void clientCreated(ClientThread client);
}
