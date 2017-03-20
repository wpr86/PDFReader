package com.carl.co.reader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carl.co.reader.adapter.MyItemTouchHelperCallback;
import com.carl.co.reader.book.BookBean;
import com.carl.co.reader.adapter.MyRecyclerAdapter;
import com.carl.co.reader.db.BookDbHelper;
import com.carl.co.reader.dialog.FileBrowserDialog;
import com.carl.co.reader.pdf.PdfViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerAdapter.OnClickListener,MyRecyclerAdapter.OnItemAllDissmissListener {
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private RelativeLayout mainLayout;
    private FloatingActionButton mFloatingActionButton;
    private TextView mDefaultText;

    private ContentResolver mResolver;
    private List<BookBean> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mResolver = getContentResolver();
        initView();
    }

    private void initView(){
        mainLayout = (RelativeLayout)findViewById(R.id.main_relative);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDefaultText = (TextView)findViewById(R.id.default_text);
        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.add_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        initAdapter();
    }

    private void initAdapter(){
        bookList = BookDbHelper.getBookInfo(mResolver);
        if(bookList.size()<=0){
            mRecyclerView.setVisibility(View.GONE);
            mDefaultText.setVisibility(View.VISIBLE);
        }
        else{
            mDefaultText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mRecyclerAdapter = new MyRecyclerAdapter(this, bookList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnClickListener(this);
        mRecyclerAdapter.setOnItemAllDissmissListener(this);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(mRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void openFile(){
        FileBrowserDialog dialog = new FileBrowserDialog(this,new FileBrowserDialog.OnFileClickListener(){
            @Override
            public void onClick(File file) {
                String imagePath = "";
                BookBean bookBean = new BookBean(System.currentTimeMillis(),file.getName(),imagePath,0f,file.getAbsolutePath());
                if(!mRecyclerAdapter.containsItem(bookBean)){
                    mRecyclerAdapter.itemAdd(mRecyclerAdapter.getItemCount(),bookBean);

                    openBook(bookBean);
                }
                else {
                    Snackbar.make(mainLayout, bookBean.title + " 已存在", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(BookBean bookBean, int position) {
        if(new File(bookBean.path).exists()) {
            openBook(bookBean);
        }
        else {
            Snackbar.make(mainLayout, bookBean.title + " 不存在", Snackbar.LENGTH_LONG).show();
            mRecyclerAdapter.itemDismiss(position);
        }
    }

    private void openBook(BookBean bookBean){
        Intent intent = new Intent(MainActivity.this, PdfViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book",bookBean);
        intent.putExtras(bundle);
        startActivityForResult(intent,1000);
    }

    @Override
    public void onItemAllDissmiss(boolean isAllDissmiss) {
        if(isAllDissmiss){
            mRecyclerView.setVisibility(View.GONE);
            mDefaultText.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mDefaultText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            BookBean bookBean = (BookBean) bundle.getSerializable("book");
            mRecyclerAdapter.upDataProgress(bookBean.id,bookBean.getSpeed());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BookDbHelper.saveBookInfo(mResolver,mRecyclerAdapter.getCurrentBookList());
    }
}
