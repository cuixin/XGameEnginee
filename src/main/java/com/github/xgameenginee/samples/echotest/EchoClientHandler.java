package com.github.xgameenginee.samples.echotest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xgameenginee.buffer.GameDownBuffer;

public class EchoClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    private final GameDownBuffer firstMessage;
    
    private final AtomicLong transferredBytes = new AtomicLong();

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler(int firstMessageSize) {
        if (firstMessageSize <= 0) {
            throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
        }
        firstMessage = GameDownBuffer.allocat(20, // game type
        		firstMessageSize + 4);
        firstMessage.putInt(0); // value
        for (int i = 0; i < firstMessageSize; i++) {
            firstMessage.put((byte) i);
        }
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        e.getChannel().write(firstMessage.getChannelBuffer());
    }

    AtomicInteger value = new AtomicInteger();
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        if (value.get() % 10000 == 0) {		
        	logger.info("send bytes " + transferredBytes.get());
        }
        GameDownBuffer msg = GameDownBuffer.allocat(20, // game type
        		104); // game data size
        msg.putInt(value.incrementAndGet()); // value
        for (int i = 0; i < 100; i++) {
            msg.put((byte) i);
        }
        e.getChannel().write(msg.getChannelBuffer());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.info("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
