package hangman;

import java.util.ArrayList;

public class GameState {
    public String word;
    public ArrayList<String> bogstaver;
    public int mistakes;
    public int winstate;

    public GameState(String word, ArrayList<String> bogstaver, int mistakes, int winstate) {
        this.word = word;
        this.bogstaver = bogstaver;
        this.mistakes = mistakes;
        this.winstate = winstate;
    }
}
