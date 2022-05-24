package com.github.grhscompsci2.java2DGame;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.grhscompsci2.java2DGame.actors.Actor;
import com.github.grhscompsci2.java2DGame.actors.Astronaut;
import com.github.grhscompsci2.java2DGame.actors.Car;
import com.github.grhscompsci2.java2DGame.actors.Frog;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;

/**
 * This class controls the JPanel where the game logic and rendering happen. You
 * will need to edit the attributes to add your enemies and player classes so
 * they can draw and act here.
 */
public class Board extends JPanel {
  private final String BACKGROUND_FILE_NAME = "background.png";
  // This value would probably be stored elsewhere.
  final double GAME_HERTZ = 30.0;
  // Calculate how many ns each frame should take for our target game hertz.
  final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
  // At the very most we will update the game this many times before a new render.
  // If you're worried about visual hitches more than perfect timing, set this to
  // 1.
  final int MAX_UPDATES_BEFORE_RENDER = 5;
  // If we are able to get as high as this FPS, don't render again.
  final double TARGET_FPS = 60;
  final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
  private BufferedImage background;
  private int frameCount = 0;
  private int fps = 0;
  public boolean running = false;
  private double carTime;
  public static boolean paused = false;

  /**
   * Initialize the board
   */
  public Board() {
    // load the background image
    loadBackground();
    // set the size of the panel to the size of the background
    setPreferredSize(getPreferredSize());
    // Update the scale based on the size of the JPanel
    Utility.updateScale(new Dimension(background.getWidth(), background.getHeight()), getSize());
    // Allow us to focus on this JPanel
    setFocusable(true);
    // Initialize all actors below here
    initBoard();
  }

  /**
   * This method will initialize the key listener, load the background image, set
   * the window size to the size of the image, and initialize an actor. When you
   * are modifying this for your game, you should initialize all of your actors
   * that need to be used in the game.
   */
  private void initBoard() {
    // Initialize all of your actors here: players, enemies, obstacles, etc.
    Utility.castAndCrew.add(new Frog());
    carTime=0;
  }

  /**
   * Returns the preferred size of the background. Used to set the starting size
   * of the JPanel window.
   */
  @Override
  public Dimension getPreferredSize() {
    // if there is no image, give a default size
    if (background == null) {
      return new Dimension(400, 300);
    }
    // give a size based on the background image
    return new Dimension(background.getWidth(), background.getHeight());
  }

  /**
   * This method will assign the BACKGROUND_FILE_NAME as the background of the
   * JPanel. The background.png file will determine the resolution of your screen.
   * All of your other sprites should match the resolution of the background.
   */
  private void loadBackground() {
    // Load the image
    try {
      this.background = ImageIO.read(Utility.getImageURL(BACKGROUND_FILE_NAME));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method will draw everything in JPanel. It will update the scale so when
   * the JFrame is resized, the image will resize to maintain the aspect ratio.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Graphics 2D offers greater control than Graphics, so use it instead.
    Graphics2D g2d = (Graphics2D) g;
    // Update the scale based on the size of the JPanel
    Utility.updateScale(new Dimension(background.getWidth(), background.getHeight()), getSize());

    // Always draw this first so it will be on the bottom
    g2d.drawImage(background, 0, 0, Utility.scale(background.getWidth()), Utility.scale(background.getHeight()), this);

    ArrayList<Actor> paintActors = new ArrayList<>();
    paintActors.addAll(Utility.castAndCrew);
    // call other drawing stuff here
    for (Actor actor : paintActors) {
      if (!actor.isDead()) {
        actor.draw(g2d, this);
      }
    }

    // g2d.setColor(Color.BLACK);
    // g2d.drawString("FPS: " + fps, 5, 10);

    frameCount++;
  }

  // Only run this in another Thread!
  public void gameLoop() {
    // We will need the last update time.
    double lastUpdateTime = System.nanoTime();
    // Store the last time we rendered.
    double lastRenderTime = System.nanoTime();

    // Simple way of finding FPS.
    int lastSecondTime = (int) (lastUpdateTime / 1000000000);

    while (running) {
      double now = System.nanoTime();
      int updateCount = 0;

      if (!paused) {
        // Do as many game updates as we need to, potentially playing catchup.
        while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
          // convert nanoseconds to seconds
          updateGame(TIME_BETWEEN_UPDATES / 1000000000);
          lastUpdateTime += TIME_BETWEEN_UPDATES;
          updateCount++;
        }

        // If for some reason an update takes forever, we don't want to do an insane
        // number of catchups.
        // If you were doing some sort of game that needed to keep EXACT time, you would
        // get rid of this.
        if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
          lastUpdateTime = now - TIME_BETWEEN_UPDATES;
        }

        drawGame();
        lastRenderTime = now;

        // Update the frames we got.
        int thisSecond = (int) (lastUpdateTime / 1000000000);
        if (thisSecond > lastSecondTime) {
          // System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
          fps = frameCount;
          frameCount = 0;
          lastSecondTime = thisSecond;
        }

        // Yield until it has been at least the target time between renders. This saves
        // the CPU from hogging.
        while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
          // allow the threading system to play threads that are waiting to run.
          Thread.yield();

          // This stops the app from consuming all your CPU. It makes this slightly less
          // accurate, but is worth it.
          // You can remove this line and it will still work (better), your CPU just
          // climbs on certain OSes.
          // FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a
          // look at different peoples' solutions to this.
          // On my OS it does not unpuase the game if i take this away
          try {
            Thread.sleep(1);
          } catch (Exception e) {
          }

          now = System.nanoTime();
        }
      }
    }
  }

  /**
   * This method is called once per frame. This will allow us to advance the game
   * logic every frame so the actors move and react to input.
   * 
   * @param deltaTime the time elapsed since last tick
   */
  private void updateGame(double deltaTime) {
    // Check for collisions between actors. Do it before they act so you can handle
    // death and other cases appropriately
    checkCollisions();
    Utility.clearDead();
    Utility.addNew();
    // Have all of your actor attributes act here.
    for (Actor actor : Utility.castAndCrew) {
      actor.act(deltaTime);
    }
    // Add random cars!
    carTime+=deltaTime;
    if (carTime >=2) {
      generateRandomCar();
      carTime=0;
    }
  }

  private void generateRandomCar() {
    int min = Utility.gameHeight - 8 - (6 * 16);
    int lane = (int) (Math.random() * 5);

    switch (lane) {
      case 0:
        Utility.addActor(new Car(8, min + lane * 16, 30, 1));
        break;
      case 1:
        Utility.addActor(new Car(Utility.gameWidth-8, min + lane * 16, 30, -1));
        break;
      case 2:
        Utility.addActor(new Car(8, min + lane * 16, 30, 1));
        break;
      case 3:
        Utility.addActor(new Car(Utility.gameWidth-8, min + lane * 16, 30, -1));
        break;
      case 4:
        Utility.addActor(new Car(8, min + lane * 16, 30, 1));
        break;
    }
  }

  private void drawGame() {

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        repaint();
      }
    });
  }

  /**
   * This method will check each actor to see if the bounding box overlaps the
   * bounding box of any other actor. Not the most efficient, but will work for
   * small games.
   */
  public void checkCollisions() {
    // check player against all other objects
    Rectangle boundry = new Rectangle(Utility.gameWidth, Utility.gameHeight);
    for (Actor actor : Utility.castAndCrew) {
      if (!boundry.contains(actor.getBounds())) {
        actor.hitEdge();
      }
    }
  }
}