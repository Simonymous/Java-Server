package model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
public class TokenHelper {
    public TokenHelper() {

    }

    public String generateToken() {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return "";
        }
    }

    public String validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getToken();
        } catch (JWTDecodeException exception){
            return "";
            //Invalid token
        }
    }
}
