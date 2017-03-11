package q_learning;

/**
 *
 * @author matthewsokoloff
 * This program creates a random maze for an ai agent to solve using the q-learning algorithm.
 * This file contains the entry point to the program.
 */
public class Q_learning {
  public static void main(String[] args) throws InterruptedException {
    Game game = new Game(600,350);
    float gamma = 0.92f;
    float epsilon = 0.5f;
    float alpha = 0.1f;
    Agent agent = new Agent(gamma, epsilon,alpha,game);
    agent.play(10000000);
  }
}

