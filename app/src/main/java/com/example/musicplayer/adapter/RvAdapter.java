package com.example.musicplayer.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.ImgChangeToBitMap;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Album> mList;
    Bitmap bitMap_imgPath;
    MyViewHolder holder;

    public RvAdapter(List<Album> list){
        mList = list;
        System.out.println(mList);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.onlinemusic_list_item, parent, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        holder = (MyViewHolder) viewHolder;
        Album album = mList.get(position);
            String imgPath = album.getAlbumImgPath();
            System.out.println(imgPath);
            changeToBitMap(imgPath);
//            Bitmap bitMap_imgPath = changeToBitMap(imgPath);
//            System.out.println("imgPath:"+bitMap_imgPath);
//            holder.cover.setImageBitmap(bitMap_imgPath);
//
//            holder.top1.setText("top1");
//            holder.top2.setText("top2");
//            holder.top3.setText("top3");

    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView top1;
        TextView top2;
        TextView top3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            top1 = itemView.findViewById(R.id.top1);
            top2 = itemView.findViewById(R.id.top2);
            top3 = itemView.findViewById(R.id.top3);
        }
    }

    public void changeToBitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                URL myFileUrl = null;
                Bitmap bitmap = null;
                System.out.println("url:"+url);
                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    System.out.println("bitmap:"+bitmap);
                    bitMap_imgPath = bitmap;
                    msg.what = 1;
                    handler.sendMessage(msg);

                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    System.out.println("imgPath:"+bitMap_imgPath);
                    holder.cover.setImageBitmap(bitMap_imgPath);

                    holder.top1.setText("top1");
                    holder.top2.setText("top2");
                    holder.top3.setText("top3");
                    break;
            }
        }
    };
}
