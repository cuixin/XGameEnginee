
package com.github.xgameenginee.samples.echotest;

import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.core.Connection;
import com.github.xgameenginee.core.ConnectionManager;
import com.github.xgameenginee.handler.GameHandler;

/**
 * 断开连接测试
 * 
 * @author Steven
 * 
 */
public class DisconnectedHandler implements GameHandler {

    @Override
    public void process(final Connection connection, final GameUpBuffer buffer) throws Throwable {
        ConnectionManager.getInstance().removeConnection(connection); // This is must be down by yourself.
        if (connection.isKilled())
            System.out.println(connection + " killed!");
        else
            System.out.println(connection + " disconnected!");
    }

    @Override
    public boolean isSystem() {
        return false;
    }

}