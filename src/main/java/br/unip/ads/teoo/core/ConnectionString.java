package br.unip.ads.teoo.core;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import com.google.gson.Gson;

public class ConnectionString {
    private String url;
    private String user;
    private String password;

    public ConnectionString() {
    }

    public void loadConnectionString(){
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("connectionstring.json")));
            ConnectionString config = gson.fromJson(reader, ConnectionString.class);
            this.url = config.getUrl();
            this.user = config.getUser();
            this.password = config.getPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
