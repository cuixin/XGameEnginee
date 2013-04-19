package com.github.xgameenginee.buffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class GameBufferFactory {

    private static int     readHeader;

    private static boolean hasReadSize;

    private static int     writeHeader;

    private static boolean hasWriteSize;

    private GameBufferFactory(int readSize, boolean hasReader, int writeSize, boolean hasWriter) {
        if (!(readSize == 2 || readSize == 4)) {
            throw new IllegalArgumentException("readSize must be 2 or 4");
        }

        if (!(writeSize == 2 || writeSize == 4)) {
            throw new IllegalArgumentException("writeSize must be 2 or 4");
        }
        readHeader = readSize;
        hasReadSize = hasReader;
        writeHeader = writeSize;
        hasWriteSize = hasWriter;
    }

    public static int getReadHeaderSize() {
        return readHeader;
    }

    public static boolean isHeaderSize() {
        return hasReadSize;
    }

    public static int getWriteHeaderSize() {
        return writeHeader;
    }

    public static boolean isWriterSize() {
        return hasWriteSize;
    }

    private static int getWriterSize() {
        return hasWriteSize ? 2 : 0;
    }

    public static final ChannelBuffer getBuffer(final int capacity) {
        final ChannelBuffer buffer;
        buffer = ChannelBuffers.buffer(writeHeader + capacity);
        if (writeHeader == 2)
            buffer.writeShort(capacity + getWriterSize());
        else
            buffer.writeInt(capacity + getWriterSize());
        return buffer;
    }

    private static GameBufferFactory instance;

    public synchronized static final GameBufferFactory setupGameBuffer(int readSize, boolean hasReader, int writeSize, boolean hasWriter) {
        if (instance == null)
            instance = new GameBufferFactory(readSize, hasReader, writeSize, hasWriter);
        return instance;
    }
}
