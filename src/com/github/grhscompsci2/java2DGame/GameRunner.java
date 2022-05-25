package com.github.grhscompsci2.java2DGame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * This is the main class that will run the game. You should not have to edit
 * anything here. As a matter of fact, editing this file should be thought about
 * VERY HARD before you do it, with the exception of "YOUR GAME NAME HERE". That
 * you should change to match the name of your game.
 */
public class GameRunner {

  // the JPanel that holds the game
  private Board board;

  /**
   * Default constructor that will initialize the user interface
   */
  public GameRunner() {
    // initialize the UI
    initUI();
  }

  /**
   * This method sets up the JFrame and initializes the code that will center the
   * board on the screen
   */
  private void initUI() {
    board = new Board();
    JFrame frame = new JFrame();

    // We want the JFrame to quit when the window is closed.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set up the key bindings for the game
    setGamePanelKeyBindings();
    // The wrapper panel will hold the aspect ratio of the board and keep it
    // centered
    JPanel wrapperPanel = new JPanel(new SingleComponentAspectRatioKeeperLayout());
    wrapperPanel.add(board);
    frame.add(wrapperPanel);
    // resizes the game window to the preferred size of the Board
    frame.pack();
    // set the title

    frame.setTitle("FROGGER");

    // put the window in the center of the screen
    frame.setLocationRelativeTo(null);
    // make the frame visible
    frame.setVisible(true);
    // start the game loop which will repaint the screen
    runGameLoop();
  }

  /**
   * This method creates a new thread for the game logic
   */
  public void runGameLoop() {
    Thread loop = new Thread(new Runnable() {
      @Override
      public void run() {
        // tell the board class that it has started running. Useful for pause and menus
        // and stuff.
        board.running = true;
        // start running the game loop
        board.gameLoop();
      }
    });
    loop.start();
  }

  /**
   * This method will define what we want to happen when certain keys are pressed.
   * We are using the Utility class to hold the values so the Actor classes and
   * the Board class can have access to them
   */
  private void setGamePanelKeyBindings() {
    // We only want to pay attention to key presses that happen when we have focused
    // on the JFrame
    InputMap inputMap = board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    // Capture WASD and &uarr; &larr; &rarr; &darr;. Google "Virtual Key java" for a
    // list of all VK codes. To add another keybinding, just trim off the "VK_" and
    // put it where "W" is. If you only include the VK code, it will assume you want
    // it to trigger when the key is pressed. The actionMapKey "up arrow" is just a
    // string to define what we are doing. We are using them multiple times because
    // we want to use the arrow keys and wasd to control the player
    inputMap.put(KeyStroke.getKeyStroke("A"), "left arrow");
    inputMap.put(KeyStroke.getKeyStroke("D"), "right arrow");
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down arrow");
    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left arrow");
    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right arrow");
    inputMap.put(KeyStroke.getKeyStroke("S"), "down arrow");
    inputMap.put(KeyStroke.getKeyStroke("SPACE"), "space");
    inputMap.put(KeyStroke.getKeyStroke("UP"), "up arrow");
    inputMap.put(KeyStroke.getKeyStroke("W"), "up arrow");

    // If you include the string "released " before the VK code, it will trigger
    // when the key is released.
    inputMap.put(KeyStroke.getKeyStroke("released A"), "left arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released D"), "right arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "down arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "left arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "right arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released S"), "down arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "space released");
    inputMap.put(KeyStroke.getKeyStroke("released UP"), "up arrow released");
    inputMap.put(KeyStroke.getKeyStroke("released W"), "up arrow released");

    // The actionMap relates the actionMapKey from the input map to something
    // happening
    ActionMap actionMap = board.getActionMap();

    // When we see this action map key, do this abstract action. We are just setting
    // the Utility values to true when the button is pressed, and false when it is
    // released.
    actionMap.put("up arrow", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.UP_ARROW = true;
      }
    });
    actionMap.put("left arrow", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.LEFT_ARROW = true;
      }
    });
    actionMap.put("down arrow", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.DOWN_ARROW = true;
      }
    });
    actionMap.put("right arrow", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.RIGHT_ARROW = true;
      }
    });
    actionMap.put("space", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.SPACE = true;
      }
    });

    actionMap.put("up arrow released", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.UP_ARROW = false;
      }
    });
    actionMap.put("left arrow released", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.LEFT_ARROW = false;
      }
    });
    actionMap.put("down arrow released", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.DOWN_ARROW = false;
      }
    });
    actionMap.put("right arrow released", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.RIGHT_ARROW = false;
      }
    });
    actionMap.put("space released", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Utility.SPACE = false;
      }
    });
  }

  /**
   * The main method that will initialize the game runner.
   * 
   * @param args command line arguments that are not used in this class.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new GameRunner();
      }
    });
  }
}
