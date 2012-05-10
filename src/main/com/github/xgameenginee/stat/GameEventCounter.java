package com.github.xgameenginee.stat;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEventCounter extends SimpleChannelHandler {

	AtomicLong readEventCount = new AtomicLong();

	AtomicLong writeEventCount = new AtomicLong();

	private static final Logger logger = LoggerFactory.getLogger(GameBytesCounter.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof ChannelBuffer) {
			if (readEventCount.incrementAndGet() % 10000 == 0)
				logger.info("ReadEvent count is " + readEventCount.get());
		}
		super.messageReceived(ctx, e);
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof ChannelBuffer) {
			if (writeEventCount.incrementAndGet() % 10000 == 0)
				logger.info("WriteEvent count is " + writeEventCount.get());
		}
		super.writeRequested(ctx, e);
	}
}