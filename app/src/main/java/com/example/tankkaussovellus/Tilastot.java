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

    String valittu = "01.04.2021";

    DecimalFormat kaksiDesimaalia = new DecimalFormat("#,###,##0.00");
    DecimalFormat yksiDesimaali = new DecimalFormat("#,###,##0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilastot);

        //spinneri();

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        tietokanta = new Tankkauskanta(Tilastot.this);

        keskikulutus();
        keskihinta();
        tankattuYhteensaLitroina();
        tankattuYhteensaEuroina();

        paivitaLista();

    }

    private void paivitaLista() {
        tankkauslistaanData();
        arrayAdapter = new ArrayAdapter(Tilastot.this, R.layout.custom_layout, tankkaustulokset);
        listView.setAdapter(arrayAdapter);

    }

    public void listaan(){
        tankkauslista = new ArrayList<>();
        tankkauslista = tietokanta.kaikkiTankkaukset();
        System.out.println(tankkauslista);
    }

    public void tankkauslistaanData(){
        listaan();

        DecimalFormat kaksiDesimaalia = new DecimalFormat("#,###,##0.00");
        DecimalFormat yksiDesimaali = new DecimalFormat("#,###,##0.0");

        tankkaustulokset = new ArrayList<>();
        double kulutus = 0.0;

        for (int i = 0; i < tankkauslista.size(); i++){
            int matkaNyt = tankkauslista.get(i).getMittarilukema();
            double tankattuNyt = tankkauslista.get(i).getMaara();
            String tankattuNytTekstina = String.valueOf(kaksiDesimaalia.format(tankattuNyt));
            String paiva = tankkauslista.get(i).getPaiva();
            double kokonaishinta = tankkauslista.get(i).getKokonaishinta();

            if (i < 1){
                String kulutusTekstina = String.valueOf(yksiDesimaali.format(kulutus));
                String listaan = tankattuNytTekstina + " l, " + String.valueOf(matkaNyt) + " km, " + kulutusTekstina + " l/100 km, "+ kokonaishinta + " €, " + paiva;
                tankkaustulokset.add(listaan);
            } else {
                int matkaEdellinen = tankkauslista.get(i-1).getMittarilukema();
                kulutus = 100.0 * ((tankattuNyt) / (matkaNyt - matkaEdellinen));
                String kulutusTekstina = String.valueOf(yksiDesimaali.format(kulutus));

                String listaan = tankattuNytTekstina + " l, " + String.valueOf(matkaNyt) + " km, " + kulutusTekstina + " l/100 km, " + kokonaishinta + " €, "+ paiva;
                tankkaustulokset.add(listaan);
                System.out.println(tankkaustulokset);
            }
        }

    }

    public void spinneri(){
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vuosi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        valittu = parent.getItemAtPosition(position).toString();
        //keskikulutus();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void keskikulutus(){
        TextView keskikulutus = (TextView) findViewById(R.id.keskikulutus);
        Double keskikulutusLuku = tietokanta.keskikulutus();
        String keskikulutusStringina = String.valueOf((yksiDesimaali.format(keskikulutusLuku)));
        keskikulutus.setText(keskikulutusStringina);
    }

    public void keskihinta(){
        TextView keskihinta = (TextView) findViewById(R.id.keskihinta);
        Double keskihintaLuku = tietokanta.keskihinta();
        String keskihintaStringina = String.valueOf((kaksiDesimaalia.format(keskihintaLuku)));
        keskihinta.setText(keskihintaStringina);
    }

    public void tankattuYhteensaLitroina() {
        TextView tankattuYhteensa = (TextView) findViewById(R.id.tankattuYhteensa);
        Double sumTankattu = tietokanta.tankattuYhteensa();
        String sumTankattuStringina = String.valueOf((kaksiDesimaalia.format(sumTankattu)));
        tankattuYhteensa.setText(sumTankattuStringina);
    }

    public void tankattuYhteensaEuroina(){
        TextView tankattuYhteensaEuroina = (TextView) findViewById(R.id.tankattuYhteensaEuroina);
        Double sumTankattuEuroina = tietokanta.tankattuYhteensaEuroina();
        String sumTankattuEuroinaStringina = String.valueOf((kaksiDesimaalia.format(sumTankattuEuroina)));
        tankattuYhteensaEuroina.setText(sumTankattuEuroinaStringina);
    }
}
