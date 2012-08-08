package com.github.xgameenginee;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xgameenginee.core.GamePipeFactory;
import com.github.xgameenginee.core.GameUpProcessor;
import com.github.xgameenginee.core.ProtocolCoder;

/**
 * 游戏设置类
 * 1.增加你自己的游戏逻辑接口，负责逻辑数据分发
 * 2.设置游戏的基本协议
 * @author Steven
 *
 */
public class GameBoss {

	private static final Logger logger = LoggerFactory.getLogger(GameBoss.class);
	
	private static final GameBoss instance = new GameBoss();
	
	public static GameBoss getInstance() {
		return instance;
	}
	
	public GameUpProcessor getProcessor() {
		return processor;
	}
	
	private GameUpProcessor processor;
	
	private ProtocolCoder coder;
	
	public ProtocolCoder getProtocolCoder() {
		return coder;
	}

    /**
     * 端口是否可用
     * @param port
     * @return
     */
    public static final boolean isPortAvailable(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private int writeHeaderSize;
    
    public int getWriteHeaderSize() {
    	return writeHeaderSize;
    }
	
	/**
	 * 绑定游戏端口
	 * @param headerSize
	 * @param readMax
	 * @param writerSize
	 * @param host
	 * @param port
	 * @param processor 上行消息的处理接口
	 * @param timeOut 超时时间(毫秒,如果不检测设为0)
	 */
	public void bind(int headerSize, int readMax, int writerSize, String host, short port, final GameUpProcessor processor, int timeOut, ProtocolCoder coder) {
		if (isPortAvailable(port)) {
			logger.info("Server is establishing to listening at :" + port);
		} else {
			logger.error("Server's port :" + port + " not available");
			System.exit(-1);
			return;
		}
		this.coder = coder;
		this.writeHeaderSize = writerSize;
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setPipelineFactory(new GamePipeFactory(headerSize, readMax, false, writerSize, false, timeOut));
		
        Runtime.getRuntime().addShutdownHook(new Thread("System Shutdown Hooker") {
        	@Override
        	public void run(){
        		processor.onShutdown();
        	}
        });
		bootstrap.bind(new InetSocketAddress(host, port));
		this.processor = processor;
	}
}
