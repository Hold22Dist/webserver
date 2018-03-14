package hangman;

import java.util.HashMap;

public class GameManager {
    private static HashMap<String, Galgelogik> games = new HashMap<>();
    protected GameManager(){};
    public static Galgelogik getGame(String brugernavn){
        Galgelogik game;
        if (games.containsKey(brugernavn)){
            game = games.get(brugernavn);
        } else {
            game = new Galgelogik();
            try {
                game.hentOrdFraDr();
            } catch (Exception e) {
                game.nulstil();
            }
            games.put(brugernavn, game);
        }

        return game;
    }

    public static Galgelogik restartFor(String brugernavn){
        Galgelogik game = games.get(brugernavn);
        try {
            game.hentOrdFraDr();
        } catch (Exception e) {
            game.nulstil();
        }
        return game;
    }

}
