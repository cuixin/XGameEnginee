package com.github.xgameenginee.core;

import java.nio.ByteBuffer;

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
	
	public ChannelFuture sendRawData(ByteBuffer buffer) {
		final Channel channel = ctx.getChannel();
		return channel.write(GameDownBuffer.wrappedBuffer(buffer));
	}
	
	public ChannelFuture sendRawData(byte[] buffer) {
		final Channel channel = ctx.getChannel();
		return channel.write(GameDownBuffer.wrappedBuffer(buffer));
	}
	
	public ChannelFuture sendGameDownBuffer(GameDownBuffer gameBuffer) {
		final Channel channel = ctx.getChannel();
		return channel.write(gameBuffer.getChannelBuffer());
	}
	
	public void kill(GameDownBuffer buffer) {
		final Channel channel = ctx.getChannel();
		if (channel.isConnected()) {
			if (buffer != null) {
				ChannelFuture cf = channel.write(buffer);
				cf.addListener(ChannelFutureListener.CLOSE);
			} else
				ctx.getChannel().close();
		}
	}
	
	public ChannelHandlerContext getChannelHandlerContext() {
		return ctx;
	}

	public Connection(int id, ChannelHandlerContext ctx) {
		this.id = id;
		this.ctx = ctx;
	}
	
	public int getId() {
		return id;
	}
}
