package com.example.tankkaussovellus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Tankkauskanta extends SQLiteOpenHelper{

    public static final String TANKKAUSTIEDOT = "TANKKAUSTIEDOT";
    public static final String COLUMN_IDTANKKAUSTIEDOT = "ID";
    public static final String COLUMN_TANKKAUSID = "TANKKAUSID";
    public static final String COLUMN_AUTOID_TANKKAUSTIEDOT = "AUTOID";
    public static final String COLUMN_TANKKAUSASEMAID_TANKKAUSTIEDOT = "TANKKAUSASEMAID";

    public static final String TANKKAUKSET = "TANKKAUKSET";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MAARA = "MAARA";
    public static final String COLUMN_MITTARILUKEMA = "MITTARILUKEMA";
    public static final String COLUMN_KOKONAISHINTA = "KOKONAISHINTA";
    public static final String COLUMN_PAIVA = "PAIVA";
    public static final String COLUMN_VUOSI = "VUOSI";
    public static final String COLUMN_KUUKAUSI = "KUUKAUSI";

    public static final String AUTO = "AUTO";
    public static final String COLUMN_IDAUTO = "ID";
    public static final String COLUMN_AUTONMALLI = "MALLI";
    public static final String COLUMN_OLETUS = "OLETUS";

    public Tankkauskanta(@Nullable Context context){
        super(context, "tankkaukset.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TANKKAUKSET + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MAARA + " REAL, " + COLUMN_MITTARILUKEMA + " INTEGER, "
                + COLUMN_KOKONAISHINTA + " REAL, " + COLUMN_PAIVA + " TEXT, " + COLUMN_VUOSI + " INTEGER, " + COLUMN_KUUKAUSI + " INTEGER)";

        db.execSQL(createTableStatement);

        String createTableStatement2 = "CREATE TABLE " + AUTO + " (" + COLUMN_IDAUTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_AUTONMALLI + " STRING, " + COLUMN_OLETUS + " BOOL)";

        db.execSQL(createTableStatement2);

        String createTableStatement3 = "CREATE TABLE " + TANKKAUSTIEDOT + " (" + COLUMN_IDTANKKAUSTIEDOT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TANKKAUSID + " INTEGER, " + COLUMN_AUTOID_TANKKAUSTIEDOT + " INTEGER, " + COLUMN_TANKKAUSASEMAID_TANKKAUSTIEDOT + " INTEGER)";

        db.execSQL(createTableStatement3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int aiempi, int uudempi) {

    }

    public long createTankkaustiedot(long autoid, long tankkausasemaid){

        int viimeisinID = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        String queryStringViimeisin = "SELECT MAX(" + COLUMN_ID + ") as viimeisinID FROM " + TANKKAUKSET;

        Cursor cursor = db.rawQuery(queryStringViimeisin, null);

        if (cursor.moveToFirst()) {
            do {
                viimeisinID = cursor.getInt(cursor.getColumnIndex("viimeisinID"));
            } while (cursor.moveToNext());
        }

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TANKKAUSID, viimeisinID);
        cv.put(COLUMN_AUTOID_TANKKAUSTIEDOT, autoid);
        cv.put(COLUMN_TANKKAUSASEMAID_TANKKAUSTIEDOT, tankkausasemaid);

        long id = db.insert(TANKKAUSTIEDOT, null, cv);

        return id;
    }

    public boolean lisaaTankkaus(Tankkaus tankkaus){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MAARA, tankkaus.getMaara());
        cv.put(COLUMN_MITTARILUKEMA, tankkaus.getMittarilukema());
        cv.put(COLUMN_KOKONAISHINTA, tankkaus.getKokonaishinta());
        cv.put(COLUMN_PAIVA, tankkaus.getPaiva());
        cv.put(COLUMN_VUOSI, tankkaus.getVuosi());
        cv.put(COLUMN_KUUKAUSI, tankkaus.getKuukausi());

        long insert = db.insert(TANKKAUKSET, null, cv);

        if (insert == -1){
            return false;
        } else{
            return true;
        }
    }

    public boolean poistaValittu(Tankkaus tankkaus){

        int poistettavaID = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        String queryStringPoistettava = "SELECT " + COLUMN_ID + " as poistettavaID FROM " + TANKKAUKSET + " WHERE " + COLUMN_ID + " = " + tankkaus.getId();

        Cursor cursor = db.rawQuery(queryStringPoistettava, null);

        if (cursor.moveToFirst()) {
            do {
                poistettavaID = cursor.getInt(cursor.getColumnIndex("poistettavaID"));
            } while (cursor.moveToNext());
        }

        String queryString = "DELETE FROM " + TANKKAUKSET + " WHERE " + COLUMN_ID + " = " + tankkaus.getId();

        Cursor cursor2 = db.rawQuery(queryString, null);

        poistaValittuTankkaustiedoista(poistettavaID);

        if (cursor2.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }

    public boolean poistaValittuTankkaustiedoista(int poistettavaID){

        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + TANKKAUSTIEDOT + " WHERE " + COLUMN_TANKKAUSID + " = " + poistettavaID;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }

    public boolean muokkaaTankkausta(int valittuID, double maara, int mittarilukema, double kokonaishinta, String uusiPaiva, int tankkausvuosi, int tankkauskuukausi) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "UPDATE " + TANKKAUKSET + " SET " + COLUMN_MAARA + " = ?," + COLUMN_MITTARILUKEMA + " = ?," + COLUMN_KOKONAISHINTA + " = ?," + COLUMN_PAIVA + " = ?," + COLUMN_VUOSI + " = " + tankkausvuosi + ","+ COLUMN_KUUKAUSI + " = " + tankkauskuukausi + " WHERE " + COLUMN_ID + " = " + valittuID;
        String[] valinnat = {String.valueOf(maara), String.valueOf(mittarilukema), String.valueOf(kokonaishinta), uusiPaiva};

        Cursor cursor = db.rawQuery(queryString, valinnat);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public List<Tankkaus> kaikkiTankkaukset(){
        List<Tankkaus> tankkauslista = new ArrayList<>();

        String queryString = "SELECT * FROM " + TANKKAUKSET;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Käy läpi kaikki tallennetut tiedot ja luo uuden nimike-olion. Laittaa ne sitten listaan, joka palautetaan.
            do {
                int id = cursor.getInt(0);
                double maara = cursor.getDouble(1);
                int mittarilukemanSyotto = cursor.getInt(2);
                double kokonaishinta = cursor.getDouble(cursor.getColumnIndex(COLUMN_KOKONAISHINTA));
                String paiva = cursor.getString(cursor.getColumnIndex(COLUMN_PAIVA));

                Tankkaus uusiTankkaus = new Tankkaus(id, maara, mittarilukemanSyotto, kokonaishinta, paiva);
                tankkauslista.add(uusiTankkaus);

            } while (cursor.moveToNext());

        } else {
            // Jos tulee virhe
        }
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return tankkauslista;
    }

    public List<Tankkaus> yksiTankkaus(int valittuID){
        List<Tankkaus> tankkauslista = new ArrayList<>();
        String queryString = "SELECT * FROM " + TANKKAUKSET + " WHERE " + COLUMN_ID + " = " + valittuID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Käy läpi kaikki tallennetut tiedot ja luo uuden nimike-olion. Laittaa ne sitten listaan, joka palautetaan.
            do {
                int id = cursor.getInt(0);
                double maara = cursor.getDouble(1);
                int mittarilukemanSyotto = cursor.getInt(2);
                double kokonaishinta = cursor.getDouble(cursor.getColumnIndex(COLUMN_KOKONAISHINTA));
                String paiva = cursor.getString(cursor.getColumnIndex(COLUMN_PAIVA));

                Tankkaus uusiTankkaus = new Tankkaus(id, maara, mittarilukemanSyotto, kokonaishinta, paiva);
                tankkauslista.add(uusiTankkaus);
            } while (cursor.moveToNext());

        } else {
            // Jos tulee virhe
        }
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return tankkauslista;
    }

    public ArrayList<String> vuodet(){
        ArrayList<String> vuodet = new ArrayList<>();
        vuodet.add("Kaikki");
        String queryString = "SELECT * FROM " + TANKKAUKSET + " GROUP BY " + COLUMN_VUOSI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Käy läpi kaikki tallennetut tiedot ja luo uuden nimike-olion. Laittaa ne sitten listaan, joka palautetaan.
            do {
                String vuosi = cursor.getString(cursor.getColumnIndex(COLUMN_VUOSI));
                vuodet.add(vuosi);
            } while (cursor.moveToNext());

        } else {
            // Jos tulee virhe
        }
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return vuodet;
    }

    public double keskikulutus(){

        double sumTankattu = 0;
        int viimeisinMittarilukema = 0;
        int minMittarilukema = 0;
        double ekaTankattu = 0;
        // Datan haku kannasta

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT SUM(" + COLUMN_MAARA + ") as sumTankattu, MIN(" + COLUMN_MAARA + ") as minTankattu, MAX(" + COLUMN_MITTARILUKEMA + ") as viimeisinMittarilukema, MIN(" + COLUMN_MITTARILUKEMA + ") as minMittarilukema FROM " + TANKKAUKSET;
        String queryStringToinen = "SELECT " + COLUMN_MAARA + " as ekaTankattu FROM " + TANKKAUKSET + " WHERE " + COLUMN_ID + " =(SELECT MIN(" + COLUMN_ID + ") FROM " + TANKKAUKSET + ")";

        Cursor cursor = db.rawQuery(queryString, null);
        Cursor cursorToinen = db.rawQuery(queryStringToinen, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattu = cursor.getDouble(cursor.getColumnIndex("sumTankattu"));
                viimeisinMittarilukema = cursor.getInt(cursor.getColumnIndex("viimeisinMittarilukema"));
                minMittarilukema = cursor.getInt(cursor.getColumnIndex("minMittarilukema"));
            } while (cursor.moveToNext());
        }

        if (cursorToinen.moveToFirst()) {
            do {
                ekaTankattu = cursorToinen.getDouble(cursorToinen.getColumnIndex("ekaTankattu"));
            } while (cursorToinen.moveToNext());
        }

        double keskikulutus = (((sumTankattu-ekaTankattu) / (viimeisinMittarilukema - minMittarilukema)) * 100);
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return keskikulutus;
    }

    public double keskikulutus(String aikavali){

        double sumTankattu = 0;
        int viimeisinMittarilukema = 0;
        int minMittarilukema = 0;
        double ekaTankattu = 0;
        // Datan haku kannasta

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT SUM(" + COLUMN_MAARA + ") as sumTankattu, MIN(" + COLUMN_MAARA + ") as minTankattu, MAX(" + COLUMN_MITTARILUKEMA + ") as viimeisinMittarilukema, MIN(" + COLUMN_MITTARILUKEMA + ") as minMittarilukema FROM " + TANKKAUKSET + " WHERE " + COLUMN_VUOSI + " = " + aikavali;
        String queryStringToinen = "SELECT " + COLUMN_MAARA + " as ekaTankattu FROM " + TANKKAUKSET + " WHERE " + COLUMN_ID + " =(SELECT MIN(" + COLUMN_ID + ") FROM " + TANKKAUKSET + " WHERE " + COLUMN_VUOSI + " = " + aikavali + ")" + " AND " + COLUMN_VUOSI + " = " + aikavali;

        Cursor cursor = db.rawQuery(queryString, null);
        Cursor cursorToinen = db.rawQuery(queryStringToinen, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattu = cursor.getDouble(cursor.getColumnIndex("sumTankattu"));
                viimeisinMittarilukema = cursor.getInt(cursor.getColumnIndex("viimeisinMittarilukema"));
                minMittarilukema = cursor.getInt(cursor.getColumnIndex("minMittarilukema"));
            } while (cursor.moveToNext());
        }

        if (cursorToinen.moveToFirst()) {
            do {
                ekaTankattu = cursorToinen.getDouble(cursorToinen.getColumnIndex("ekaTankattu"));
            } while (cursorToinen.moveToNext());
        }

        double keskikulutus = (((sumTankattu-ekaTankattu) / (viimeisinMittarilukema - minMittarilukema)) * 100);
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return keskikulutus;
    }

    public double keskihinta(){

        double hintojenSummat = 0;
        double maaranSumma = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_KOKONAISHINTA + ") as hintojenSummat, SUM(" + COLUMN_MAARA + ") as maaranSumma FROM " + TANKKAUKSET + " WHERE " + COLUMN_KOKONAISHINTA + "> 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                hintojenSummat = cursor.getDouble(cursor.getColumnIndex("hintojenSummat"));
                maaranSumma = cursor.getDouble(cursor.getColumnIndex("maaranSumma"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();

        double keskihinta = hintojenSummat / maaranSumma;

        return keskihinta;
    }

    public double keskihinta(String aikavali){

        double hintojenSummat = 0;
        double maaranSumma = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_KOKONAISHINTA + ") as hintojenSummat, SUM(" + COLUMN_MAARA + ") as maaranSumma FROM " + TANKKAUKSET + " WHERE " + COLUMN_KOKONAISHINTA + "> 0 AND " + COLUMN_VUOSI + " = " + aikavali;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                hintojenSummat = cursor.getDouble(cursor.getColumnIndex("hintojenSummat"));
                maaranSumma = cursor.getDouble(cursor.getColumnIndex("maaranSumma"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();

        double keskihinta = hintojenSummat / maaranSumma;

        return keskihinta;
    }

    public double tankattuYhteensa(){

        double sumTankattu = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_MAARA + ") as tankattuYhteensa FROM " + TANKKAUKSET;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattu = cursor.getDouble(cursor.getColumnIndex("tankattuYhteensa"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();
        return sumTankattu;
    }

    public double tankattuYhteensa(String aikavali){

        double sumTankattu = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_MAARA + ") as tankattuYhteensa FROM " + TANKKAUKSET + " WHERE " + COLUMN_VUOSI + " = " + aikavali;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattu = cursor.getDouble(cursor.getColumnIndex("tankattuYhteensa"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();
        return sumTankattu;
    }

    public double tankattuYhteensaEuroina(){

        double sumTankattuEurot = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_KOKONAISHINTA + ") as tankattuYhteensaEuroina FROM " + TANKKAUKSET;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattuEurot = cursor.getDouble(cursor.getColumnIndex("tankattuYhteensaEuroina"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();
        return sumTankattuEurot;
    }

    public double tankattuYhteensaEuroina(String aikavali){

        double sumTankattuEurot = 0;
        // Datan haku kannasta

        String queryString = "SELECT SUM(" + COLUMN_KOKONAISHINTA + ") as tankattuYhteensaEuroina FROM " + TANKKAUKSET + " WHERE " + COLUMN_VUOSI + " = " + aikavali;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                sumTankattuEurot = cursor.getDouble(cursor.getColumnIndex("tankattuYhteensaEuroina"));
            } while (cursor.moveToNext());
        }

        // Sulkee tietokannan
        cursor.close();
        db.close();
        return sumTankattuEurot;
    }

    public boolean lisaaAuto(Auto auto){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AUTONMALLI, auto.getAutonMalli());
        cv.put(COLUMN_OLETUS, auto.getOletus());

        long insert = db.insert(AUTO, null, cv);
        if (insert == -1){
            return false;
        } else{
            return true;
        }
    }

    public List<Auto> kaikkiAutot(){
        List<Auto> autolista = new ArrayList<>();

        String queryString = "SELECT * FROM " + AUTO;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Käy läpi kaikki tallennetut tiedot ja luo uuden nimike-olion. Laittaa ne sitten listaan, joka palautetaan.
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_IDAUTO));
                String autonMalli = cursor.getString(cursor.getColumnIndex(COLUMN_AUTONMALLI));
                boolean onko = cursor.getInt(cursor.getColumnIndex(COLUMN_OLETUS)) == 1 ? true: false; // Tämä on lyhenne if-else rakenteesta. Jos ensimmäinen on totta, valitaan kysymysmerkin jälkeen tuleva, jos ei, niin sitten kaksoispisteen jälkeen oleva.

                Auto uusiAuto = new Auto(id, autonMalli, onko);
                autolista.add(uusiAuto);

            } while (cursor.moveToNext());

        } else {
            // Jos tulee virhe
        }
        // Sulkee tietokannan
        cursor.close();
        db.close();
        return autolista;
    }


}
