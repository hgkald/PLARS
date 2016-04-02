package com.engo551.plars.app;

import android.net.Uri;
import android.content.res.AssetManager;
import android.util.Log;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


public class SecondActivity extends AppCompatActivity {

    private LocalDbHelper ResListDb;
    private RestaurantDatabaseHelper ResDb;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ResListDb = new LocalDbHelper(this);
        ResDb= new RestaurantDatabaseHelper(this);
        SQLiteDatabase db = ResDb.getReadableDatabase();
        String[] columns = {"ID", "NAME", "Latitude", "Longitude"};
        Cursor cursor = db.query("restaurants", columns, null, null, null, null, null);
        List<String[]> ResList = read("reslist.csv");
        Pair[] sort = SortIndex(CalculateIndex());


        String[] resNames = {ResList.get(sort[1].sort)[0],ResList.get(sort[2].sort)[0],ResList.get(sort[3].sort)[0],ResList.get(sort[4].sort)[0],
                ResList.get(sort[5].sort)[0],ResList.get(sort[6].sort)[0],ResList.get(sort[7].sort)[0],ResList.get(sort[8].sort)[0],
                ResList.get(sort[9].sort)[0],ResList.get(sort[10].sort)[0],ResList.get(sort[11].sort)[0]};
        ListAdapter resAdapter = new CustomAdapter(this, resNames);

        ListView resListview = (ListView) findViewById(R.id.SecActListRecom);
        resListview.setAdapter(resAdapter);

 /*       resListview.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String type;
                        type = ResList.get(sort[position].sort)[8];
                        *//*whenever the user click on an item, it gets the position of the item and pass it as int position*//*
                        *//*the string res is the text info needs to be uploaded*//*
                        boolean isinserted = ResListDb.insertRecord(type);
                        if (isinserted == true)
                            Toast.makeText(SecondActivity.this, "data inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SecondActivity.this, "data not inserted", Toast.LENGTH_LONG).show();

                    }
                }
        );*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public List<Double> CalculateIndex(){
        ResDb= new RestaurantDatabaseHelper(this);
        SQLiteDatabase db = ResDb.getReadableDatabase();

        double ulat = 56.0486;
        double ulon = -113.0708;
        /*
        * double italian_pref=;
        * */

        List<Double> index = new ArrayList<Double>();

        String[] columns = {"ID", "NAME","RATING", "Latitude", "Longitude"};
        Cursor cursor = db.query("restaurants", columns, null, null, null, null, null);
        try {

            while (cursor.moveToNext()) {
                 double lat = cursor.getDouble(3);
                 double lon = cursor.getDouble(4);
                 double rating = cursor.getDouble(2);
                 double distance = Math.sqrt((lat-ulat)*(lat-ulat)+(lon-ulon)*(lon-ulon));

                /*add a switch method for the preference index*/
                 index.add(rating/distance*1000);
                 /*Log.d("test",cursor.getString(1));*/

            }
        }
        finally {
            cursor.close();
        }
        return index;
    }



    public Pair[] SortIndex(List<Double> index){
        Pair [] sort = new Pair[index.size()];
        for (int i=0; i<index.size(); i++){
            sort[i]= new Pair(i,index.get(i));

        }

        Arrays.sort(sort);
        for (int j=0; j<index.size();j++){
            Log.d("pair", String.valueOf(sort[j].sort));
        }
        return sort;
    }

    public List<String[]> read(String name){

        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open(name)));//Specify asset file name
//in open();
            int i =0;
            for(;;) {
                next = reader.readNext();
                if(next != null) {
                    list.add(next);
                } else {
                    break;
                }

                Log.d("list", list.get(i)[1]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();}

        return list;

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.engo551.plars.app/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.engo551.plars.app/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}




