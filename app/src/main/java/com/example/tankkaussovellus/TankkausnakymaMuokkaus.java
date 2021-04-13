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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankkausnakyma);

        tietokanta = new Tankkauskanta(TankkausnakymaMuokkaus.this);

        Intent intent = getIntent();
        final int valittuID = (int) intent.getSerializableExtra("tankkausID");

        listaan(valittuID);

        System.out.println(tankkauslista.size());
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

        Tankkaus tankkaus = new Tankkaus(-1, getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva);

        Tankkauskanta tankkauskanta = new Tankkauskanta(TankkausnakymaMuokkaus.this);
        boolean onnistui = tankkauskanta.muokkaaTankkausta(valittuID, getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva);
        Toast.makeText(TankkausnakymaMuokkaus.this, "Muokkaus onnistui", Toast.LENGTH_SHORT).show();

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
        System.out.println(tankkauslista);
    }

    public void tilastoihin(){
        Intent intent = new Intent(this, Tilastot.class);
        startActivity(intent);
    }
}