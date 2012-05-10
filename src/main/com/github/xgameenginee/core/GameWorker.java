package com.github.xgameenginee.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.handler.GameHandler;
import com.github.xgameenginee.handler.GameHandlerManager;

/**
 * 全局消息处理器
 * @author Steven
 *
 */
public class GameWorker implements Runnable {
	Queue<GameUpBuffer> msgQueue = new ConcurrentLinkedQueue<GameUpBuffer>();
	
	public void addConnection(GameUpBuffer c) {
		synchronized (msgQueue) {
			msgQueue.add(c);
			msgQueue.notifyAll();
		}
	}
	
	private static final GameWorker instance = new GameWorker();
	
	public static final GameWorker getInstance() {
		return instance;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (msgQueue) {
				if (msgQueue.isEmpty()) {
					try {
						msgQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
			GameUpBuffer c = null;
			while ((c = msgQueue.poll()) != null) {
				short type = c.getShort();
				try {
					GameHandler handler = GameHandlerManager.getInstance().getHandler(type);
					if (handler != null)
						handler.process(c.getConnection(), c);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
}
