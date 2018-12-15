package app.streaming;

import io.humble.video.*;
import io.humble.video.awt.ImageFrame;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import io.humble.video.javaxsound.AudioFrame;
import io.humble.video.javaxsound.MediaAudioConverter;
import io.humble.video.javaxsound.MediaAudioConverterFactory;
import org.junit.jupiter.api.Test;

import io.humble.video.Decoder;
import io.humble.video.Demuxer;
import io.humble.video.DemuxerStream;
import io.humble.video.Global;
import io.humble.video.Media;
import io.humble.video.MediaDescriptor;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Rational;
import io.humble.video.awt.ImageFrame;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;


import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

public class HumbleTest {

    @Test
    void playSound() throws IOException, InterruptedException, LineUnavailableException {

        /*
         * Start by creating a container object, in this case a demuxer since
         * we are reading, to get audio data from.
         */
        Demuxer demuxer = Demuxer.make();

        String filename = "C:\\Users\\Vuk\\Desktop\\Diplomskiv2\\resources\\test.mp3";

        /*
         * Open the demuxer with the filename passed on.
         */
        demuxer.open(filename, null, false, true, null, null);


        /*
         * Query how many streams the call to open found
         */
        int numStreams = demuxer.getNumStreams();

        /*
         * Iterate through the streams to find the first audio stream
         */
        int audioStreamId = -1;
        Decoder audioDecoder = null;
        for(int i = 0; i < numStreams; i++)
        {
            final DemuxerStream stream = demuxer.getStream(i);
            final Decoder decoder = stream.getDecoder();
            if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_AUDIO) {
                audioStreamId = i;
                audioDecoder = decoder;
                // stop at the first one.
                break;
            }
        }
        if (audioStreamId == -1)
            throw new RuntimeException("could not find audio stream in container: "+filename);

        /*
         * Now we have found the audio stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        audioDecoder.open(null, null);

        /*
         * We allocate a set of samples with the same number of channels as the
         * coder tells us is in this buffer.
         */
        final MediaAudio samples = MediaAudio.make(
                audioDecoder.getFrameSize(),
                audioDecoder.getSampleRate(),
                audioDecoder.getChannels(),
                audioDecoder.getChannelLayout(),
                audioDecoder.getSampleFormat());

        /*
         * A converter object we'll use to convert Humble Audio to a format that
         * Java Audio can actually play. The details are complicated, but essentially
         * this converts any audio format (represented in the samples object) into
         * a default audio format suitable for Java's speaker system (which will
         * be signed 16-bit audio, stereo (2-channels), resampled to 22,050 samples
         * per second).
         */

        final MediaAudioConverter converter =
                MediaAudioConverterFactory.createConverter(
                        MediaAudioConverterFactory.DEFAULT_JAVA_AUDIO,
                        samples);

        /*
         * An AudioFrame is a wrapper for the Java Sound system that abstracts away
         * some stuff. Go read the source code if you want -- it's not very complicated.
         */
        final AudioFrame audioFrame = AudioFrame.make(converter.getJavaFormat());
        if (audioFrame == null)
            throw new LineUnavailableException();

        /* We will use this to cache the raw-audio we pass to and from
         * the java sound system.
         */
        ByteBuffer rawAudio = null;

        /*
         * Now, we start walking through the container looking at each packet. This
         * is a decoding loop, and as you work with Humble you'll write a lot
         * of these.
         *
         * Notice how in this loop we reuse all of our objects to avoid
         * reallocating them. Each call to Humble resets objects to avoid
         * unnecessary reallocation.
         */
        final MediaPacket packet = MediaPacket.make();
        while(demuxer.read(packet) >= 0) {
            /*
             * Now we have a packet, let's see if it belongs to our audio stream
             */
            if (packet.getStreamIndex() == audioStreamId)
            {
                /*
                 * A packet can actually contain multiple sets of samples (or frames of samples
                 * in audio-decoding speak).  So, we may need to call decode audio multiple
                 * times at different offsets in the packet's data.  We capture that here.
                 */
                int offset = 0;
                int bytesRead = 0;
                do {
                    bytesRead += audioDecoder.decode(samples, packet, offset);
                    if (samples.isComplete()) {
                        rawAudio = converter.toJavaAudio(rawAudio, samples);
                        audioFrame.play(rawAudio);
                    }
                    offset += bytesRead;
                } while (offset < packet.getSize());
            }
        }

