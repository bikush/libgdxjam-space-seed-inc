package com.starseed.martianrun;

import com.badlogic.gdx.math.Vector2;
import com.starseed.util.Constants;

public class MConstants {

	public static final float GROUND_WIDTH = Constants.WORLD_WIDTH * 1.5f;
    public static final float GROUND_HEIGHT = Constants.WORLD_HEIGHT * 0.15f;
	public static final float GROUND_X = Constants.WORLD_WIDTH * 0.5f;
    public static final float GROUND_Y = GROUND_HEIGHT * 0.5f; 
    public static final float GROUND_TOP = GROUND_Y + GROUND_HEIGHT * 0.5f;
    public static final float GROUND_DENSITY = 0f;
    
    public static final float ENEMY_X = Constants.WORLD_WIDTH + 2;
    public static final float ENEMY_DENSITY = Constants.RUNNER_DENSITY;
    public static final float RUNNING_SHORT_ENEMY_Y = GROUND_TOP;
    public static final float RUNNING_LONG_ENEMY_Y = GROUND_TOP;
    public static final float FLYING_ENEMY_Y = Constants.RUNNER_Y + Constants.RUNNER_DODGE_HEIGHT + 0.5f;
    public static final Vector2 ENEMY_LINEAR_VELOCITY = new Vector2(-10f, 0);
    
    public static final String[] RUNNING_SMALL_ENEMY_REGION_NAMES = new String[] {"ladyBug_walk1", "ladyBug_walk2"};
    public static final String[] RUNNING_LONG_ENEMY_REGION_NAMES = new String[] {"barnacle_bite1", "barnacle_bite2"};
    public static final String[] RUNNING_BIG_ENEMY_REGION_NAMES = new String[] {"spider_walk1", "spider_walk2"};
    public static final String[] RUNNING_WIDE_ENEMY_REGION_NAMES = new String[] {"worm_walk1", "worm_walk2"};
    public static final String[] FLYING_SMALL_ENEMY_REGION_NAMES = new String[] {"bee_fly1", "bee_fly2"};
    public static final String[] FLYING_WIDE_ENEMY_REGION_NAMES = new String[] {"fly_fly1", "fly_fly2"};
    
}
