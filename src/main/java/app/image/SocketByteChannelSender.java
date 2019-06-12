package app.image;

import org.jcodec.common.io.SeekableByteChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SocketByteChannelSender implements SeekableByteChannel {


    private DataOutputStream out;
    private boolean open;
    private long position;
    private int sendInterval = 500; // milisekunde
    private long lastTime=0;

    public SocketByteChannelSender(DataOutputStream out) {
        this.out = out;
        this.open = true;
        this.position = 0;
        //this.sent = new ByteBuffer();
    }

    @Override
    public long position() throws IOException {
        return position;
    }

    @Override
    public SeekableByteChannel setPosition(long newPosition) throws IOException {
        this.position = position;
        System.out.println("set position");
        return this;
    }

    @Override
    public long size() throws IOException {
        System.out.println("Size");
        return Long.MAX_VALUE;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        System.out.println("truncate");
        return this;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        System.out.println("Read");
        return dst.array().length;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        System.out.println("Send "+src.array().length);
        this.position+= src.position() + src.array().length;
        this.out.write(src.array(),0,src.array().length);
        return src.array().length;
    }

    @Override
    public boolean isOpen() {
        System.out.println("Open");
        return this.open;
    }

    @Override
    public void close() throws IOException {
        this.open = false;
        System.out.println("close");
    }
}
