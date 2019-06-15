package app.image;

import org.jcodec.common.io.SeekableByteChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ArrayByteChannelReceiver implements SeekableByteChannel {

    private ByteArrayInputStream in;
    private long position;
    private boolean open;

    public ArrayByteChannelReceiver(byte[] array) {
        this.in = new ByteArrayInputStream(array);
        this.position = 0;
    }

    @Override
    public long position() throws IOException {
        System.out.println("[R]2. position");
        return position;
    }

    @Override
    public SeekableByteChannel setPosition(long newPosition) throws IOException {
        System.out.println("[R]2. set position");
        this.position = position;
        return this;
    }

    @Override
    public long size() throws IOException {
        System.out.println("[R]2. Size");
        return Long.MAX_VALUE;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        System.out.println("[R]2. Truncate");
        return this;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        //moramo da cekamo i procitamo tacno kolko se trazi
        int available = this.in.available();
        System.out.println("[R]Read "+available);
        byte[] arr = new byte[available];

        this.in.read(arr,0,available);
//        this.in.readFully(arr,0,available);
        this.position = dst.position()+arr.length;
        return available;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        System.out.println("[R]2. write");
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
