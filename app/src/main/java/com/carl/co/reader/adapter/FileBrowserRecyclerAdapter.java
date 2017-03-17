package com.carl.co.reader.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carl.co.reader.R;

import java.io.File;
import java.util.List;

/**
 * Created by Host-0 on 2017/3/16.
 */

public class FileBrowserRecyclerAdapter extends RecyclerView.Adapter<FileBrowserRecyclerAdapter.ViewHolder>{
    private List<File> mFiles;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public FileBrowserRecyclerAdapter(Context context, List<File> files){
        mFiles = files;
    }

    public FileBrowserRecyclerAdapter(Context context){
        mContext = context;
    }

    public void setFileList(List<File> files){
        mFiles = null;
        mFiles = files;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file,parent,false);
        FileBrowserRecyclerAdapter.ViewHolder viewHolder = new FileBrowserRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.fileName.setText(mFiles.get(position).getName());
        holder.filePath.setText(mFiles.get(position).getAbsolutePath());
        if(mFiles.get(position).isDirectory()){
            holder.fileImage.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_folder_open_black,null));
        }
        else{
            holder.fileImage.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_picture_as_pdf,null));
        }
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(mFiles.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fileName;
        public TextView filePath;
        public ImageView fileImage;
        public ViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView)itemView.findViewById(R.id.item_file_name);
            filePath = (TextView)itemView.findViewById(R.id.item_file_path);
            fileImage = (ImageView)itemView.findViewById(R.id.item_file_image);
        }
    }

    public interface OnItemClickListener{
        void onClick(File file);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
}
