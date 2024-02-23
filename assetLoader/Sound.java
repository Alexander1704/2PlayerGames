package assetLoader;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import  java.io.*;
import java.util.Map;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    private AudioClip clip;
    private URL url;

    public Sound (String pSoundName) {
        try {
            url = this.getClass().getClassLoader().getResource("assets/sounds/" + pSoundName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler beim Laden des Sounds: " + e.getMessage());
        }
    }

    public void playSound(){
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (clip != null) {
            clip.stop();
        }
    }

    public static void main(String[] args) {
        Sound player = new Sound("test.wav");
        player.playSound();
    }
}
