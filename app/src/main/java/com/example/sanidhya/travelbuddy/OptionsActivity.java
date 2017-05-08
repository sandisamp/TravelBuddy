package com.example.sanidhya.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Sanidhya on 25-02-2017.
 */

public class OptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        //Toast.makeText(getApplicationContext(),"welcome!",Toast.LENGTH_LONG).show();

    try{
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("usr_id");
        Button tomap=(Button) findViewById(R.id.tomap_butt);
        Button towallet=(Button) findViewById((R.id.towallet_butt));
        Button hotels=(Button)findViewById(R.id.hotels);
        tomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Coming soon!",Toast.LENGTH_LONG).show();
                Intent map= new Intent(OptionsActivity.this,MapsActivity.class);
                startActivity(map);
            }
        });
        towallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wallet= new Intent(OptionsActivity.this,WalletActivity.class);
                wallet.putExtra("usr_id",id);
                startActivity(wallet);
                //Toast.makeText(getApplicationContext(),"Coming soon!",Toast.LENGTH_LONG).show();
            }
        });
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  Intent hotels= new Intent(OptionsActivity.this,hotelsActivity.class);
                startActivity(hotels);   }
        });
    }catch (Exception e){
        Toast.makeText(getApplicationContext(), String.valueOf(e),Toast.LENGTH_LONG).show();
    }

}}
