package com.github.xgameenginee.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flash安全验证
 * @author Steven
 *
 */
public class FlashCrossDomainDecoder extends OneToOneDecoder {

	private static final byte[] XML_REPLAY = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>".getBytes();
	
	private Logger logger = LoggerFactory.getLogger(FlashCrossDomainDecoder.class);
	
    private static final ChannelBuffer requestBuffer = ChannelBuffers.copiedBuffer("<policy-file-request/>", CharsetUtil.US_ASCII);

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (msg instanceof ChannelBuffer) {
			ChannelBuffer cb = (ChannelBuffer)msg;
			if (cb.readableBytes() == 23 && cb.getShort(0) == 15472) {
				ChannelBuffer data = cb.readBytes(requestBuffer.readableBytes());
				if (data.equals(requestBuffer)) {
					if (logger.isDebugEnabled())
						logger.debug("replay flashpolicy " + channel.getRemoteAddress());
					channel.write(ChannelBuffers.wrappedBuffer(XML_REPLAY)).addListener(ChannelFutureListener.CLOSE);
					return null;
				}
			}
		}
		return msg;
	}

}
