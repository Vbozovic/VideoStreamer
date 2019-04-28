package app.image;

import org.jcodec.common.io.SeekableByteChannel;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SocketByteChannelReader implements SeekableByteChannel {

    private DataInputStream in;
    private long position;
    private boolean open;

    public SocketByteChannelReader(DataInputStream in) {
        this.in = in;
        this.position = 0;
        this.open = true;
    }

    @Override
    public long position() throws IOException {
        System.out.println("2. position");
        return position;
    }

    @Override
    public SeekableByteChannel setPosition(long newPosition) throws IOException {
        System.out.println("2. set position");
        this.position = position;
        return this;
    }

    @Override
    public long size() throws IOException {
        System.out.println("2. Size");
        return Long.MAX_VALUE;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        System.out.println("2. Truncate");
        return this;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        System.out.println("2. Read");
        return 0;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        System.out.println("2. write");
        return 0;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void close() throws IOException {
        this.open = false;
    }
}
