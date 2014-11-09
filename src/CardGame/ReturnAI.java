package CardGame;

/**
 * Created by Kristján on 9.11.2014.
 */
public class ReturnAI {
    static public AI getTestAI(int playerTurn, State state) {
        return new Test(playerTurn, state);
    }
}
