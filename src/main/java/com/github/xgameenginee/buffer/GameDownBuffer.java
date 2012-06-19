package com.github.xgameenginee.buffer;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
 
/**
 * 游戏下行消息
 * @author Steven
 *
 */
public class GameDownBuffer {
	private ChannelBuffer buffer;
		
	private GameDownBuffer(ChannelBuffer buffer) {
		this.buffer = buffer;
	}
	
	public GameDownBuffer putShort(short value) {
		buffer.writeShort(value);
		return this;
	}
	
	public GameDownBuffer putInt(int value) {
		buffer.writeInt(value);
		return this;
	}
	
	public GameDownBuffer put(byte value) {
		buffer.writeByte(value);
		return this;
	}
	
	public GameDownBuffer put(byte[] src) {
		buffer.writeBytes(src);
		return this;
	}
	
	public GameDownBuffer put(byte[] src, int srcIndex, int length) {
		buffer.writeBytes(src, srcIndex, length);
		return this;
	}
	
	public GameDownBuffer putLong(long value) {
		buffer.writeLong(value);
		return this;
	}
	
	public GameDownBuffer putUTFString(String src) {
		if (src == null)
			src = "";
		byte[] strData;
		try {
			strData = src.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			strData = src.getBytes();
			e1.printStackTrace();
		}
		buffer.writeShort(strData.length);
		buffer.writeBytes(strData);
		return this;
	}
	
	public GameDownBuffer putString(String src, String encode) throws UnsupportedEncodingException {
		if (src == null)
			src = "";
		byte[] strData = src.getBytes(encode);
		buffer.writeShort(strData.length);
		buffer.writeBytes(strData);
		return this;
	}
	
	public int remaining() {
		return buffer.writableBytes();
	}
	
	public boolean hasRemain() {
		return buffer.writableBytes() > 0;
	}
	
	public static final GameDownBuffer allocat(int capacity) {
		return new GameDownBuffer(GameBufferFactory.getBuffer(capacity));
	}
	
	public static final GameDownBuffer wrappedBuffer(ByteBuffer buffer) {
		return new GameDownBuffer(ChannelBuffers.wrappedBuffer(buffer));
	}

	public static final GameDownBuffer wrappedBuffer(byte[] buffer) {
		return new GameDownBuffer(ChannelBuffers.wrappedBuffer(buffer));
	}
	
	public static final GameDownBuffer allocat(int gameType, int capacity) {
		GameDownBuffer gb = new GameDownBuffer(GameBufferFactory.getBuffer(capacity + 2));
		gb.putShort((short)gameType);
		return gb;
	}
	
	public ChannelBuffer getChannelBuffer() {
		return this.buffer;
	}
	
}
