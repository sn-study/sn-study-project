package sn.example.demo.error;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(){
        super("Token Not Valid");

    }
}
