package com.abohomol.gifky.tools;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    /**
     * The total number of items in the data set after the last load
     */
    private int previousTotal = 0;
    private boolean isLoading = true;

    /**
     * The minimum amount of items to have below your current scroll position before isLoading more.
     */
    private static final int visibleThreshold = 10;

    private final StaggeredGridLayoutManager layoutManager;

    public EndlessScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void reset() {
        previousTotal = 0;
        isLoading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int[] firstVisibleItemPositions = new int[3];
        int firstVisibleItem = layoutManager.findFirstVisibleItemPositions(firstVisibleItemPositions)[0];

        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore();
            isLoading = true;
        }
    }

    public abstract void onLoadMore();
}
