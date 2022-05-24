package com.github.grhscompsci2.java2DGame.actors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.grhscompsci2.java2DGame.Utility;

/**
 * This is the base class for anything that needs to move/act on the screen. You
 * should write classes that extend the actor class to make your game work. The
 * player class, enemy classes, bullet classes, and obstacle classes should all
 * extend the Actor class.
 */
public abstract class Actor {
  private double dx;
  private double dy;
  private double x;
  private double y;

  private BufferedImage sprite;
  private String fileName;
  private double speed;

  private boolean isDead;

  // Store what type of actor this is
  public static enum Type {
    player, enemy, bullet, obstacle
  };

  private Type type;

  /**
   * Constructor that will set the image to the fileName and the position to the
   * provided coordinates, and set the speed and type of the actor.
   * 
   * @param fileName the relative location of the file ("images/filename.png")
   * @param x        the x coordinate of the sprite
   * @param y        the y coordinate of the sprite
   * @param speed    the speed of the sprite, in scaled pixels
   * @param type     the type of actor
   */
  public Actor(String fileName, double x, double y, double speed, Type type) {
    this.fileName = fileName;
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.type = type;
    this.dx = 0;
    this.dy = 0;
    this.isDead = false;
    loadImage();
  }

  /**
   * Using the fileName attribute, load the image into the sprite attribute
   */
  private void loadImage() {
    // Load the image
    try {
      this.sprite = ImageIO.read(Utility.getImageURL(fileName));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the position based on the change in x and y attributes
   * 
   * @param deltaTime the number of seconds since the last tick
   */
  public void act(double deltaTime) {
    x += dx * deltaTime;
    y += dy * deltaTime;
  }

  /**
   * Draws the sprite centered at the x, y location. Adapts to scale of the
   * JFrame.
   * 
   * @param g             Graphics2D object to draw the image
   * @param imageObserver the JPanel where the sprite will be drawn
   */
  public void draw(Graphics2D g, ImageObserver imageObserver) {
    double offsetX = x - sprite.getWidth() / 2;
    double offsetY = y - sprite.getHeight() / 2;
    g.drawImage(sprite, Utility.scale(offsetX), Utility.scale(offsetY), Utility.scale(sprite.getWidth()),
        Utility.scale(sprite.getHeight()), imageObserver);
    g.setColor(Color.CYAN);
    g.drawRect(Utility.scale(getBounds().x), Utility.scale(getBounds().y), Utility.scale(getBounds().width),
        Utility.scale(getBounds().height));
  }

  /**
   * Returns the x attribute
   * 
   * @return the x attribute
   */
  public double getX() {
    return x;
  }

  /**
   * Sets the x attribute
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Returns the y attribute
   * 
   * @return the y attribute
   */
  public double getY() {
    return y;
  }

  /**
   * Sets the y attribute
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Returns the width of the sprite
   * 
   * @return the width attribute.
   */
  public int getWidth() {
    return sprite.getWidth();
  }

  /**
   * Returns the height of the sprite
   * 
   * @return the height attribute
   */
  public int getHeight() {
    return sprite.getHeight();
  }

  /**
   * Returns the sprite
   * 
   * @return the sprite attribute
   */
  public BufferedImage getImage() {
    return sprite;
  }

  /**
   * Returns the file name
   * 
   * @return the fileName attribute
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Update the image for the actor sprite. Useful for animations.
   * 
   * @param fileName the relative location of the file ("images/filename.png")
   */
  public void setImage(String fileName) {
    this.fileName = fileName;
    loadImage();
  }

  /**
   * Returns the dx
   * 
   * @return the dx attribute
   */
  public double getDX() {
    return dx;
  }

  /**
   * Updates the value for dx
   * 
   * @param dx2 the new value for dx
   */
  public void setDX(double dx2) {
    this.dx = dx2;
  }

  /**
   * Returns the dy
   * 
   * @return the attribute dy
   */
  public double getDY() {
    return dy;
  }

  /**
   * Updates the value of dy
   * 
   * @param dy2 the new value for dy
   */
  public void setDY(double dy2) {
    this.dy = dy2;
  }

  /**
   * Returns the speed
   * 
   * @return the attribute speed
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * Updates the value of speed
   * 
   * @param speed the new value for speed
   */
  public void setSpeed(int speed) {
    this.speed = speed;
  }

  /**
   * Returns the bounds of the sprite. Used for collision dectection.
   * 
   * @return a rectangle in the position and size of the sprite
   */
  public Rectangle getBounds() {
    return new Rectangle((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getWidth() / 2), sprite.getWidth(),
        sprite.getHeight());
  }

  /**
   * When the actor needs to be removed from the castAndCrew array list
   */
  public void die() {
    isDead = true;
  }

  /**
   * This method is called during collision dection when the actor bounds have hit
   * the board bounds
   */
  public abstract void hitEdge();

  /**
   * This method is called during collision detection when the actor bounds have
   * intersected with another actor's bounds
   * 
   * @param actor the collided actor
   */
  public abstract void hitActor(Actor actor);

  /**
   * Gets the life or death status of the actor
   * 
   * @return isDead attribute
   */
  public boolean isDead() {
    return isDead;
  }

  /**
   * Gets the type of the actor
   * 
   * @return the type attribute
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type of the actor
   * 
   * @param type the new actor type
   */
  public void setType(Type type) {
    this.type = type;
  }
}
