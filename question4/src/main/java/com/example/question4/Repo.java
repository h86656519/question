package com.example.question4;

public class Repo {
    public String ID = "";
    public String Name = "";
    public int Attack = 0;
    public int Defense = 0; //用gson 變數不能亂取，要跟資料一樣

    public void setDefense(int defense) {
        this.Defense = defense;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAttack(int attack) {
        Attack = attack;
    }

    public String getName() {
        return Name;
    }

    public String getID() {
        return ID;
    }

    public int getDefense() {
        return Defense;
    }

    public int getAttack() {
        return Attack;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ID:").append(getName()).append(", Name:").append(", Attack:").append(getDefense()).toString();
    }
}
