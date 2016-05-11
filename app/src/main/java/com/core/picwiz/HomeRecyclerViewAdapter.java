package com.core.picwiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chronix on 4/4/16.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewHolder> {
    public LayoutInflater inflater;
    private ReceivePost receivePost;
    Context context;
    private PicWizBackend picWizBackend;

    String[] month = {
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
    };

    HomeRecyclerViewAdapter(Context context, ReceivePost receivePost) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.receivePost = receivePost;
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.card_view_layout, parent, false);
        HomeRecyclerViewHolder homeRecyclerViewHolder = new HomeRecyclerViewHolder(v);
        return homeRecyclerViewHolder;
    }

    void setupCommentRecyclerView(final HomeRecyclerViewHolder holder, int position) {
        CommentRecyclerViewAdapter commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(context, receivePost.getOutput().get(position).getComments());
        holder.commentRecyclerView.setVisibility(View.VISIBLE);
        holder.commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
        holder.commentRecyclerView.setHasFixedSize(true);
        holder.commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private String getTime(int position) {
        String[] time = receivePost.getOutput().get(position).getTime().split("-");
        return time[3]+":"+time[4]+", "+time[0]+"th "+month[Integer.parseInt(time[1])-1]+" "+time[2];
    }

    private void updateComment(final HomeRecyclerViewHolder holder, String post_key) {
        final CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("time", String.valueOf(millisUntilFinished));
                if (picWizBackend.getWait()) {
                    Log.i("refresh_comment: ", picWizBackend.getRefreshComment().getComments().get("chronix"));
                    holder.commentRecyclerView.setAdapter(new CommentRecyclerViewAdapter(context, picWizBackend.getRefreshComment().getComments()));
                    this.cancel();
                }
            }

            @Override
            public void onFinish() {
                Log.i("time", "Clock finished");
                this.cancel();
            }
        };
        picWizBackend.refreshComments(post_key);
        countDownTimer.start();
    }

    @Override
    public void onBindViewHolder(final HomeRecyclerViewHolder holder, int position) {
        final String id = receivePost.getOutput().get(position).getId();
        picWizBackend = new PicWizBackend(context);
        final SharedPreferences settings = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        holder.username.setText(receivePost.getOutput().get(position).getUsername());
        holder.tagLine.setText(receivePost.getOutput().get(position).getTag_line());
        holder.timeStamp.setText(getTime(position));
        holder.tags.setText(Joiner.on(",").skipNulls().join(receivePost.getOutput().get(position).getTags()));
        Glide.with(context).load("http://192.168.1.4/~chronix/python/"+receivePost.getOutput().get(position).getImage()).into(holder.imageView);
        holder.caption.setText(receivePost.getOutput().get(position).getCaption());
        holder.location.setText(String.format(" %s", receivePost.getOutput().get(position).getLocation()));
        holder.comments.setText(String.format(" %s", receivePost.getOutput().get(position).getComments().size()));
        setupCommentRecyclerView(holder, position);

        holder.newComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Toast.makeText(context, "Adding comment", Toast.LENGTH_LONG).show();
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    picWizBackend.comment(id, settings.getString("USERNAME", null), v.getText().toString());
                    Toast.makeText(context, "updating recycler view comment", Toast.LENGTH_LONG).show();
                    v.setText("");
                    v.setVisibility(View.GONE);
                    updateComment(holder, id);
                }
                return true;
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find a way to start activity from here/
                //Intent profileViewIntent = new Intent(context.getApplicationContext(), SecondaryActivity.class);
                Toast.makeText(context, id+": "+holder.username.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textInputLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receivePost.getOutput().size();
        //return homeRecyclerLists.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView username;
        TextView tagLine;
        TextView timeStamp;
        TextView tags;
        ImageView imageView;
        TextView caption;
        TextInputLayout textInputLayout;
        RecyclerView commentRecyclerView;
        EditText newComment;
        TextView location;
        com.like.LikeButton likeButton;
        TextView direction;
        TextView comments;
        LinearLayout linearLayout;

        public HomeRecyclerViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            username = (TextView) itemView.findViewById(R.id.text_view_username_card_view);
            tagLine = (TextView) itemView.findViewById(R.id.text_view_tag_line_card_view);
            timeStamp = (TextView) itemView.findViewById(R.id.text_view_time_card_view);
            tags = (TextView) itemView.findViewById(R.id.text_view_tags_card_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_picture_card_view);
            caption = (TextView) itemView.findViewById(R.id.text_view_caption_card_view);
            textInputLayout = (TextInputLayout) itemView.findViewById(R.id.text_input_layout_comment_card_view);
            commentRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_comment);
            newComment = (EditText) itemView.findViewById(R.id.edit_text_comment_card_view);
            location = (TextView) itemView.findViewById(R.id.text_view_location);
            likeButton = (com.like.LikeButton) itemView.findViewById(R.id.like_button_card_view);
            direction = (TextView) itemView.findViewById(R.id.text_view_direction);
            comments = (TextView) itemView.findViewById(R.id.text_view_comment);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
