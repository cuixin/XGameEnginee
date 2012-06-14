package com.github.xgameenginee;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.github.xgameenginee.core.GamePipeFactory;
import com.github.xgameenginee.core.GameUpProcessor;

/**
 * 游戏设置类
 * 1.增加你自己的游戏逻辑接口，负责逻辑数据分发
 * 2.设置游戏的基本协议
 * @author Steven
 *
 */
public class GameBoss {
	
	private static final GameBoss instance = new GameBoss();
	
	public static GameBoss getInstance() {
		return instance;
	}
	
	public GameUpProcessor getProcessor() {
		return processor;
	}
	
	private GameUpProcessor processor;
	
	/**
	 * 绑定游戏端口
	 * @param headerSize
	 * @param readMax
	 * @param writerSize
	 * @param host
	 * @param port
	 * @param processor 上行消息的处理接口
	 */
	public void bind(int headerSize, int readMax, int writerSize, String host, short port, GameUpProcessor processor) {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setPipelineFactory(new GamePipeFactory(headerSize, readMax, false, writerSize, false));
		bootstrap.bind(new InetSocketAddress(host, port));
		this.processor = processor;
	}
}
