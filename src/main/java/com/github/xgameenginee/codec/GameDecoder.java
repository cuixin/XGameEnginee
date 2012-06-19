package com.github.xgameenginee.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

public class GameDecoder extends LengthFieldBasedFrameDecoder {

	public GameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, ChannelBuffer cb) throws Exception {
		return super.decode(ctx, ch, cb);
		// TODO: 需要实现解密
	}

}
