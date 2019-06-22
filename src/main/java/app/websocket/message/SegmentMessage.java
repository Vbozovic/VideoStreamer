package app.websocket.message;

public class SegmentMessage {

    public int frames;
    public int length;
    public String video;

    public SegmentMessage(int frames, int length, String video) {
        this.frames = frames;
        this.length = length;
        this.video = video;
    }

}
