package com.github.xgameenginee.channel;


import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;

import com.github.xgameenginee.buffer.GameDownBuffer;
import com.github.xgameenginee.core.Connection;

public class GameChannel {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GameChannel.class);
	
	private DefaultChannelGroup channel;
	
	private String name;
	
	public GameChannel(String name) {
		this.name = name;
		channel = new DefaultChannelGroup(name);
	}
	
	public boolean addConnection(Connection c) {
		return channel.add(c.getChannel());
	}
	
	public boolean removeConnection(Connection c) {
		return channel.remove(c.getChannel());
	}
	
	public void broadcast(Connection except, GameDownBuffer buffer) {
		channel.remove(except.getChannel());
		channel.write(buffer.getChannelBuffer());
		channel.add(except.getChannel());
	}
	
	public void broadcast(GameDownBuffer buffer) {
		channel.write(buffer.getChannelBuffer());
	}
}
