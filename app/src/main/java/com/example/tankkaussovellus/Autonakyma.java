package com.example.tankkaussovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Autonakyma extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autonakyma);

        final EditText mallinSyotto = (EditText) findViewById(R.id.mallinSyotto);
        final Button ok = (Button) findViewById(R.id.autonakymaOK);
        final CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String malli = mallinSyotto.getText().toString();
                Boolean oletusauto = false; //Tietoa auton oletuksesta ei käytetä vielä mihinkään
                if (checkbox.isChecked()) {
                    oletusauto = true;
                }

                tallenna(malli, oletusauto);

                mallinSyotto.setText("");
                checkbox.setChecked(false);

                suljeNappaimisto();
            }
        });
    }


    public void tallenna(String malli, boolean oletusauto){

        Auto auto = new Auto(-1, malli, oletusauto);
        Tankkauskanta tankkauskanta = new Tankkauskanta(Autonakyma.this);

        boolean onnistui = tankkauskanta.lisaaAuto(auto);
        Toast.makeText(Autonakyma.this, "Tallennus onnistui", Toast.LENGTH_SHORT).show();

    }

    private void suljeNappaimisto() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}