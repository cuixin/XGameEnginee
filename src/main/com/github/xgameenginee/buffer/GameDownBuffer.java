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
	
	public void putShort(short value) {
		buffer.writeShort(value);
	}
	
	public void putInt(int value) {
		buffer.writeInt(value);
	}
	
	public void put(byte value) {
		buffer.writeByte(value);
	}
	
	public void put(byte[] src) {
		buffer.writeBytes(src);
	}
	
	public void put(byte[] src, int srcIndex, int length) {
		buffer.writeBytes(src, srcIndex, length);
	}
	
	public void putLong(long value) {
		buffer.writeLong(value);
	}
	
	public void putUTFString(String src) {
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
	}
	
	public void putString(String src, String encode) throws UnsupportedEncodingException {
		if (src == null)
			src = "";
		byte[] strData = src.getBytes(encode);
		buffer.writeShort(strData.length);
		buffer.writeBytes(strData);
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
	
	public static final GameDownBuffer allocat(short gameType, int capacity) {
		GameDownBuffer gb = new GameDownBuffer(GameBufferFactory.getBuffer(capacity + 2));
		gb.putShort(gameType);
		return gb;
	}
	
	public ChannelBuffer getChannelBuffer() {
		return this.buffer;
	}
	
}
