package com.starseed.util;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.starseed.box2d.*;
import com.starseed.enums.AsteroidType;
import com.starseed.enums.EdgeSideType;
import com.starseed.enums.UserDataType;

public class WorldUtils {
	
	public static World createWorld() {
		return new World(Constants.WORLD_GRAVITY, true);
	}
	
	public static Body createEdge( World world, EdgeSideType type )
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set( new Vector2( type.getXCenter(), type.getYCenter() ) );
				
		PolygonShape shape = new PolygonShape();
		shape.setAsBox( type.getWidth() / 2, type.getHeight() / 2);
		
		Body body = world.createBody( bodyDef );
		body.createFixture( shape, type.getDensity() );
		body.setUserData( new EdgeUserData( type.getWidth(), type.getHeight() ) );
		
		shape.dispose();
		return body;
	}
	
	public static Body createPlayerShip(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angle = (float)Math.PI * 1.5f;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.SHIP_WIDTH / 2, Constants.SHIP_HEIGHT / 2);
        Vector2[] triangle = new Vector2[] { 
        		new Vector2( -Constants.SHIP_WIDTH * 0.3f, -Constants.SHIP_HEIGHT * 0.41f ), 
        		new Vector2(  Constants.SHIP_WIDTH * 0.3f, -Constants.SHIP_HEIGHT * 0.41f ),
        		new Vector2(  Constants.SHIP_WIDTH * 0.05f, Constants.SHIP_HEIGHT * 0.47f ),
        		new Vector2( -Constants.SHIP_WIDTH * 0.05f, Constants.SHIP_HEIGHT * 0.47f ) };
        shape.set( triangle );
        
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.SHIP_DENSITY);
        body.resetMassData();
        body.setUserData(new ShipUserData( Constants.SHIP_WIDTH, Constants.SHIP_WIDTH ));
        body.setAngularDamping(Constants.SHIP_ANGULAR_DAMPING);
        body.setLinearDamping(Constants.SHIP_LINEAR_DAMPING);
        
        shape.dispose();
        return body;
    }
	
	private static float angle = 0;
	public static Body createAsteroid(World world, AsteroidType aType, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
       
        bodyDef.angle = angle;
        angle += 3.1415926f * 0.5f;
        
        CircleShape shape = new CircleShape();
        shape.setRadius(aType.getRadius());
                
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.ASTEROID_DENSITY);
        body.resetMassData();
        body.setUserData(new AsteroidUserData( aType.getRadius() * 2, aType.getRadius() * 2, aType ));
        body.setAngularVelocity( (new Random()).nextFloat() * 5f - 2.5f );
        shape.dispose();
        return body;
    }
		
	public static Body createRunner(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.setGravityScale(Constants.RUNNER_GRAVITY_SCALE);
        body.resetMassData();
        body.setUserData(new RunnerUserData( Constants.RUNNER_WIDTH, Constants.RUNNER_HEIGHT ));
        shape.dispose();
        return body;
    }
	
	public static Body createSeed(World world, Vector2 position, Vector2 direction) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
				
		CircleShape shape = new CircleShape();
		shape.setRadius( Constants.SEED_RADIUS );
		
		Body body = world.createBody(bodyDef);
		body.createFixture(shape, Constants.SEED_DENSITY);
		body.resetMassData();
		body.setUserData(new UserData( Constants.SEED_RADIUS * 2f, Constants.SEED_RADIUS * 2f, UserDataType.SEED ));
		
		Vector2 exitSpeed = new Vector2(direction);
		exitSpeed.scl( Constants.SEED_SPEED );
		body.setLinearVelocity( exitSpeed );
		
		shape.dispose();
		
		return body;
	}
	
}
