package com.github.xgameenginee.core;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.github.xgameenginee.buffer.GameDownBuffer;

public class Connection {
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connection other = (Connection) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private int id;
	
	private Object attachment;
	
	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	
	private ChannelHandlerContext ctx;
	
	public void sendRawData(ByteBuffer buffer) {
		final Channel channel = ctx.getChannel();
		channel.write(GameDownBuffer.wrappedBuffer(buffer));
	}
	
	public boolean isConnected() {
		return ctx.getChannel().isConnected();
	}
	
	public void kill() {
		ConnectionManager.getInstance().removeConnection(this);
		ctx.getChannel().close();
	}
	
	public void sendRawData(byte[] buffer) {
		final Channel channel = ctx.getChannel();
		channel.write(GameDownBuffer.wrappedBuffer(buffer));
	}
	
	public void sendGameDownBuffer(GameDownBuffer gameBuffer) throws IllegalStateException {
		final Channel channel = ctx.getChannel();
		ChannelBuffer channelBuffer = gameBuffer.getChannelBuffer();
		if (channelBuffer.writableBytes() != 0)
			throw new IllegalStateException("write bytes not be full! type = " + gameBuffer.getChannelBuffer().getShort(2));
		channel.write(gameBuffer.getChannelBuffer());
	}
	
	public void kill(GameDownBuffer buffer) throws IllegalStateException {
		final Channel channel = ctx.getChannel();
		if (channel.isConnected()) {
			if (buffer != null) {
				ChannelBuffer channelBuffer = buffer.getChannelBuffer();
				if (channelBuffer.writableBytes() != 0)
					throw new IllegalStateException("write bytes not be full! type = " + channelBuffer.getShort(2));
				
				ChannelFuture cf = channel.write(channelBuffer);
				cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						kill();
					}
				});
			}
		} else
			kill();
	}
	
	public Channel getChannel() {
		return ctx.getChannel();
	}
	
	public Connection(int id, ChannelHandlerContext ctx) {
		this.id = id;
		this.ctx = ctx;
	}
	
	public int getId() {
		return id;
	}
}
