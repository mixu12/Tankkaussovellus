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

    //Tämä metodi päivittää tietokantaan vuodeksi aina 20xx tyyppisen luvun. Käyttäjälle näkyvää päivää se ei muokkaa.
    public int getVuosi(){
        String[] vuosiStringina = this.paiva.split("\\.");

        //Tarkistetaan montako numeroa annettu vuosi sisältää ja valitaan käsittelytapa sen mukaan.
        if (vuosiStringina[2].matches(("20[0-9]{2}"))){
            int vuosi = Integer.valueOf(vuosiStringina[2]);
            return vuosi;
        }

        if (vuosiStringina[2].matches(("[0-9]{2}"))){
            String vuoteenEkatNumerot = "20" + vuosiStringina[2];
            int vuosi = Integer.valueOf(vuoteenEkatNumerot);
            return vuosi;
        }
        return 0;
    }

    //Tämä metodi päivittää tietokantaan kuukauden aina ilman edessä olevaa nollaa. Käyttäjälle näkyvää päivää se ei muokkaa.
    public int getKuukausi(){
        String[] kuukausiStringina = this.paiva.split("\\.");

        //Tarkistetaan montako numeroa annettu kuukausi sisältää ja valitaan käsittelytapa sen mukaan.
        if (kuukausiStringina[1].matches(("0[1-9]{1}"))){
            String[] kuukausiIlmanNollaa = kuukausiStringina[1].split("");
            int kuukausi = Integer.valueOf(kuukausiIlmanNollaa[1]);
            return kuukausi;
        } else{
            int kuukausi = Integer.valueOf(kuukausiStringina[1]);
            return kuukausi;
        }
    }

    public String toString(){
        return this.maara + " l, " + " " + this.mittarilukema + " km, " + kokonaishinta + " €, " + this.getPaiva();
    }

}
