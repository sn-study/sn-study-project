package sn.example.demo.error;

public class TokenAlreadyExistsException extends RuntimeException {
    public TokenAlreadyExistsException(String token){
        super("Token Already Exists : " + token);

    }
}
