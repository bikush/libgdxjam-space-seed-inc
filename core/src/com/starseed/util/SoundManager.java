package com.starseed.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	static Sound engine1 = null;
	static Sound engine2 = null;
	
	public static long playSound( String soundPath ){
		return playSound( soundPath, 1.0f );
	}
	
	public static long playSound( String soundPath, float volume ){
		return playSound(soundPath, volume, 1.0f);
	}

	public static long playSound( String soundPath, float volume, float pitch ){		
		Sound sound = Gdx.audio.newSound( Gdx.files.getFileHandle(soundPath, FileType.Internal) );
		long soundId = sound.play( volume, pitch, 0f );
		return soundId;
	}
	
	public static void playEngine( int playerIndex ){
		if( playerIndex == 1 ){
			if( engine1 == null ){
				engine1 = Gdx.audio.newSound( Gdx.files.getFileHandle(Constants.SOUND_ENGINE_1, FileType.Internal) );
			}
			engine1.loop(0.5f);
		}else if( playerIndex == 2 ){
			if( engine2 == null ){
				engine2 = Gdx.audio.newSound( Gdx.files.getFileHandle(Constants.SOUND_ENGINE_1, FileType.Internal) );
			}
			engine2.loop(0.5f);
		}
	}
	
	public static void stopEngine( int playerIndex ){
		if( playerIndex == 1 && engine1 != null ){
			engine1.stop();
		}
		if( playerIndex == 2 && engine2 != null ){
			engine2.stop();
		}
	}
		
}
