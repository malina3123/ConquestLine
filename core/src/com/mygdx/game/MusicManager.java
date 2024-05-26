package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicManager {
    private List<Music> tracks;
    private Music currentTrack;
    private Random random;

    public MusicManager(String[] musicFiles) {
        tracks = new ArrayList<>();
        for (String file : musicFiles) {
            tracks.add(Gdx.audio.newMusic(Gdx.files.internal(file)));
        }
        random = new Random();
        playRandomTrack();
    }

    private void playRandomTrack() {
        if (currentTrack != null) {
            currentTrack.stop();
        }
        currentTrack = tracks.get(random.nextInt(tracks.size()));
        currentTrack.play();
        currentTrack.setOnCompletionListener(music -> playRandomTrack());
    }

    public void dispose() {
        for (Music track : tracks) {
            track.dispose();
        }
    }
}
