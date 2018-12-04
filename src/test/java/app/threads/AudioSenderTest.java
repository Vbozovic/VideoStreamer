package app.threads;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import static org.junit.jupiter.api.Assertions.*;

class AudioSenderTest {

    @Test
    public void microphoneTest(){

        AudioSender as = new AudioSender();

        try {
            as.available();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void availableTest() throws LineUnavailableException {

    }


}