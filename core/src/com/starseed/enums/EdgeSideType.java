package com.starseed.enums;

import com.starseed.util.Constants;

public enum EdgeSideType {
	
	TOP( 	0, Constants.EDGE_INNER_Y_END, 								Constants.EDGE_INNER_X_END, Constants.WORLD_HEIGHT),
	LEFT(	0, 0, 														Constants.EDGE_INNER_X_START, Constants.EDGE_INNER_Y_END),
	RIGHT(	Constants.EDGE_INNER_X_END, Constants.EDGE_INNER_Y_START,	Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT),
	BOTTOM(	Constants.EDGE_INNER_X_START, 0,							Constants.WORLD_WIDTH, Constants.EDGE_INNER_Y_START);
	
	private float xStart;
    private float yStart;
    private float xEnd;
    private float yEnd;
	
	EdgeSideType( float xStart, float yStart, float xEnd, float yEnd )
	{
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public float getXStart() {
		return xStart;
	}

	public float getYStart() {
		return yStart;
	}

	public float getXEnd() {
		return xEnd;
	}

	public float getYEnd() {
		return yEnd;
	}
	
	public float getXCenter()
	{
		return (xStart + xEnd) * 0.5f;
	}
	
	public float getYCenter()
	{
		return (yStart + yEnd) * 0.5f;
	}
	
	public float getWidth()
	{
		return Math.abs( xEnd - xStart );	
	}
	
	public float getHeight()
	{
		return Math.abs( yEnd - yStart );	
	}
	
	public float getDensity()
	{
		return 0f;
	}	

}
