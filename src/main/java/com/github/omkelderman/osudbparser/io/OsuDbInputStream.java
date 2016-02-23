package com.github.omkelderman.osudbparser.io;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class OsuDbInputStream extends BufferedInputStream {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private byte[] byteBuffer = new byte[8];

    public OsuDbInputStream(InputStream in) {
        super(in);
    }

    public void readFully(byte[] bytes) throws IOException {
        readFully(bytes, 0, bytes.length);
    }

    public final void readFully(byte[] bytes, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IndexOutOfBoundsException();
        }
        int bytesRead = 0;
        while (bytesRead < length) {
            int localBytesRead = read(bytes, offset + bytesRead, length - bytesRead);
            if (localBytesRead < 0) {
                throw new EOFException();
            }
            bytesRead += localBytesRead;
        }
    }

    private void readIntoBuffer(int amount) throws IOException {
        readFully(byteBuffer, 0, amount);
    }

    public void skipFully(long amount) throws IOException {
        long totalSkipped = 0;
        while (totalSkipped < amount) {
            long localSkipped = skip(amount - totalSkipped);
            if (localSkipped <= 0) {
                throw new IOException("Could not skip " + amount + " bytes, only skipped " + totalSkipped + " bytes");
            }
            totalSkipped += localSkipped;
        }
    }

    public boolean readBoolean() throws IOException {
        return (readUInt8() != 0);
    }

    /**
     * Read unsigned 8 bit integer
     *
     * @return int representing an unsigned 8 bit integer
     * @throws IOException
     */
    public int readUInt8() throws IOException {
        int b = read();
        if (b < 0) {
            throw new EOFException();
        }
        return b;

    }

    /**
     * Read little endian unsigned 16 bit integer
     *
     * @return int representing an unsigned 16 bit integer
     * @throws IOException
     */
    public int readUInt16() throws IOException {
        readIntoBuffer(2);
        return (byteBuffer[0] & 0xFF)
                | ((byteBuffer[1] & 0xFF) << 8);
    }

    /**
     * Read little endian unsigned 32 bit integer
     *
     * @return long representing an unsigned 32 bit integer
     * @throws IOException
     */
    public long readUInt32() throws IOException {
        readIntoBuffer(4);
        return (byteBuffer[0] & 0xFFL)
                | ((byteBuffer[1] & 0xFFL) << 8)
                | ((byteBuffer[2] & 0xFFL) << 16)
                | ((byteBuffer[3] & 0xFFL) << 24);
    }

    /**
     * Read little endian unsigned 64 bit integer.
     * <p>
     * <b>NOTE: there is no java-type to store an 64 bit unsigned number... so its stored in an long, this will give
     * wrong results for numbers above Long.MAX_VALUE.</b>
     * <i>And yes, i could have used BigInteger, was to lazy.</i>
     *
     * @return long representing an unsigned 64 bit integer
     * (negative values possible if original is above Long.MAX_VALUE)
     * @throws IOException
     */
    public long readUInt64() throws IOException {
        readIntoBuffer(8);
        return (byteBuffer[0] & 0xFFL)
                | ((byteBuffer[1] & 0xFFL) << 8)
                | ((byteBuffer[2] & 0xFFL) << 16)
                | ((byteBuffer[3] & 0xFFL) << 24)
                | ((byteBuffer[4] & 0xFFL) << 32)
                | ((byteBuffer[5] & 0xFFL) << 40)
                | ((byteBuffer[6] & 0xFFL) << 48)
                | ((byteBuffer[7] & 0xFFL) << 56); // OVERFLOW!!
    }

    public float readFloat() throws IOException {
        readIntoBuffer(4);
        return Float.intBitsToFloat((byteBuffer[0] & 0xFF)
                | ((byteBuffer[1] & 0xFF) << 8)
                | ((byteBuffer[2] & 0xFF) << 16)
                | ((byteBuffer[3] & 0xFF) << 24));
    }

    public double readDouble() throws IOException {
        readIntoBuffer(8);
        return Double.longBitsToDouble((byteBuffer[0] & 0xFFL)
                | ((byteBuffer[1] & 0xFFL) << 8)
                | ((byteBuffer[2] & 0xFFL) << 16)
                | ((byteBuffer[3] & 0xFFL) << 24)
                | ((byteBuffer[4] & 0xFFL) << 32)
                | ((byteBuffer[5] & 0xFFL) << 40)
                | ((byteBuffer[6] & 0xFFL) << 48)
                | ((byteBuffer[7] & 0xFFL) << 56));
    }

//    public long readULEB128asLong() throws IOException {
//        long result = 0;
//        int shift = 0;
//        while (true) {
//            int b = readUInt8();
//            result |= (b & 0x7F) << shift;
//            if ((b & 0x80) != 0x80) {
//                break;
//            }
//            shift += 7;
//            // TODO throw exception when not fits in long anymore
//        }
//        return result;
//    }

    public int readULEB128asInt() throws IOException {
        int result = 0;
        int shift = 0;
        while (true) {
            int b = readUInt8();
            result |= (b & 0x7F) << shift;
            if ((b & 0x80) != 0x80) {
                break;
            }
            shift += 7;
            // TODO throw exception when not fits in int anymore
        }
        return result;
    }

    public String readString() throws IOException {
        int b = readUInt8();
        if (b == 0) {
            return null;
        }
        if (b != 0x0B) {
            throw new IOException("expected byte 0x0B");
        }
        int length = readULEB128asInt();
        byte[] bytes = new byte[length];
        readFully(bytes);

        return new String(bytes, UTF_8);
    }
}
