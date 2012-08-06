package com.github.xgameenginee.samples.echotest;

import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import com.github.xgameenginee.GameBoss;
import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.core.GameUpProcessor;
import com.github.xgameenginee.core.GameWorker;
import com.github.xgameenginee.handler.GameHandlerManager;

public class EchoServer {

	public void run(String host, short port) {
		final GameWorker worker = new GameWorker();
		new Thread(worker).start();
		
		GameBoss.getInstance().bind(2, 1024, 2, host, port, new GameUpProcessor() {
			@Override
			public void process(GameUpBuffer buffer) {
				worker.pushUpstreamBuffer(buffer);
			}
			@Override
			public void onShutdown() {
				// System has been killed
				System.out.println("System has been killed");
			}
		}, 0);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 // Print usage if no argument is specified.
        if (args.length < 2 || args.length > 3) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                    " <host> <port> [<first message size>]");
            return;
        }
        
        // Parse options.
        final String host = args[0];
        final short port = Short.parseShort(args[1]);
        
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory()); 
		GameHandlerManager.getInstance().initHandlers(100);
		
		try {
			GameHandlerManager.getInstance().register(LoginHandler.class, (short)20);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		new EchoServer().run(host, port);
	}

}
