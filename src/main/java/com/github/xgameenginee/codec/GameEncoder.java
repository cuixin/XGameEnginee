package com.github.xgameenginee.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.github.xgameenginee.GameBoss;
import com.github.xgameenginee.core.ProtocolCoder;

public class GameEncoder extends OneToOneEncoder {
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel ch, Object obj) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)obj;
		ProtocolCoder coder = GameBoss.getInstance().getProtocolCoder();
		if (coder != null) {
			coder.encode(buffer.array(), GameBoss.getInstance().getWriteHeaderSize());
		}
		return buffer;
	}

}
