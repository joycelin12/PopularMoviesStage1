package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by joycelin12 on 5/6/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.NumberViewHolder> {

    //add a variable to display the number of items
    private int mNumberItems;
    private ArrayList<Movie> mData = new ArrayList<Movie>();
    private ItemClickListener mClickListener;
    private Context mContext;
    private Cursor mCursor;
    private static String M_BASEURL = "http://image.tmdb.org/t/p/w185";



    //create a constructor that accepts int as a parameter for number of items and store in the variable
    public MovieAdapter(int numberOfItems, ArrayList<Movie> data, Context context){
        mNumberItems = numberOfItems;
        this.mData = data;
        this.mContext = context;
    }

    //create a constructor that accepts int as a parameter for number of items and store in the variable
    /*public MovieAdapter(int numberOfItems, Cursor cursor, Context context){
        mNumberItems = numberOfItems;
        this.mContext = context;
        this.mCursor = cursor;
    }*/

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutIdForItem = R.layout.recyclerview_item;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {

        String url = null;
        url = M_BASEURL + mData.get(position).getPoster_path();

        Context context = holder.listItemMovieView.getContext();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(holder.listItemMovieView);

    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listItemMovieView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemMovieView = (ImageView) itemView.findViewById(R.id.moviesImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) try {
                mClickListener.onItemClick(view, getAdapterPosition());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // convenience method for getting data at click position
    Movie getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position) throws JSONException;
    }


}
