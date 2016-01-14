package com.starseed.martianrun;

public enum EnemyType {

	RUNNING_SMALL(1f, 1f, MConstants.ENEMY_X, MConstants.RUNNING_SHORT_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.RUNNING_SMALL_ENEMY_REGION_NAMES),
    RUNNING_WIDE(2f, 1f, MConstants.ENEMY_X, MConstants.RUNNING_SHORT_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.RUNNING_WIDE_ENEMY_REGION_NAMES),
    RUNNING_LONG(1f, 2f, MConstants.ENEMY_X, MConstants.RUNNING_LONG_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.RUNNING_LONG_ENEMY_REGION_NAMES),
    RUNNING_BIG(2f, 2f, MConstants.ENEMY_X, MConstants.RUNNING_LONG_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.RUNNING_BIG_ENEMY_REGION_NAMES),
    FLYING_SMALL(1f, 1f, MConstants.ENEMY_X, MConstants.FLYING_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.FLYING_SMALL_ENEMY_REGION_NAMES),
    FLYING_WIDE(2f, 1f, MConstants.ENEMY_X, MConstants.FLYING_ENEMY_Y, MConstants.ENEMY_DENSITY,
            MConstants.FLYING_WIDE_ENEMY_REGION_NAMES);

    private float width;
    private float height;
    private float x;
    private float y;
    private float density;
    private String[] regions;

    EnemyType(float width, float height, float x, float y, float density, String[] regions) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y + height * 0.5f;
        this.density = density;
        this.regions = regions;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDensity() {
        return density;
    }
    
    public String[] getRegions() {
        return regions;
    }

}