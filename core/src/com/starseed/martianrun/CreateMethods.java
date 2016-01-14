package com.starseed.martianrun;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.starseed.util.RandomUtils.RandomEnum;

public class CreateMethods {	

	public static EnemyType getRandomEnemyType() {
        RandomEnum<EnemyType> randomEnum = new RandomEnum<EnemyType>(EnemyType.class);
        return randomEnum.random();
    }

	public static Body createEnemy(World world) {
        EnemyType enemyType = getRandomEnemyType();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(enemyType.getX(), enemyType.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(enemyType.getWidth() / 2, enemyType.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, enemyType.getDensity());
        body.resetMassData();
        EnemyUserData userData = new EnemyUserData(enemyType.getWidth(), enemyType.getHeight(), enemyType.getRegions());
        body.setUserData(userData);
        shape.dispose();
        return body;
    }
	
	public static Body createGround(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(MConstants.GROUND_X, MConstants.GROUND_Y));
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(MConstants.GROUND_WIDTH / 2, MConstants.GROUND_HEIGHT / 2);
		body.createFixture(shape, MConstants.GROUND_DENSITY);
		body.setUserData( new GroundUserData(MConstants.GROUND_WIDTH, MConstants.GROUND_HEIGHT) );
		shape.dispose();
		return body;
	}
	
}
