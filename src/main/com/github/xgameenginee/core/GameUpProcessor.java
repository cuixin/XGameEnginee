package com.github.xgameenginee.core;

import com.github.xgameenginee.buffer.GameUpBuffer;

/**
 * 游戏上行消息解析接口
 * 可以基于此接口实现消息的不同计算队列的分发
 * @author Steven
 *
 */
public interface GameUpProcessor {
	public abstract void process(final GameUpBuffer buffer);
}
