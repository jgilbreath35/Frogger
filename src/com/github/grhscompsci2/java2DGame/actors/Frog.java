package com.github.grhscompsci2.java2DGame.actors;

import com.github.grhscompsci2.java2DGame.Utility;

public class Frog extends Actor {
  private final static String upImg = "frog_up.png";
  private final static String downImg = "frog_down.png";
  private final static String leftImg = "frog_left.png";
  private final static String rightImg = "frog_right.png";
  private final static String deadImg="frog_dead.png";

  private boolean squashed;
  private final double HOP_DELAY=0.125;
  private double hopTime;
  public Frog() {
    super(upImg, Utility.gameWidth/2, Utility.gameHeight-8, 16, Type.player);
    squashed=false;
  }

  public void act(double deltaTime) {
    // create new changes in x and y
    double dx = 0;
    double dy = 0;
    if(!squashed){
    if (Utility.UP_ARROW) {
      setImage(upImg);
      dy = -1 * getSpeed();
    }
    if (Utility.DOWN_ARROW) {
      setImage(downImg);
      dy = getSpeed();
    }
    if (Utility.LEFT_ARROW) {
      setImage(leftImg);
      dx = -1 * getSpeed();
    }
    if (Utility.RIGHT_ARROW) {
      setImage(rightImg);
      dx = getSpeed();
    }
    
    // check where our dx and dy values will send us
    double futureX = getX() + dx;
    double futureY = getY() + dy;
    // Get half the width or height of the sprite, whichever is largest
    double max = (Math.max(getWidth(), getHeight()) / 2.0);
    // If the new position will send us off the screen, set the dx or dy to zero
    if (futureX < max || futureX > Utility.gameWidth - max) {
      dx = 0;
    }
    if (futureY < max || futureY > Utility.gameHeight - max) {
      dy = 0;
    }
  }
  else {
    setImage(deadImg);
  }
    // update the dx and dy officially
    setDX(dx);
    setDY(dy);
    
    hopTime+=deltaTime;
    if(hopTime>=HOP_DELAY){
      hopTime=0;
      setX(getX()+getDX());
      setY(getY()+getDY());
      if(squashed){
        die();
      }
    }
    //super.act(deltaTime);
  }

  @Override
  public void hitEdge() {
    // TODO Auto-generated method stub

  }

  @Override
  public void hitActor(Actor actor) {
    // TODO Auto-generated method stub
    if(actor.getType()==Type.enemy){
      squashed=true;
    }
  }
}