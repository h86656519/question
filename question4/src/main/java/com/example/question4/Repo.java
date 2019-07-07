package com.example.question4;

public class Repo {
    public String ID = "";
    public String Name = "";
    public String Attack = "";
    public String Defense = ""; //用gson 變數不能亂取，要跟資料一樣

    public void setDefense(String defense) {
        this.Defense = defense;
    }

    public void setNodeId(String nodeId) {
        this.Attack = nodeId;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public String getID(){
        return ID;
    }

    public String getDefense() {
        return Defense;
    }

    public String getNodeId() {
        return Attack;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ID:").append(getName()).append(", Name:").append(", Attack:").append(getDefense()).toString();
    }
}
