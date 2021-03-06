package Applet.Main;

import javax.sound.sampled.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int totalFramesRead = 0;
        File fileIn = new File("rec_2s.wav");
// somePathName is a pre-existing string whose value was
// based on a user selection.
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
            AudioFormat audioFormat = audioInputStream.getFormat();
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            }

            byte[] allAudioBytes = new byte[(int) audioInputStream.getFrameLength() * bytesPerFrame];
            audioInputStream.read(allAudioBytes);
            int[] samples = getSamples(allAudioBytes);
            multipleVolume(samples, 1.1);
            allAudioBytes = getBytesFromSamples(samples);

            writeTxtFile(allAudioBytes, "Mult.txt");

            try {
                Clip clip = AudioSystem.getClip();
                clip.open(audioFormat, allAudioBytes, 0, (int) audioInputStream.getFrameLength() * bytesPerFrame);
                // clip.setFramePosition(0);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);

                System.out.println("Frames: " + audioInputStream.getFrameLength());
                System.out.println("Channels: " + audioInputStream.getFormat().getChannels());
                System.out.println("Frame Size: " + audioInputStream.getFormat().getFrameSize());
                System.out.println("Frame Rate: " + audioInputStream.getFormat().getFrameRate());
                System.out.println("Sample Rate: " + audioInputStream.getFormat().getSampleRate());
                System.out.println("SampleSizeInBits: " + audioInputStream.getFormat().getSampleSizeInBits());
                System.out.println("Total frames read = " + totalFramesRead);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] getBytesFromSamples(int[] samples) {
        byte[] allAudioBytes = new byte[samples.length * 4];
        int k = 0;
        for (int i = 0; i < samples.length; i++) {
            allAudioBytes[k] = (byte) (samples[i] >> 24);
            k++;
            allAudioBytes[k] = (byte) (samples[i] >> 16);
            k++;
            allAudioBytes[k] = (byte) (samples[i] >> 8);
            k++;
            allAudioBytes[k] = (byte) (samples[i]);
            k++;
        }
        return allAudioBytes;
    }

    public static void multipleVolume(int[] samples, double x) {
        for (int i = 0; i < samples.length; i++) {
            samples[i] *= x;
        }
    }

    //    public static int[] getSamples(byte[] allAudioBytes) {
//        int[] samples = new int[allAudioBytes.length/2];
//        int k = 0;
//        for (int i = 0; i < allAudioBytes.length; i++) {
//            if (i % 2 == 0 && i != 0) {
//                int temp = allAudioBytes[i - 1];
//                temp = temp << 8;
//                temp += allAudioBytes[i];
//                samples[k] = temp;
//                k++;
//            }
//        }
//        return samples;
//    }
    public static int[] getSamples(byte[] allAudioBytes) {
        int[] samples = new int[allAudioBytes.length / 4];
        int k = 0;
        for (int i = 0; i < allAudioBytes.length; i++) {
            if (i % 4 == 0) {
                int temp = allAudioBytes[i];
                temp = temp << 8;
                temp += allAudioBytes[i + 1];
                temp <<= 8;
                temp += allAudioBytes[i + 2];
                temp <<= 8;
                temp += allAudioBytes[i + 3];
                samples[k] = temp;
                k++;
            }
        }
        return samples;
    }

    static void writeTxtFile(byte[] bytes, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("hello world");
            for (int i = 0; i < bytes.length; i++) {
                bw.write(((Byte) (bytes[i])).toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
