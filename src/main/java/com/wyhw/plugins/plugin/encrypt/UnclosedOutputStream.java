package com.wyhw.plugins.plugin.encrypt;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 不关闭的输出流
 *
 * @author wanyanhw
 * @since 2021/12/31 10:44
 */
public class UnclosedOutputStream extends OutputStream {
    private final OutputStream out;

    public UnclosedOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() {
    }
}
