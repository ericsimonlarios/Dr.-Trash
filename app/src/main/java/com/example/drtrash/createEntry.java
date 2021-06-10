package com.example.drtrash;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.transition.TransitionManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;


public class createEntry extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TITLE_KEY = "title";
    private static final String WAYDISPOSE_KEY = "way of disposal";
    private static final String WAYRISK_KEY = "way of disposal with risk";
    private static final String TYPE_KEY = "type";
    private static final String CLASS_KEY = "classification";
    private static final String RADIO_KEY = "bio or non bio";
    private static final String DOWNLOAD_KEY = "img download url";
    private static final String TYPE_RADIO_KEY = "type of solid wastes";
    private static final String LINK_REUSE_KEY = "link for reuse";
    private static final String LINK_RISK_KEY = "link for risk";
    public static final String VIEWS_KEY = "views";

    private static int views = 1;
    private Uri imageUri = null;
    private Bitmap compressed;
    private Spinner typeSpinner;
    private Spinner classSpinner;
    private RadioGroup radGroup, typeSolidRad;
    private Button saveButton;
    private ConstraintLayout constraintLayout, expandedTabSolid, typeWasteLayout;
    private CoordinatorLayout createEntryLayout;
    private ImageView imgInput;
    private EditText titleView;
    private TextView textButtonDisposal, textButtonDisposalRisk;

    private FirebaseStorage storageDB = FirebaseStorage.getInstance();

    private StorageReference storageRef = storageDB.getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference colRef = db.collection("Items");

    private String titleKey="not set";

    private String editTxtCacheReuse = "";

    private String editTxtCacheRisk = "";

    private String editTxtLinkRisk="";

    private String editTxtLinkReuse = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        instanceUI();
        spinner(typeSpinner, classSpinner);
        imgInput(imgInput);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("Solid")) {
                    TransitionManager.beginDelayedTransition(typeWasteLayout);
                    expandedTabSolid.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(typeWasteLayout);
                    expandedTabSolid.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showDialog(saveButton);

    }

    //the method for creating an instance for the UI that was declared
    private void instanceUI() {

        createEntryLayout = findViewById(R.id.createEntryLayout);
        constraintLayout = findViewById(R.id.progressLayout);
        typeWasteLayout = findViewById(R.id.typeWasteLayout);
        expandedTabSolid = findViewById(R.id.expandedTabSolid);
        saveButton = findViewById(R.id.saveButton);
        classSpinner = findViewById(R.id.classSpinner);
        imgInput = findViewById(R.id.titleImage);
        titleView = findViewById(R.id.titleEditTxt);
        typeSpinner = findViewById(R.id.spinner);
        textButtonDisposal = findViewById(R.id.textButtonDisposal);
        textButtonDisposalRisk = findViewById(R.id.textButtonDisposalRisk);


    }

    private void showDialog(Button saveButton){

        textButtonDisposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTitleTxt = "Ways of Reusing it: ";

                AlertDialog.Builder builder = new AlertDialog.Builder(createEntry.this);
                LayoutInflater inflater = getLayoutInflater();
                View customView = inflater.inflate(R.layout.edit_txt_box,null);
                RichEditor wayDisposeView = customView.findViewById(R.id.waysDisposalEditTxt);
                AppCompatEditText linkReuse = customView.findViewById(R.id.linkEditTxtReuse);
                ConstraintLayout layoutParent = customView.findViewById(R.id.layoutParent);
                MaterialButton boldReuse = customView.findViewById(R.id.action_boldReuse);
                MaterialButton italicReuse = customView.findViewById(R.id.action_italicReuse);
                MaterialButton underLineReuse = customView.findViewById(R.id.action_underlineReuse);
                AppCompatToggleButton insertReuse = customView.findViewById(R.id.action_insertLinkReuse);
                AppCompatTextView editTitleTxtBox = customView.findViewById(R.id.editTitleText);

                editTitleTxtBox.setText(editTitleTxt);
                builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                richEditTextFormat(boldReuse,italicReuse,underLineReuse,insertReuse,wayDisposeView,layoutParent);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        editTxtCacheReuse = wayDisposeView.getHtml();
                        editTxtLinkReuse = linkReuse.getText().toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            textButtonDisposal.setText(Html.fromHtml(editTxtCacheReuse,Html.FROM_HTML_MODE_COMPACT));
                        }
                        else {
                            textButtonDisposal.setText(Html.fromHtml(editTxtCacheReuse));
                        }

                    }
                });

                editTitleTxtBox.setText(editTitleTxt);
                wayDisposeView.setHtml(editTxtCacheReuse);
                linkReuse.setText(editTxtLinkReuse);

                richEditorAttributes(wayDisposeView);

                builder.setView(customView);
                builder.create();
                builder.show();
            }
        });

        textButtonDisposalRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTitleTxt = "Risky Approaches: ";

                AlertDialog.Builder builder = new AlertDialog.Builder(createEntry.this);
                LayoutInflater inflater = getLayoutInflater();
                View customView = inflater.inflate(R.layout.edit_txt_box,null);
                RichEditor wayDisposeView = customView.findViewById(R.id.waysDisposalEditTxt);
                AppCompatEditText linkRisk = customView.findViewById(R.id.linkEditTxtReuse);
                ConstraintLayout layoutParent = customView.findViewById(R.id.layoutParent);
                MaterialButton boldRisk = customView.findViewById(R.id.action_boldReuse);
                MaterialButton italicRisk = customView.findViewById(R.id.action_italicReuse);
                MaterialButton underLineRisk = customView.findViewById(R.id.action_underlineReuse);
                AppCompatToggleButton insertRisk = customView.findViewById(R.id.action_insertLinkReuse);
                AppCompatTextView editTitleTxtBox = customView.findViewById(R.id.editTitleText);

                editTitleTxtBox.setText(editTitleTxt);
                builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                richEditTextFormat(boldRisk,italicRisk,underLineRisk,insertRisk,wayDisposeView,layoutParent);

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        editTxtCacheRisk = wayDisposeView.getHtml();
                        editTxtLinkRisk = linkRisk.getText().toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            textButtonDisposalRisk.setText(Html.fromHtml(editTxtCacheRisk,Html.FROM_HTML_MODE_COMPACT));
                        }
                        else {
                            textButtonDisposalRisk.setText(Html.fromHtml(editTxtCacheRisk));
                        }

                    }
                });

                editTitleTxtBox.setText(editTitleTxt);
                wayDisposeView.setHtml(editTxtCacheRisk);
                linkRisk.setText(editTxtLinkRisk);

                richEditorAttributes(wayDisposeView);

                builder.setView(customView);
                builder.create();
                builder.show();

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createEntryLayout.setEnabled(false);
                constraintLayout.setVisibility(View.VISIBLE);
                saveButton.setEnabled(false);

                radGroup = findViewById(R.id.radioGroup);
                typeSolidRad = findViewById(R.id.typeSolid);

                int checkedButton = radGroup.getCheckedRadioButtonId();
                int typeCheckedButton = typeSolidRad.getCheckedRadioButtonId();
                final String typeTxt = typeSpinner.getSelectedItem().toString();
                final String classTxt = classSpinner.getSelectedItem().toString();
                final String titleTxt = titleView.getText().toString();
                final String wayDispose = editTxtCacheReuse;
                final String wayRisk = editTxtCacheRisk;
                final String linkReuse = editTxtLinkRisk;
                final String linkRisk = editTxtLinkReuse;

                colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            String title = queryDocumentSnapshot.getString("title");
                            if(title.toLowerCase().trim().contentEquals(titleTxt.toLowerCase().trim())){

                                titleKey=title.toLowerCase().trim();
                                break;

                            }
                        }
                        if(!titleKey.contentEquals(titleTxt.toLowerCase().trim())){

                            if (!titleTxt.isEmpty() && !wayDispose.isEmpty() && !wayRisk.isEmpty() && imageUri != null) {

                                imgInput.setDrawingCacheEnabled(true);
                                imgInput.buildDrawingCache();
                                compressed = ((BitmapDrawable) imgInput.getDrawable()).getBitmap();
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] thumbData = byteArrayOutputStream.toByteArray();

                                StorageReference imgRef = storageRef.child("Images").child(titleTxt);

                                UploadTask image_path = imgRef.putBytes(thumbData);

                                image_path.addOnCompleteListener(createEntry.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {


                                        }


                                    }
                                });

                                Task<Uri> urlTask = image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {

                                            String error = task.getException().getMessage();
                                            Toast.makeText(createEntry.this, "Error: " + error, Toast.LENGTH_LONG).show();

                                        }

                                        return imgRef.getDownloadUrl();
                                    }
                                });

                                urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                        Uri downloadUri;

                                        if (task.isSuccessful()) {

                                            downloadUri = task.getResult();
                                            String imageURL = downloadUri.toString();
                                            storeData(typeTxt, classTxt, titleTxt, wayDispose, wayRisk, checkedButton, imageURL, typeCheckedButton, linkReuse, linkRisk);


                                        } else {

                                            downloadUri = imageUri;

                                        }

                                    }
                                });

                            } else {

                                constraintLayout.setVisibility(View.GONE);
                                saveButton.setEnabled(true);
                                createEntryLayout.setEnabled(true);
                                Toast.makeText(createEntry.this, "Must fill all the required fields", Toast.LENGTH_LONG).show();

                            }

                        }
                        else{

                            constraintLayout.setVisibility(View.GONE);
                            saveButton.setEnabled(true);
                            createEntryLayout.setEnabled(true);
                            Toast.makeText(createEntry.this,"Item already exist",Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

    }

    private void richEditTextFormat(MaterialButton bold,
                                    MaterialButton italic,
                                    MaterialButton underline,
                                    AppCompatToggleButton insert,
                                    RichEditor editTextInput,
                                    ConstraintLayout layoutParent){

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextInput.setBold();

            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextInput.setItalic();

            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextInput.setUnderline();

            }
        });

        insert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    TransitionManager.beginDelayedTransition(layoutParent);
                    editTextInput.setVisibility(View.VISIBLE);
                }
                else{
                    TransitionManager.beginDelayedTransition(layoutParent);
                    editTextInput.setVisibility(View.GONE);
                }

            }
        });

    }

    //this is where the additional attributes of rich editor can be found
    private void richEditorAttributes(RichEditor wayDisposeView) {

        wayDisposeView.setEditorFontSize(18);
        wayDisposeView.setEditorHeight(100);
        wayDisposeView.setBackgroundColor(Color.parseColor("#8bc34a"));
        wayDisposeView.setPlaceholder("(Type here...)");
        wayDisposeView.setPadding(10, 10, 10, 100);

    }

    // the method for the spinner adapters
    private void spinner(Spinner typeSpinner, Spinner classSpinner) {

        //Adapter for typeSpinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.typeWasteSpinner, R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //Adapter for classSpinner
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this, R.array.classWasteSpinner, R.layout.support_simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

    }


    //Permission for the app to access the gallery of a device
    private void imgInput(ImageView imgInput) {

        imgInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(createEntry.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(createEntry.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                    }
                } else {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                }

            }
        });

    }

    //the method where the data are being stored into the web database
    private void storeData(String typeTxt, String classTxt, String titleTxt, String wayDispose, String wayRisk, int checkedButton, String imageURL, int typeCheckedButton, String linkReuse, String linkRisk) {

        Map<String, Object> dataToAdd = new HashMap<>();

        if (typeTxt.equals("Solid")) {

            switch (typeCheckedButton) {

                case R.id.glassAndCeramicsRad: {

                    String typeRad = "Glass or Ceramic";
                    dataToAdd.put(TYPE_RADIO_KEY, typeRad);
                    break;

                }

                case R.id.plasticWasteRad: {

                    String typeRad = "Plastic Waste";
                    dataToAdd.put(TYPE_RADIO_KEY, typeRad);
                    break;

                }

                case R.id.paperRubbishRad: {

                    String typeRad = "Paper Rubbish";
                    dataToAdd.put(TYPE_RADIO_KEY, typeRad);
                    break;

                }

                case R.id.metalAndTinsRad: {

                    String typeRad = "Metal and Tins";
                    dataToAdd.put(TYPE_RADIO_KEY, typeRad);
                    break;

                }

            }

        }

        switch (checkedButton) {
            case    R.id.bioCheck: {
                String radText = "Biodegradable";
                dataToAdd.put(RADIO_KEY, radText);
                break;
            }
            case R.id.nonBioCheck: {
                String radText = "Non-Biodegradable";
                dataToAdd.put(RADIO_KEY, radText);
                break;
            }
            case R.id.bioNonCheck: {
                String radText = "Biodegradable or Non-Biodegradable";
                dataToAdd.put(RADIO_KEY, radText);
                break;
            }
        }

        String viewString = String.valueOf(views);
        dataToAdd.put(VIEWS_KEY, viewString);
        dataToAdd.put(LINK_RISK_KEY, linkRisk);
        dataToAdd.put(LINK_REUSE_KEY, linkReuse);
        dataToAdd.put(DOWNLOAD_KEY, imageURL);
        dataToAdd.put(TITLE_KEY, titleTxt);
        dataToAdd.put(WAYDISPOSE_KEY, wayDispose);
        dataToAdd.put(WAYRISK_KEY, wayRisk);
        dataToAdd.put(TYPE_KEY, typeTxt);
        dataToAdd.put(CLASS_KEY, classTxt);

        db.collection("Items").document(titleTxt).set(dataToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(createEntry.this, "Saved", Toast.LENGTH_LONG).show();
                    constraintLayout.setVisibility(View.GONE);
                    saveButton.setEnabled(true);
                    createEntryLayout.setEnabled(true);
                    startActivity(new Intent(createEntry.this, homeMainUI.class));
                    finish();

                } else {

                    Toast.makeText(createEntry.this, "Failed", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
    // the method for saving the image to the UI component that was called from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imgInput.setImageURI(imageUri);

        }
    }
}