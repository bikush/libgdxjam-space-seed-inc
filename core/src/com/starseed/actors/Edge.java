package com.starseed.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.starseed.box2d.EdgeUserData;

public class Edge extends GameActor {

	public Edge(Body body) {
		super(body);
	}

	@Override
	public EdgeUserData getUserData() {
		return (EdgeUserData) userData;
	}

}
