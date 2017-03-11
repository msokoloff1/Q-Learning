/**
 *
 * @author matthewsokoloff
 * This file contains the Game specific information and renders it to the screen. 
 * 
 */
package q_learning;

import java.awt.Color;
import java.awt.Frame;
import java.util.Random;
import java.awt.Graphics;

public class Game extends Frame {
  private int gameNumber;
  private final int agentStartX = 0;
  private final int agentStartY = 5;
  private int[] agentPosition = {agentStartX,agentStartY};
  private final int tileWH = 50;
  private int startX = 20;
  private int startY = 20;
 
  private float[][] board = {
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0 , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
  };
    

  public Game() {
    super("Q-Learning Maze");
    this.gameNumber = 0;
    setSize(600,350);
    setVisible(true);
    randomizeBoard();
  }
  
  /*
  *This function adds random obstacles to the board for the agent to naviagate around 
  */
  private void randomizeBoard(){
    float[] boardValues = {0,0,-1};
    Random rand = new Random();
    for(int row = 1; row < board.length; row++) {
      for(int col = 1; col<board[0].length; col++) {
        board[row][col] = boardValues[rand.nextInt(3)];
      }
    }
  }
  
  /*
  *This function updates the agents position on the board.
  *@x : The x coordinate of the agent
  *@y : The y coordinate of the agent
  * Returns : The reward of the agent's new position
  */
  public float setAgentPosition(int x, int y){
      this.agentPosition[0] = x;
      this.agentPosition[1] = y;
      return board[y][x];
  }
  
  /*
  *Resets the agents position and then increments the game counter 
  */
  public void gameOver() {
      this.gameNumber += 1;
      this.agentPosition[0] = agentStartX;
      this.agentPosition[1] = agentStartY;
  }
  
  
  /*
  *@value : The reward of the position on the board
  *Returns : The appropriate color for the specific tile on the board
  */
  private Color lookupPaintColor(int value){
      switch(value){
          case 0:
              return Color.GREEN;
          case -1: 
              return Color.RED;
          case 1:
              return Color.YELLOW;
      }
      return Color.white;
  }
    
  
  /*
  *This method renders the graphics to the screen
  *None of the game logic occurs here
  */
  public void paint(Graphics g) {
    g.setColor(Color.red);
    for(float[] row: board) {
      for(float col: row) {
        g.setColor(lookupPaintColor((int) col));
        g.fill3DRect(startX, startY, tileWH, tileWH, true);
        this.startX += this.tileWH;
      }
      this.startX = 20;
      this.startY += this.tileWH;
    }
    this.startY = 20;
    this.startX = 20;
    g.setColor(Color.MAGENTA);
    g.fillOval(this.agentPosition[0]*tileWH + this.startY, this.agentPosition[1]*tileWH + this.startY,  50, 50);
    g.setColor(Color.BLACK);
    g.drawString("Game number : " + this.gameNumber, 230, 335);
    repaint();
  }
    
    
}

