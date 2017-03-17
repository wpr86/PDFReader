package com.carl.co.reader.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carl.co.reader.R;
import com.carl.co.reader.book.BookBean;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Host-0 on 2017/3/7.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private List<BookBean> mList;
    private Context mContext;
    private OnClickListener mOnClickListener;
    private OnItemAllDissmissListener mOnItemAllDissmissListener;
    public MyRecyclerAdapter(Context context, List<BookBean> list){
        mList = list;
        mContext = context;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecyclerAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_book,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItemTextView.setText(mList.get(position).getTitle());

        DecimalFormat fnum = new DecimalFormat("##0.0");
        holder.speedText.setText( fnum.format(mList.get(position).getSpeed()) + "%");
        holder.mProgress.setProgress((int) mList.get(position).getSpeed());
        holder.mItemPath.setText(mList.get(position).getPath());
        if(mOnClickListener!=null){
            holder.mItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOnClickListener.onItemClick(mList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final RecyclerView.ViewHolder viewHolder) {
        final BookBean bookBean = mList.get(viewHolder.getLayoutPosition());
        final int position = viewHolder.getLayoutPosition();
        itemDismiss(position);
        Snackbar.make(viewHolder.itemView, "删除\""+bookBean.getTitle()+"\"", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemAdd(position,bookBean);
                    }
                }).show();
    }

    public void itemAdd(int position, BookBean item) {
        mList.add(position,item);
        notifyItemInserted(position);
        mOnItemAllDissmissListener.onItemAllDissmiss(false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mItem;
        public TextView mItemTextView;
        public ImageView mItemImageView;
        public TextView speedText;
        public ProgressBar mProgress;
        public TextView mItemPath;
        public ViewHolder(View v) {
            super(v);
            mItem = (CardView)v.findViewById(R.id.item_card);
            mItemTextView = (TextView)v.findViewById(R.id.item_book_title);
            mItemImageView = (ImageView)v.findViewById(R.id.item_book_image);
            speedText = (TextView)v.findViewById(R.id.item_speed);
            mProgress = (ProgressBar)v.findViewById(R.id.item_progressbar);
            mItemPath = (TextView)v.findViewById(R.id.item_book_path);
        }
    }

    public void itemDismiss(int position){
        mList.remove(position);
        notifyItemRemoved(position);
        if(mList.size()<=0)
            mOnItemAllDissmissListener.onItemAllDissmiss(true);
    }

    public void upDataProgress(long id, float speed){
        for(BookBean bookBean : mList){
            if(bookBean.getId() == id){
                bookBean.setSpeed(speed);
            }
        }
        notifyDataSetChanged();
    }

    public boolean containsItem(BookBean bookBean){
        for(BookBean book : mList){
            if(book.equals(bookBean))
                return true;
        }
        return false;
    }

    public List<BookBean> getCurrentBookList(){
        return  mList;
    }

    public interface OnClickListener{
        void onItemClick(BookBean bookBean);
    }

    public void setOnClickListener(MyRecyclerAdapter.OnClickListener onClickListener){
        mOnClickListener = onClickListener;
    }

    public interface OnItemAllDissmissListener{
        void onItemAllDissmiss(boolean isAllDissmiss);
    }

    public void setOnItemAllDissmissListener(OnItemAllDissmissListener listener){
        mOnItemAllDissmissListener = listener;
    }
}
