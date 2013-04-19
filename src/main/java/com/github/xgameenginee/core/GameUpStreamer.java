package com.github.xgameenginee.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xgameenginee.GameBoss;
import com.github.xgameenginee.buffer.GameUpBuffer;

public class GameUpStreamer extends SimpleChannelUpstreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleChannelUpstreamHandler.class);

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        // logger.info("Handler up " + e.getChannel().getInterestOps());
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer cb = (ChannelBuffer) e.getMessage();
        if (cb.getShort(0) < 1) // client upstream msg must be more than zero.
            return;

        GameBoss.getInstance().getProcessor().process(new GameUpBuffer(cb, ((Connection) ctx.getAttachment())));
        // logger.info("#" + ctx.getChannel().getId() + " recv = " + type);
        super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (e.getCause() instanceof ReadTimeoutException) {
            if (ctx.getAttachment() != null) {
                Connection c = (Connection) ctx.getAttachment();
                if (c.getAttachment() == null) {
                    ctx.getChannel().close();
                }
            }
            return;
        }

        if (e.getCause() instanceof TooLongFrameException) {
            ctx.getChannel().close();
            return;
        }
        if (e.getCause() instanceof ClosedChannelException) {
            return;
        }
        if (e.getCause() instanceof IOException) {
            logger.debug("", e);
            ctx.getChannel().close();
            return;
        }
        if (logger.isDebugEnabled())
            logger.debug("", e);
        super.exceptionCaught(ctx, e);
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // logger.info("Open");
        super.channelOpen(ctx, e);
    }

    @Override
    public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // logger.info("Bound");
        super.channelBound(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.setAttachment(ConnectionManager.getInstance().newConnection(ctx));
        // logger.info(ctx + " connected");
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // logger.info("changed");
        super.channelInterestChanged(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Connection c = (Connection) ctx.getAttachment();
        if (logger.isDebugEnabled())
            logger.debug("#" + c.getId() + " disconnected");
        super.channelDisconnected(ctx, e);
        if (c.getAttachment() != null) {
            ChannelBuffer cb = ChannelBuffers.buffer(2);
            cb.writeShort(0);
            GameUpBuffer disconnectEvent = new GameUpBuffer(cb, c);
            GameBoss.getInstance().getProcessor().process(disconnectEvent);
        } else {
            ConnectionManager.getInstance().removeConnection(c);
        }
        // XXX: You need to do this by yourself:
        // ConnectionManager.getInstance().removeConnection(c);
        // when you processed over;
    }

    @Override
    public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // logger.info("Channel unbound");
        super.channelUnbound(ctx, e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // logger.info("Channel closed");
        super.channelClosed(ctx, e);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        // logger.info("Write complete");
        super.writeComplete(ctx, e);
    }

    @Override
    public void childChannelOpen(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        // logger.info("Child channel opend");
        super.childChannelOpen(ctx, e);
    }

    @Override
    public void childChannelClosed(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        logger.info("Child channel closed");
        super.childChannelClosed(ctx, e);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}