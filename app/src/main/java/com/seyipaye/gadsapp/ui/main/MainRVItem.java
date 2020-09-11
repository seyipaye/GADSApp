package com.seyipaye.gadsapp.ui.main;

public class MainRVItem {

    public String badgeUrl;
    public String country;
    public int hours;
    public String name;
    public int score;

    public MainRVItem(String badgeUrl, String country, int hours, String name, int score) {
        this.badgeUrl = badgeUrl;
        this.country = country;
        this.hours = hours;
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "MainRVItem{" +
                "badgeUrl='" + badgeUrl + '\'' +
                ", country='" + country + '\'' +
                ", hours=" + hours +
                ", name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
