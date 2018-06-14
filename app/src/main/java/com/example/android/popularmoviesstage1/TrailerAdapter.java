package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Model.Trailer;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by joycelin12 on 6/3/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.NumberViewHolder>  {

    //add a variable to display the number of items
    private int mNumberItems;
    private ArrayList<Trailer> mData = new ArrayList<>();
    private TrailerAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private static String M_BASEURL = "https://img.youtube.com/vi/";
    private static String M_ENDURL = "/0.jpg";



    //create a constructor that accepts int as a parameter for number of items and store in the variable
    public TrailerAdapter(int numberOfItems, ArrayList<Trailer> data, Context context){
        mNumberItems = numberOfItems;
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public TrailerAdapter.NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutIdForItem = R.layout.trailer_item;
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, viewGroup, shouldAttachToParentImmediately);
        TrailerAdapter.NumberViewHolder viewHolder = new TrailerAdapter.NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.NumberViewHolder holder, int position) {

         Trailer trailer = mData.get(position);

        //System.out.println(url);
        //holder.listItemMovieView.setText(animal);
        String url = M_BASEURL + trailer.getKey() + M_ENDURL;
       // Log.i("TAG", "this is trailer" + url);
        Context context = holder.listItemTrailerView.getContext();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
               /// .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.listItemTrailerView);

        holder.listItemNameView.setText(trailer.getName());

    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listItemTrailerView;
        TextView listItemNameView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemTrailerView = (ImageView) itemView.findViewById(R.id.trailerImage);
            listItemNameView = (TextView) itemView.findViewById(R.id.trailerName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) try {
                mClickListener.onItemClick2(view, getAdapterPosition());
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
    Trailer getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(TrailerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick2(View view, int position) throws JSONException;
    }

}
