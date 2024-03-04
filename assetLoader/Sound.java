package assetLoader;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import  java.io.*;
import java.util.Map;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/**Mithilfe des Klasse Sound kann eine Sounddatei abgespielt werden.
 */
public class Sound {
    private AudioClip clip;
    private URL url;

    /**Konstruktor, der ein neues Objekt der Klasse Sound erstellt und es initialisiert
     * 
     * @param pSoundPath Pfad der Sounddatei, die im assets/sounds/-Ordner liegt
     */
    public Sound (String pSoundPath) {
        try {
            url = this.getClass().getClassLoader().getResource("assets/sounds/" + pSoundPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler beim Laden des Sounds: " + e.getMessage());
        }
    }

    /**Spiele den Sound der Sounddatei ab, die im Konstruktor als Pfad übergeben worden
     * ist
     */
    //mit StackOverflow erstellt
    public void playSound(){
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
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

    /**Stoppe den Sound, falls er gerade abgespielt wird
     */
    //mithilfe von StackOverflow erstellt
    public void stopSound() {
        if (clip != null) {
            clip.stop();
        }
    }
}
