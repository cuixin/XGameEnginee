package com.github.xgameenginee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Bootstrap {
    private static ServerBootstrap bootstrap   = null;

    private static ChannelGroup    allChannels = new DefaultChannelGroup();

    public static final synchronized void addChannel(Channel channel) {
        allChannels.add(channel);
    }

    public static final synchronized ServerBootstrap defaultBootstrap() {
        if (bootstrap == null)
            bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        return bootstrap;
    }

    public static final synchronized ServerBootstrap defaultBootstrap(ExecutorService bossExecutor, ExecutorService workExecutor) {
        if (bootstrap == null)
            bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(bossExecutor, workExecutor));
        return bootstrap;
    }

    public static final void release() {
        if (allChannels.size() > 0)
            allChannels.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }

}
