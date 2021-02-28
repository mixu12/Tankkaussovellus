package com.example.tankkaussovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tilastot extends AppCompatActivity {

    public ListView listView;
    ArrayAdapter arrayAdapter;
    Tankkauskanta tietokanta;

    List<Tankkaus> tankkauslista;
    ArrayList<String> tankkaustulokset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilastot);

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setLongClickable(true);

        tietokanta = new Tankkauskanta(Tilastot.this);

        DecimalFormat kaksiDesimaalia = new DecimalFormat("#,###,##0.00");
        DecimalFormat yksiDesimaali = new DecimalFormat("#,###,##0.0");

        TextView keskikulutus = (TextView) findViewById(R.id.keskikulutus);
        Double keskikulutusLuku = tietokanta.keskikulutus();
        String keskikulutusStringina = String.valueOf((yksiDesimaali.format(keskikulutusLuku)));
        keskikulutus.setText(keskikulutusStringina);

        TextView keskihinta = (TextView) findViewById(R.id.keskihinta);
        Double keskihintaLuku = tietokanta.keskihinta();
        String keskihintaStringina = String.valueOf((kaksiDesimaalia.format(keskihintaLuku)));
        keskihinta.setText(keskihintaStringina);

        TextView tankattuYhteensa = (TextView) findViewById(R.id.tankattuYhteensa);
        Double sumTankattu = tietokanta.tankattuYhteensa();
        String sumTankattuStringina = String.valueOf((kaksiDesimaalia.format(sumTankattu)));
        tankattuYhteensa.setText(sumTankattuStringina);

        TextView tankattuYhteensaEuroina = (TextView) findViewById(R.id.tankattuYhteensaEuroina);
        Double sumTankattuEuroina = tietokanta.tankattuYhteensaEuroina();
        String sumTankattuEuroinaStringina = String.valueOf((kaksiDesimaalia.format(sumTankattuEuroina)));
        tankattuYhteensaEuroina.setText(sumTankattuEuroinaStringina);

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
}
