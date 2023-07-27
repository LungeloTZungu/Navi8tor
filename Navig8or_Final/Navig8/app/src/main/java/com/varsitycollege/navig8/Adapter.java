package com.varsitycollege.navig8;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
//---Code attribution---
//Author:MyOnlineTrainingHub
//Title:How to build Interactive Excel Dashboards that Update with ONE CLICK!
//Date:17/11/2022
//Link:https://www.youtube.com/watch?v=ePKC5ZEqeNY
public class Adapter  extends FirebaseRecyclerAdapter<ReviewModel,Adapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter(@NonNull FirebaseRecyclerOptions<ReviewModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ReviewModel model) {
        holder.name.setText(model.getName());
        holder.review.setText(model.getReview());
        holder.location.setText(model.getLocation());
        holder.date.setText(model.getTimestamp());



    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name, review , location, date;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            // populating the recyclerview
             name = (TextView) itemView.findViewById(R.id.nameTv);
             review = (TextView) itemView.findViewById(R.id.reviewTv);
             location = (TextView) itemView.findViewById(R.id.LocationTv);
             review = (TextView) itemView.findViewById(R.id.reviewDate);

        }
    }
}
