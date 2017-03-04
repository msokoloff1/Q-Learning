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
                                //x,y
  private int[] agentPosition = {0,5};
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
    
  
  public Game(int boardWidth, int boardHeight) {
    super("Q-Learning Maze");
    this.gameNumber = 0;
    setSize(boardWidth,boardHeight);
    setVisible(true);
    randomizeBoard();
  }
  
  private void randomizeBoard(){
    float[] boardValues = {0,0,-1};
    Random rand = new Random();
    for(int row = 1; row < board.length; row++) {
      for(int col = 1; col<board[0].length; col++) {
        board[row][col] = boardValues[rand.nextInt(3)];
      }
    }
  }
  
  public float setAgentPosition(int x, int y){
      this.agentPosition[0] = x;
      this.agentPosition[1] = y;
      return board[y][x];
  }
  
  public void gameOver() {
      this.gameNumber += 1;
      this.agentPosition[0] = 0;
      this.agentPosition[1] = 5;
  }
  
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

