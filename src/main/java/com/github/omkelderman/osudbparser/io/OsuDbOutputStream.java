package com.github.omkelderman.osudbparser.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class OsuDbOutputStream extends BufferedOutputStream {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int UINT8_MAX = 0xFF;
    private static final int UINT16_MAX = 0xFFFF;
    private static final long UINT32_MAX = 0xFFFFFFFFL;
    private byte[] byteBuffer = new byte[8];

    public OsuDbOutputStream(OutputStream out) {
        super(out);
    }

    // the writeFully-methods seems kinda redundant here...
    // BufferedOutputStream implements them already as I want them

    private void writeFromBuffer(int amount) throws IOException {
        write(byteBuffer, 0, amount);
    }

    public void writeBoolean(boolean b) throws IOException {
        writeUInt8(b ? 1 : 0);
    }

    private void checkBounds(long number, long max) {
        if (number < 0 || number > max) {
            throw new IllegalArgumentException("amount out of bounds");
        }
    }

    public void writeUInt8(int uint8) throws IOException {
        checkBounds(uint8, UINT8_MAX);
        write(uint8);
    }

    public void writeUInt16(int uint16) throws IOException {
        checkBounds(uint16, UINT16_MAX);
        byteBuffer[0] = (byte) (uint16 & 0xFF);
        byteBuffer[1] = (byte) ((uint16 >> 8) & 0xFF);
        writeFromBuffer(2);
    }

    public void writeUInt32(long uint32) throws IOException {
        checkBounds(uint32, UINT32_MAX);
        byteBuffer[0] = (byte) (uint32 & 0xFFL);
        byteBuffer[1] = (byte) ((uint32 >> 8) & 0xFFL);
        byteBuffer[2] = (byte) ((uint32 >> 16) & 0xFFL);
        byteBuffer[3] = (byte) ((uint32 >> 24) & 0xFFL);
        writeFromBuffer(4);
    }

    public void writeUInt64(long uint64) throws IOException {
        // no bounds checking, cause this thing is flawed anyways
        byteBuffer[0] = (byte) (uint64 & 0xFFL);
        byteBuffer[1] = (byte) ((uint64 >> 8) & 0xFFL);
        byteBuffer[2] = (byte) ((uint64 >> 16) & 0xFFL);
        byteBuffer[3] = (byte) ((uint64 >> 24) & 0xFFL);
        byteBuffer[4] = (byte) ((uint64 >> 32) & 0xFFL);
        byteBuffer[5] = (byte) ((uint64 >> 40) & 0xFFL);
        byteBuffer[6] = (byte) ((uint64 >> 48) & 0xFFL);
        byteBuffer[7] = (byte) ((uint64 >> 56) & 0xFFL);
        writeFromBuffer(8);
    }

    public void writeFloat(float f) throws IOException {
        int i = Float.floatToRawIntBits(f);
        byteBuffer[0] = (byte) (i & 0xFF);
        byteBuffer[1] = (byte) ((i >> 8) & 0xFF);
        byteBuffer[2] = (byte) ((i >> 16) & 0xFF);
        byteBuffer[3] = (byte) ((i >> 24) & 0xFF);
        writeFromBuffer(4);
    }

    public void writeDouble(double d) throws IOException {
        long i = Double.doubleToRawLongBits(d);
        byteBuffer[0] = (byte) (i & 0xFFL);
        byteBuffer[1] = (byte) ((i >> 8) & 0xFFL);
        byteBuffer[2] = (byte) ((i >> 16) & 0xFFL);
        byteBuffer[3] = (byte) ((i >> 24) & 0xFFL);
        byteBuffer[4] = (byte) ((i >> 32) & 0xFFL);
        byteBuffer[5] = (byte) ((i >> 40) & 0xFFL);
        byteBuffer[6] = (byte) ((i >> 48) & 0xFFL);
        byteBuffer[7] = (byte) ((i >> 56) & 0xFFL);
        writeFromBuffer(8);
    }

    public void writeULEB128(int uleb128) throws IOException {
        while (true) {
            if ((uleb128 & 0xFFFFFF80) == 0) {
                write(uleb128);
                return;
            }

            write(uleb128 & 0x7F | 0x80);
            uleb128 >>>= 7;
        }
    }

    public void writeString(String string) throws IOException {
        if (string == null) {
            writeUInt8(0);
            return;
        }
        write(0x0B);
        byte[] bytes = string.getBytes(UTF_8);
        writeULEB128(bytes.length);
        write(bytes);
    }
}
