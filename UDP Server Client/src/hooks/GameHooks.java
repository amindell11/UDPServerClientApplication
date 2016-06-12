package hooks;

import java.util.HashSet;
import java.util.Set;

public class GameHooks {
	public static Set<UnhandledMessageHook> onUnhandledMessageHooks;

	public static void addUnhandledMessagedHook(UnhandledMessageHook hook) {
		if (hooksNull()) {
		    onUnhandledMessageHooks = new HashSet<>();
		}
		onUnhandledMessageHooks.add(hook);
	}

	public static void handleMessage() {
		if(!hooksNull()){
		    for(UnhandledMessageHook hook : onUnhandledMessageHooks){
			hook.handleMessage();
		    }
		}
	}
	
	private static boolean hooksNull(){
	    return onUnhandledMessageHooks == null;
	}
}
