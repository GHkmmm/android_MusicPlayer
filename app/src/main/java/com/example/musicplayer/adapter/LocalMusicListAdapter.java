package com.example.musicplayer.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.Song;

import java.io.File;
import java.util.List;

public class LocalMusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Song> mList;
    private LocalMusicListAdapter.OnItemClickListener onItemClickListener;

    public LocalMusicListAdapter(List<Song> list){
        mList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.localmusic_list_item, parent, false);
        RecyclerView.ViewHolder holder = new LMViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        LMViewHolder holder = (LMViewHolder) viewHolder;
        Song song = mList.get(position);
        System.out.println("url is -----"+song.getAlbum_img());
        Bitmap bm_imgPath = song.getAlbum_img();
        if(bm_imgPath == null){
            holder.cover.setImageResource(R.drawable.default_cover);
        }else {
            holder.cover.setImageBitmap(bm_imgPath);
        }
        holder.name.setText(song.getName());
        holder.singer.setText(song.getSinger());
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    class LMViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        TextView singer;

        public LMViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.lm_cover);
            name = itemView.findViewById(R.id.lm_name);
            singer = itemView.findViewById(R.id.lm_singer);

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
        public void onItemClick(View view, Song song, int position);
    }

    public void setOnItemClickListener(LocalMusicListAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
