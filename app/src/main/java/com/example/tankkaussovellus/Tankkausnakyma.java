package com.example.tankkaussovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Tankkausnakyma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankkausnakyma);

        final EditText maara = (EditText) findViewById(R.id.maara);
        final EditText mittarilukemanSyotto = (EditText) findViewById(R.id.mittarilukemanSyotto);
        final EditText hinnanSyotto = (EditText) findViewById(R.id.hinnanSyotto);

        final Button tallennaTankkaus = (Button) findViewById(R.id.tallennaTankkaus);



        tallennaTankkaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double getMaara = Double.valueOf(maara.getText().toString());
                int getMittarilukemanSyotto = Integer.valueOf(mittarilukemanSyotto.getText().toString());
                double getHinnanSyotto = 0;

                if(hinnanSyotto.getText().length() > 0) {
                    getHinnanSyotto = Double.valueOf(hinnanSyotto.getText().toString());
                }

                tallenna(getMaara,getMittarilukemanSyotto,getHinnanSyotto);

                maara.setText("");
                mittarilukemanSyotto.setText("");
                hinnanSyotto.setText("");

                suljeNappaimisto();
            }
        });
    }

    public void tallenna(double getMaara, int getMittarilukemanSyotto, double getHinnanSyotto){

        Tankkaus tankkaus = new Tankkaus(-1, getMaara, getMittarilukemanSyotto, getHinnanSyotto);

        Tankkauskanta tankkauskanta = new Tankkauskanta(Tankkausnakyma.this);
        boolean onnistui = tankkauskanta.lisaaTankkaus(tankkaus);
        Toast.makeText(Tankkausnakyma.this, "Tallennus onnistui", Toast.LENGTH_SHORT).show();

    }

    private void suljeNappaimisto() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}