package com.core.picwiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chronix on 16/4/16.
 */
public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentRecyclerViewHolder> {
    LayoutInflater inflater;
    Map<String, String> comments;

    CommentRecyclerViewAdapter(Context context, Map<String, String> list) {
        inflater = LayoutInflater.from(context);
        this.comments = list;
    }

    @Override
    public CommentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.comment_recycter_view_layout, parent, false);
        CommentRecyclerViewHolder commentRecyclerViewHolder = new CommentRecyclerViewHolder(v);
        return commentRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentRecyclerViewHolder holder, int position) {
        List<String> username = new ArrayList<String>(comments.keySet());
        List<String> comment = new ArrayList<String>(comments.values());
        String u = username.get(position);
        String c = comment.get(position);
        Log.i( "comment: ",u+": "+c);
        if(u != null)
            holder.mTextViewUsername.setText(String.format("%s:", u));
        if(c != null)
            holder.mTextViewComment.setText(String.format("%s", c));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewUsername;
        public TextView mTextViewComment;

        public CommentRecyclerViewHolder(View itemView) {
            super(itemView);
            mTextViewUsername = (TextView) itemView.findViewById(R.id.text_view_username_comment_recycler_view);
            mTextViewComment = (TextView) itemView.findViewById(R.id.text_view_comment_comment_recycler_view);
        }
    }
}
