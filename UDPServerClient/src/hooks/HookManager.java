package hooks;

import java.util.HashSet;

import net.proto.ExchangeProto.Exchange;

public class HookManager{
    public HookManager(){
	hooks=new HashSet<>();
    }
    private HashSet<UnhandledMessageHook> hooks;
    public void addHook(UnhandledMessageHook hook){
	if(hooks!=null){
	    hooks.add(hook);
	}
    }
    public void handleMessage(Exchange message){
	for(UnhandledMessageHook hook:hooks){
	    hook.handleMessage(message);
	}
    }
}
