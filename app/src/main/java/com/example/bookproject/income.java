package com.example.bookproject;

public class income {
    private String name;
    private String money;
    private String date;
    private String time;
    private String img_uri;

    public income(){}

    public income(String name,String money,String date,String time,String img_uri){
        this.name = name;
        this.money = money;
        this.date = date;
        this.time = time;
        this.img_uri=img_uri;
    }
    public String getImg_uri(){
        return img_uri;
    }
    
    public void setImg_uri(String img_uri){
        this.img_uri=img_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
