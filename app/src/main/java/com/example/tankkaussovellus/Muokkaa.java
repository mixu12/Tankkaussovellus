package com.example.tankkaussovellus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Muokkaa extends AppCompatActivity {

    public ListView listView;
    ArrayAdapter arrayAdapter;
    Tankkauskanta tietokanta;

    List<Tankkaus> tankkauslista;
    ArrayList<String> tankkaustulokset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muokkaa);

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        registerForContextMenu(listView);

        tietokanta = new Tankkauskanta(Muokkaa.this);
        listaan();

        paivitaLista();

    }

    private void paivitaLista() {
        listaan();
        arrayAdapter = new ArrayAdapter(Muokkaa.this, R.layout.custom_layout, tankkauslista);
        listView.setAdapter(arrayAdapter);

    }

    public void listaan() {
        tankkauslista = new ArrayList<>();
        tankkauslista = tietokanta.kaikkiTankkaukset();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.listview_menu, menu);
        menu.setHeaderTitle("Valitse toiminto");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Tankkaus klikattu = (Tankkaus) listView.getItemAtPosition(menuInfo.position);
        switch (item.getItemId()){
            case R.id.poista:
                tietokanta.poistaValittu(klikattu);
                paivitaLista();
                return true;
            case R.id.muokkaa:
                tankkausnakymaMuokkaus(klikattu);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void tankkausnakymaMuokkaus(Tankkaus klikattu){
        Intent intent = new Intent(this, TankkausnakymaMuokkaus.class);
        intent.putExtra("tankkausID", klikattu.getId());
        startActivity(intent);
    }

}