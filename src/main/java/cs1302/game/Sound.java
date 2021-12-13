package cs1302.game;

import javafx.scene.media.AudioClip;

/**
 * Sound player class for the audio clips.
 */
public class Sound {
    private AudioClip se;

    /**
     * Creates the sound object.
     * @param filePath the filepath for the sound
     */
    public Sound(String filePath) {
        se = new AudioClip(getClass().getResource(filePath).toExternalForm());
    }

    /**
     * Plays the audioClip.
     */
    public void playAudio() {
        se.play();
    }

}
