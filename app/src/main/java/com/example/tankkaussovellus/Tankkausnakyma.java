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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Tankkausnakyma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankkausnakyma);

        final EditText maara = (EditText) findViewById(R.id.maara);
        final EditText mittarilukemanSyotto = (EditText) findViewById(R.id.mittarilukemanSyotto);
        final EditText hinnanSyotto = (EditText) findViewById(R.id.hinnanSyotto);
        final EditText tankkauspaiva = (EditText) findViewById(R.id.tankkauspaiva);

        final Button tallennaTankkaus = (Button) findViewById(R.id.tallennaTankkaus);

        tankkauspaiva.setText(Paiva());

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

                tallenna(getMaara,getMittarilukemanSyotto,getHinnanSyotto,getTankkauspaiva);

                maara.setText("");
                mittarilukemanSyotto.setText("");
                hinnanSyotto.setText("");
                tankkauspaiva.setText(Paiva());

                suljeNappaimisto();
            }
        });
    }

    public void tallenna(double getMaara, int getMittarilukemanSyotto, double getHinnanSyotto, String getTankkauspaiva){

        Tankkaus tankkaus = new Tankkaus(-1, getMaara, getMittarilukemanSyotto, getHinnanSyotto, getTankkauspaiva);

        Tankkauskanta tankkauskanta = new Tankkauskanta(Tankkausnakyma.this);
        boolean onnistui = tankkauskanta.lisaaTankkaus(tankkaus);
        long testi = tankkauskanta.createTankkaustiedot(1,1);
        Toast.makeText(Tankkausnakyma.this, "Tallennus onnistui", Toast.LENGTH_SHORT).show();

    }

    private void suljeNappaimisto() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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
}