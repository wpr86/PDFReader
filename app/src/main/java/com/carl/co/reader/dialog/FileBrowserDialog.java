package com.carl.co.reader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.carl.co.reader.R;
import com.carl.co.reader.adapter.FileBrowserRecyclerAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Host-0 on 2017/3/16.
 */

public class FileBrowserDialog extends Dialog implements FileBrowserRecyclerAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private LinearLayout back;
    private Context mContext;
    private FileBrowserRecyclerAdapter mAdapter;
    private String rootPath = Environment.getExternalStorageDirectory().getPath();
    private File currentFilePath;
    private OnFileClickListener mOnFileClickListener;

    public FileBrowserDialog(@NonNull Context context, String rootPath, OnFileClickListener onFileClickListener) {
        super(context);
        mContext = context;
        this.rootPath = rootPath;
        mOnFileClickListener = onFileClickListener;
    }

    public FileBrowserDialog(@NonNull Context context, OnFileClickListener onFileClickListener) {
        super(context);
        mContext = context;
        mOnFileClickListener = onFileClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_file_browser);
        setTitle(R.string.dialog_title);
        currentFilePath = new File(rootPath);
        initView();
        openFilePath();
    }

    public void initView(){
        back = (LinearLayout)findViewById(R.id.dialog_item_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootPath = currentFilePath.getParent();
                openFilePath();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.dialog_file_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mAdapter = new FileBrowserRecyclerAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void openFilePath(){
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.isDirectory())
                    return true;
                else
                {
                    String name = pathname.getName().toLowerCase();
                    if(name.endsWith(".pdf"))
                        return true;
                    else
                        return false;
                }
            }
        };

        currentFilePath = new File(rootPath);
        File[] files = currentFilePath.listFiles(fileFilter);
        List<File> fileList = new ArrayList<>();

        List<File> lfolders = new ArrayList<>();    // 记录文件夹
        List<File> lfiles = new ArrayList<>();      // 记录文件

        if(files != null){
            for(File file:files){
                if(file.isDirectory()){
                    lfolders.add(file);
                }
                else{
                    lfiles.add(file);
                }
            }
        }

        fileList.addAll(lfolders);    // 文件夹在前，文件在后
        fileList.addAll(lfiles);

        mAdapter.setFileList(fileList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(File file) {
        if(file.isDirectory()){
            rootPath = file.getAbsolutePath();
            openFilePath();
        }
        else {
            mOnFileClickListener.onClick(file);
            this.dismiss();
        }
    }

    public interface OnFileClickListener{
        void onClick(File file);
    }
}
