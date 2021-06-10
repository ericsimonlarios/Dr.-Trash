package com.example.drtrash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.drtrash.createEntry.VIEWS_KEY;
import static com.example.drtrash.item_ui.ITEM_TITLE_KEY;

public class ObjRecViewAdapter extends RecyclerView.Adapter<ObjRecViewAdapter.ViewHolder> {

    public static final String ACTIVITY_KEY = "activity key";
    private ArrayList<objItemModel> itemModels = new ArrayList<>();
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Items");

    //Constructor for the context of the main class
    public ObjRecViewAdapter(Context context) {
        this.context = context;
    }


    public void setItemModel(ArrayList<objItemModel> itemModels) {

        this.itemModels = itemModels;
        notifyDataSetChanged();

    }

    public ObjRecViewAdapter() {

    }

    @NonNull
    @Override // this methods displays the layout that we inflated from a another layour resource file everytime there is an instance of the class
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override // this is the viewholder that displays the data into the UI widgets
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.objTitle.setText(itemModels.get(position).getObjTitle());
        holder.objTitle.setTextSize(24);
        holder.objType.setText(itemModels.get(position).getObjType());
        holder.objType.setTextSize(16);
        holder.objClass.setText(itemModels.get(position).getObjClass());
        holder.objClass.setTextSize(16);

        Glide.with(context)
                .asBitmap()
                .load(itemModels.get(position).getImgUrl())
                .into(holder.imgUrl);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int views = Integer.parseInt(itemModels.get(position).getViews()) + 1;
                String viewString = String.valueOf(views);
                colRef.document(itemModels.get(position).getObjTitle()).update(VIEWS_KEY, viewString);
                Toast.makeText(context, "views: " + viewString, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, item_ui.class);
                Bundle extras = new Bundle();
                extras.putString(ITEM_TITLE_KEY, itemModels.get(position).getObjTitle());
                intent.putExtras(extras);
                context.startActivity(intent);

            }
        });
    }

    @Override // this method is responsible for handling the size that the recyclerview displays
    public int getItemCount() {
        return itemModels.size();
    }

    // This inner class holds the view for every object in our recycle view
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView objTitle;
        private TextView objType;
        private TextView objClass;
        private ImageView imgUrl;
        private MaterialCardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //since we can not call findViewById directly,
            //we can use View object that is passed to the ViewHolder constructor
            objTitle = itemView.findViewById(R.id.objTitle);
            objType = itemView.findViewById(R.id.objType);
            objClass = itemView.findViewById(R.id.objClassification);
            imgUrl = itemView.findViewById(R.id.objImage);
            parent = itemView.findViewById(R.id.itemParent);


        }
    }


}
