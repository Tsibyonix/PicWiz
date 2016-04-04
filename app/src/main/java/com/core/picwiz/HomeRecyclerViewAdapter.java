package com.core.picwiz;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by chronix on 4/4/16.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewHolder> {
    public LayoutInflater inflater;
    List<HomeRecyclerList> homeRecyclerLists;

    HomeRecyclerViewAdapter(Context context, List<HomeRecyclerList> homeRecyclerLists) {
        inflater = LayoutInflater.from(context);
        this.homeRecyclerLists = homeRecyclerLists;
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        View v = inflater.inflate(R.layout.card_view_layout, parent, false);
        HomeRecyclerViewHolder homeRecyclerViewHolder = new HomeRecyclerViewHolder(v);
        return homeRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewHolder holder, int position) {
        holder.username.setText(homeRecyclerLists.get(position).username);
        holder.tagLine.setText(homeRecyclerLists.get(position).tagLine);
        holder.timeStamp.setText(homeRecyclerLists.get(position).time);
        holder.tags.setText(homeRecyclerLists.get(position).tags);
        holder.imageView.setImageResource(homeRecyclerLists.get(position).photoId);
        holder.caption.setText(homeRecyclerLists.get(position).caption);
    }

    @Override
    public int getItemCount() {
        return homeRecyclerLists.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView username;
        TextView tagLine;
        TextView timeStamp;
        TextView tags;
        ImageView imageView;
        TextView caption;

        public HomeRecyclerViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            username = (TextView) itemView.findViewById(R.id.text_view_username_card_view);
            tagLine = (TextView) itemView.findViewById(R.id.text_view_tag_line_card_view);
            timeStamp = (TextView) itemView.findViewById(R.id.text_view_time_card_view);
            tags = (TextView) itemView.findViewById(R.id.text_view_tags_card_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_picture_card_view);
            caption = (TextView) itemView.findViewById(R.id.text_view_caption_card_view);
        }
    }
}
