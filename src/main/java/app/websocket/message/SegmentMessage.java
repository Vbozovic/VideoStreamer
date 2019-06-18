package app.websocket.message;

public class SegmentMessage {

    public int frames;
    public int length;
    public byte[] video;

    public SegmentMessage(int frames, int length, byte[] video) {
        this.frames = frames;
        this.length = length;
        this.video = video;
    }
}
