package q_learning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/**
 *
 * @author matthewsokoloff
 * This file contains the agent code primarily for decision making purposes.
 */
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
  
  public Agent(float gamma, float epsilon,float alpha, Game board) {
    this.epsilon = epsilon;
    this.gamma = gamma; 
    this.alpha = alpha;
    this.qValues = new HashMap<>();
    this.board = board;
    this.rn = new Random();
    this.epsilonDecay = 0.0001;
  }
  
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
  
  private int[] getMove() {
    ArrayList<int[]> legalMoves = getLegalMoves(this.agentPosition);
    if(this.rn.nextFloat() < this.epsilon) {
      int randomMove = this.rn.nextInt(legalMoves.size());
      return legalMoves.get(randomMove);
    }else {
      return getMaxQValueMove(legalMoves);
    }
  }
  
  private void updateQValues(int[] prevState, float reward, int[] nextState) {
    String hash = prevState[0] + "_" + prevState[1];
    float oldValue = this.qValues.containsKey(hash) ? this.qValues.get(hash): 0f;
    float newValue = oldValue + this.alpha*(reward + this.gamma*getMaxQValue(nextState) - oldValue);
    this.qValues.put(hash,newValue);
  }
  
  private void updateAgent(int[] move, float reward){
        int[] prevState = this.agentPosition;
        this.agentPosition[0] = move[0];
        this.agentPosition[1] = move[1];
        updateQValues(prevState, reward, this.agentPosition);
  }
  
 
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

