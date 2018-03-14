package highscore;

import other.MapUtil;

import java.util.*;

public class HighscoreManager {
    private static HashMap<String, HashSet<String>> guessedWords = new HashMap<>();
    protected HighscoreManager(){};

    public static HashSet<String> addWord(String brugernavn, String word){
        HashSet<String> words = guessedWords.get(brugernavn);
        if (words==null) {
            words = new HashSet<String>();
        }
        words.add(word);
        return guessedWords.put(brugernavn, words);
    }

    public static ScoreState getScore(String brugernavn){
        HashSet<String> words = guessedWords.get(brugernavn);
        return new ScoreState(brugernavn, words);
    }

    public static HashMap<String, Integer> getAllScore(){
        HashMap<String, Integer> points = new HashMap<>();
        int amount;
        for (Map.Entry<String, HashSet<String>> entry : guessedWords.entrySet()) {
            amount = 0;
            for (String s : entry.getValue()) {
                amount += s.length();
            }

            points.put(entry.getKey(), amount);
        }
        MapUtil.sortByValue(points);
        return points;
    }
}
