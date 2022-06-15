package com.arithfighter.not.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioHandler {
    private final SoundManager soundManager;
    private final MusicManager musicManager;

    public AudioHandler(Sound[] sounds, Music[] music) {
        soundManager = new SoundManager(sounds);

        musicManager = new MusicManager(music);
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public void setSoundVolume(float volume) {
        float soundVolume = volume / 10f;
        soundManager.setVolume(soundVolume);
    }

    public void setMusicVolume(float volume){
        float musicVolume = volume / 8f;
        musicManager.setVolume(musicVolume);
    }

    public void dispose() {
        soundManager.dispose();
        musicManager.dispose();
    }
}
