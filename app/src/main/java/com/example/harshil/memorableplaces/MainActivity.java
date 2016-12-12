package com.example.harshil.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> places=new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static ArrayList<LatLng> locations=new ArrayList<>();
    static Set<String> set;
    static SharedPreferences sharedPreferences;
    public void addCity(View view){
        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra("locationInfo",places.size()-1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView)findViewById(R.id.listview);
        sharedPreferences=this.getSharedPreferences("com.example.harshil.memorableplaces", Context.MODE_PRIVATE);
        set=sharedPreferences.getStringSet("notes",null);
        places.clear();
        if(set!=null){
            places.addAll(set);
        }else {
            places.add("Example Place");
            set=new HashSet<String>();
            set.addAll(places);
            sharedPreferences.edit().remove("places").apply();

            sharedPreferences.edit().putStringSet("places",set).apply();
        }
        locations.add(new LatLng(0,0));


        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,places);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("locationInfo",i);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                places.remove(i);
                if(MainActivity.set==null)
                {MainActivity.set=new HashSet<String>();}
                else{
                    MainActivity.set.clear();}
                MainActivity.set.addAll(MainActivity.places);
                sharedPreferences.edit().remove("places").apply();
                MainActivity.sharedPreferences.edit().putStringSet("places",MainActivity.set).apply();
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Place Deleted",Toast.LENGTH_SHORT).show();
                return false;    }
        });
    }
}