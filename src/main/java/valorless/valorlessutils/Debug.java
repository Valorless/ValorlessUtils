package valorless.valorlessutils;

import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.ValorlessUtils.Log;

public class Debug {

	public static void PrintStackTrace(JavaPlugin caller) {
		for(StackTraceElement stack : Thread.currentThread().getStackTrace()) {
    		Log.Error(caller, stack.toString());
    	}
	}
	
}
