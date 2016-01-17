package com.starseed.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	public static void playSound( String soundPath ){
		playSound( soundPath, 1.0f );
	}
	
	public static void playSound( String soundPath, float volume ){
		playSound(soundPath, volume, 1.0f);
	}

	@SuppressWarnings("unused")
	public static void playSound( String soundPath, float volume, float pitch ){		
		Sound sound = Gdx.audio.newSound( Gdx.files.getFileHandle(soundPath, FileType.Internal) );
		long soundId = sound.play( volume, pitch, 0f );
	}
	
}
