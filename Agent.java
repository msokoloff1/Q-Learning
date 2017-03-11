/**
 *
 * @author matthewsokoloff
 * This file contains the code for the agent's decision making processes.
 */


package q_learning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Agent {
  private float epsilon;
  private float gamma;
  private float alpha;
  HashMap<String, Float> qValues;
  ArrayList <String> moves = new ArrayList<>();
  private int[] agentPosition = {0,5};
  private final int outOfBoundsX = 11;
  private final int outOfBoundsY = 6;
  Random rn;
  Game board;
  private double epsilonDecay;
  
  
  /*
  *@gamma   : The discount factor for future rewards
  *@epsilon : The probability the agent makes a random move
  *@alpha   : The learning rate for the agent
  *@board   : A reference to a game object for the agent to interact with
  */
  public Agent(float gamma, float epsilon,float alpha, Game board) {
    this.epsilon = epsilon;
    this.gamma = gamma; 
    this.alpha = alpha;
    this.qValues = new HashMap<>();
    this.board = board;
    this.rn = new Random();
    this.epsilonDecay = 0.0001;
  }
  
  
  /*
  *@possibleMove    : The x,y coordinate of a potential move for the agent to make
  *@agentPosition   : The current x,y coordinates of the agent
  *Returns a boolean indicating weather or not the move is legal
  */
  private boolean legal(int[] possibleMove,int[] agentPosition) {
    return 
      possibleMove[0]<outOfBoundsX && possibleMove[0]>= 0
        &&
      possibleMove[1]<outOfBoundsY && possibleMove[1]>= 0
        && 
      !(possibleMove[0] == agentPosition[0] && possibleMove[1] == agentPosition[1])
        && 
      !(possibleMove[0] == (agentPosition[0]+1) && possibleMove[1] == (agentPosition[1]+1) )
        && 
      !(possibleMove[0] == (agentPosition[0]-1) && possibleMove[1] == (agentPosition[1]-1) )
        && 
      !(possibleMove[0] == (agentPosition[0]+1) && possibleMove[1] == (agentPosition[1]-1) )
        && 
      !(possibleMove[0] == (agentPosition[0]-1) && possibleMove[1] == (agentPosition[1]+1) )
    ;
  }
    
  
  /*
  *@state   : The current x,y coordinates of the agent
  *Returns an ArrayList containing all legal moves for the agent to make
  */
  private ArrayList<int[]> getLegalMoves(int[] state){
    ArrayList <int[]> legalMoves = new ArrayList<>();
    int[] xCandidates = {state[0]-1,state[0], state[0]+1};
    int[] yCandidates = {state[1]-1,state[1], state[1]+1};
    for(int x: xCandidates) {
      for(int y: yCandidates) {
        int[] move = {x,y};
        if(legal(move, state)) {
          legalMoves.add(move);
        }
      }
    }
    return legalMoves;
  }
  
  
  /*
  *@legalMoves : A list of legal moves that the agent can make
  * Returns the x,y coordinates of the move with the greatest q-value
  */
  private int[] getMaxQValueMove(ArrayList<int[]> legalMoves){
    float maxValue = 0f;
    int maxIndex = 0;
    String hash = "";
    int index = 0;
    for(int[] move: legalMoves){
      hash += move[0] + "_" + move[1];           
      if(qValues.containsKey(hash)) {
        float value = qValues.get(hash);
        if(value > maxValue) {
          maxValue = value;
          maxIndex = index;     
        }
      }
      hash = ""; //reset
      index ++;
    }
    if(maxValue == 0.0) {
     int randomMove = this.rn.nextInt(legalMoves.size());
     return legalMoves.get(randomMove);
    }
    int[] bestMove = legalMoves.get(maxIndex);
    return bestMove;
  }
  
  
  /*
  *@state   : The current x,y coordinates of the agent
  * Returns the largest q value for all states the agent can reach from its current state
  */
  private float getMaxQValue(int[] state) {
    float maxValue = -1;
    String hash = "";
    ArrayList<int[]> legalMoves = getLegalMoves(state);
    for(int[] move: legalMoves) {
      hash += move[0] + "_" + move[1];          
      if(qValues.containsKey(hash)) {
        float value = qValues.get(hash);   
        if(value > maxValue) {
          maxValue = value;
        }
      }
      hash = ""; //reset
    }
    if(maxValue == 0.0) {
      return 0f;
    }
    return maxValue;
  }
  
  
  /*
  *Returns either a random move (with epsilon % chance) 
  * or returns the move with the largest q-value
  */
  private int[] getMove() {
    ArrayList<int[]> legalMoves = getLegalMoves(this.agentPosition);
    if(this.rn.nextFloat() < this.epsilon) {
      int randomMove = this.rn.nextInt(legalMoves.size());
      return legalMoves.get(randomMove);
    }else {
      return getMaxQValueMove(legalMoves);
    }
  }
  
  /*
  *@prevState : The state the agent was in prior to its last move (x,y coordinate)
  *@reward    : The observed reward after moving to the agents current state
  *@nextState : The current state of the agent (x,y coordinate)
  */
  private void updateQValues(int[] prevState, float reward, int[] nextState) {
    String hash = prevState[0] + "_" + prevState[1];
    float oldValue = this.qValues.containsKey(hash) ? this.qValues.get(hash): 0f;
    float newValue = oldValue + this.alpha*(reward + this.gamma*getMaxQValue(nextState) - oldValue);
    this.qValues.put(hash,newValue);
  }
  
  
  /*
  *@move   : The move the agent has chosen to make
  *@reward : The reward of the agent's move
  */
  private void updateAgent(int[] move, float reward){
        int[] prevState = this.agentPosition;
        this.agentPosition[0] = move[0];
        this.agentPosition[1] = move[1];
        updateQValues(prevState, reward, this.agentPosition);
  }
  
 /*
  *@numGames   : How many games the agent should play
  */
  public void play(int numGames) throws InterruptedException {
    boolean sleep = false;
    int madeCount = 0;
    while(numGames > 0) {
      float reward = 0;
      while(reward == 0) {
        int[] move = getMove(); 
        reward = this.board.setAgentPosition(move[0], move[1]);
        updateAgent(move, reward);
        if(reward > 0.9) {
          numGames--;
          madeCount++;
          if(madeCount > 20) {
            sleep = true;
          }
          agentPosition = new int[]{0,5};
          this.epsilon -= this.epsilonDecay;
          reward = 0;
          break;
        }
        if(reward == -1) {
          board.gameOver();
          numGames--;
          agentPosition = new int[]{0,5};
          this.epsilon -= 0.0001;
          reward = 0;
          break;
        }        
        if(sleep)
            Thread.sleep(200);
        }
    }   
  }  
}

