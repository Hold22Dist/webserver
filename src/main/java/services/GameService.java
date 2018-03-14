package services;

import brugerautorisation.data.Bruger;
import brugerautorisation.jwt.JWTUtillities;
import brugerautorisation.jwt.Secured;
import hangman.Galgelogik;
import hangman.GameManager;
import hangman.GameState;
import highscore.HighscoreManager;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/hangman")
public class GameService {
    @POST
    @Path("/restart")
    @Produces(MediaType.APPLICATION_JSON)
    public GameState start(@Context HttpServletRequest request){
        String brugernavn = JWTUtillities.getUsername(request);
        Galgelogik game = GameManager.restartFor(brugernavn);

        return game.getState();
    }



    @GET
    @Path("/state")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public GameState state(@Context HttpServletRequest request){
        String brugernavn = JWTUtillities.getUsername(request);
        Galgelogik game = GameManager.getGame(brugernavn);

        return game.getState();
    }

    @POST
    @Path("/gaet")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GameState geat(String bogstav, @Context HttpServletRequest request){
        String brugernavn = JWTUtillities.getUsername(request);
        Galgelogik game = GameManager.getGame(brugernavn);

        game.gætBogstav(bogstav);
        if (game.erSpilletVundet()){
            HighscoreManager.addWord(brugernavn, game.getOrdet());
        }

        return game.getState();
    }

}
