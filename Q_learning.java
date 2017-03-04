package q_learning;

/**
 *
 * @author matthewsokoloff
 * This program creates a random maze for an ai agent to solve using the q-learning algorithm.
 * This file contains the entry point to the program.
 */
public class Q_learning {
  public static void main(String[] args) throws InterruptedException {
    Game g = new Game(600,350);
    Agent a = new Agent(0.92f, 0.5f,0.1f,g);
    a.play(10000000);
  }
}

