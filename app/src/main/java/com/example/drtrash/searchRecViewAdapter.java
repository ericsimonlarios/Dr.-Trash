package com.example.drtrash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.List;

import static com.example.drtrash.ObjRecViewAdapter.ACTIVITY_KEY;
import static com.example.drtrash.createEntry.VIEWS_KEY;
import static com.example.drtrash.item_ui.ITEM_TITLE_KEY;

public class searchRecViewAdapter extends RecyclerView.Adapter<searchRecViewAdapter.Viewholder> implements Filterable {

    private ArrayList<objItemModel> objList = new ArrayList<>();
    private ArrayList<objItemModel> objListFull;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Items");

    //Constructor for the context of the main class
    public searchRecViewAdapter(Context context) {

        this.context = context;

    }

    public void setObjList(ArrayList<objItemModel> objList) {

        this.objList = objList;
        objListFull = new ArrayList<>(objList);
        notifyDataSetChanged();

    }

    public searchRecViewAdapter() {

    }

    @NonNull
    @Override // this methods displays the layout that we inflated from a another layour resource file everytime there is an instance of the class
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_list_item, parent, false);
        Viewholder holder = new Viewholder(view);
        return holder;
    }

    @Override // this is the viewholder that displays the data into the UI widgets
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.objTitle.setText(objList.get(position).getObjTitle());
        holder.objClass.setText(objList.get(position).getObjClass());
        holder.objType.setText(objList.get(position).getObjType());

        Glide.with(context)
                .asBitmap()
                .load(objList.get(position).getImgUrl())
                .into(holder.imgUrl);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int views = Integer.parseInt(objList.get(position).getViews()) + 1;
                String viewString = String.valueOf(views);
                colRef.document(objList.get(position).getObjTitle()).update(VIEWS_KEY, viewString);
                Toast.makeText(context, "views: " + viewString, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, item_ui.class);
                Bundle extras = new Bundle();
                extras.putString(ITEM_TITLE_KEY, objList.get(position).getObjTitle());
                intent.putExtras(extras);
                context.startActivity(intent);

            }
        });
    }

    @Override // this method is responsible for handling the size that the recyclerview displays
    public int getItemCount() {
        return objList.size();
    }


    @Override // the method that gets the data from the filter class
    public Filter getFilter() {
        return objFilter;
    }

    // A filter instance that is responsible for filtering the data that is typed on the search widget
    private Filter objFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<objItemModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(objListFull);

            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (objItemModel item : objListFull) {
                    if (item.getObjTitle().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);

                    }


                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            objList.clear();
            objList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

    // This inner class holds the view for every object in our recycle view
    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView objTitle;
        private TextView objType;
        private TextView objClass;
        private ImageView imgUrl;
        private MaterialCardView parent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            objTitle = itemView.findViewById(R.id.objTitle);
            objType = itemView.findViewById(R.id.objType);
            objClass = itemView.findViewById(R.id.objClassification);
            imgUrl = itemView.findViewById(R.id.objImage);
            parent = itemView.findViewById(R.id.itemParent);

        }
    }

}

