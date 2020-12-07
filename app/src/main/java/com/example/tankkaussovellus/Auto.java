package com.example.tankkaussovellus;

public class Auto {
    private int id;
    private String autonMalli;
    private Boolean oletus;

    public Auto(int id, String autonMalli, boolean oletus){
        this.id = id;
        this.autonMalli = autonMalli;
        this.oletus = oletus;

    }

    public String getAutonMalli() {
        return autonMalli;
    }

    public Boolean getOletus() {
        return oletus;
    }
}


