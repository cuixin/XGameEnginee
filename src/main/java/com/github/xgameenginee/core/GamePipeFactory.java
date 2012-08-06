package com.github.xgameenginee.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import com.github.xgameenginee.buffer.GameBufferFactory;
import com.github.xgameenginee.codec.FlashCrossDomainDecoder;
import com.github.xgameenginee.codec.GameDecoder;
import com.github.xgameenginee.codec.GameEncoder;

public class GamePipeFactory implements ChannelPipelineFactory {

	private int maxReadSize;
	
	private int readOffset;
	
	private int timeOut;
	
	/**
	 * 
	 * @param readSize
	 * @param maxReadSize
	 * @param hasReadSize 是否读取消息中的包含消息本身的大小
	 * @param writeSize
	 * @param timeOut 超时时间
	 */
	public GamePipeFactory(int readSize, int maxReadSize, boolean hasReadSize, int writeSize, boolean hasWriteSize, int timeOut) {
		GameBufferFactory.setupGameBuffer(readSize, hasReadSize, writeSize, hasWriteSize);
		this.maxReadSize = maxReadSize;
		this.readOffset = hasReadSize ? 0 : readSize;
		this.timeOut = timeOut;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		if (timeOut > 0)
			return Channels.pipeline(
					// new GameBytesCounter(),
					// new GameEventCounter(),
					new ReadTimeoutHandler(new HashedWheelTimer(), timeOut), // 一分钟不发送消息断开连接
					new FlashCrossDomainDecoder(), new GameDecoder
							(maxReadSize, 0, GameBufferFactory.getReadHeaderSize(), 0,
							readOffset), new GameEncoder(),
					new GameUpStreamer());
		else {
			return Channels.pipeline(
					new FlashCrossDomainDecoder(),
					new GameDecoder(maxReadSize, 0, GameBufferFactory
							.getReadHeaderSize(), 0, readOffset),
					new GameEncoder(), new GameUpStreamer());
		}
	}

}
