package com.github.grhscompsci2.java2DGame.actors;

public class Car extends Actor {

  private final static String leftImg = "car1_left.png";
  private final static String rightImg = "car1_right.png";

  public Car(double x, double y, double speed, double dX) {
    super(leftImg, x, y, speed, Type.enemy);
    if(dX>0){
      setImage(rightImg);
    }
    setDX(dX*speed);
    //TODO Auto-generated constructor stub
  }

  @Override
  public void hitEdge() {
    // TODO Auto-generated method stub
    die();
  }

  @Override
  public void hitActor(Actor actor) {
    // TODO Auto-generated method stub
    
  }
    
    
}
