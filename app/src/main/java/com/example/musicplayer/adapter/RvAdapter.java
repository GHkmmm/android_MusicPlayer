package com.example.musicplayer.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Album> mList;
//    Bitmap bitMap_imgPath;
    private List<Bitmap> bitMap_imgPath = new ArrayList<Bitmap>(){{add(null); add(null); add(null);}};  ;
//    MyViewHolder holder;

    public RvAdapter(List<Album> list){
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.onlinemusic_list_item, parent, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        Album album = mList.get(position);
        String imgPath = album.getAlbumImgPath();
        changeToBitMap(imgPath, position);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = new Object[]{ holder, position};
                handler.sendMessage(msg);
            }
        },500);

        holder.top1.setText("1."+album.getTop1());
        holder.top2.setText("2."+album.getTop2());
        holder.top3.setText("3."+album.getTop3());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(v, mList.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });
        }

    }

    public interface OnItemClickListener{
        public void onItemClick(View view, Album album, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public void changeToBitMap(final String url, final int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL myFileUrl = null;
                Bitmap bitmap = null;
                try {
                    myFileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        bitMap_imgPath.set (pos,bitmap);
                        System.out.println(bitMap_imgPath);
//                        msg.what = pos;
//                        System.out.println("pos is"+pos);
//                        handler.sendMessage(msg);
                    }

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
            Object[] objs = (Object[]) msg.obj;
            System.out.println("obj: "+objs[1]);
            ((MyViewHolder)objs[0]).cover.setImageBitmap(bitMap_imgPath.get((Integer) objs[1]));
//            switch (msg.what){
//                case 0:
//                    holder.cover.setImageBitmap(bitMap_imgPath.get(0));
//                    break;
//                case 1:
//                    holder.cover.setImageBitmap(bitMap_imgPath.get(1));
//                    break;
//                case 2:
//                    holder.cover.setImageBitmap(bitMap_imgPath.get(2));
//            }
        }
    };
}
