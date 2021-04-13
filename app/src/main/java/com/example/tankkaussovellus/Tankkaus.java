package com.example.tankkaussovellus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Tankkaus {
    private int id;
    private double maara;
    private int mittarilukema;
    private double kokonaishinta;
    private String paiva;

    public Tankkaus(int id, double maara, int mittarilukema, double kokonaishinta){
        this.id = id;
        this.maara = maara;
        this.mittarilukema = mittarilukema;
        this.kokonaishinta = kokonaishinta;
        this.paiva = "a";
    }

    public Tankkaus(int id, double maara, int mittarilukema, double kokonaishinta, String paiva){
        this.id = id;
        this.maara = maara;
        this.mittarilukema = mittarilukema;
        this.kokonaishinta = kokonaishinta;
        this.paiva = paiva;
    }

    public Tankkaus(int id, double maara, int mittarilukema){
        this(id, maara, mittarilukema, 0.0);
    }

    public int getId(){
        return this.id;
    }

    public double getMaara(){
        return this.maara;
    }

    public int getMittarilukema(){
        return this.mittarilukema;
    }

    public double getKokonaishinta(){
        return this.kokonaishinta;
    }

    public String getPaiva(){
        if (this.paiva.equals("a")) {
            ZonedDateTime aika = ZonedDateTime.now();
            DateTimeFormatter muokkaaja = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return aika.format(muokkaaja);
        } else{
            return this.paiva;
        }
    }

    public String toString(){
        return this.maara + " l, " + " " + this.mittarilukema + " km, " + kokonaishinta + " €, " + this.getPaiva();
    }

}
