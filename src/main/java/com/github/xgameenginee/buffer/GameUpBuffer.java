package com.github.xgameenginee.buffer;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.buffer.ChannelBuffer;

import com.github.xgameenginee.core.Connection;

/**
 * 游戏上行消息或者是计算用途的上行消息
 * @author Steven
 *
 */
public class GameUpBuffer {
	
	private final ChannelBuffer buffer;
	
	private Connection connection;
	
	private Object attachment;
	
	public Object attachment() {
		return attachment;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public GameUpBuffer(ChannelBuffer buffer, Connection connection) {
		this.connection = connection;
		this.buffer = buffer;
	}
	
	public GameUpBuffer(ChannelBuffer buffer) {
		this.buffer = buffer;
	}
	
	public GameUpBuffer(ChannelBuffer buffer, Object attachment) {
		this.buffer = buffer;
		this.attachment = attachment;
	}
	
	public GameUpBuffer(ChannelBuffer buffer, Connection connection, Object attachment) {
		this.buffer = buffer;
		this.connection = connection;
		this.attachment = attachment;
	}
	
	public short getShort() {
		return buffer.readShort();
	}
	
	public byte[] array() {
		return buffer.array();
	}
	
	public int getInt() {
		return buffer.readInt();
	}
	
	public byte get() {
		return buffer.readByte();
	}
	
	public void get(byte[] dst) {
		buffer.readBytes(dst);
	}
	
	public void get(byte[] dst, int dstIndex, int length) {
		buffer.readBytes(dst, dstIndex, length);
	}
	
	public long getLong() {
		return buffer.readLong();
	}
	
	public String getUTFString() {
		int len = buffer.readShort();
		if (len <= 0 || len > 1024)
			throw new IllegalArgumentException("string length cannot less than zero or len is more than 1024");
		byte[] strData = new byte[len];
		if (buffer.readableBytes() < len) {
			throw new IllegalArgumentException("readableBytes less than " + len);
		}
		buffer.readBytes(strData);
		String result = null;
		try {
			result = new String(strData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			result = new String(strData);
			e.printStackTrace();
		}
		return result;
	}
	
	public String getString(String encode) throws UnsupportedEncodingException {
		int len = buffer.readShort();
		if (len <= 0)
			throw new IllegalArgumentException("string length cannot less than zero");
		byte[] strData = new byte[len];
		String result = new String(strData, encode);
		return result;
	}
	
	public int remaining() {
		return buffer.readableBytes();
	}
	
	public boolean hasRemain() {
		return buffer.readableBytes() > 0;
	}

}
