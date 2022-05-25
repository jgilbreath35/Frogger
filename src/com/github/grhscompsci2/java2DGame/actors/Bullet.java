package com.github.grhscompsci2.java2DGame.actors;

/**
 * This is an example bullet class that extends the Actor class. It only needs
 * to set the image, position and speed of the actor. The rest is handled in the
 * Board class.
 */
public class Bullet extends Actor {
  public Bullet(double bulX, double bulY, double vX, double vY) {
    super("bullet.png", bulX, bulY, 100, Type.bullet);
    setDX(vX * getSpeed());
    setDY(vY * getSpeed());
  }

  /**
   * This method will remove bullets that have left the board area.
   */
  @Override
  public void hitEdge() {
    // TODO Auto-generated method stub
    die();
  }

  /**
   * This method will remove bullets that have collided with another actor
   */
  @Override
  public void hitActor(Actor actor) {
    //if we did not hit another bullet
    if (actor.getType() != getType()&&actor.getType()!=Type.player) {
      die();
    }
  }
}
