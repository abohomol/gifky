package com.abohomol.gifky;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abohomol.gifky.repository.GifItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GifListAdapter extends RecyclerView.Adapter<GifListAdapter.GifCardHolder> {

    private static final @IdRes int gifViewId = 100;
    private static final @IdRes int descriptionViewId = 101;
    private final Context context;

    private final List<GifItem> items = new ArrayList<>();
    private final RequestManager glide;

    public GifListAdapter(Context context) {
        this.context = context;
        this.glide = Glide.with(context);
    }

    public void addItems(List<GifItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void replaceItems(List<GifItem> items) {
        this.items.clear();
        addItems(items);
    }

    @Override public GifCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView gif = new ImageView(context);
        gif.setId(gifViewId);
        gif.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        TextView description = new TextView(context);
        description.setId(descriptionViewId);
        description.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        description.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.addView(gif);
        layout.addView(description);

        CardView gifCard = new CardView(context);
        gifCard.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        gifCard.addView(layout);
        return new GifCardHolder(gifCard);
    }

    @Override public void onBindViewHolder(GifCardHolder holder, int position) {
        GifItem item = items.get(position);
        holder.descriptionField.setText(item.description());
        glide.load(item.uri())
                .placeholder(R.mipmap.ic_launcher)
                .into(new GlideDrawableImageViewTarget(holder.gifView));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    static class GifCardHolder extends RecyclerView.ViewHolder {

        private final ImageView gifView;
        private final TextView descriptionField;

        GifCardHolder(View itemView) {
            super(itemView);
            this.gifView = (ImageView) itemView.findViewById(gifViewId);
            this.descriptionField = (TextView) itemView.findViewById(descriptionViewId);
        }
    }
}
