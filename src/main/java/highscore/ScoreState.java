package highscore;

import java.util.HashSet;

public class ScoreState {
    public String brugernavn;
    public HashSet<String> guessedWords;

    public ScoreState(String brugernavn, HashSet<String> guessedWords) {
        this.brugernavn = brugernavn;
        this.guessedWords = guessedWords;
    }
}
