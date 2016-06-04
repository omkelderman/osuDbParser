package com.github.omkelderman.osudbparser.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SubInputStreamTest {
    private ByteArrayInputStream byis;
    private SubInputStream subInputStream;

    @Before
    public void setUp() {
        byte[] bytes = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        byis = new ByteArrayInputStream(bytes);
        assertEquals("first byte", 0, byis.read());
        subInputStream = new SubInputStream(byis, 5);
    }

    @After
    public void tearDown() throws IOException {
        subInputStream.close();
        assertEquals("byte after subsection", 6, byis.read());
    }

    @Test
    public void read() throws Exception {
        assertEquals(1, subInputStream.read());
    }

    @Test
    public void readTooMuch() throws Exception {
        assertEquals("skip all 5 bytes", 5, subInputStream.skip(5));
        // attempt to read again
        assertEquals(-1, subInputStream.read());
    }

    @Test
    public void readBytes() throws Exception {
        byte[] bytes = new byte[3];
        int amountRead = subInputStream.read(bytes);
        assertEquals("3 bytes read", 3, amountRead);
        byte[] expected = {1, 2, 3};
        assertArrayEquals("the bytes", expected, bytes);
    }

    @Test
    public void readBytesTooMuch1() throws Exception {
        byte[] bytes = {42, 42, 42, 42, 42, 42, 42};
        int amountRead = subInputStream.read(bytes);
        assertEquals("5 bytes read", 5, amountRead);
        byte[] expected = {1, 2, 3, 4, 5, 42, 42};
        assertArrayEquals("the bytes", expected, bytes);
    }

    @Test
    public void readBytesTooMuch2() throws Exception {
        assertEquals("skip all 5 bytes", 5, subInputStream.skip(5));
        // attempt to read again
        byte[] bytes = {42, 42};
        int amountRead = subInputStream.read(bytes);
        assertEquals(-1, amountRead);
        byte[] expected = {42, 42};
        assertArrayEquals("the bytes", expected, bytes);
    }

    @Test
    public void readBytesWithOffset() throws Exception {
        byte[] bytes = {42, 42, 42, 42, 42};
        int amountRead = subInputStream.read(bytes, 1, 3);
        assertEquals("3 bytes read", 3, amountRead);
        byte[] expected = {42, 1, 2, 3, 42};
        assertArrayEquals("the bytes", expected, bytes);
    }

    @Test
    public void skip() throws Exception {
        assertEquals("skip 2", 2, subInputStream.skip(2));
        assertEquals("next byte after skip", 3, subInputStream.read());
    }

    @Test
    public void skipTooMuch() throws Exception {
        assertEquals("skip too much", 5, subInputStream.skip(10));
        assertEquals("next byte after skip", -1, subInputStream.read());
    }

    @Test
    public void available() throws Exception {
        assertEquals(5, subInputStream.available());
    }

    @Test
    public void skipAndAvailable() throws Exception {
        assertEquals("skip 2", 2, subInputStream.skip(2));
        assertEquals("3 remaining", 3, subInputStream.available());
    }

    // close doesn't need its own test-method, its getting tested in tearDown() already

    // depends in skip working correctly
    @Test
    public void markAndReset() throws Exception {
        assertEquals("skip 1", 1, subInputStream.skip(1));
        byte[] expectedBytes = {2, 3, 4};
        // mark 4 ahead
        subInputStream.mark(4);

        // read next 3 bytes and assert
        readAndAssert(expectedBytes);

        // reset
        subInputStream.reset();

        // read the bytes again
        readAndAssert(expectedBytes);

    }

    private void readAndAssert(byte[] expectedBytes) throws IOException {
        byte[] bytes = new byte[expectedBytes.length];
        assertEquals(subInputStream.read(bytes), expectedBytes.length);
        assertArrayEquals(expectedBytes, bytes);
    }

    @Test
    public void markSupported() throws Exception {
        assertEquals(byis.markSupported(), subInputStream.markSupported());
    }

}