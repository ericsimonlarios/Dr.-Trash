package com.example.drtrash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class homeMainUI extends AppCompatActivity {

    MaterialCardView objBrowse, objCreate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main_u_i);

        objBrowse = findViewById(R.id.browseObj);
        objBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseUI();
            }
        });

        objCreate = findViewById(R.id.createObj);
        objCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUI();
            }
        });

    }

    public void browseUI(){

        Intent callIntent = new Intent(this, item_list_tab.class);
        startActivity(callIntent);

    }

    public void createUI(){

        Intent callIntent = new Intent(this, createEntry.class);
        startActivity(callIntent);

    }
}