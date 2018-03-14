package services;

import brugerautorisation.Credentials;
import brugerautorisation.data.Bruger;
import brugerautorisation.jwt.AuthenticationFilter;
import brugerautorisation.transport.rmi.Brugeradmin;
import brugerautorisation.transport.rmi.BrugeradminHolder;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;


@Path("")
public class UserService {
    @Path("/login")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Credentials credentials) {
        try {
            String brugernavn = credentials.getBrugernavn();
            String password = credentials.getPassword();

            // Authenticate the user using the credentials provided
            authenticate(brugernavn, password);

            // Issue a token for the user
            String token = issueToken(brugernavn);

            // Return the token on the response
            return Response.ok(token).header("Authorization", "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }



//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response check(@Context HttpServletRequest request) {
//        String i = Jwts.parser().setSigningKey(AuthenticationFilter.tokenSignerKey).parseClaimsJws(request.getHeader("Authorization")).getBody().getIssuer();
//        return Response.ok(i).build();
//    }



    private Bruger authenticate(String brugernavn, String password) throws Exception {
        Brugeradmin ba = BrugeradminHolder.getBrugerAdmin();
        return ba.hentBruger(brugernavn, password);
    }

    private String issueToken(String brugernavn) {
        List<String> roller = null;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long ttlMillis = 3600000L; // en time



        JwtBuilder builder =  Jwts.builder()
                .setIssuer("gruppe 22")
                .claim("brugernavn", brugernavn)
                .signWith(SignatureAlgorithm.HS512, AuthenticationFilter.tokenSignerKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

}