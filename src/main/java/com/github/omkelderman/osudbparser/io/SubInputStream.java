package com.github.omkelderman.osudbparser.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Read a subsection of an {@link InputStream}.
 * <p>
 * Provide a base {@link InputStream} and a size, and after that you should NOT use the base {@link InputStream}
 * anymore. Instead use only the instance of {@link SubInputStream} you just created, and read any amount of bytes you
 * desire. When you close the {@link SubInputStream} any bytes that haven't been read yet will be skipped so that now
 * the base {@link InputStream} is ready again to be read from and it will start reading right after the sub-section.
 * <p>
 * Within a section, all read calls are redirected to the underlying {@link InputStream} with the set sub-section-size
 * in mind. So when you try to read more than the sub-section, it will act as EOF.
 */
public class SubInputStream extends InputStream {
    private final InputStream in;
    private final int size;
    private int cursor;
    private boolean closed;
    private int markedCursor;

    /**
     * Construct a {@link SubInputStream}
     *
     * @param in   The base {@link InputStream}
     * @param size The size of the sub-section
     */
    public SubInputStream(InputStream in, int size) {
        this.in = in;
        this.size = size;
        this.cursor = 0;
        this.closed = false;
        this.markedCursor = 0;
    }

    @Override
    public int read() throws IOException {
        throwIfClosed();
        if (cursor >= size) {
            // no more bytes to read in this sub-section
            return -1;
        }
        int ret = in.read();
        if (ret >= 0) {
            // if no EOF, increase cursor
            ++cursor;
        }
        return ret;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throwIfClosed();
        if (cursor >= size) {
            // no more bytes to read in this sub-section
            return -1;
        }

        // maximum available bytes to read in this sub-section
        int max = size - cursor;
        if (len > max) {
            len = max;
        }

        // increase cursor by amount read
        int ret = in.read(b, off, len);
        if (ret >= 0) {
            // if no EOF, increase cursor
            cursor += ret;
        }
        return ret;
    }

    @Override
    public long skip(long n) throws IOException {
        int max = size - cursor;
        if (n > max) {
            n = max;
        }

        long skipped = in.skip(n);
        cursor += skipped;
        return skipped;
    }

    @Override
    public int available() throws IOException {
        return Math.min(size - cursor, in.available());
    }

    private void throwIfClosed() throws IOException {
        if (closed) throw new IOException("Stream closed");
    }

    @Override
    public void close() throws IOException {
        if (closed) return;

        closed = true;

        // skip all remaining bytes, and then this subsection is "closed"
        int remaining = size - cursor;
        while (remaining > 0) {
            remaining -= in.skip(remaining);
        }
    }

    @Override
    public void mark(int readlimit) {
        int max = size - cursor;
        if (readlimit > max) {
            // silently decrease the readLimit, they wont notice anyway, since reading more will yield EOF
            readlimit = max;
        }

        markedCursor = cursor;
        in.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        in.reset();
        cursor = markedCursor;
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }
}
