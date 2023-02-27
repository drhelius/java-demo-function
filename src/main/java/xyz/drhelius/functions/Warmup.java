package xyz.drhelius.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.WarmupTrigger;

import xyz.drhelius.load.BusyLoad;

public class Warmup {
  
    @FunctionName("Warmup")
    public void warmup( @WarmupTrigger Object warmupContext, ExecutionContext context) {
        context.getLogger().info("Function App instance is warming up...");
        BusyLoad.createBlockingLoad(1.0, 3000);
        context.getLogger().info("Function App instance is warm 🌞🌞🌞");
    }
}
