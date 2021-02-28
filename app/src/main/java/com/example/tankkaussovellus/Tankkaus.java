package com.example.tankkaussovellus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Tankkaus {
    private int id;
    private double maara;
    private int mittarilukema;
    private double kokonaishinta;
    private int tankkausasemaid;
    private int autoid;
    private String paiva;

    public Tankkaus(int id, double maara, int mittarilukema, double kokonaishinta){
        this.id = id;
        this.maara = maara;
        this.mittarilukema = mittarilukema;
        this.kokonaishinta = kokonaishinta;
        this.tankkausasemaid = 0;
        this.autoid = 0;
        this.paiva = "a";
    }

    public Tankkaus(int id, double maara, int mittarilukema, double kokonaishinta, String paiva){
        this.id = id;
        this.maara = maara;
        this.mittarilukema = mittarilukema;
        this.kokonaishinta = kokonaishinta;
        this.tankkausasemaid = 0;
        this.autoid = 0;
        this.paiva = paiva;
    }

    public Tankkaus(int id, double maara, int mittarilukema){
        this(id, maara, mittarilukema, 0.0);
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

    public int getTankkausasemaid(){
        return this.tankkausasemaid;
    }

    public int getAutoid(){
        return this.autoid;
    }

    public String toString(){
        return this.maara + " l, " + " " + this.mittarilukema + " km, " + kokonaishinta + " €, " + this.getPaiva();
    }

}
