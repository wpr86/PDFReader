package com.carl.co.reader.adapter;

import android.support.v7.widget.RecyclerView;

import com.carl.co.reader.book.BookBean;

import java.util.Objects;

/**
 * Created by Host-0 on 2017/3/7.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(RecyclerView.ViewHolder viewHolder);
}
