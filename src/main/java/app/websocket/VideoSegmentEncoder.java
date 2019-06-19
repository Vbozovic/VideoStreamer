package app.websocket;

import app.websocket.message.SegmentMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

public class VideoSegmentEncoder implements Encoder.Binary<SegmentMessage> {
    @Override
    public ByteBuffer encode(SegmentMessage segmentMessage) throws EncodeException {
        System.out.println("Encode segment");
        ByteBuffer buff = ByteBuffer.allocate(segmentMessage.length+4+4);
        buff.putInt(segmentMessage.frames);
        buff.putInt(segmentMessage.length);
        buff.put(segmentMessage.video);
        return buff;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
