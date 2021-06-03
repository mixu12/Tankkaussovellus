package com.example.tankkaussovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tilastot extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public ListView listView;
    ArrayAdapter arrayAdapter;
    Tankkauskanta tietokanta;

    List<Tankkaus> tankkauslista;
    ArrayList<String> tankkaustulokset;
    ArrayList<String> vuodet;
    List<Auto> autotListaan;

    String valittuVuosi = "Kaikki";
    int valitunAutonID = 0;

    DecimalFormat kaksiDesimaalia = new DecimalFormat("#,###,##0.00");
    DecimalFormat yksiDesimaali = new DecimalFormat("#,###,##0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilastot);

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        tietokanta = new Tankkauskanta(Tilastot.this);

        keskikulutus();
        keskihinta();
        tankattuYhteensaLitroina();
        tankattuYhteensaEuroina();

        paivitaLista();
        autotListaan();
        spinnerit();

    }

    private void paivitaLista() {
        tankkauslistaanData();
        arrayAdapter = new ArrayAdapter(Tilastot.this, R.layout.custom_layout, tankkaustulokset);
        listView.setAdapter(arrayAdapter);

    }

    public void listaan(){
        tankkauslista = new ArrayList<>();
        tankkauslista = tietokanta.kaikkiTankkaukset(valitunAutonID);

        vuodet = new ArrayList<>();
        vuodet = tietokanta.vuodet();
    }

    // Tässä muokataan tietokannasta saatua dataa listviewiin.
    public void tankkauslistaanData(){
        listaan();

        DecimalFormat kaksiDesimaalia = new DecimalFormat("#,###,##0.00");
        DecimalFormat yksiDesimaali = new DecimalFormat("#,###,##0.0");

        tankkaustulokset = new ArrayList<>();
        double kulutus = 0.0; //Lähtöarvo kulutukselle

        for (int i = 0; i < tankkauslista.size(); i++){
            int matkaNyt = tankkauslista.get(i).getMittarilukema();
            double tankattuNyt = tankkauslista.get(i).getMaara();
            String tankattuNytTekstina = String.valueOf(kaksiDesimaalia.format(tankattuNyt));
            String paiva = tankkauslista.get(i).getPaiva();
            double kokonaishinta = tankkauslista.get(i).getKokonaishinta();

            //Nämä päivittyy listaan, jonka perusteella muodostetaan listview-näkymä.
            //Ensimmäistä tankkausta ei voida verrata mihinkään, joten kulutusta ei voi laskea edellisestä tankkauksesta. Oletus on 0,0 l/100 km.
            if (i < 1){
                String kulutusTekstina = String.valueOf(yksiDesimaali.format(kulutus));
                String listaan = tankattuNytTekstina + " l, " + String.valueOf(matkaNyt) + " km, " + kulutusTekstina + " l/100 km, "+ kokonaishinta + " €, " + paiva;
                naytettavatListalla(listaan, i);

                //Tässä lasketaan matkasta, joka on ajettu edellisestä tankkauksesta ja verrataan sitä tankattuun määrään.
            } else {
                int matkaEdellinen = tankkauslista.get(i-1).getMittarilukema();
                kulutus = 100.0 * ((tankattuNyt) / (matkaNyt - matkaEdellinen));
                String kulutusTekstina = String.valueOf(yksiDesimaali.format(kulutus));

                String listaan = tankattuNytTekstina + " l, " + String.valueOf(matkaNyt) + " km, " + kulutusTekstina + " l/100 km, " + kokonaishinta + " €, "+ paiva;
                naytettavatListalla(listaan, i);
            }
        }
    }

    // Spinnerissä näytettävät vuodet
    public void naytettavatListalla(String listaan, int i){
        if (valittuVuosi.equals("Kaikki")){
            tankkaustulokset.add(listaan);
        } else {
            if (tankkauslista.get(i).getVuosi() == Integer.valueOf(valittuVuosi)){
                tankkaustulokset.add(listaan);
            }
        }
    }

    // Spinnerissä näytettävät autot
    public void autotListaan(){
        autotListaan = new ArrayList<>();
        autotListaan = tietokanta.kaikkiAutot();

    }

    public void spinnerit(){
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vuodet);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinnerAutoille = findViewById(R.id.spinnerAutoille);
        ArrayAdapter<Auto> adapterAutoille = new ArrayAdapter<Auto>(this, android.R.layout.simple_spinner_dropdown_item, autotListaan);
        adapterAutoille.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAutoille.setAdapter(adapterAutoille);
        spinnerAutoille.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        Spinner spinnerAutoille = (Spinner) parent;

        if (spinner.getId() == R.id.spinner){
            valittuVuosi = parent.getItemAtPosition(position).toString();
        }

        if (spinnerAutoille.getId() == R.id.spinnerAutoille){
            String valittuAuto = parent.getItemAtPosition(position).toString();
            valitunAutonID = tietokanta.valitunAutonID(valittuAuto);
        }

        // Päivittää näkyvillä olevat luvut valitulle vuodelle tai autolle
        tankattuYhteensaLitroina();
        tankattuYhteensaEuroina();
        keskihinta();
        keskikulutus();
        paivitaLista();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void keskikulutus(){
        TextView keskikulutus = (TextView) findViewById(R.id.keskikulutus);
        double keskikulutusLuku = 0.0;
        if (valittuVuosi.equals("Kaikki")){
            keskikulutusLuku = tietokanta.keskikulutus(valitunAutonID);
        } else {
            keskikulutusLuku = tietokanta.keskikulutus(valittuVuosi, valitunAutonID);
        }
        String keskikulutusStringina = String.valueOf((yksiDesimaali.format(keskikulutusLuku)));
        keskikulutus.setText(keskikulutusStringina);
    }

    public void keskihinta(){
        TextView keskihinta = (TextView) findViewById(R.id.keskihinta);
        double keskihintaLuku = 0.0;
        if (valittuVuosi.equals("Kaikki")){
            keskihintaLuku = tietokanta.keskihinta(valitunAutonID);
        } else {
            keskihintaLuku = tietokanta.keskihinta(valittuVuosi, valitunAutonID);
        }

        String keskihintaStringina = String.valueOf((kaksiDesimaalia.format(keskihintaLuku)));
        keskihinta.setText(keskihintaStringina);
    }

    public void tankattuYhteensaLitroina() {
        TextView tankattuYhteensa = (TextView) findViewById(R.id.tankattuYhteensa);
        double sumTankattu = 0.0;
        if (valittuVuosi.equals("Kaikki")){
            sumTankattu = tietokanta.tankattuYhteensa(valitunAutonID);
        } else {
            sumTankattu = tietokanta.tankattuYhteensa(valittuVuosi, valitunAutonID);
        }

        String sumTankattuStringina = String.valueOf((kaksiDesimaalia.format(sumTankattu)));
        tankattuYhteensa.setText(sumTankattuStringina);
    }

    public void tankattuYhteensaEuroina(){
        TextView tankattuYhteensaEuroina = (TextView) findViewById(R.id.tankattuYhteensaEuroina);
        double sumTankattuEuroina = 0.0;
        if (valittuVuosi.equals("Kaikki")) {
            sumTankattuEuroina = tietokanta.tankattuYhteensaEuroina(valitunAutonID);
        } else {
            sumTankattuEuroina = tietokanta.tankattuYhteensaEuroina(valittuVuosi, valitunAutonID);
        }
        String sumTankattuEuroinaStringina = String.valueOf((kaksiDesimaalia.format(sumTankattuEuroina)));
        tankattuYhteensaEuroina.setText(sumTankattuEuroinaStringina);
    }
}
