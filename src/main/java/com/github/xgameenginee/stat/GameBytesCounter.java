package com.github.xgameenginee.stat;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameBytesCounter extends SimpleChannelHandler {

    AtomicLong                  readBytesCount  = new AtomicLong();

    AtomicLong                  writeBytesCount = new AtomicLong();

    private static final int    M_DATA          = 1024 * 1024;

    private static final Logger logger          = LoggerFactory.getLogger(GameBytesCounter.class);

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof ChannelBuffer) {
            if (readBytesCount.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes()) % M_DATA == 0)
                logger.info("Read count is " + (readBytesCount.get() / M_DATA) + 'M');
        }
        super.messageReceived(ctx, e);
    }

    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        if (writeBytesCount.addAndGet(e.getWrittenAmount()) % M_DATA == 0)
            logger.info("Write count is " + (writeBytesCount.get() / M_DATA) + 'M');
        super.writeComplete(ctx, e);
    }
}
