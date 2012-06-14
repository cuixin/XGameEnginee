package com.github.xgameenginee.samples.echotest;

import com.github.xgameenginee.buffer.GameDownBuffer;
import com.github.xgameenginee.buffer.GameUpBuffer;
import com.github.xgameenginee.core.Connection;
import com.github.xgameenginee.handler.GameHandler;

/**
 * 类似登陆消息的测试
 * @author Steven
 *
 */
public class LoginHandler implements GameHandler {

	@Override
	public void process(final Connection connection, final GameUpBuffer buffer) throws Throwable {
		int number = buffer.getInt();
		if (connection.getAttachment() == null) {
			connection.setAttachment(new Integer(0));
		}
		if (number != (Integer)connection.getAttachment()) {
			System.out.println("error in number " + number + ":" + (Integer)connection.getAttachment());
		}
		connection.setAttachment(number + 1);

		GameDownBuffer writeBuffer = GameDownBuffer.allocat((short)20, 1000);
		writeBuffer.putInt(number + 1);
		writeBuffer.put(new byte[996]);
		connection.sendGameDownBuffer(writeBuffer);
//		if (number >= 20000) {
//			cf.addListener(new ChannelFutureListener() {
//				
//				@Override
//				public void operationComplete(ChannelFuture future) throws Exception {
//					connection.kill(null);
//				}
//			});
//		}
//		System.out.println("Number = " + number);
	}

}
