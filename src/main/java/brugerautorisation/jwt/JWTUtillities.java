package brugerautorisation.jwt;

import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;

public class JWTUtillities {
    public static String getUsername(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer".length()).trim();
        return Jwts.parser().setSigningKey(AuthenticationFilter.tokenSignerKey).parseClaimsJws(token).getBody().get("brugernavn").toString();
    }
}
