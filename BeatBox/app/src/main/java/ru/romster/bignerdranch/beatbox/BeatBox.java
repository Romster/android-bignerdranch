package ru.romster.bignerdranch.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
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

	private AssetManager assetManager;

	private List<Sound> sounds = new ArrayList<>();

	public BeatBox(Context contex) {
		this.assetManager = contex.getAssets();
		loadSounds();
	}

	private void loadSounds() {
		String[] soundNames;
		try {
			soundNames = assetManager.list(SOUNDS_FOLDER);
			Log.i(TAG, "Found " + soundNames.length + " sounds");
			for(String fileName : soundNames) {
				String assetPath = SOUNDS_FOLDER + "/" + fileName;
				sounds.add(new Sound(assetPath));
			}
		} catch (IOException ex) {
			Log.e(TAG, "Could not list asserts", ex);
		}
	}

	public List<Sound> getSounds() {
		return sounds;
	}
}
