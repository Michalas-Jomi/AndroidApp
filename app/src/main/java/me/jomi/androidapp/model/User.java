package me.jomi.androidapp.model;

public class User {

    private String name;
    private String age;
    private String email;

    public User(String name, String age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
