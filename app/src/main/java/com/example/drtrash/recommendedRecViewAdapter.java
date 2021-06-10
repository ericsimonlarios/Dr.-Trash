package com.example.drtrash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.drtrash.createEntry.VIEWS_KEY;
import static com.example.drtrash.item_ui.ITEM_TITLE_KEY;

public class recommendedRecViewAdapter extends RecyclerView.Adapter<recommendedRecViewAdapter.myViewHolder> {

    private Context context;
    private ArrayList<objItemModel> recomItem = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Items");

    // This inner class holds the view for every object in our recycle view
    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle;
        private ImageView objImg;
        private MaterialCardView recomParent;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.objImg = itemView.findViewById(R.id.objImg);
            this.recomParent = itemView.findViewById(R.id.recomParent);

        }
    }

    public void setObjModel(ArrayList<objItemModel> recomItem) {

        this.recomItem = recomItem;
        notifyDataSetChanged();

    }

    //Constructor for the context of the main class
    public recommendedRecViewAdapter(Context context) {

        this.context = context;

    }

    @NonNull
    @NotNull
    @Override // this methods displays the layout that we inflated from a another layout resource file everytime there is an instance of the class
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_items, parent, false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;

    }

    @Override // this is the viewholder that displays the data into the UI widgets
    public void onBindViewHolder(@NonNull @NotNull recommendedRecViewAdapter.myViewHolder holder, int position) {

        holder.itemTitle.setText(recomItem.get(position).getObjTitle());

        Glide.with(context)
                .asBitmap()
                .load(recomItem.get(position).getImgUrl())
                .into(holder.objImg);

        holder.recomParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int views = Integer.parseInt(recomItem.get(position).getViews()) + 1;
                String viewString = String.valueOf(views);
                colRef.document(recomItem.get(position).getObjTitle()).update(VIEWS_KEY, viewString);
                Toast.makeText(context, "views: " + viewString, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(context, item_ui.class);
                callIntent.putExtra(ITEM_TITLE_KEY, recomItem.get(position).getObjTitle());
                context.startActivity(callIntent);
            }
        });

    }

    @Override //this method is responsible for handling the size that the recyclerview displays
    public int getItemCount() {
        return 5;
    }
}
