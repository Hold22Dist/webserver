// DENNE FIL ER (for det meste) LÅNT FRA https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey


package brugerautorisation.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;


@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    final public static Key tokenSignerKey = MacProvider.generateKey();


    @Override
    public void filter(final ContainerRequestContext requestContext) {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            // Validate the token
            validateToken(token);

        } catch (InvalidTokenException e) {
            requestContext.abortWith( Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws InvalidTokenException {
        try {
            Date exp = Jwts.parser().setSigningKey(tokenSignerKey).parseClaimsJws(token).getBody().getExpiration();
            long expmillis = exp.getTime();
            isTokenExpired(expmillis);
            // vi kan GODT stole på denne her token

        } catch (Exception e) {
            // Vi kan IKKE stole på denne her token
            throw new InvalidTokenException("Token er ikke godkendt");
        }
    }

    private void isTokenExpired(long expmillis) throws InvalidTokenException {
        long nowMillis = System.currentTimeMillis();

        if (nowMillis > expmillis) {
            throw new InvalidTokenException("Token er udløbet");
        }
    }
}