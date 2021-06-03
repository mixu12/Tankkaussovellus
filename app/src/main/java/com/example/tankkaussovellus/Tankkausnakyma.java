package com.example.tankkaussovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Tankkausnakyma extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    List<Auto> autotListaan;

    int valitunAutonID = 0;

    Tankkauskanta tietokanta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankkausnakyma);

        tietokanta = new Tankkauskanta(Tankkausnakyma.this);

        final EditText maara = (EditText) findViewById(R.id.maara);
        final EditText mittarilukemanSyotto = (EditText) findViewById(R.id.mittarilukemanSyotto);
        final EditText hinnanSyotto = (EditText) findViewById(R.id.hinnanSyotto);
        final EditText tankkauspaiva = (EditText) findViewById(R.id.tankkauspaiva);

        final Button tallennaTankkaus = (Button) findViewById(R.id.tallennaTankkaus);

        asetaAlkuarvot(maara, mittarilukemanSyotto, hinnanSyotto, tankkauspaiva);

        tankkauspaiva.setText(Paiva());
        autotListaan();
        spinneri();

        tallennaTankkaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Konstruktoreiden ja hinnansyötön tarkastuksen puolesta on mahdollista antaa tankkaus ilman hintaa.
                // Tätä ei kuitenkaan sallita, koska muuten keskihinnan ja kokonaishinnan laskennat eivät toimi.
                if (!maara.getText().toString().equals("") && !mittarilukemanSyotto.getText().toString().equals("")
                        && !hinnanSyotto.getText().toString().equals("") && !tankkauspaiva.getText().toString().equals("")) {
                
                    double getMaara = Double.valueOf(maara.getText().toString());
                    int getMittarilukemanSyotto = Integer.valueOf(mittarilukemanSyotto.getText().toString());
                    double getHinnanSyotto = 0;
                    String getTankkauspaiva = tankkauspaiva.getText().toString();

                    if(hinnanSyotto.getText().length() > 0) {
                        getHinnanSyotto = Double.valueOf(hinnanSyotto.getText().toString());
                    }

                    tallenna(getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva);

                    suljeNappaimisto();

                    asetaAlkuarvot(maara, mittarilukemanSyotto, hinnanSyotto, tankkauspaiva);
                } else {
                    Toast.makeText(Tankkausnakyma.this, "Anna kaikki tiedot", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void asetaAlkuarvot(EditText maara, EditText mittarilukemanSyotto, EditText hinnanSyotto, EditText tankkauspaiva){

        maara.setText("");
        mittarilukemanSyotto.setText("");
        hinnanSyotto.setText("");
        tankkauspaiva.setText(Paiva());
    }

    public void tallenna(double getMaara, int getMittarilukemanSyotto, double getHinnanSyotto, String getTankkauspaiva){

        Tankkaus tankkaus = new Tankkaus(-1, getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva);

        Tankkauskanta tankkauskanta = new Tankkauskanta(Tankkausnakyma.this);
        boolean onnistui = tankkauskanta.lisaaTankkaus(tankkaus);
        long testi = tankkauskanta.createTankkaustiedot(valitunAutonID,1);
        Toast.makeText(Tankkausnakyma.this, "Tallennus onnistui", Toast.LENGTH_SHORT).show();

    }

    private void suljeNappaimisto() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Hakee oletuksesi kuluvan päivän.
    public String Paiva() {
        ZonedDateTime aika = ZonedDateTime.now();
        DateTimeFormatter muokkaaja = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return aika.format(muokkaaja);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ylamenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.muokkaa:
                Intent intent = new Intent(Tankkausnakyma.this, Muokkaa.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Tästä alkaa autovalikon koodi
    public void autotListaan(){
        autotListaan = new ArrayList<>();
        autotListaan = tietokanta.kaikkiAutot();

    }

    public void spinneri(){
        Spinner spinner = findViewById(R.id.spinnerAuto);
        ArrayAdapter<Auto> adapter = new ArrayAdapter<Auto>(this, android.R.layout.simple_spinner_dropdown_item, autotListaan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String valittuAuto = parent.getItemAtPosition(position).toString();

        valitunAutonID = tietokanta.valitunAutonID(valittuAuto);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}