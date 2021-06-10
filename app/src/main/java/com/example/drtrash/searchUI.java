package com.example.drtrash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.text.Html;
import android.view.inputmethod.EditorInfo;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.widget.SearchView;

import static com.example.drtrash.item_ui.ITEM_TITLE_KEY;

public class searchUI extends AppCompatActivity {


    private RecyclerView recView;
    private searchRecViewAdapter adapter;
    private TypeWriter typeWriter;
    private SearchView searchView;
    private Toolbar homeToolBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Items");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ui);

        homeToolBar = findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeWriter = findViewById(R.id.searchTypeWriterTxt);
        recView = findViewById(R.id.searchRecView);
        searchView = findViewById(R.id.searchViewObj);

        fillData();
        typeWriter(typeWriter);
        searchViewObj(searchView);

    }

    private void typeWriter(TypeWriter typeWriter){

        typeWriter.setText("");
        typeWriter.setCharacterDelay(50);
        typeWriter.animateText("A search bar for you to \nfind tips easily!");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    private void fillData(){

        ArrayList<objItemModel> objModel = new ArrayList<>();

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document: task.getResult()){

                        String title = document.getString( "title");
                        String classification = document.getString("classification");
                        String imageUrl = document.getString("img download url");
                        String type = document.getString("type");
                        String views = document.getString("views");
                        objModel.add(new objItemModel(title,type,classification,imageUrl,views));

                        adapter = new searchRecViewAdapter(searchUI.this);
                        Collections.shuffle(objModel);
                        adapter.setObjList(objModel);
                        recView.setAdapter(adapter);
                        recView.setLayoutManager(new LinearLayoutManager(searchUI.this));

                    }

                }

            }
        });

    }

    private void searchViewObj(SearchView searchView){

        searchView.setQueryHint(Html.fromHtml("<font color = #000000 >" + getResources().getString(R.string.search_hint) + "</font>"));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}