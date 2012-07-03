package com.github.xgameenginee.handler;

import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.core.Connection;

/**
 * 消息处理接口
 * @author Steven
 *
 */
public interface GameHandler {
	
	/**
	 * 处理玩家客户端发的消息或者服务器发的消息
	 * @param obj
	 * @param buffer
	 * @throws Throwable
	 */
	public abstract void process(final Connection connection, final GameUpBuffer buffer) throws Throwable;
	
	public boolean isSystem();
	
}
