package com.github.omkelderman.osudbparser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class OsuDbInputStreamTest {

    @Test(expected = EOFException.class)
    public void testReadFullyToShort() throws IOException {
        byte[] bytes = new byte[]{0, 1}; // only two bytes
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(inputStream);
        byte[] bytesToRead = new byte[4]; // to much
        osuDbInputStream.readFully(bytesToRead);
    }

    private static ByteArrayInputStream buildInputStream(int... bytes) {
        byte[] realBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            realBytes[i] = (byte) bytes[i];
        }
        return new ByteArrayInputStream(realBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] bytes = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(inputStream);
        byte[] bytesToRead = new byte[4];
        osuDbInputStream.readFully(bytesToRead);

        assertArrayEquals(bytes, bytesToRead);
    }

    @Test
    public void testReadBooleanFalse() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0));

        boolean result = osuDbInputStream.readBoolean();

        assertEquals(false, result);
    }

    @Test
    public void testReadBooleanTrue() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(1));

        boolean result = osuDbInputStream.readBoolean();

        assertEquals(true, result);
    }

    @Test
    public void testReadUInt8() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x42));

        int result = osuDbInputStream.readUInt8();

        assertEquals(0x42, result);
    }

    @Test
    public void testReadUInt16() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x42, 0x03));
        // expected: 0x4203, little endian, so 0x42 + 0x0300
        int expectedShort = 0x42 + 0x0300;

        int result = osuDbInputStream.readUInt16();

        assertEquals(expectedShort, result);
    }

    @Test
    public void testReadUInt32() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x01, 0x02, 0x03, 0x04));
        // expected: 0x01020304, little endian, so 0x01 + 0x0200 + 0x030000 + 0x04000000
        long expectedInt = 0x01 + 0x0200 + 0x030000 + 0x04000000;

        long result = osuDbInputStream.readUInt32();

        assertEquals(expectedInt, result);
    }

    @Test
    public void testReadUInt64() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01));
        // expected: 0x0807060504030201, little endian, so
        //                  0x08 +
        //                0x0700 +
        //              0x060000 +
        //            0x05000000 +
        //          0x0400000000 +
        //        0x030000000000 +
        //      0x02000000000000 +
        //    0x0100000000000000
        long expectedLong = 0x08 + 0x0700 + 0x060000 + 0x05000000 + 0x0400000000L + 0x030000000000L + 0x02000000000000L + 0x0100000000000000L;

        long result = osuDbInputStream.readUInt64();

        assertEquals(expectedLong, result);
    }

    @Test
    public void testReadFloat() throws Exception {
        float f = 3.1415926535F; // PI :D
        byte[] bytes = ByteBuffer.allocate(4).putFloat(f).array();
        // ok java uses big endian, we little endian, lets reverse the bytes
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(inputStream);
        float resultF = osuDbInputStream.readFloat();

        assertEquals(f, resultF, 0.0000000001);
    }

    @Test
    public void testReadDouble() throws Exception {
        double d = 3.1415926535F; // PI :D
        byte[] bytes = ByteBuffer.allocate(8).putDouble(d).array();
        // ok java uses big endian, we little endian, lets reverse the bytes
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(inputStream);
        double resultD = osuDbInputStream.readDouble();

        assertEquals(d, resultD, 0.0000000001);
    }

    @Test
    public void testReadULEB128asInt() throws Exception {
        // sample from https://en.wikipedia.org/wiki/LEB128
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0xE5, 0x8E, 0x26));
        int expected = 624485;

        int result = osuDbInputStream.readULEB128asInt();

        assertEquals(expected, result);
    }

    @Test
    public void testReadULEB128asIntSingleByt() throws Exception {
        int expected = 0x42;
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(expected));

        int result = osuDbInputStream.readULEB128asInt();

        assertEquals(expected, result);
    }

    @Test
    public void testReadStringNull() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x00));
        String result = osuDbInputStream.readString();

        assertEquals(null, result);
    }

    @Test
    public void testReadStringEmpty() throws Exception {
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(buildInputStream(0x0B, 0x00));
        String result = osuDbInputStream.readString();

        assertEquals("", result);
    }

    @Test
    public void testReadString() throws Exception {
        String helloWorld = "Hello World!";
        byte[] strBytes = helloWorld.getBytes("UTF-8");

        byte[] bytes = new byte[strBytes.length + 2];
        bytes[0] = 0x0B;
        bytes[1] = (byte) strBytes.length; // string is short enough that this should not be a problem
        System.arraycopy(strBytes, 0, bytes, 2, strBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        OsuDbInputStream osuDbInputStream = new OsuDbInputStream(inputStream);

        String result = osuDbInputStream.readString();
        assertEquals(helloWorld, result);
    }
}