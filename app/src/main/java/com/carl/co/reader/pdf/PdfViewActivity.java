package com.carl.co.reader.pdf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.carl.co.reader.R;
import com.carl.co.reader.book.BookBean;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

/**
 * Created by Host-0 on 2017/3/16.
 */

public class PdfViewActivity extends Activity implements OnPageChangeListener{
    private PDFView pdfView;
    private ProgressBar pdfProgressBar;
    private BookBean bookBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        initView();
        loadPdf();
    }

    private void initView(){
        pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfProgressBar = (ProgressBar)findViewById(R.id.pdf_progress_bar);
    }

    private void loadPdf(){
        Bundle bundle = getIntent().getExtras();
        bookBean = (BookBean) bundle.getSerializable("book");
        pdfView.fromFile(new File(bookBean.getPath())).onPageChange(this).load();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(pdfView.getPageCount() <= 0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int count = pdfView.getPageCount();
                        float speed = bookBean.getSpeed();
                        int page = (int)(count * speed) / 100;
                        if(page == 0) page = 1;
                        pdfView.jumpTo(page);
                        pdfProgressBar.setMax(count);
                        pdfProgressBar.setProgress(page);
                    }
                });
            }
        }.start();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pdfProgressBar.setProgress(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            int currentPage = pdfView.getCurrentPage() + 1;
            int pageCount = pdfView.getPageCount();
            float speed = (float)currentPage*100/(float)pageCount;
            bookBean.setSpeed(speed);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("book",bookBean);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK,intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
