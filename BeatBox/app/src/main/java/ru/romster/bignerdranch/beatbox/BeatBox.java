package ru.romster.bignerdranch.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romster on 12/05/16.
 */
public class BeatBox {
	private static final String TAG = "BeatBox";
	private static final String SOUNDS_FOLDER = "sample_sounds";
	private static final int MAX_SOUNDS = 5;

	private AssetManager assetManager;
	private List<Sound> sounds = new ArrayList<>();
	private SoundPool soundPool;

	@SuppressWarnings("deprecation")
	public BeatBox(Context contex) {
		this.assetManager = contex.getAssets();
		soundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
		loadSounds();
	}


	public List<Sound> getSounds() {
		return sounds;
	}
	public void play(Sound sound) {
		Integer soundId = sound.getSoundId();
		if (soundId != null) {
			soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
		}
	}

	private void loadSounds() {
		String[] soundNames;
		try {
			soundNames = assetManager.list(SOUNDS_FOLDER);
			Log.i(TAG, "Found " + soundNames.length + " sounds");
			for (String fileName : soundNames) {
				String assetPath = SOUNDS_FOLDER + "/" + fileName;
				Sound s = new Sound(assetPath);
				try {
					load(s);
					sounds.add(s);
				} catch (IOException ex) {
					Log.e(TAG, "Could not load sound" + fileName, ex);
				}

			}
		} catch (IOException ex) {
			Log.e(TAG, "Could not list asserts", ex);
		}
	}

	private void load(Sound sound) throws IOException {
		AssetFileDescriptor assetFileDescriptor = assetManager.openFd(sound.getAssetPath());
		int soundId = soundPool.load(assetFileDescriptor, 1);
		sound.setSoundId(soundId);
	}

	public void release() {
		soundPool.release();
	}
}
