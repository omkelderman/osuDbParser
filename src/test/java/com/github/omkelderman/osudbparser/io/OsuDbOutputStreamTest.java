package com.github.omkelderman.osudbparser.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertArrayEquals;

public class OsuDbOutputStreamTest {

    private void assertByteArrayOutputStream(byte[] expectedBytes, ByteArrayOutputStream outputStream) {
        assertArrayEquals(expectedBytes, outputStream.toByteArray());
    }

    private void assertByteArrayOutputStream(ByteArrayOutputStream outputStream, int... expectedBytes) {
        byte[] realBytes = new byte[expectedBytes.length];
        for (int i = 0; i < expectedBytes.length; ++i) {
            realBytes[i] = (byte) expectedBytes[i];
        }
        assertByteArrayOutputStream(realBytes, outputStream);
    }

    @Test
    public void testWriteBooleanFalse() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeBoolean(false);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0);
    }

    @Test
    public void testWriteBooleanTrue() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeBoolean(true);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 1);
    }

    @Test
    public void testWriteUInt8() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        int b = 0x42;

        osuDbOutputStream.writeUInt8(b);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, b);
    }

    @Test
    public void testWriteUInt16() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        // hex: 0x4203, little endian, so 0x42 + 0x0300
        int shortToTest = 0x42 + 0x0300;
        osuDbOutputStream.writeUInt16(shortToTest);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0x42, 0x03);
    }

    @Test
    public void testWriteUInt32() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        // hex: 0x01020304, little endian, so 0x01 + 0x0200 + 0x030000 + 0x04000000
        long intToTest = 0x01 + 0x0200 + 0x030000 + 0x04000000;
        osuDbOutputStream.writeUInt32(intToTest);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0x01, 0x02, 0x03, 0x04);
    }

    @Test
    public void testWriteUInt64() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        // hex: 0x0807060504030201, little endian, so
        //                  0x08 +
        //                0x0700 +
        //              0x060000 +
        //            0x05000000 +
        //          0x0400000000 +
        //        0x030000000000 +
        //      0x02000000000000 +
        //    0x0100000000000000
        long intToTest = 0x08 + 0x0700 + 0x060000 + 0x05000000 + 0x0400000000L + 0x030000000000L + 0x02000000000000L + 0x0100000000000000L;
        osuDbOutputStream.writeUInt64(intToTest);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01);
    }

    @Test
    public void testWriteFloat() throws Exception {
        float f = 3.1415926535F; // PI :D
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(f).array();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeFloat(f);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(bytes, outputStream);
    }

    @Test
    public void testWriteDouble() throws Exception {
        double d = 3.1415926535F; // PI :D
        byte[] bytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(d).array();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeDouble(d);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(bytes, outputStream);
    }

    @Test
    public void testWriteULEB128() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(3);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        // sample from https://en.wikipedia.org/wiki/LEB128
        osuDbOutputStream.writeULEB128(624485);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0xE5, 0x8E, 0x26);
    }

    @Test
    public void testWriteULEB128SingleByte() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        int i = 0x42;
        osuDbOutputStream.writeULEB128(i);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, i);
    }

    @Test
    public void testWriteStringNull() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeString(null);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0x00);
    }

    @Test
    public void testWriteStringEmpty() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeString("");
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(outputStream, 0x0B, 0x00);
    }

    @Test
    public void testWriteString() throws Exception {
        String helloWorld = "Hello World!";
        byte[] strBytes = helloWorld.getBytes("UTF-8");

        byte[] bytes = new byte[strBytes.length + 2];
        bytes[0] = 0x0B;
        bytes[1] = (byte) strBytes.length; // string is short enough that this should not be a problem
        System.arraycopy(strBytes, 0, bytes, 2, strBytes.length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        OsuDbOutputStream osuDbOutputStream = new OsuDbOutputStream(outputStream);

        osuDbOutputStream.writeString(helloWorld);
        osuDbOutputStream.flush();

        assertByteArrayOutputStream(bytes, outputStream);
    }
}