package xyz.drhelius.impl;

import xyz.drhelius.load.BusyLoad;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;


public class FunctionImpl {

    public static HttpResponseMessage run(ExecutionContext context, HttpRequestMessage<Optional<String>> request) {

        context.getLogger().info("-> run started");

        // Parse query parameter
        final String operation = request.getQueryParameters().get("operation");
        final String time = request.getQueryParameters().get("time");
        
        int milliseconds = 1000;

        try {
            milliseconds = Integer.parseInt(time);
            context.getLogger().info("-> parsed time: " + milliseconds);
        }
        catch (NumberFormatException ex){
            context.getLogger().info("-> time not parsed");
        }

        if (operation == null) {
            context.getLogger().info("-> about to return BAD_REQUEST");
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("No operation (cpu|idle)").build();
        
        } else if(operation.equals("cpu")) {
            context.getLogger().info("-> generating CPU load for " + milliseconds + " milliseconds");
           
            BusyLoad.createBlockingLoad(1.0, milliseconds);
            
            context.getLogger().info("-> CPU load finished");
            
            context.getLogger().info("-> about to return OK");
            return request.createResponseBuilder(HttpStatus.OK).body("CPU load").build();     

        } else if(operation.equals("idle")) {
            context.getLogger().info("-> waiting for " + milliseconds + " milliseconds");
        
            try {
                Thread.sleep(milliseconds);
            }
            catch(InterruptedException e) {
                context.getLogger().info("-> waiting interrupted");
            }

            context.getLogger().info("-> waiting finished");
            
            context.getLogger().info("-> about to return OK");
            return request.createResponseBuilder(HttpStatus.OK).body("Idle").build();
        
        } else {
            context.getLogger().info("-> about to return OK");
            return request.createResponseBuilder(HttpStatus.OK).body("Doing nothing").build();
        }    
    }
}
