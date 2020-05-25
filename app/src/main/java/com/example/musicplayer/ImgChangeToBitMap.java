package com.example.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 加载网络图片的类 并且加载到对应到imageView上
 */
public class ImgChangeToBitMap extends AsyncTask<String,Void, Bitmap> {
    ImageView imageView;

    public ImgChangeToBitMap(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        String url= params[0];  //获取URL
        URLConnection connection;   //网络连接对象
        InputStream is;    //数据输入流
        try {
            connection = new URL(url).openConnection();
            is = connection.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(buf);
            is.close();
            buf.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}

