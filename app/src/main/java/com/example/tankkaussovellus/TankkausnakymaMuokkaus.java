package com.example.tankkaussovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TankkausnakymaMuokkaus extends AppCompatActivity {

    Tankkauskanta tietokanta;

    List<Tankkaus> tankkauslista;

    int valittuID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankkausnakyma);

        tietokanta = new Tankkauskanta(TankkausnakymaMuokkaus.this);

        setIntentit();

        listaan(valittuID);

        final EditText maara = (EditText) findViewById(R.id.maara);
        final EditText mittarilukemanSyotto = (EditText) findViewById(R.id.mittarilukemanSyotto);
        final EditText hinnanSyotto = (EditText) findViewById(R.id.hinnanSyotto);
        final EditText tankkauspaiva = (EditText) findViewById(R.id.tankkauspaiva);

        final Button tallennaTankkaus = (Button) findViewById(R.id.tallennaTankkaus);

        maara.setText(String.valueOf(tankkauslista.get(0).getMaara()));
        mittarilukemanSyotto.setText(String.valueOf(tankkauslista.get(0).getMittarilukema()));
        hinnanSyotto.setText(String.valueOf(tankkauslista.get(0).getKokonaishinta()));
        tankkauspaiva.setText(tankkauslista.get(0).getPaiva());

        tallennaTankkaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double getMaara = Double.valueOf(maara.getText().toString());
                int getMittarilukemanSyotto = Integer.valueOf(mittarilukemanSyotto.getText().toString());
                double getHinnanSyotto = 0;
                String getTankkauspaiva = tankkauspaiva.getText().toString();

                if(hinnanSyotto.getText().length() > 0) {
                    getHinnanSyotto = Double.valueOf(hinnanSyotto.getText().toString());
                }

                tallenna(valittuID, getMaara,getMittarilukemanSyotto,getHinnanSyotto,getTankkauspaiva);

                suljeNappaimisto();

                tilastoihin();
            }
        });
    }

    public void tallenna(int valittuID, double getMaara, int getMittarilukemanSyotto, double getHinnanSyotto, String getTankkauspaiva){

        Tankkauskanta tankkauskanta = new Tankkauskanta(TankkausnakymaMuokkaus.this);
        boolean onnistui = tankkauskanta.muokkaaTankkausta(valittuID, getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva, getVuosiUudestaTankkauspaivasta(getTankkauspaiva), getKuukausiUudestaTankkauspaivasta(getTankkauspaiva));

        Toast.makeText(TankkausnakymaMuokkaus.this, "Muokkaus onnistui", Toast.LENGTH_SHORT).show();

    }

    public int getVuosiUudestaTankkauspaivasta(String uusiTankkauspaiva){
        String[] vuosiStringina = uusiTankkauspaiva.split("\\.");
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

    public int getKuukausiUudestaTankkauspaivasta(String uusiTankkauspaiva){
        String[] kuukausiStringina = uusiTankkauspaiva.split("\\.");
        if (kuukausiStringina[1].matches(("0[1-9]{1}"))){
            String[] kuukausiIlmanNollaa = kuukausiStringina[1].split("");
            int kuukausi = Integer.valueOf(kuukausiIlmanNollaa[kuukausiIlmanNollaa.length - 1]);
            return kuukausi;
        } else{
            int kuukausi = Integer.valueOf(kuukausiStringina[1]);
            return kuukausi;
        }
    }

    private void suljeNappaimisto() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void listaan(int valittuID){
        tankkauslista = new ArrayList<>();
        tankkauslista = tietokanta.yksiTankkaus(valittuID);
    }

    public void tilastoihin(){
        Intent intent = new Intent(this, Tilastot.class);
        startActivity(intent);
        finish();
    }

    public void setIntentit(){
        Intent intent = getIntent();
        valittuID = (int) intent.getSerializableExtra("tankkausID");
    }
}