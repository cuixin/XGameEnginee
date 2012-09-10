package com.github.xgameenginee.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import com.github.xgameenginee.GameBoss;
import com.github.xgameenginee.core.ProtocolCoder;

public class GameDecoder extends LengthFieldBasedFrameDecoder {

	public GameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, ChannelBuffer cb) throws Exception {
		ChannelBuffer buffer =  (ChannelBuffer)super.decode(ctx, ch, cb);
		ProtocolCoder coder = GameBoss.getInstance().getProtocolCoder();
		if (coder != null && buffer != null) {
			byte[] decodebytes = coder.decode(buffer.array());
			return ChannelBuffers.wrappedBuffer(decodebytes);
		}
		return buffer;
	}

}