        // Some audio decoders (especially advanced ones) will cache
        // audio data before they begin decoding, so when you are done you need
        // to flush them. The convention to flush Encoders or Decoders in Humble Video
        // is to keep passing in null until incomplete samples or packets are returned.
        do {
            audioDecoder.decode(samples, null, 0);
            if (samples.isComplete()) {
                rawAudio = converter.toJavaAudio(rawAudio, samples);
                audioFrame.play(rawAudio);
            }
        } while (samples.isComplete());

        // It is good practice to close demuxers when you're done to free
        // up file handles. Humble will EVENTUALLY detect if nothing else
        // references this demuxer and close it then, but get in the habit
        // of cleaning up after yourself, and your future girlfriend/boyfriend
        // will appreciate it.
        demuxer.close();

        // similar with the demuxer, for the audio playback stuff, clean up after yourself.
        audioFrame.dispose();

    }

    @Test
    public void playVideo() throws IOException, InterruptedException {

        String filename = null;

        /*
         * Start by creating a container object, in this case a demuxer since
         * we are reading, to get video data from.
         */
        Demuxer demuxer = Demuxer.make();

        /*
         * Open the demuxer with the filename passed on.
         */
        demuxer.open(filename, null, false, true, null, null);

        /*
         * Query how many streams the call to open found
         */
        int numStreams = demuxer.getNumStreams();

        /*
         * Iterate through the streams to find the first video stream
         */
        int videoStreamId = -1;
        long streamStartTime = Global.NO_PTS;
        Decoder videoDecoder = null;
        for(int i = 0; i < numStreams; i++)
        {
            final DemuxerStream stream = demuxer.getStream(i);
            streamStartTime = stream.getStartTime();
            final Decoder decoder = stream.getDecoder();
            if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_VIDEO) {
                videoStreamId = i;
                videoDecoder = decoder;
                // stop at the first one.
                break;
            }
        }
        if (videoStreamId == -1)
            throw new RuntimeException("could not find video stream in container: "+filename);

        /*
         * Now we have found the audio stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        videoDecoder.open(null, null);

        final MediaPicture picture = MediaPicture.make(
                videoDecoder.getWidth(),
                videoDecoder.getHeight(),
                videoDecoder.getPixelFormat());

        /** A converter object we'll use to convert the picture in the video to a BGR_24 format that Java Swing
         * can work with. You can still access the data directly in the MediaPicture if you prefer, but this
         * abstracts away from this demo most of that byte-conversion work. Go read the source code for the
         * converters if you're a glutton for punishment.
         */
        final MediaPictureConverter converter =
                MediaPictureConverterFactory.createConverter(
                        MediaPictureConverterFactory.HUMBLE_BGR_24,
                        picture);
        BufferedImage image = null;

        /**
         * This is the Window we will display in. See the code for this if you're curious, but to keep this demo clean
         * we're 'simplifying' Java AWT UI updating code. This method just creates a single window on the UI thread, and blocks
         * until it is displayed.
         */
        final ImageFrame window = ImageFrame.make();
        if (window == null) {
            throw new RuntimeException("Attempting this demo on a headless machine, and that will not work. Sad day for you.");
        }

        /**
         * Media playback, like comedy, is all about timing. Here we're going to introduce <b>very very basic</b>
         * timing. This code is deliberately kept simple (i.e. doesn't worry about A/V drift, garbage collection pause time, etc.)
         * because that will quickly make things more complicated.
         *
         * But the basic idea is there are two clocks:
         * <ul>
         * <li>Player Clock: The time that the player sees (relative to the system clock).</li>
         * <li>Stream Clock: Each stream has its own clock, and the ticks are measured in units of time-bases</li>
         * </ul>
         *
         * And we need to convert between the two units of time. Each MediaPicture and MediaAudio object have associated
         * time stamps, and much of the complexity in video players goes into making sure the right picture (or sound) is
         * seen (or heard) at the right time. This is actually very tricky and many folks get it wrong -- watch enough
         * Netflix and you'll see what I mean -- audio and video slightly out of sync. But for this demo, we're erring for
         * 'simplicity' of code, not correctness. It is beyond the scope of this demo to make a full fledged video player.
         */

        // Calculate the time BEFORE we start playing.
        long systemStartTime = System.nanoTime();
        // Set units for the system time, which because we used System.nanoTime will be in nanoseconds.
        final Rational systemTimeBase = Rational.make(1, 1000000000);
        // All the MediaPicture objects decoded from the videoDecoder will share this timebase.
        final Rational streamTimebase = videoDecoder.getTimeBase();

        /**
         * Now, we start walking through the container looking at each packet. This
         * is a decoding loop, and as you work with Humble you'll write a lot
         * of these.
         *
         * Notice how in this loop we reuse all of our objects to avoid
         * reallocating them. Each call to Humble resets objects to avoid
         * unnecessary reallocation.
         */
        final MediaPacket packet = MediaPacket.make();
        while(demuxer.read(packet) >= 0) {
            /**
             * Now we have a packet, let's see if it belongs to our video stream
             */
            if (packet.getStreamIndex() == videoStreamId)
            {
                /**
                 * A packet can actually contain multiple sets of samples (or frames of samples
                 * in decoding speak).  So, we may need to call decode  multiple
                 * times at different offsets in the packet's data.  We capture that here.
                 */
                int offset = 0;
                int bytesRead = 0;
                do {
                    bytesRead += videoDecoder.decode(picture, packet, offset);
                    if (picture.isComplete()) {
                        image = displayVideoAtCorrectTime(streamStartTime, picture,
                                converter, image, window, systemStartTime, systemTimeBase,
                                streamTimebase);
                    }
                    offset += bytesRead;
                } while (offset < packet.getSize());
            }
        }

        // Some video decoders (especially advanced ones) will cache
        // video data before they begin decoding, so when you are done you need
        // to flush them. The convention to flush Encoders or Decoders in Humble Video
        // is to keep passing in null until incomplete samples or packets are returned.
        do {
            videoDecoder.decode(picture, null, 0);
            if (picture.isComplete()) {
                image = displayVideoAtCorrectTime(streamStartTime, picture, converter,
                        image, window, systemStartTime, systemTimeBase, streamTimebase);
            }
        } while (picture.isComplete());

        // It is good practice to close demuxers when you're done to free
        // up file handles. Humble will EVENTUALLY detect if nothing else
        // references this demuxer and close it then, but get in the habit
        // of cleaning up after yourself, and your future girlfriend/boyfriend
        // will appreciate it.
        demuxer.close();

        // similar with the demuxer, for the windowing system, clean up after yourself.
        window.dispose();
    }

    /**
     * Takes the video picture and displays it at the right time.
     */
    private static BufferedImage displayVideoAtCorrectTime(long streamStartTime,
                                                           final MediaPicture picture, final MediaPictureConverter converter,
                                                           BufferedImage image, final ImageFrame window, long systemStartTime,
                                                           final Rational systemTimeBase, final Rational streamTimebase)
            throws InterruptedException {
        long streamTimestamp = picture.getTimeStamp();
        // convert streamTimestamp into system units (i.e. nano-seconds)
        streamTimestamp = systemTimeBase.rescale(streamTimestamp-streamStartTime, streamTimebase);
        // get the current clock time, with our most accurate clock
        long systemTimestamp = System.nanoTime();
        // loop in a sleeping loop until we're within 1 ms of the time for that video frame.
        // a real video player needs to be much more sophisticated than this.
        while (streamTimestamp > (systemTimestamp - systemStartTime + 1000000)) {
            Thread.sleep(1);
            systemTimestamp = System.nanoTime();
        }
        // finally, convert the image from Humble format into Java images.
        image = converter.toImage(image, picture);
        // And ask the UI thread to repaint with the new image.
        window.setImage(image);
        return image;
    }



}
