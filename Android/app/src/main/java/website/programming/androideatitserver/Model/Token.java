package website.programming.androideatitserver.Model;

/**
 * Created by cokel on 3/17/2018.
 */

public class Token {
    private String token;
    private boolean serverSide;

    public Token() {
    }

    public Token(String token, boolean serverSide) {
        this.token = token;
        this.serverSide = serverSide;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }
}
