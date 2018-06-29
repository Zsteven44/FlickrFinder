package com.zafranitechllcpc.flickrfinder.browse;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zafranitechllcpc.flickrfinder.R;
import com.zafranitechllcpc.flickrfinder.models.FlickrItem;

import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.FlickrItemViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private final BrowseFragment.DialogDisplay dialogDisplay;
    private List<FlickrItem> itemList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public BrowseAdapter(@NonNull final FragmentActivity activity,
                         @NonNull final List<FlickrItem> itemList,
                         @NonNull final RecyclerView recyclerView) {
        this.dialogDisplay = (BrowseFragment.DialogDisplay) activity;
        this.itemList = itemList;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        Log.e(TAG, "Scrolled! Current item count is: " + totalItemCount
                                + "\nCurrent lastVisibleItem is: " + lastVisibleItem);
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void clearFlickrItemList() {
        this.itemList.clear();
    }
    public void updateFlickrItemList(List<FlickrItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
        loading = false;
    }


    @NonNull
    @Override
    public FlickrItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent, false);
        return new FlickrItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrItemViewHolder holder, int position) {
        final FlickrItem flickrItem = itemList.get(position);
        if (flickrItem.getSmallUrl() != null ){
            Log.e(TAG, "Item url is not null, " +  flickrItem.toString());
            Picasso.get()
                    .load(flickrItem.getSmallUrl())
                    .resize(320, 320)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            Log.e(TAG, "Item url is null, " +  flickrItem.toString());
        }
        Log.e(TAG, flickrItem.toString());
        holder.textView.setText(flickrItem.getTitle());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDisplay.displayDetailsDialog(flickrItem);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDisplay.displayDetailsDialog(flickrItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class FlickrItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public FlickrItemViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageview);
            this.textView = itemView.findViewById(R.id.textview_title);
        }
    }

}
