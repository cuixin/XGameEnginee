package com.github.xgameenginee.core;

/**
 * 协议加解密接口
 * @author Steven
 *
 */
public interface ProtocolCoder {
	void encode(byte[] source);
	void encode(byte[] source, int skipBytes);
	void decode(byte[] source);
}
