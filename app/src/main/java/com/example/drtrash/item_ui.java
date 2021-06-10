package com.example.drtrash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class item_ui extends AppCompatActivity {

    public static final String ITEM_TITLE_KEY = "objTitle";

    private RecyclerView recView;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    private ConstraintLayout searchItemViewLayout;
    private ImageView titleImg;
    private TextView itemTitleTxt,typeTxt,solidTypeText,classTxt,optionTxt,waysDisposal,waysRisk,linksReuse, linksRisk;
    private Toolbar toolbarItemList;

    @Override // All the functionalities here will run once there is an instance of this class
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        searchItemViewLayout = findViewById(R.id.searchItemViewLayout);
        recView = findViewById(R.id.recomView);
        recView.setLayoutManager(new LinearLayoutManager(item_ui.this, RecyclerView.HORIZONTAL, true));

        toolbarItemList = findViewById(R.id.toolbarItemList);
        setSupportActionBar(toolbarItemList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleImg = findViewById(R.id.itemImage);
        itemTitleTxt = findViewById(R.id.itemTitleTxt);
        typeTxt = findViewById(R.id.typeTxt);
        classTxt = findViewById(R.id.classTxt);
        solidTypeText = findViewById(R.id.solidTypeTxt);
        optionTxt = findViewById(R.id.optionTxt);
        waysDisposal = findViewById(R.id.waysDiposalTxtBox);
        waysRisk = findViewById(R.id.waysRiskTxtBox);
        linksReuse = findViewById(R.id.linksReuse);
        linksRisk = findViewById(R.id.linksRisk);

        Bundle extras = getIntent().getExtras();
        String titleKey = extras.getString(ITEM_TITLE_KEY) ;

        displayData(titleKey);
        recommendedView(titleKey);
        search(searchItemViewLayout);

    }

    // this methods holds the functionality for getting data from the database and appending it to the arraylist
    public void displayData(String titleKey){

        DocumentReference docRef = dataBase.collection("Items").document(titleKey);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                if(task.isSuccessful()){

                    String title = document.getString( "title");
                    String classification = document.getString("classification");
                    String imageUrl = document.getString("img download url");
                    String type = document.getString("type");
                    String option = document.getString("bio or non bio");
                    String wayDisposal = document.getString("way of disposal").trim();
                    String wayRisk = document.getString("way of disposal with risk").trim();
                    String typeWaste = document.getString("type of solid wastes");
                    String linkReuse = document.getString("link for reuse");
                    String linkRisk = document.getString("link for risk");

                    /*Toast.makeText(item_ui.this,title, Toast.LENGTH_LONG).show();*/

                    Glide.with(item_ui.this)
                            .asBitmap()
                            .load(imageUrl)
                            .into(titleImg);

                    if(type.equals("Solid")){
                        
                        solidTypeText.setVisibility(View.VISIBLE);
                        solidTypeText.setText(typeWaste);

                    }

                        if (linkRisk.contentEquals("") && linkReuse.contentEquals("")){

                            linksRisk.setText("No source link added");
                            linksReuse.setText("No source link added");

                        }
                        else if( linkReuse.contentEquals("")){

                            linksReuse.setText("No source link added");
                            linksRisk.setText(linkRisk);
                            Linkify.addLinks(linksRisk,Linkify.ALL);

                        }
                        else if(linkRisk.contentEquals("")){

                            linksRisk.setText("No source link added");
                            linksReuse.setText(linkReuse);
                            Linkify.addLinks(linksReuse, Linkify.ALL);

                        }
                        else{

                            linksRisk.setText(linkRisk);
                            Linkify.addLinks(linksRisk,Linkify.ALL);

                            linksReuse.setText(linkReuse);
                            Linkify.addLinks(linksReuse, Linkify.ALL);

                        }

                    itemTitleTxt.setText(title);
                    typeTxt.setText(type);
                    classTxt.setText(classification);
                    optionTxt.setText(option);
                    typeTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String typeString = type + " Waste";
                            showDialog(typeString);
                        }
                    });

                    solidTypeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(typeWaste);
                        }
                    });

                    classTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String classString = classification + " Waste";
                            showDialog(classString);
                        }
                    });

                    optionTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String optionString = option + " Waste";
                            showDialog(optionString);
                        }
                    });


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        waysDisposal.setText(Html.fromHtml(wayDisposal,Html.FROM_HTML_MODE_COMPACT));
                    }
                    else {
                        waysDisposal.setText(Html.fromHtml(wayDisposal));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        waysRisk.setText(Html.fromHtml(wayRisk,Html.FROM_HTML_MODE_COMPACT));
                    }
                    else {
                        waysRisk.setText(Html.fromHtml(wayRisk));
                    }


                }
                else{

                    Toast.makeText(item_ui.this,task.getException().toString(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    // this methods restarts the activity everytime there is an instance of this class
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    // this methods holds the functionalities for the information in tags
    private void showDialog (String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(item_ui.this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.information_dialog_box, null);

        AppCompatTextView titleInfo = customView.findViewById(R.id.titleInfo);
        AppCompatTextView inputInfo = customView.findViewById(R.id.inputInfo);

        builder.setNegativeButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        titleInfo.setText(data);

        switch (data){

            case "Biodegradable Waste":{

                inputInfo.setText(getText(R.string.bio_info));
                break;
            }

            case "Non-Biodegradable Waste":{

                inputInfo.setText(getText(R.string.nonBio_info));
                break;
            }

            case "Biodegradable or Non-Biodegradable Waste":{

                inputInfo.setText(getText(R.string.nonOrBio_info));
                break;
            }

            case "Glass or Ceramic":{

                inputInfo.setText(getText(R.string.glass_info));
                break;
            }

            case "Plastic Waste":{

                inputInfo.setText(getText(R.string.plastic_info));
                break;
            }

            case "Paper Rubbish":{

                inputInfo.setText(getText(R.string.paper_info));
                break;
            }

            case "Metal and Tins":{

                inputInfo.setText(getText(R.string.metal_info));
                break;
            }

            case "Industrial Waste":{

                inputInfo.setText(getText(R.string.industrial_info));
                break;
            }

            case "Commercial Waste":{

                inputInfo.setText(getText(R.string.commercial_info));
                break;
            }

            case "Domestic Waste":{

                inputInfo.setText(getText(R.string.domestic_info));
                break;
            }

            case "Agricultural Waste":{

                inputInfo.setText(getText(R.string.agricultural_info));
                break;
            }

            case "Solid Waste":{

                inputInfo.setText(getText(R.string.solid_info));
                break;

            }

            case "Organic Waste":{

                inputInfo.setText(getText(R.string.organic_info));
                break;
            }

            case "Hazardous Waste":{

                inputInfo.setText(getText(R.string.hazardous_info));
                break;
            }

            case "Liquid Waste":{

                inputInfo.setText(getText(R.string.liquid_info));
                break;
            }

        }

        builder.setView(customView);
        builder.create();
        builder.show();

    }

    // this method starts an intent towards the searchUI class
    private void search(View searchItemViewLayout){

        searchItemViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(item_ui.this, searchUI.class);
                startActivity(callIntent);

            }
        });



    }

    // this method is responsible for getting data from the database and appending it to the recommended recyclerview
    private void recommendedView(String titleKey){

        ArrayList<objItemModel> objItem = new ArrayList<>();

        dataBase.collection("Items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                    String title = documentSnapshot.getString("title");
                    String classification = documentSnapshot.getString("classification");
                    String imageUrl = documentSnapshot.getString("img download url");
                    String type = documentSnapshot.getString("type");
                    String views = documentSnapshot.getString("views");

                    if(!title.equals(titleKey)){

                        objItem.add(new objItemModel(title, type, classification, imageUrl, views));
                        recommendedRecViewAdapter adapter = new recommendedRecViewAdapter(item_ui.this);
                        Collections.shuffle(objItem);
                        adapter.setObjModel(objItem);
                        recView.setAdapter(adapter);
                        recView.setLayoutManager(new LinearLayoutManager(item_ui.this, LinearLayoutManager.HORIZONTAL,false));

                    }
                    else{

                        /*Toast.makeText(item_ui.this, "Continue", Toast.LENGTH_SHORT).show();*/

                    }

                }

            }
        });

    }

}