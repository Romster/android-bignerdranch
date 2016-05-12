package ru.romster.bignerdranch.beatbox;

/**
 * Created by romster on 12/05/16.
 */
public class Sound {
	private String assetPath;
	private String name;

	public Sound(String assetPath) {
		this.assetPath = assetPath;
		String[] components = assetPath.split("/");
		String fileName = components[components.length - 1];
		name = fileName.replace(".wav", "");
	}

	public String getAssetPath() {
		return assetPath;
	}

	public String getName() {
		return name;
	}
}
