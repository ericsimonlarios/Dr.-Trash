package com.example.drtrash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class item_list_tab extends AppCompatActivity {


    private RecyclerView objRecView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Items");

    private Toolbar toolbarHome;
    private TypeWriter typeWriter;

    @Override // All the functionalities here will run once there is an instance of this class
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_tab);

        typeWriter = findViewById(R.id.typeWriterTxt);

        toolbarHome = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(item_list_tab.this, searchUI.class);
                startActivity(callIntent);

            }
        });

        objRecView = findViewById(R.id.objRecView);

        typeWriter(typeWriter);
        getData();

    }

    // customized text view that has an effect of a typewriter
    private void typeWriter(TypeWriter typeWriter) {

        typeWriter.setText("");
        typeWriter.setCharacterDelay(50);
        typeWriter.animateText("Find out efficient ways of \ndisposing garbage waste!");

    }

    @Override // this methods restarts the activity everytime there is an instance of this class
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    // method responsible for fetching data from the database and displaying it to the recyclerview
    private void getData() {

        ArrayList<objItemModel> itemModels = new ArrayList<>();

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String title = document.getString("title");
                        String classification = document.getString("classification");
                        String imageUrl = document.getString("img download url");
                        String type = document.getString("type");
                        String views = document.getString("views");
                        itemModels.add(new objItemModel(title, type, classification, imageUrl, views));

                        ObjRecViewAdapter adapter = new ObjRecViewAdapter(item_list_tab.this);
                        Collections.sort(itemModels);
                        adapter.setItemModel(itemModels);
                        objRecView.setAdapter(adapter);
                        objRecView.setLayoutManager(new LinearLayoutManager(item_list_tab.this));

                    }
                }

            }
        });

    }

}