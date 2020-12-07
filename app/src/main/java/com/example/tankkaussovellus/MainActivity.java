package com.example.tankkaussovellus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button tankkaus = (Button) findViewById(R.id.tankkaus);
        tankkaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Tankkausnakyma.class);
                startActivity(intent);
            }
        });

        final Button tilastot = (Button) findViewById(R.id.tilastot);
        tilastot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Tilastot.class);
                startActivity(intent);
            }
        });

        final Button auto = (Button) findViewById(R.id.auto);
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Autonakyma.class);
                startActivity(intent);
            }
        });
    }


}