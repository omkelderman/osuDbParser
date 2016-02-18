package com.github.omkelderman.osudbparser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class OsuDbInputStream extends DataInputStream {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public OsuDbInputStream(InputStream in) {
        super(in);
    }

    public void skipFully(int n) throws IOException {
        int skipped = skipBytes(n);
        if (skipped != n) {
            throw new IOException("Could not skip " + n + " bytes, only skipped " + skipped + " bytes");
        }
    }

    // next method only cause reasons :P, consistency or something, idk xd
    /**
     * Read unsigned 8 bit integer
     *
     * @return int representing an unsigned 8 bit integer
     * @throws IOException
     */
    public int readUInt8() throws IOException {
        return readUnsignedByte();
    }

    /**
     * Read little endian unsigned 16 bit integer
     *
     * @return int representing an unsigned 16 bit integer
     * @throws IOException
     */
    public int readUInt16() throws IOException {
        byte[] bytes = new byte[2];
        readFully(bytes);
        return (bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8);
    }

    /**
     * Read little endian unsigned 32 bit integer
     *
     * @return long representing an unsigned 32 bit integer
     * @throws IOException
     */
    public long readUInt32() throws IOException {
        byte[] bytes = new byte[4];
        readFully(bytes);
        return (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24);
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
        byte[] bytes = new byte[8];
        readFully(bytes);
        return (bytes[0] & 0xFFL)
                | ((bytes[1] & 0xFFL) << 8)
                | ((bytes[2] & 0xFFL) << 16)
                | ((bytes[3] & 0xFFL) << 24)
                | ((bytes[4] & 0xFFL) << 32)
                | ((bytes[5] & 0xFFL) << 40)
                | ((bytes[6] & 0xFFL) << 48)
                | ((bytes[7] & 0xFFL) << 56); // OVERFLOW!!
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
