package com.github.xgameenginee.channel;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.github.xgameenginee.buffer.GameDownBuffer;
import com.github.xgameenginee.core.Connection;

public class GameChannel {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GameChannel.class);
		
	private Set<Connection> list = new HashSet<Connection>();
	
	private String name;
	
	public GameChannel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean addConnection(Connection c) {
		logger.info(getName() + " joined " + c.getAttachment());
		return list.add(c);
	}
	
	public boolean removeConnection(Connection c) {
		logger.info(getName() + " removed " + c.getAttachment());
		return list.remove(c);
	}
	
	public void broadcast(Connection except, GameDownBuffer buffer) {
		removeConnection(except);
		for (Connection c: list) {
			c.sendGameDownBuffer(buffer.duplicate());
		}
		addConnection(except);
	}
	
	public void broadcast(GameDownBuffer buffer) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(getName() + " broadcast: " + buffer.getChannelBuffer().getShort(2)).append(" size = ").append(list.size());
		sBuilder.append("{");
		for (Connection c: list) {
			sBuilder.append(c.getId()).append("=").append(c.getAttachment()).append(",");
			c.sendGameDownBuffer(buffer.duplicate());
		}
		sBuilder.append("}");
		logger.info(sBuilder.toString());
//		channel.write(buffer.getChannelBuffer());
	}
}
