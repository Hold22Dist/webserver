package brugerautorisation.jwt;

/**
 * Created by emilbonnekristiansen on 07/06/2017.
 */
public class InvalidTokenException extends Throwable {
    private static final long serialVersionUID = -5490114628089339322L;
    public InvalidTokenException(String message) { super(message); }
    public InvalidTokenException(Exception e) { super(e); }
}
