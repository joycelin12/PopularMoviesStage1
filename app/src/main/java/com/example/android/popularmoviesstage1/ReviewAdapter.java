package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Model.Review;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.NumberViewHolder> {
    //add a variable to display the number of items
    private int mNumberItems;
    private ArrayList<Review> mData = new ArrayList<>();
    private ReviewAdapter.ItemClickListener mClickListener;
    private Context mContext;

    //create a constructor that accepts int as a parameter for number of items and store in the variable
    public ReviewAdapter(int numberOfItems, ArrayList<Review> data, Context context) {
        mNumberItems = numberOfItems;
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public ReviewAdapter.NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutIdForItem = R.layout.review_item;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapter.NumberViewHolder viewHolder = new ReviewAdapter.NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.NumberViewHolder holder, int position) {

        Review review = mData.get(position);

        //System.out.println(url);
        //holder.listItemMovieView.setText(animal);

        holder.listItemAuthorView.setText(review.getAuthor());
        holder.listItemContentView.setText(review.getContent());


    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listItemAuthorView;
        TextView listItemContentView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemAuthorView = (TextView) itemView.findViewById(R.id.reviewAuthor);
            listItemContentView = (TextView) itemView.findViewById(R.id.reviewContent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) try {
                mClickListener.onItemClick3(view, getAdapterPosition());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    // convenience method for getting data at click position
    Review getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ReviewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick3(View view, int position) throws JSONException;
    }

}

