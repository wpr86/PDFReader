package com.carl.co.reader.book;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Host-0 on 2017/3/7.
 */

public class BookBean implements Serializable {
    public long id;
    public String title;
    public String imagePath;
    public float speed;
    public String path;
    public BookBean(long id, String title, String image, float speed, String path){
        this.id = id;
        this.title = title;
        this.imagePath = image;
        this.speed = speed;
        this.path = path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getPath() {
        return path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookBean bookBean = (BookBean) o;

        return path != null ? path.equals(bookBean.path) : bookBean.path == null;

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

}
