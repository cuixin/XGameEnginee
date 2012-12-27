package com.github.xgameenginee.channel;

import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.github.xgameenginee.GameBoss;
import com.github.xgameenginee.buffer.GameDownBuffer;
import com.github.xgameenginee.core.Connection;
import com.github.xgameenginee.core.ProtocolCoder;

public class GameChannel {
	
	private DefaultChannelGroup channel;
	
	public GameChannel(String name) {
		channel = new DefaultChannelGroup(name);
	}
	
	public boolean addConnection(Connection c) {
		return channel.add(c.getChannel());
	}
	
	public boolean removeConnection(Connection c) {
		return channel.remove(c.getChannel());
	}
	
	public void broadcast(Connection except, GameDownBuffer buffer) throws Exception {
		channel.remove(except.getChannel());
		broadcast(buffer);
		channel.add(except.getChannel());
	}
	
	public void broadcast(GameDownBuffer buffer) throws Exception {
		if (buffer.getChannelBuffer().writable()) {
			throw new IllegalStateException("write bytes not be filled full! type = " + buffer.getChannelBuffer().getShort(2));
		}
		
		ProtocolCoder coder = GameBoss.getInstance().getProtocolCoder();
		if (coder != null) {
			coder.encode(buffer.getChannelBuffer().array(), 
					GameBoss.getInstance().getWriteHeaderSize());
		}
		channel.write(buffer.getChannelBuffer());
	}
	
	@Override
	public String toString() {
		return channel.toString();
	}
	
	public int size() {
		return channel.size();
	}
	
}
