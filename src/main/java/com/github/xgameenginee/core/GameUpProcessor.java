package com.github.xgameenginee.core;

import com.github.xgameenginee.buffer.GameUpBuffer;

/**
 * 游戏上行消息解析接口
 * 可以基于此接口实现消息的不同计算队列的分发
 * @author Steven
 *
 */
public interface GameUpProcessor {
	/**
	 * 处理消息逻辑或者分发你的消息
	 * @param buffer
	 */
	public abstract void process(final GameUpBuffer buffer);
	
	/**
	 * 游戏服务器收到关闭消息
	 */
	public abstract void onShutdown();
}
