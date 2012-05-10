package com.github.xgameenginee.samples.echotest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    private final ChannelBuffer firstMessage;
    
    private final AtomicLong transferredBytes = new AtomicLong();

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler(int firstMessageSize) {
        if (firstMessageSize <= 0) {
            throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
        }
        firstMessage = ChannelBuffers.buffer(firstMessageSize + 8);
        firstMessage.writeShort(firstMessageSize + 6);
        firstMessage.writeShort(20); // game type
        firstMessage.writeInt(0); // value
        for (int i = 0; i < firstMessageSize; i++) {
            firstMessage.writeByte((byte) i);
        }
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        e.getChannel().write(firstMessage);
    }

    AtomicInteger value = new AtomicInteger();
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        if (value.get() % 10000 == 0) {		
        	logger.info("read " + transferredBytes.get());
        }
        ChannelBuffer msg = ChannelBuffers.buffer(108);
        msg.writeShort(106);
        msg.writeShort(20); // game type
        msg.writeInt(value.incrementAndGet()); // value
        for (int i = 0; i < 100; i++) {
            msg.writeByte((byte) i);
        }
        e.getChannel().write(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.info("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
