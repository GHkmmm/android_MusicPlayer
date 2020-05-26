package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.ImgChangeToBitMap;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.SongList;

import java.util.List;

public class songListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SongList> mList;

    public songListAdapter(List<SongList> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        RecyclerView.ViewHolder holder = new SLViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder ViewHolder, int position) {
        SLViewHolder holder = (SLViewHolder) ViewHolder;
        SongList songList = mList.get(position);
        holder.name.setText(songList.getTitle());
        holder.singer.setText(songList.getAuthor());
        String url = songList.getPic_radio();
        ImgChangeToBitMap task = new ImgChangeToBitMap(holder.cover);
        task.execute(url);
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    class SLViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        TextView singer;

        public SLViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.song_list_cover);
            name = itemView.findViewById(R.id.song_list_name);
            singer = itemView.findViewById(R.id.song_list_singer);

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
        public void onItemClick(View view, SongList songList, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
