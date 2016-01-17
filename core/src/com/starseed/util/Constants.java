package com.starseed.util;

import com.badlogic.gdx.math.Vector2;
import com.starseed.martianrun.MConstants;

public class Constants {
	
	public static final String TITLE = "Space Seed Inc.";
	
	public static final int APP_WIDTH = 1024;
	public static final int APP_HEIGHT = 768;
	public static final float WORLD_TO_SCREEN = 8;
	public static final Vector2 WORLD_GRAVITY = new Vector2( 0, 0 );
	
	public static final float RAD_TO_DEG = 180.0f / (float)Math.PI;
	
	public static final float DELTA_MAX = 1.0f/ 30.0f;
	/*
	 * Multiplayer game constants
	 */
	
	// Screen space
	// Top UI will take 1/16 of the screen
	public static final float GAME_UI_HEIGHT = APP_WIDTH * 0.0625f;	
		
	// Physics space
	// Multiplayer game space is 128 * 84 meters
	public static final float WORLD_WIDTH = APP_WIDTH / WORLD_TO_SCREEN;
	public static final float WORLD_HEIGHT = (APP_HEIGHT - GAME_UI_HEIGHT) / WORLD_TO_SCREEN;
	public static final float WORLD_HALF_WIDTH = WORLD_WIDTH * 0.5f;
	public static final float WORLD_HALF_HEIGHT = WORLD_HEIGHT * 0.5f;

	// Border around the game field
	public static final float EDGE_WIDTH = WORLD_WIDTH * 0.0125f;
	public static final float EDGE_INNER_X_START = EDGE_WIDTH;
	public static final float EDGE_INNER_Y_START = EDGE_WIDTH;
	public static final float EDGE_INNER_X_END = WORLD_WIDTH - EDGE_WIDTH;
	public static final float EDGE_INNER_Y_END = WORLD_HEIGHT - EDGE_WIDTH;
	
	// Runners
	public static final int NUMBER_OF_RUNNERS = 10;
	
	// Ship values
	public static final float SHIP_WIDTH = 8f;
	public static final float SHIP_HEIGHT = 8f; 	
	public static final float SHIP_DENSITY = 1.5f;
	
	public static final float SHIP_ANGULAR_IMPULSE = 80;
	public static final float SHIP_ANGULAR_DAMPING = 6f;
	public static final float SHIP_ENGINE_FORCE = 10000;
	public static final float SHIP_LINEAR_DAMPING = 1f;
	
	// Asteroid values
	public static final float ASTEROID_DENSITY = 10f;
	public static final float ASTEROID_CREATION_START = 0.5f;
	public static final float ASTEROID_CREATION_END = 1.5f;	
	// Seed values
	public static final float SEED_RADIUS = 0.5f;
	public static final float SEED_DENSITY = 0.5f;
	public static final float SEED_SPEED = 125f;
	
	// Laser values
	public static final float LASER_WIDTH = 1.0f;
	public static final float LASER_HEIGHT = 0.05f;
	public static final float LASER_DENSITY = 0f;
	public static final float LASER_SPEED = SEED_SPEED * 1.75f;
	
	// Asteroid spawn values
	public static final float ASTEROID_SPAWN_DELAY_MAX = 3f;
	public static final float ASTEROID_SPAWN_DELAY_MIN = 1f;
	
	/*
	 * Texture file paths
	 */
	
	public static final String ATLAS_SHIP = "ship_and_seed.atlas";
	public static final String ATLAS_SHIP_PLAYER_REGION = "ship_player";
	public static final String ATLAS_SHIP_SEED_REGION = "seed";
	
	public static final String IMAGE_SHIP = "ship_player_%d.png";
	public static final String ATLAS_SHIP_EXHAUST = "exhaust.atlas";
	public static final String ATLAS_SHIP_EXHAUST_REGION = "exhaust";
	public static final int ATLAS_SHIP_EXHAUST_COUNT = 4;
	
	public static final String IMAGE_SEED = "seed_%d.png";
	
	public static final String ATLAS_ASTEROID = "asteroids.atlas";
	
	public static final String ATLAS_FLOWER_FORMAT = "flower_player_%d.atlas";
	public static final String ATLAS_FLOWER_REGION = "flower";
	public static final int ATLAS_FLOWER_FRAME_COUNT = 7;
	public static final int ATLAS_FLOWER_START_INDEX = 1;
	
	public static final String ATLAS_DEBRIS = "asteroid_debris.atlas";
	
	/*
	 * Fonts
	 */

    public static final String GAME_FONT_TITLE = "falconpunchcond.ttf";
    public static final String GAME_FONT = "zorque.ttf";

    /*
     * Sounds
     */
    
	public static final String MAIN_SOUND_FILE = "sounds/teru_-_Velvet_Sun.ogg";
	public static final String SOUND_ASTEROID_DESTROY = "sounds/asteroid_boom.ogg";
	public static final String[] SOUND_ASTEROID_HIT = { 
			"sounds/asteroid_hit_1.ogg", 
			"sounds/asteroid_hit_2.ogg",
			"sounds/asteroid_hit_3.ogg", 
			"sounds/asteroid_hit_4.ogg" };
	
	
	/*
	 *  MartianRun tutorial constants
	 */
	
	public static final float RUNNER_WIDTH = 1.5f;
    public static final float RUNNER_HEIGHT = 3f;  
    public static final float RUNNER_X = WORLD_WIDTH * 0.1f;
    public static final float RUNNER_Y = MConstants.GROUND_TOP + RUNNER_HEIGHT * 0.5f;  
    public static final float RUNNER_GRAVITY_SCALE = 3f;
    public static final float RUNNER_DENSITY = 0.5f;
    public static final float RUNNER_DODGE_HEIGHT = RUNNER_WIDTH * 0.5f;
    public static final float RUNNER_DODGE_X = RUNNER_X;
    public static final float RUNNER_DODGE_Y = RUNNER_Y - RUNNER_DODGE_HEIGHT;
    public static final Vector2 RUNNER_JUMPING_LINEAR_IMPULSE = new Vector2(0, RUNNER_HEIGHT * RUNNER_WIDTH * 9f);
    public static final float RUNNER_HIT_ANGULAR_IMPULSE = 10f;
    
    public static final String BACKGROUND_IMAGE_PATH = "background.png";
    public static final String GROUND_IMAGE_PATH = "ground.png";
    
    public static final String  ROCKET_BUTTON_ATLAS_PATH = "rocket_buttons.atlas";
    
    
    public static final String CHARACTERS_ATLAS_PATH = "characters.atlas";
    public static final String[] RUNNER_RUNNING_REGION_NAMES = new String[] {"alienGreen_run1", "alienGreen_run2"};
    public static final String RUNNER_DODGING_REGION_NAME = "alienGreen_dodge";
    public static final String RUNNER_HIT_REGION_NAME = "alienGreen_hit";
    public static final String RUNNER_JUMPING_REGION_NAME = "alienGreen_jump";
   
}
