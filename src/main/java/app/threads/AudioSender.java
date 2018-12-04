package app.threads;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioSender {


    private AudioFormat format;
    private TargetDataLine microphone;

    public AudioSender() {

        format = new AudioFormat(8000.0f,16,1,true,true);
        try {
            microphone = AudioSystem.getTargetDataLine(this.format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    public AudioSender(TargetDataLine microphone) {
        this.microphone = microphone;
    }


    public static void availableInput() throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info: mixerInfos) {
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                System.out.println(info.getName() + "---" + lineInfo);
                Line line = m.getLine(lineInfo);
                //System.out.println("\t-----" + line);
            }
        }
    }

    public static Mixer getNamed(String name){
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for(Mixer.Info info:mixerInfos){
            if(info.getName().equals(name)){
                return AudioSystem.getMixer(info);
            }
        }
        return null;
    }

    public static void available() throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info: mixerInfos){
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();
            for (Line.Info lineInfo:lineInfos){
                System.out.println (info.getName()+"---"+lineInfo);
                Line line = m.getLine(lineInfo);
                System.out.println("\t-----"+line);
            }
            lineInfos = m.getTargetLineInfo();
            for (Line.Info lineInfo:lineInfos){
                System.out.println (m+"---"+lineInfo);
                Line line = m.getLine(lineInfo);
                System.out.println("\t-----"+line);
            }

        }
    }

    public void sendAudio(){


        while(true){
            int numBytesRead;
            byte[] data = new byte[microphone.getBufferSize()/5];
            microphone.start();

            while(true){
                numBytesRead = microphone.read(data,0,data.length);
                System.out.println(Arrays.toString(data));
            }

        }
    }


}
