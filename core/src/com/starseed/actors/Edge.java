package com.starseed.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.EdgeUserData;
import com.starseed.box2d.UserData;

public class Edge extends GameActor {

	public Edge(Body body) {
		super(body);
	}

	@Override
	public UserData getUserData() {
		return (EdgeUserData) userData;
	}

}
