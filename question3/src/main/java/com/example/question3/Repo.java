package com.example.question3;

public class Repo {
    public String name = "";

    public String description = "";

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getName()).append(", name:").append(getDescription()).toString();
    }
}
