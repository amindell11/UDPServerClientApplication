package hooks;

import net.proto.ExchangeProto.Exchange;

public interface UnhandledMessageHook {
    
    public void handleMessage(Exchange message);
}
