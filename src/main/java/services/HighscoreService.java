package services;

import brugerautorisation.jwt.JWTUtillities;
import brugerautorisation.jwt.Secured;
import hangman.Galgelogik;
import hangman.GameManager;
import highscore.HighscoreManager;
import highscore.ScoreState;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/highscore")
public class HighscoreService {
    @GET
    @Path("/me")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public ScoreState me(@Context HttpServletRequest request){
        String brugernavn = JWTUtillities.getUsername(request);
        Galgelogik game = GameManager.getGame(brugernavn);

        return HighscoreManager.getScore(brugernavn);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Integer> all(){
        return HighscoreManager.getAllScore();
    }
}
