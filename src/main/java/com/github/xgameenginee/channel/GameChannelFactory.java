package com.github.xgameenginee.channel;

import java.util.HashMap;
import java.util.Map;

public class GameChannelFactory {

	private Map<String, GameChannel> channelMap = new HashMap<String, GameChannel>();
	
	private static final GameChannelFactory instance = new GameChannelFactory(); 
	
	private GameChannel all = new GameChannel("All");
	
	private GameChannelFactory() {
	}
	
	public static final GameChannelFactory getInstance() {
		return instance;
	}
	
	public GameChannel getAllChannel() {
		return all;
	}
	
	public GameChannel getChannel(String name) {
		synchronized(channelMap) {
			return channelMap.get(name);
		}
	}
	
	public GameChannel newChannel(String name) {
		synchronized (channelMap) {
			GameChannel channel = channelMap.get(name);
			if (channel != null)
				return channel;
			else {
				channel = new GameChannel(name);
				channelMap.put(name, channel);
				return channel;
			}
		}
	}
}
