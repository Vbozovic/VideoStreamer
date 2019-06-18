package app.websocket;

import app.websocket.message.SegmentMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

public class VideoSegnemtDecoder implements Decoder.Binary<SegmentMessage> {
    @Override
    public SegmentMessage decode(ByteBuffer byteBuffer) throws DecodeException {
        int frames = byteBuffer.getInt();
        int length = byteBuffer.getInt();
        byte[] video = new byte[length];
        byteBuffer.get(video);
        return new SegmentMessage(frames,length,video);
    }

    @Override
    public boolean willDecode(ByteBuffer byteBuffer) {
        return byteBuffer != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
