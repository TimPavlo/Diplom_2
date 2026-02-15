package ru.yandex.practicum.models;

public class User {
    private String email;
    private String password;
    private String name;
    private String accessToken;

    public String getEmail() {return email;}
    public User setEmail(String email) {this.email = email; return this;}
    public String getPassword() {return password;}
    public User setPassword(String password) {this.password = password; return this;}
    public String getName() {return name;}
    public User setName(String name) {this.name = name; return this;}
    public String getAccessToken() {return accessToken;}
    public User setAccessToken(String accessToken) {this.accessToken = accessToken; return this;}
}
