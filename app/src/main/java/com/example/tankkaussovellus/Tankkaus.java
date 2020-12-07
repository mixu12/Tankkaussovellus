package com.example.tankkaussovellus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Tankkaus {
    private int id;
    private double maara;
    private int mittarilukema;
    private double litrahinta;
    private int tankkausasemaid;
    private int autoid;

    public Tankkaus(int id, double maara, int mittarilukema, double litrahinta){
        this.id = id;
        this.maara = maara;
        this.mittarilukema = mittarilukema;
        this.litrahinta = litrahinta;
        this.tankkausasemaid = 0;
        this.autoid = 0;
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

    public double getLitrahinta(){
        return this.litrahinta;
    }

    public String getPaiva(){
        ZonedDateTime aika = ZonedDateTime.now();
        DateTimeFormatter muokkaaja = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return aika.format(muokkaaja);
    }

    public int getTankkausasemaid(){
        return this.tankkausasemaid;
    }

    public int getAutoid(){
        return this.autoid;
    }


    public double kokonaishinta(){
        return this.maara * this.litrahinta;
    }

    public String toString(){
        return "Tankattu " + this.maara + " l, " + " " + this.mittarilukema + " km, " + this.getPaiva();
    }

}
