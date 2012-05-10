package com.github.xgameenginee.samples.echotest;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import com.github.xgameenginee.core.GamePipeFactory;
import com.github.xgameenginee.core.GameWorker;
import com.github.xgameenginee.handler.GameHandlerManager;

public class EchoServer {

	public void run(String host, short port) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setOption("tcpNoDelay", true);
		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new GamePipeFactory(2, 1024, false, 2, false));
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(host, port));
		new Thread(GameWorker.getInstance()).start();
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
