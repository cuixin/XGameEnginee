package com.github.xgameenginee.core;

/**
 * 协议加解密接口
 * @author Steven
 *
 */
public interface ProtocolCoder {
	byte[] encode(byte[] source);
	byte[] encode(byte[] source, int skipBytes);
	byte[] decode(byte[] source);
}
