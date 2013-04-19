package com.github.xgameenginee.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.handler.GameHandler;
import com.github.xgameenginee.handler.GameHandlerManager;

/**
 * 消息处理器
 * 
 * @author Steven
 * 
 */
public class GameWorker implements Runnable {

    private static final Logger logger    = LoggerFactory.getLogger(GameWorker.class);

    private volatile boolean    isRunning = true;

    /**
     * 关闭网络上行消息窗口
     */
    public void closeWorker() {
        isRunning = false;
    }

    Queue<GameUpBuffer> msgQueue = new ConcurrentLinkedQueue<GameUpBuffer>();

    /**
     * 推送网络上行的消息
     * 
     * @param c
     */
    public void pushUpstreamBuffer(GameUpBuffer c) {
        if (isRunning) {
            synchronized (msgQueue) {
                msgQueue.add(c);
                msgQueue.notifyAll();
            }
        }
    }

    /**
     * 推送由系统本身产生消息
     * 
     * @param c
     */
    public void pushSystemBuffer(GameUpBuffer c) {
        synchronized (msgQueue) {
            msgQueue.add(c);
            msgQueue.notifyAll();
        }
    }

    /**
     * 消息执行完毕
     * 
     * @return
     */
    public boolean isDone() {
        return msgQueue.isEmpty();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (msgQueue) {
                if (msgQueue.isEmpty()) {
                    try {
                        msgQueue.wait();
                    } catch (InterruptedException e) {
                        logger.info("receive an interrupted msg, now the state is " + isRunning);
                        if (!isRunning) {
                            return;
                        }
                    }
                }
            }
            GameUpBuffer c = null;
            while ((c = msgQueue.poll()) != null) {
                short type = c.getShort();
                try {
                    GameHandler handler = GameHandlerManager.getInstance().getHandler(type);
                    if (handler != null) {
                        if (!handler.isSystem() && c.attachment() == null) // real upstream message
                            handler.process(c.getConnection(), c);
                        else if (handler.isSystem()) // the system message
                            handler.process(c.getConnection(), c);
                        else {
                            logger.error("error in upstream :" + type);
                        }
                    } else {
                        logger.error("error type = " + type);
                    }
                } catch (Throwable e) {
                    logger.error("", e);
                }
            }
        }
    }
}
