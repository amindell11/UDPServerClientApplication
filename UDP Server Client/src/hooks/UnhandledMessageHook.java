package hooks;

import net.proto.ExchangeProto.exchange;

public interface UnhandledMessageHook {
    
    public void handleMessage(exchange message);
}
